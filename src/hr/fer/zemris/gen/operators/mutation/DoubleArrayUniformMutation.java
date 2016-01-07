package hr.fer.zemris.gen.operators.mutation;

import hr.fer.zemris.gen.operators.abstracts.AbstractDoubleArrayDistributionMutator;
import hr.fer.zemris.gen.population.abstracts.AbstractDoubleArrayChromosome;

public class DoubleArrayUniformMutation<T, C extends AbstractDoubleArrayChromosome<T, ?>>
        extends AbstractDoubleArrayDistributionMutator<T, C> {
        
    public DoubleArrayUniformMutation(double delta, double mutationChance) {
        super(delta, mutationChance);
    }
    
    @Override
    protected double randomNumber() {
        
        return 2.0 * rand.nextDouble() - 1.0;
    }
}
