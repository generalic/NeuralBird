package hr.fer.zemris.game.physics;

import hr.fer.zemris.game.environment.Constants;

public final class Physics {

	private Physics() {}

	public static double calculateShiftX(double v, int t) {
		return v * t;
	}

	public static double calculateShiftY(double v0, int t) {
		return v0 * t + 0.5 * Constants.PlayerConstants.GRAVITY * t * t;
	}

	public static double calculateVelocity(double v0, int t) {
		return v0 + Constants.PlayerConstants.GRAVITY * t;
	}

}
