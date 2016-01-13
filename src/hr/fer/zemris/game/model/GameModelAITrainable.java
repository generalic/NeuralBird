package hr.fer.zemris.game.model;

public class GameModelAITrainable extends GameModelAI {

	@Override
	protected void setupGround() {
		//does nothing performance-wise
	}

	@Override
	protected double initialiseGround(double nextGroundX) {
		return 0;
	}

	@Override
	protected void moveGround(int time) {
		//does nothing performance-wise
	}

}
