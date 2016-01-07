package hr.fer.zemris.gen.impl;

import hr.fer.zemris.game.model.GameModelAI;
import hr.fer.zemris.gen.population.interfaces.ITestData;

public class FlappyBirdTestData implements ITestData<GameModelAI[]> {
    
    private static final int NUMBER_OF_GAMES = 5;
    
    @Override
    public GameModelAI[] getTestDataObject() {
        
        GameModelAI[] models = new GameModelAI[NUMBER_OF_GAMES];
        
        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            models[i] = new GameModelAI();
        }
        
        return models;
    }
}
