package hr.fer.zemris.gen.operators.mutation;

import hr.fer.zemris.gen.operators.abstracts.AbstractDoubleArrayDistributionMutator;
import hr.fer.zemris.gen.population.abstracts.AbstractDoubleArrayChromosome;

public class DoubleArrayGaussianMutation<T, C extends AbstractDoubleArrayChromosome<T, ?>>
        extends AbstractDoubleArrayDistributionMutator<T, C> {
        
    public DoubleArrayGaussianMutation(double delta, double mutationChance) {
        super(delta, mutationChance);
    }
    
    @Override
    protected double randomNumber() {
        
        return rand.nextGaussian();
    }
}
