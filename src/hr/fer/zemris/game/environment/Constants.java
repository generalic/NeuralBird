package hr.fer.zemris.game.environment;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Constants implements Serializable {

	public int NUMBER_OF_PIPES = 5;
    public double PIPES_SPEED_X = 10;
    public double REWARD_SPEED_X = PIPES_SPEED_X;
    public double PIPES_SPEED_Y = 5;
    public double JUMP_SPEED = -20;
    public double PIPE_GAP_X = 350;
    public double PIPE_GAP_Y = 200;
    public double PIPE_WIDTH = 70;
    public double INITIAL_PIPE_OFFSET = 100;
    public double REWARD_GAP_X = PIPE_GAP_X + PIPE_WIDTH;
    public int PIPE_PASSED_BONUS = 1;
    public int REWARD_COLLECTED_BONUS = 10;
    public double REWARD_PROBABILITY = 0.5;

   
}
