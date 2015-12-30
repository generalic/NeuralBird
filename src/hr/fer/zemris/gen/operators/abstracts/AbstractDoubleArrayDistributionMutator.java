package hr.fer.zemris.gen.operators.abstracts;

import hr.fer.zemris.gen.population.abstracts.AbstractDoubleArrayChromosome;

public abstract class AbstractDoubleArrayDistributionMutator<T, C extends AbstractDoubleArrayChromosome<T, ?>>
        extends AbstractMutationOperator<T, C> {
        
    protected double delta;
    protected double mutationChance;
    
    public AbstractDoubleArrayDistributionMutator(double delta, double mutationChance) {
        
        this.delta = delta;
        this.mutationChance = mutationChance;
    }
    
    @Override
    public void performMutation(C unit) {
        
        double[] values = unit.getValues();
        
        for (int i = 0; i < values.length; i++) {
            double number = rand.nextDouble();
            
            if (number <= mutationChance) {
                values[i] += randomNumber() * delta;
            }
        }
        
        unit.setValues(values);
    }
    
    protected abstract double randomNumber();
}
