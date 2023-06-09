package com.github.maxopoly.finale.combat;

public class DamageDeclineConfig {

	private boolean enabled;
	private double factor;

	public DamageDeclineConfig(boolean enabled, double factor) {
		this.enabled = enabled;
		this.factor = factor;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public double getFactor() {
		return factor;
	}
}
