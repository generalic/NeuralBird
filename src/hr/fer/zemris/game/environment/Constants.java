package hr.fer.zemris.game.environment;

import java.io.Serializable;

@SuppressWarnings("serial")
public class Constants implements Serializable {

//	public int NUMBER_OF_PIPES = 5;
//    public double PIPES_SPEED_X = 10;
//    public double REWARD_SPEED_X = PIPES_SPEED_X;
//    public double PIPES_SPEED_Y = 5;
//    public double JUMP_SPEED = -20;
//    public double PIPE_GAP_X = 350;
//    public double PIPE_GAP_Y = 200;
//    public double PIPE_WIDTH = 70;
//    public double INITIAL_PIPE_OFFSET = 100;
//    public double REWARD_GAP_X = PIPE_GAP_X + PIPE_WIDTH;
//    public int PIPE_PASSED_BONUS = 1;
//    public int REWARD_COLLECTED_BONUS = 10;
//    public double REWARD_PROBABILITY = 0.5;
	
	public static int NUMBER_OF_PIPES = 5;
    public static double PIPES_SPEED_X = 10;
    public static double REWARD_SPEED_X = PIPES_SPEED_X;
    public static double PIPES_SPEED_Y = 5;
    public static double JUMP_SPEED = -20;
    public static double PIPE_GAP_X = 350;
    public static double PIPE_GAP_Y = 200;
    public static double PIPE_WIDTH = 70;
    public static double INITIAL_PIPE_OFFSET = 100;
    public static double REWARD_GAP_X = PIPE_GAP_X + PIPE_WIDTH;
    public static int PIPE_PASSED_BONUS = 1;
    public static int REWARD_COLLECTED_BONUS = 10;
    public static double REWARD_PROBABILITY = 0.5;

    public static void setDefaultConstants() {
    	NUMBER_OF_PIPES = 5;
    	PIPES_SPEED_X = 10;
    	REWARD_SPEED_X = PIPES_SPEED_X;
    	PIPES_SPEED_Y = 5;
    	JUMP_SPEED = -20;
    	PIPE_GAP_X = 350;
    	PIPE_GAP_Y = 200;
    	PIPE_WIDTH = 70;
    	INITIAL_PIPE_OFFSET = 100;
    	REWARD_GAP_X = PIPE_GAP_X + PIPE_WIDTH;
    	PIPE_PASSED_BONUS = 1;
    	REWARD_COLLECTED_BONUS = 10;
    	REWARD_PROBABILITY = 0.5;
    }
   
}
