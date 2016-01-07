package hr.fer.zemris.gen.impl;

import hr.fer.zemris.game.model.GameModelAI;
import hr.fer.zemris.gen.decoders.IDoubleArrayDecoder;
import hr.fer.zemris.gen.population.abstracts.AbstractDoubleArrayChromosome;
import hr.fer.zemris.gen.population.interfaces.ITestData;
import hr.fer.zemris.network.NeuralNetwork;

public class NeuralNetworkChromosome extends AbstractDoubleArrayChromosome<GameModelAI[], NeuralNetwork> {
    
    public static final int SCORE_LIMIT = 1000;
    
    public NeuralNetworkChromosome(NeuralNetwork chromosome, IDoubleArrayDecoder<NeuralNetwork> decoder) {
        super(chromosome, decoder);
    }
    
    public NeuralNetworkChromosome(double[] values, IDoubleArrayDecoder<NeuralNetwork> decoder) {
        super(values, decoder);
    }
    
    @Override
    public AbstractDoubleArrayChromosome<GameModelAI[], NeuralNetwork> newLikeThis(double[] values) {
        
        return new NeuralNetworkChromosome(values, decoder);
    }
    
    @Override
    protected void calculateFitness(ITestData<GameModelAI[]> data) {
        
        fitness = 0.0;
        
        GameModelAI[] models = data.getTestDataObject();
        
        for (GameModelAI model : models) {
            model.addEnvironmentListener(chromosome);
            
            int tmpScore = 0;
            
            while (true) {
                
                if (!model.update(1) || model.getScore() > SCORE_LIMIT) {
                    break;
                }
                
                tmpScore = model.getScore();
                
            }
            
            fitness += tmpScore * 1.0;
            
        }
        
        fitness /= models.length;
    }
}
