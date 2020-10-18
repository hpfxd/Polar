package com.hpfxd.polar.event.player;

import com.hpfxd.polar.event.CancellableEvent;
import com.hpfxd.polar.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PlayerInteractAtEntityEvent extends CancellableEvent {
    private final Player player;
    private int entityId;
    private final Action action;

    public enum Action {
        INTERACT,
        ATTACK,
        INTERACT_AT
    }
}
