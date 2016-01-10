package hr.fer.zemris.gen.impl;

import hr.fer.zemris.game.model.GameModelAITrainable;
import hr.fer.zemris.gen.operators.interfaces.ITestDataProvider;
import hr.fer.zemris.gen.population.interfaces.ITestData;

public class FlappyBirdTestDataProivder implements ITestDataProvider<GameModelAITrainable[]> {
    
    private FlappyBirdTestData testData;
    
    public FlappyBirdTestDataProivder() {
        testData = new FlappyBirdTestData();
    }
    
    @Override
    public ITestData<GameModelAITrainable[]> getTestData() {
        
        return testData;
    }
}
