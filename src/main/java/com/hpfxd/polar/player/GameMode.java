package com.hpfxd.polar.player;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum GameMode {
    SURVIVAL(false),
    CREATIVE(true),
    ADVENTURE(false),
    SPECTATOR(false)
    ;

    private final boolean instantBreak;
}
