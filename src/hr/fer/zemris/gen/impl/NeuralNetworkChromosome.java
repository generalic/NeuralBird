package hr.fer.zemris.gen.impl;

import hr.fer.zemris.game.model.GameModel;
import hr.fer.zemris.gen.decoders.IDoubleArrayDecoder;
import hr.fer.zemris.gen.population.abstracts.AbstractDoubleArrayChromosome;
import hr.fer.zemris.gen.population.interfaces.ITestData;
import hr.fer.zemris.network.NeuralNetwork;

public class NeuralNetworkChromosome extends AbstractDoubleArrayChromosome<GameModel[], NeuralNetwork> {
    
    public static final int SCORE_LIMIT = 1000;
    
    public NeuralNetworkChromosome(NeuralNetwork chromosome, IDoubleArrayDecoder<NeuralNetwork> decoder) {
        super(chromosome, decoder);
    }
    
    public NeuralNetworkChromosome(double[] values, IDoubleArrayDecoder<NeuralNetwork> decoder) {
        super(values, decoder);
    }
    
    @Override
    public AbstractDoubleArrayChromosome<GameModel[], NeuralNetwork> newLikeThis(double[] values) {
        
        return new NeuralNetworkChromosome(values, decoder);
    }
    
    @Override
    protected void calculateFitness(ITestData<GameModel[]> data) {
        
        fitness = 0.0;
        
        GameModel[] models = data.getTestDataObject();
        
        for (GameModel model : models) {
            model.addEnvironmentListener(chromosome);
            
            int tmpScore = 0;
            
            while (true) {
                
                if (!model.update(1) || model.getCurrentScore() > SCORE_LIMIT) {
                    break;
                }
                
                tmpScore = model.getCurrentScore();
                
            }
            
            fitness += tmpScore * 1.0;
            
        }
        
        fitness /= models.length;
    }
}
