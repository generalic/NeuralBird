package hr.fer.zemris.game.model;

import hr.fer.zemris.game.components.bird.BirdTrainable;

public class GameModelAITrainable extends GameModelAI {

	@Override
	protected void initaliseBird() {
		this.bird = new BirdTrainable(gameDimension.getWidth() / 3, gameDimension.getHeight() / 2);
	}

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
