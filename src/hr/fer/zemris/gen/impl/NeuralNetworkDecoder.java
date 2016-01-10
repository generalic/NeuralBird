package hr.fer.zemris.gen.impl;

import hr.fer.zemris.game.model.GameModelAITrainable;
import hr.fer.zemris.gen.decoders.IDoubleArrayDecoder;
import hr.fer.zemris.network.NeuralNetwork;
import hr.fer.zemris.network.transfer_functions.ITransferFunction;
import hr.fer.zemris.network.transfer_functions.SigmoidTransferFunction;

import java.util.Arrays;

public class NeuralNetworkDecoder implements IDoubleArrayDecoder<NeuralNetwork> {
    
    private static final int[] NEURONS_PER_LAYER = { 7, 100, 1 };
    
    @Override
    public double[] encode(NeuralNetwork chromosome) {
        
        double[] array = chromosome.getWeights();
        
        return Arrays.copyOf(array, array.length);
    }
    
    @Override
    public void encode(NeuralNetwork chromosome, double[] values) {
        
        double[] array = chromosome.getWeights();
        
        for (int i = 0; i < array.length; i++) {
            values[i] = array[i];
        }
    }
    
    @Override
    public NeuralNetwork decode(double[] values) {
        
        ITransferFunction[] transferFunction = new ITransferFunction[NEURONS_PER_LAYER.length - 1];
        Arrays.fill(transferFunction, new SigmoidTransferFunction());
        NeuralNetwork chromosome = new NeuralNetwork(NEURONS_PER_LAYER, transferFunction, new GameModelAITrainable());
        
        chromosome.setWeights(values);
        
        return chromosome;
    }
}
