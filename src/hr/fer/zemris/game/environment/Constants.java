package hr.fer.zemris.game.environment;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Constants implements Serializable {

	public final int NUMBER_OF_PIPES = 5;
    public final double PIPES_SPEED_X = 10;
    public final double REWARD_SPEED_X = PIPES_SPEED_X;
    public final double PIPES_SPEED_Y = 5;
    public final double JUMP_SPEED = -20;
    public final double PIPE_GAP_X = 350;
    public final double PIPE_GAP_Y = 200;
    public final double PIPE_WIDTH = 70;
    public final double INITIAL_PIPE_OFFSET = 100;
    public final double REWARD_GAP_X = PIPE_GAP_X + PIPE_WIDTH;
    public final int PIPE_PASSED_BONUS = 1;
    public final int REWARD_COLLECTED_BONUS = 10;
    public final double REWARD_PROBABILITY = 0.5;

}
