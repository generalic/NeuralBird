package hr.fer.zemris.game.environment;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Constants implements Serializable {

    private static Constants currentConstants;
    private static Constants defaultConstants;

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

    public Constants() {

    }

    public Constants(int NUMBER_OF_PIPES, double PIPES_SPEED_X, double REWARD_SPEED_X, double PIPES_SPEED_Y, double JUMP_SPEED, double PIPE_GAP_X, double PIPE_GAP_Y, double PIPE_WIDTH, double INITIAL_PIPE_OFFSET, double REWARD_GAP_X, int PIPE_PASSED_BONUS, int REWARD_COLLECTED_BONUS, double REWARD_PROBABILITY) {
        this.NUMBER_OF_PIPES = NUMBER_OF_PIPES;
        this.PIPES_SPEED_X = PIPES_SPEED_X;
        this.REWARD_SPEED_X = REWARD_SPEED_X;
        this.PIPES_SPEED_Y = PIPES_SPEED_Y;
        this.JUMP_SPEED = JUMP_SPEED;
        this.PIPE_GAP_X = PIPE_GAP_X;
        this.PIPE_GAP_Y = PIPE_GAP_Y;
        this.PIPE_WIDTH = PIPE_WIDTH;
        this.INITIAL_PIPE_OFFSET = INITIAL_PIPE_OFFSET;
        this.REWARD_GAP_X = REWARD_GAP_X;
        this.PIPE_PASSED_BONUS = PIPE_PASSED_BONUS;
        this.REWARD_COLLECTED_BONUS = REWARD_COLLECTED_BONUS;
        this.REWARD_PROBABILITY = REWARD_PROBABILITY;
    }



}
