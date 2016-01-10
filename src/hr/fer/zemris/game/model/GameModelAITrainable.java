package hr.fer.zemris.game.model;

public class GameModelAITrainable extends GameModelAI {

	@Override
	protected double initialiseGround(double nextGroundX) {
		//returns 0 performance-wise
		return 0;
	}

	@Override
	protected void moveGround(int time) {
		//does nothing performance-wise
	}

}
