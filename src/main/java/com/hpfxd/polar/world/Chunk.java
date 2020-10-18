package com.hpfxd.polar.world;

import com.hpfxd.polar.util.ChunkDataMessage;
import com.hpfxd.polar.util.NibbleArray;
import lombok.Data;

public class Chunk {
    /**
     * The dimensions of a chunk (width: x, height: z, depth: y).
     */
    public static final int WIDTH = 16, HEIGHT = 16, DEPTH = 256;
    /**
     * The Y depth of a single chunk section.
     */
    private static final int SEC_DEPTH = 16;
    /**
     * The world of this chunk.
     */
    private final World world;
    /**
     * The coordinates of this chunk.
     */
    private final int x, z;
    /**
     * The array of chunk sections this chunk contains, or null if it is unloaded.
     */
    private ChunkSection[] sections;

    /**
     * The array of biomes this chunk contains, or null if it is unloaded.
     */
    private byte[] biomes;

    /**
     * The height map values values of each column, or null if it is unloaded.
     * The height for a column is one plus the y-index of the highest non-air
     * block in the column.
     */
    private byte[] heightMap;
    /**
     * Whether the chunk has been populated by special features.
     * Used in map generation.
     */
    private boolean populated = false;

    private boolean updated = true;
    private ChunkDataMessage cachedDataMessage;

    /**
     * Creates a new chunk with a specified X and Z coordinate.
     *
     * @param x The X coordinate.
     * @param z The Z coordinate.
     */
    public Chunk(World world, int x, int z) {
        this.world = world;
        this.x = x;
        this.z = z;
    }

    public String toString() {
        return "Chunk{x=" + x + ",z=" + z + '}';
    }

    public World getWorld() {
        return world;
    }

    public int getX() {
        return x;
    }

    // ======== Basic stuff ========

    public int getZ() {
        return z;
    }

    /**
     * Gets whether this chunk has been populated by special features.
     *
     * @return Population status.
     */
    public boolean isPopulated() {
        return populated;
    }

    /**
     * Sets the population status of this chunk.
     *
     * @param populated Population status.
     */
    public void setPopulated(boolean populated) {
        this.populated = populated;
    }

    // ======== Helper Functions ========

    /**
     * Initialize this chunk from the given sections.
     *
     * @param initSections The ChunkSections to use.
     */
    public void initializeSections(ChunkSection... initSections) {
        //GlowServer.logger.log(Level.INFO, "Initializing chunk ({0},{1})", new Object[]{x, z});

        sections = new ChunkSection[DEPTH / SEC_DEPTH];
        System.arraycopy(initSections, 0, sections, 0, Math.min(sections.length, initSections.length));

        biomes = new byte[WIDTH * HEIGHT];
        heightMap = new byte[WIDTH * HEIGHT];
    }

    /**
     * Attempt to get the ChunkSection at the specified height.
     *
     * @param y the y value.
     * @return The ChunkSection, or null if it is empty.
     */
    private ChunkSection getSection(int y) {
        int idx = y >> 4;
        if (y < 0 || y >= DEPTH || idx >= sections.length) {
            return null;
        }
        return sections[idx];
    }

    /**
     * Get all ChunkSection of this chunk.
     *
     * @return The chunk sections array.
     */
    public ChunkSection[] getSections() {
        return sections;
    }

    // ======== Data access ========

    /**
     * Gets the type of a block within this chunk.
     *
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The type.
     */
    public int getType(int x, int z, int y) {
        ChunkSection section = getSection(y);
        return section == null ? 0 : (section.types[section.index(x, y, z)] >> 4);
    }

    public void setBlock(int x, int y, int z, int id, int meta) {
        synchronized (this) {
            this.setType(x, z, y, id);
            this.setMetaData(x, z, y, meta);
        }
    }

    public Block getBlock(int x, int y, int z) {
        return new Block(this, x, y, z, this.getType(x, z, y), this.getMetaData(x, z, y));
    }

    /**
     * Sets the type of a block within this chunk.
     *
     * @param x    The X coordinate.
     * @param z    The Z coordinate.
     * @param y    The Y coordinate.
     * @param type The type.
     */
    public void setType(int x, int z, int y, int type) {
        if (type < 0 || type > 0xfff)
            throw new IllegalArgumentException("Block type out of range: " + type);

        ChunkSection section = getSection(y);
        if (section == null) {
            if (type == 0) {
                // don't need to create chunk for air
                return;
            } else {
                // create new ChunkSection for this y coordinate
                int idx = y >> 4;
                if (y < 0 || y >= DEPTH || idx >= sections.length) {
                    // y is out of range somehow
                    return;
                }
                sections[idx] = section = new ChunkSection();
            }
        }

        // destroy any tile entity there
        int tileEntityIndex = coordToIndex(x, z, y);

        // update the air count and height map
        int index = section.index(x, y, z);
        int heightIndex = z * WIDTH + x;
        if (type == 0) {
            if (section.types[index] != 0) {
                section.count--;
            }
            if (heightMap[heightIndex] == y + 1) {
                // erased just below old height map -> lower
                heightMap[heightIndex] = (byte) lowerHeightMap(x, y, z);
            }
        } else {
            if (section.types[index] == 0) {
                section.count++;
            }
            if (heightMap[heightIndex] <= y) {
                // placed between old height map and top -> raise
                heightMap[heightIndex] = (byte) Math.min(y + 1, 255);
            }
        }
        // update the type - also sets metadata to 0
        section.types[index] = (char) (type << 4);

        if (type == 0 && section.count == 0) {
            // destroy the empty section
            sections[y / SEC_DEPTH] = null;
            return;
        }
        this.updated = true;
    }

    /**
     * Scan downwards to determine the new height map value.
     */
    private int lowerHeightMap(int x, int y, int z) {
        for (--y; y >= 0; --y) {
            if (getType(x, z, y) != 0) {
                break;
            }
        }
        return y + 1;
    }

    /**
     * Gets the metadata of a block within this chunk.
     *
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The metadata.
     */
    public int getMetaData(int x, int z, int y) {
        ChunkSection section = getSection(y);
        return section == null ? 0 : section.types[section.index(x, y, z)] & 0xF;
    }

    /**
     * Sets the metadata of a block within this chunk.
     *
     * @param x        The X coordinate.
     * @param z        The Z coordinate.
     * @param y        The Y coordinate.
     * @param metaData The metadata.
     */
    public void setMetaData(int x, int z, int y, int metaData) {
        if (metaData < 0 || metaData >= 16)
            throw new IllegalArgumentException("Metadata out of range: " + metaData);
        ChunkSection section = getSection(y);
        if (section == null) return;  // can't set metadata on an empty section
        int index = section.index(x, y, z);
        int type = section.types[index];
        if (type == 0) return;  // can't set metadata on air
        section.types[index] = (char) ((type & 0xfff0) | metaData);
        this.updated = true;
    }

    /**
     * Gets the sky light level of a block within this chunk.
     *
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The sky light level.
     */
    public byte getSkyLight(int x, int z, int y) {
        ChunkSection section = getSection(y);
        return section == null ? 0 : section.skyLight.get(section.index(x, y, z));
    }

    /**
     * Sets the sky light level of a block within this chunk.
     *
     * @param x        The X coordinate.
     * @param z        The Z coordinate.
     * @param y        The Y coordinate.
     * @param skyLight The sky light level.
     */
    public void setSkyLight(int x, int z, int y, int skyLight) {
        ChunkSection section = getSection(y);
        if (section == null) return;  // can't set light on an empty section
        section.skyLight.set(section.index(x, y, z), (byte) skyLight);
    }

    /**
     * Gets the block light level of a block within this chunk.
     *
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The block light level.
     */
    public byte getBlockLight(int x, int z, int y) {
        ChunkSection section = getSection(y);
        return section == null ? 0 : section.blockLight.get(section.index(x, y, z));
    }

    /**
     * Sets the block light level of a block within this chunk.
     *
     * @param x          The X coordinate.
     * @param z          The Z coordinate.
     * @param y          The Y coordinate.
     * @param blockLight The block light level.
     */
    public void setBlockLight(int x, int z, int y, int blockLight) {
        ChunkSection section = getSection(y);
        if (section == null) return;  // can't set light on an empty section
        section.blockLight.set(section.index(x, y, z), (byte) blockLight);
    }

    /**
     * Gets the biome of a column within this chunk.
     *
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @return The biome.
     */
    public int getBiome(int x, int z) {
        if (biomes == null) return 0;
        return biomes[z * WIDTH + x] & 0xFF;
    }

    /**
     * Sets the biome of a column within this chunk,
     *
     * @param x     The X coordinate.
     * @param z     The Z coordinate.
     * @param biome The biome.
     */
    public void setBiome(int x, int z, int biome) {
        if (biomes == null) return;
        biomes[z * WIDTH + x] = (byte) biome;
    }

    /**
     * Set the entire biome array of this chunk.
     *
     * @param newBiomes The biome array.
     */
    public void setBiomes(byte... newBiomes) {
        if (biomes == null) {
            throw new IllegalStateException("Must initialize chunk first");
        }
        if (newBiomes.length != biomes.length) {
            throw new IllegalArgumentException("Biomes array not of length " + biomes.length);
        }
        System.arraycopy(newBiomes, 0, biomes, 0, biomes.length);
    }

    /**
     * Get the height map value of a column within this chunk.
     *
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @return The height map value.
     */
    public int getHeight(int x, int z) {
        if (heightMap == null ) return 0;
        return heightMap[z * WIDTH + x] & 0xff;
    }

    /**
     * Set the entire height map of this chunk.
     *
     * @param newHeightMap The height map.
     */
    public void setHeightMap(int... newHeightMap) {
        if (heightMap == null) {
            throw new IllegalStateException("Must initialize chunk first");
        }
        if (newHeightMap.length != heightMap.length) {
            throw new IllegalArgumentException("Height map not of length " + heightMap.length);
        }
        for (int i = 0; i < heightMap.length; ++i) {
            heightMap[i] = (byte) newHeightMap[i];
        }
    }

    /**
     * Automatically fill the height map after chunks have been initialized.
     */
    public void automaticHeightMap() {
        // determine max Y chunk section at a time
        int sy = sections.length - 1;
        for (; sy >= 0; --sy) {
            if (sections[sy] != null) {
                break;
            }
        }
        int y = (sy + 1) * 16;
        for (int x = 0; x < WIDTH; ++x) {
            for (int z = 0; z < HEIGHT; ++z) {
                heightMap[z * WIDTH + x] = (byte) lowerHeightMap(x, y, z);
            }
        }
    }

    /**
     * Converts a three-dimensional coordinate to an index within the
     * one-dimensional arrays.
     *
     * @param x The X coordinate.
     * @param z The Z coordinate.
     * @param y The Y coordinate.
     * @return The index within the arrays.
     */
    private int coordToIndex(int x, int z, int y) {
        if (x < 0 || z < 0 || y < 0 || x >= WIDTH || z >= HEIGHT || y >= DEPTH)
            throw new IndexOutOfBoundsException("Coords (x=" + x + ",y=" + y + ",z=" + z + ") invalid");

        return (y * HEIGHT + z) * WIDTH + x;
    }

    /**
     * Creates a new {@link ChunkDataMessage} which can be sent to a client to stream
     * this entire chunk to them.
     *
     * @return The {@link ChunkDataMessage}.
     */
    public ChunkDataMessage toMessage() {
        // this may need to be changed to "true" depending on resolution of
        // some inconsistencies on the wiki
        return toMessage(true);
    }

    // ======== Helper functions ========

    /**
     * Creates a new {@link ChunkDataMessage} which can be sent to a client to stream
     * this entire chunk to them.
     *
     * @param skylight Whether to include skylight data.
     * @return The {@link ChunkDataMessage}.
     */
    public ChunkDataMessage toMessage(boolean skylight) {
        return toMessage(skylight, true, 0);
    }

    /**
     * Creates a new {@link ChunkDataMessage} which can be sent to a client to stream
     * parts of this chunk to them.
     *
     * @return The {@link ChunkDataMessage}.
     */
    public ChunkDataMessage toMessage(boolean skylight, boolean entireChunk, int sectionBitmask) {
        if (this.cachedDataMessage != null && !this.updated) {
            return this.cachedDataMessage;
        }

        // filter sectionBitmask based on actual chunk contents
        int sectionCount;
        if (sections == null) {
            sectionBitmask = 0;
            sectionCount = 0;
        } else {
            final int maxBitmask = (1 << sections.length) - 1;
            if (entireChunk) {
                sectionBitmask = maxBitmask;
                sectionCount = sections.length;
            } else {
                sectionBitmask &= maxBitmask;
                sectionCount = countBits(sectionBitmask);
            }

            for (int i = 0; i < sections.length; ++i) {
                if (sections[i] == null || sections[i].count == 0) {
                    // remove empty sections from bitmask
                    sectionBitmask &= ~(1 << i);
                    sectionCount--;
                }
            }
        }

        // calculate how big the data will need to be
        int byteSize = 0;

        if (sections != null) {
            final int numBlocks = WIDTH * HEIGHT * SEC_DEPTH;
            int sectionSize = numBlocks * 5 / 2;  // (data and metadata combo) * 2 + blockLight/2
            if (skylight) {
                sectionSize += numBlocks / 2;  // + skyLight/2
            }
            byteSize += sectionCount * sectionSize;
        }

        if (entireChunk) {
            byteSize += 256;  // + biomes
        }

        byte[] tileData = new byte[byteSize];
        int pos = 0;

        if (sections != null) {
            // get the list of sections
            ChunkSection[] sendSections = new ChunkSection[sectionCount];
            for (int i = 0, j = 0, mask = 1; i < sections.length; ++i, mask <<= 1) {
                if ((sectionBitmask & mask) != 0) {
                    sendSections[j++] = sections[i];
                }
            }

            for (ChunkSection sec : sendSections) {
                for (char t : sec.types) {
                    tileData[pos++] = (byte) (t & 0xff);
                    tileData[pos++] = (byte) (t >> 8);
                }
            }

            for (ChunkSection sec : sendSections) {
                byte[] blockLight = sec.blockLight.getRawData();
                System.arraycopy(blockLight, 0, tileData, pos, blockLight.length);
                pos += blockLight.length;
            }

            if (skylight) {
                for (ChunkSection sec : sendSections) {
                    byte[] skyLight = sec.skyLight.getRawData();
                    System.arraycopy(skyLight, 0, tileData, pos, skyLight.length);
                    pos += skyLight.length;
                }
            }
        }

        // biomes
        if (entireChunk) {
            for (int i = 0; i < 256; ++i) {
                tileData[pos++] = biomes[i];
            }
        }

        if (pos != byteSize) {
            throw new IllegalStateException("only wrote " + pos + " out of expected " + byteSize + " bytes");
        }

        this.cachedDataMessage = new ChunkDataMessage(x, z, entireChunk, sectionBitmask, tileData);
        this.updated = false;
        return this.cachedDataMessage;
    }

    private int countBits(int v) {
        // http://graphics.stanford.edu/~seander/bithacks.html#CountBitsSetKernighan
        int c;
        for (c = 0; v > 0; c++) {
            v &= v - 1;
        }
        return c;
    }

    /**
     * A chunk key represents the X and Z coordinates of a chunk in a manner
     * suitable for use as a key in a hash table or set.
     */
    @Data
    public static final class Key {
        /**
         * The coordinates.
         */
        private final int x, z;
    }

    /**
     * A single cubic section of a chunk, with all data.
     */
    public static final class ChunkSection {
        private static final int ARRAY_SIZE = WIDTH * HEIGHT * SEC_DEPTH;

        // these probably should be made non-public
        public final char[] types;
        public final NibbleArray skyLight;
        public final NibbleArray blockLight;
        public int count; // amount of non-air blocks

        /**
         * Create a new, empty ChunkSection.
         */
        public ChunkSection() {
            types = new char[ARRAY_SIZE];
            skyLight = new NibbleArray(ARRAY_SIZE);
            blockLight = new NibbleArray(ARRAY_SIZE);
            skyLight.fill((byte) 0xf);
        }

        /**
         * Create a ChunkSection with the specified chunk data. This
         * ChunkSection assumes ownership of the arrays passed in, and they
         * should not be further modified.
         */
        public ChunkSection(char[] types, NibbleArray skyLight, NibbleArray blockLight) {
            if (types.length != ARRAY_SIZE || skyLight.size() != ARRAY_SIZE || blockLight.size() != ARRAY_SIZE) {
                throw new IllegalArgumentException("An array length was not " + ARRAY_SIZE + ": " + types.length + " " + skyLight.size() + " " + blockLight.size());
            }
            this.types = types;
            this.skyLight = skyLight;
            this.blockLight = blockLight;
            recount();
        }

        /**
         * Calculate the index into internal arrays for the given coordinates.
         */
        public int index(int x, int y, int z) {
            if (x < 0 || z < 0 || x >= WIDTH || z >= HEIGHT) {
                throw new IndexOutOfBoundsException("Coords (x=" + x + ",z=" + z + ") out of section bounds");
            }
            return ((y & 0xf) << 8) | (z << 4) | x;
        }

        /**
         * Recount the amount of non-air blocks in the chunk section.
         */
        public void recount() {
            count = 0;
            for (char type : types) {
                if (type != 0) {
                    count++;
                }
            }
        }

        /**
         * Take a snapshot of this section which will not reflect future changes.
         */
        public ChunkSection snapshot() {
            return new ChunkSection(types.clone(), skyLight.snapshot(), blockLight.snapshot());
        }
    }
}
