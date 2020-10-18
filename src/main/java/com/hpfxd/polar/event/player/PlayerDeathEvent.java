package com.hpfxd.polar.event.player;

import com.hpfxd.polar.event.Event;
import com.hpfxd.polar.player.Player;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class PlayerDeathEvent extends Event {
    private final Player player;
}
