# Polar
A lightweight Minecraft server implementation.  
Currently, only protocol #47 (1.8.x) is supported.

**Code is under heavy construction, using this isn't recommended yet!**

## Features
- [x] Joining the game
- [x] Multi-player viewing
- [ ] Proper multi-world support
- [x] Block placing/breaking
- [x] Event system
- [ ] Plugin system
- [x] Basic inventory system
- [x] Basic combat implementation
- [ ] Multi-version support (possible ViaVersion port as a plugin?)
- [x] Command support
- [ ] Command tab completion
- [ ] Creative inventory support
- [ ] Anti-cheat

### Worlds
Players on different worlds are completely hidden from each other, and don't show up in the player list for each other. All chat and command actions will not affect players in different worlds. This is so you can more easily separate minigame instances.
