package com.hpfxd.polar.event.player;

import com.hpfxd.polar.event.Event;
import com.hpfxd.polar.player.Player;
import com.hpfxd.polar.util.ItemStack;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PlayerInteractEvent extends Event {
    private final Player player;
    private final Action action;
    private final ItemStack itemInHand;

    public enum Action {
        RIGHT_CLICK_AIR, // todo
        RIGHT_CLICK_BLOCK, // todo
        SWING
    }
}
