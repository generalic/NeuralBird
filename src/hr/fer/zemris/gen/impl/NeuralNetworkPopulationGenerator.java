package hr.fer.zemris.gen.impl;

import hr.fer.zemris.game.model.GameModelAI;
import hr.fer.zemris.gen.decoders.IDoubleArrayDecoder;
import hr.fer.zemris.gen.operators.abstracts.AbstractPopulationGenerator;
import hr.fer.zemris.network.NeuralNetwork;
import hr.fer.zemris.network.transfer_functions.ITransferFunction;
import hr.fer.zemris.network.transfer_functions.SigmoidTransferFunction;

import java.util.Arrays;
import java.util.Random;
import java.util.SortedSet;
import java.util.TreeSet;

public class NeuralNetworkPopulationGenerator
        extends AbstractPopulationGenerator<GameModelAI[], NeuralNetworkChromosome> {
        
    private static final int[] NEURONS_PER_LAYER = { 7, 100, 1 };
    private IDoubleArrayDecoder<NeuralNetwork> decoder;
    
    public NeuralNetworkPopulationGenerator(IDoubleArrayDecoder<NeuralNetwork> decoder) {
        this.decoder = decoder;
    }
    
    @Override
    public SortedSet<NeuralNetworkChromosome> generatePopulation(int size) {
        
        TreeSet<NeuralNetworkChromosome> population = new TreeSet<>();
        
        ITransferFunction[] transferFunction = new ITransferFunction[NEURONS_PER_LAYER.length - 1];
        Arrays.fill(transferFunction, new SigmoidTransferFunction());
        
        for (int i = 0; i < size; i++) {
            NeuralNetwork network = new NeuralNetwork(NEURONS_PER_LAYER, transferFunction, new GameModelAI());
            
            double[] weights = new double[network.getWeightsCount()];
            Arrays.fill(weights, new Random().nextDouble());
            
            network.setWeights(weights);
            population.add(new NeuralNetworkChromosome(network, decoder));
        }
        
        return population;
    }
    
}
