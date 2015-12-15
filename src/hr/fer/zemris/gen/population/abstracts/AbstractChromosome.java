package hr.fer.zemris.gen.population.abstracts;

import hr.fer.zemris.gen.exceptions.FitnessNotEvaluatedException;
import hr.fer.zemris.gen.population.interfaces.IChromosome;
import hr.fer.zemris.gen.population.interfaces.ITestData;

public abstract class AbstractChromosome<T> implements IChromosome<T> {
    
    protected double fitness;
    protected boolean fitnessEvaluated;
    
    @Override
    public double getFitness() {
        
        if (!fitnessEvaluated) {
            throw new FitnessNotEvaluatedException();
        }
        
        return fitness;
    }
    
    public void evaluateFitness(ITestData<T> data) {
        
        calculateFitness(data);
        fitnessEvaluated = true;
    }
    
    protected abstract void calculateFitness(ITestData<T> data);
    
    @Override
    public int compareTo(IChromosome<T> o) {
        
        if (!fitnessEvaluated) {
            return -1;
        }
        
        if (getFitness() > o.getFitness()) {
            return -1;
        } else {
            return 1;
        }
    }
    
    @Override
    public boolean equals(Object obj) {
        
        return false;
    }
    
    @Override
    public String toString() {
        
        return Double.toString(getFitness());
    }
}
