package com.github.maxopoly.finale.combat.knockback;

import com.github.maxopoly.finale.Finale;
import com.github.maxopoly.finale.combat.CombatConfig;
import com.github.maxopoly.finale.combat.SprintHandler;
import com.github.maxopoly.finale.misc.knockback.KnockbackConfig;
import com.github.maxopoly.finale.misc.knockback.KnockbackModifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.bukkit.util.Vector;

public class ClassicKnockback implements KnockbackStrategy {

	@Override
	public void handleKnockback(Player attacker, Entity entity, float knockbackLevel) {
		CombatConfig config = Finale.getPlugin().getManager().getCombatConfig();
		SprintHandler sprintHandler = Finale.getPlugin().getManager().getSprintHandler();

		KnockbackConfig knockbackConfig = (knockbackLevel > 0) ? config.getSprintConfig() : config.getNormalConfig();
		KnockbackModifier modifier = knockbackConfig.getGroundModifier();

		if (!entity.isOnGround()) {
			modifier = knockbackConfig.getAirModifier();
		}

		if (entity.isInWater()) {
			modifier = knockbackConfig.getWaterModifier();
		}

		double deltaX = (-Mth.sin(attacker.getYRot() * 0.017453292F) * 0.5F);
		double deltaY = 0.1;
		double deltaZ = (Mth.cos(attacker.getYRot() * 0.017453292F) * 0.5F);
		if (knockbackLevel > 0) {
			deltaX *= knockbackLevel;
			deltaZ *= knockbackLevel;
		}

		Vector deltaVec = new Vector(deltaX, deltaY, deltaZ);
		Vec3 currentDelta = entity.getDeltaMovement();
		Vector currentDeltaVec = new Vector(currentDelta.x, currentDelta.y, currentDelta.z);
		Vector modifiedDelta = modifier.modifyKnockback(currentDeltaVec, deltaVec);

		if (entity instanceof LivingEntity) {
			LivingEntity livingEntity = (LivingEntity) entity;
			double kbReductionFactor = 1.0D - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
			modifiedDelta = modifiedDelta.clone().multiply(kbReductionFactor);
		}

		entity.push(modifiedDelta.getX(), modifiedDelta.getY(), modifiedDelta.getZ());

		Vector attackerMotion = config.getAttackerMotion();
		attacker.setDeltaMovement(attacker.getDeltaMovement().multiply(attackerMotion.getX(), attackerMotion.getY(), attackerMotion.getZ()));
		if (attacker.isInWater()) {
			if (config.isWaterSprintResetEnabled()) {
				//attacker.setSprinting(false);
				sprintHandler.stopSprinting(attacker);
			}
		} else {
			if (config.isSprintResetEnabled()) {
				//attacker.setSprinting(false); // looks like modern minecraft finally defeated w-tapping, but we work around it
				sprintHandler.stopSprinting(attacker);
			}
		}
	}

}
