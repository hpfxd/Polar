package com.hpfxd.polar;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hpfxd.polar.command.CommandManager;
import com.hpfxd.polar.event.EventManager;
import com.hpfxd.polar.network.NetworkManager;
import com.hpfxd.polar.player.Player;
import com.hpfxd.polar.world.World;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class Polar {
    public static final int PROTOCOL_VERSION = 47;
    public static final AtomicInteger ENTITY_ID_COUNTER = new AtomicInteger();
    public static final AtomicInteger KEEPALIVE_ID_COUNTER = new AtomicInteger();

    @Getter private static Polar polar;
    @Getter private final Gson gson = new GsonBuilder().create();

    @Getter private final ScheduledExecutorService executorService;
    @Getter private final PolarConfig config;
    @Getter private final NetworkManager networkManager;
    @Getter private final World world;
    @Getter private final List<Player> players = new ArrayList<>();
    @Getter private final EventManager eventManager = new EventManager();
    @Getter private final CommandManager commandManager = new CommandManager();

    Polar() {
        log.info("Starting Polar.");
        polar = this;
        this.config = this.readConfig();
        this.config.init();
        log.info("Loaded configuration.");

        this.executorService = Executors.newScheduledThreadPool(this.config.getCoreExecutorPoolSize());
        this.world = new World(this.config.getWorldSize());

        this.commandManager.registerEvents();
        this.commandManager.registerDefaultCommands();

        this.networkManager = new NetworkManager();
    }

    public void shutdown() {
        log.info("Shutting down.");
        this.networkManager.shutdown();
    }

    private PolarConfig readConfig() {
        File configFile = new File("config.json");

        if (!configFile.exists()) {
            throw new RuntimeException("Configuration file does not exist.");
        }

        try (FileInputStream in = new FileInputStream(configFile);
             InputStreamReader reader = new InputStreamReader(in)) {
            return this.gson.fromJson(reader, PolarConfig.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
