package hr.fer.zemris.game.environment;

public class EnvironmentVariables {

	private double distanceToUpperPipe;
	private double distanceToLowerPipe;
	private double birdHeight;
	private double pipeDirection;
	private double distanceToReward;
	private double visibleReward;
	private double relativeHeightToReward;

	public EnvironmentVariables(double distanceToLowerPipe, double distanceToUpperPipe, double birdHeight,
			double pipeDirection, double distanceToReward, double visibleReward, double relativeHeightToReward) {
		super();
		this.distanceToLowerPipe = distanceToLowerPipe;
		this.distanceToUpperPipe = distanceToUpperPipe;
		this.birdHeight = birdHeight;
		this.pipeDirection = pipeDirection;
		this.distanceToReward = distanceToReward;
		this.visibleReward = visibleReward;
		this.relativeHeightToReward = relativeHeightToReward;
	}

	public double[] getVariables() {
		return new double[] {
				distanceToUpperPipe,
				distanceToLowerPipe,
				birdHeight,
				pipeDirection,
				distanceToReward,
				visibleReward,
				relativeHeightToReward
		};
	}

}
