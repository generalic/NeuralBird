package hr.fer.zemris.gen.impl;

import hr.fer.zemris.game.model.GameModel;
import hr.fer.zemris.game.model.GameModelAI;
import hr.fer.zemris.gen.operators.interfaces.ITestDataProvider;
import hr.fer.zemris.gen.population.interfaces.ITestData;

public class FlappyBirdTestDataProivder implements ITestDataProvider<GameModelAI[]> {
    
    private FlappyBirdTestData testData;
    
    public FlappyBirdTestDataProivder() {
        testData = new FlappyBirdTestData();
    }
    
    @Override
    public ITestData<GameModelAI[]> getTestData() {
        
        return testData;
    }
}
