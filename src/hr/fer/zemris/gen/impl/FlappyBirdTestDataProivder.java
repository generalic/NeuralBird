package hr.fer.zemris.gen.impl;

import hr.fer.zemris.game.model.GameModel;
import hr.fer.zemris.gen.operators.interfaces.ITestDataProvider;
import hr.fer.zemris.gen.population.interfaces.ITestData;

public class FlappyBirdTestDataProivder implements ITestDataProvider<GameModel[]> {
    
    private FlappyBirdTestData testData;
    
    public FlappyBirdTestDataProivder() {
        testData = new FlappyBirdTestData();
    }
    
    @Override
    public ITestData<GameModel[]> getTestData() {
        
        return testData;
    }
}
