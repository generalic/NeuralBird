package hr.fer.zemris.gen.impl;

import hr.fer.zemris.game.model.GameModelAITrainable;
import hr.fer.zemris.gen.population.interfaces.ITestData;

public class FlappyBirdTestData implements ITestData<GameModelAITrainable[]> {
    
    private static final int NUMBER_OF_GAMES = 5;
    
    @Override
    public GameModelAITrainable[] getTestDataObject() {
        
        GameModelAITrainable[] models = new GameModelAITrainable[NUMBER_OF_GAMES];
        
        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            models[i] = new GameModelAITrainable();
        }
        
        return models;
    }
}
