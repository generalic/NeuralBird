package hr.fer.zemris.game.environment;


@SuppressWarnings("serial")
public class ConstantsSettings extends Constants {

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

    public static void setConstants(Constants constants){
    	NUMBER_OF_PIPES = constants.NUMBER_OF_PIPES;
    	PIPES_SPEED_X = constants.PIPES_SPEED_X;
    	REWARD_SPEED_X = constants.REWARD_SPEED_X;
    	PIPES_SPEED_Y = constants.PIPES_SPEED_Y;
    	JUMP_SPEED = constants.JUMP_SPEED;
    	PIPE_GAP_X = constants.PIPE_GAP_X;
    	PIPE_GAP_Y = constants.PIPE_GAP_Y;
    	PIPE_WIDTH = constants.PIPE_WIDTH;
    	INITIAL_PIPE_OFFSET = constants.INITIAL_PIPE_OFFSET; 
    	REWARD_GAP_X = constants.REWARD_GAP_X;
    	PIPE_PASSED_BONUS = constants.PIPE_PASSED_BONUS;
    	REWARD_COLLECTED_BONUS = constants.REWARD_COLLECTED_BONUS;
    	REWARD_PROBABILITY = constants.REWARD_PROBABILITY;
    }
}


