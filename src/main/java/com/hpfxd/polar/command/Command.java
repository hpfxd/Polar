package com.hpfxd.polar.command;

import com.hpfxd.polar.player.Player;

public abstract class Command {
    public abstract String[] getAliases();
    public abstract String getUsage();

    public abstract void execute(Player player, String[] args) throws CommandException, IllegalArgumentException;

    public boolean hasPermission(Player player) {
        return true;
    }
}
