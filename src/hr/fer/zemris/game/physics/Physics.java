package hr.fer.zemris.game.physics;

public final class Physics {

	private static final double GRAVITY = 4.81;

	private Physics() {}

	public static double calculateShiftX(double v, int t) {
		return v * t;
	}

	public static double calculateShiftY(double v0, int t) {
		return v0 * t + 0.5 * GRAVITY * t * t;
	}

	public static double calculateVelocity(double v0, int t) {
		return v0 + GRAVITY * t;
	}

}
