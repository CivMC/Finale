package com.github.maxopoly.finale.combat.knockback;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface KnockbackStrategy {

	void handleKnockback(Player attacker, Entity victim, int knockbackLevel);

}
