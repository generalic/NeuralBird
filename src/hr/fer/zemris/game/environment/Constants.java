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
	
	public  int NUMBER_OF_PIPES = 5;
    public  double PIPES_SPEED_X = 8;
    public  double REWARD_SPEED_X = PIPES_SPEED_X;
    public  double PIPES_SPEED_Y = 4;
    public  double JUMP_SPEED = -20;
    public  double PIPE_GAP_X = 350;
    public  double PIPE_GAP_Y = 200;
    public  double PIPE_WIDTH = 70;
    public  double INITIAL_PIPE_OFFSET = 100;
    public  double REWARD_GAP_X = PIPE_GAP_X + PIPE_WIDTH;
    public  int PIPE_PASSED_BONUS = 1;
    public  int REWARD_COLLECTED_BONUS = 10;
    public  double REWARD_PROBABILITY = 0.5;
    
    public static Constants AIConst = new Constants();
    public static Constants PlayerConst = new Constants();
    public static Constants DefaultConst = new Constants();

    public void setDefaultConstants() {
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
   
    public void setConstants(Constants conts){
    	NUMBER_OF_PIPES = conts.NUMBER_OF_PIPES;
    	PIPES_SPEED_X = conts.PIPES_SPEED_X;
    	REWARD_SPEED_X = conts.REWARD_SPEED_X;
    	PIPES_SPEED_Y = conts.PIPES_SPEED_Y;
    	JUMP_SPEED = conts.JUMP_SPEED;
    	PIPE_GAP_X = conts.PIPE_GAP_X;
    	PIPE_GAP_Y = conts.PIPE_GAP_Y;
    	PIPE_WIDTH = conts.PIPE_WIDTH;
    	INITIAL_PIPE_OFFSET = conts.INITIAL_PIPE_OFFSET;
    	REWARD_GAP_X = PIPE_GAP_X + PIPE_WIDTH;
    	PIPE_PASSED_BONUS = conts.PIPE_PASSED_BONUS;
    	REWARD_COLLECTED_BONUS = conts.REWARD_COLLECTED_BONUS;
    	REWARD_PROBABILITY = conts.REWARD_PROBABILITY;
    }
}
