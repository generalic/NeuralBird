package hr.fer.zemris.gen.impl;

import hr.fer.zemris.game.model.GameModel;
import hr.fer.zemris.gen.population.interfaces.ITestData;

public class FlappyBirdTestData implements ITestData<GameModel[]> {
    
    private static final int NUMBER_OF_GAMES = 5;
    
    @Override
    public GameModel[] getTestDataObject() {
        
        GameModel[] models = new GameModel[NUMBER_OF_GAMES];
        
        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            models[i] = new GameModel();
        }
        
        return models;
    }
}
