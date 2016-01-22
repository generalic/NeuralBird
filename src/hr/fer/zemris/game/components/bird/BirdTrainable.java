package hr.fer.zemris.game.components.bird;

public class BirdTrainable extends Bird {

	public BirdTrainable(double centerX, double centerY) {
		super(centerX, centerY);
	}

	@Override
    public void updateFrame() {
		//does nothing performance-wise
    }

	@Override
    protected void loadBirdFrames() {
		//does nothing performance-wise
	}

}
