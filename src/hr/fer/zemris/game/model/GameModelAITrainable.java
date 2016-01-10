package hr.fer.zemris.game.model;

public class GameModelAITrainable extends GameModelAI {

    @Override
    protected double initialisableGround(double nextGroundX) {
        //returns 0 performance-wise
        return 0;
    }

    @Override
    protected void movableGround(int time) {
        //does nothing performance-wise
    }

}
