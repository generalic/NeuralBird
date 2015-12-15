package hr.fer.zemris.gen.operators.crossover;

import java.util.SortedSet;
import java.util.TreeSet;
import hr.fer.zemris.gen.decoders.IDoubleArrayDecoder;
import hr.fer.zemris.gen.exceptions.InvalidNumberOfParentsException;
import hr.fer.zemris.gen.operators.abstracts.AbstractCrossoverOperator;
import hr.fer.zemris.gen.operators.interfaces.ICrossoverOperator;
import hr.fer.zemris.gen.population.abstracts.AbstractDoubleArrayChromosome;
import hr.fer.zemris.gen.population.interfaces.ITestData;

public class DoubleArrayArithmeticMeanCrossover<T, C>
        extends AbstractCrossoverOperator<T, AbstractDoubleArrayChromosome<T, C>> {
        
    @Override
    public AbstractDoubleArrayChromosome<T, C>
            performCrossover(SortedSet<AbstractDoubleArrayChromosome<T, C>> parents) {
            
        int crossoverSize = parents.size();
        int chromosomeLength = parents.first().getArraySize();
        
        if (crossoverSize <= 1) {
            throw new InvalidNumberOfParentsException("Invalid number of parents was provided: " + crossoverSize);
        }
        
        double[] childArray = new double[chromosomeLength];
        
        for (AbstractDoubleArrayChromosome<T, C> parent : parents) {
            double[] parentArray = parent.readValues();
            
            for (int i = 0; i < childArray.length; i++) {
                childArray[i] += parentArray[i];
            }
        }
        
        for (int i = 0; i < childArray.length; i++) {
            childArray[i] /= crossoverSize;
        }
        
        return parents.first().newLikeThis(childArray);
    }
    
    public static void main(String[] args) {
        
        class TestDoubleArray extends AbstractDoubleArrayChromosome<Double, Double[]> {
            
            public TestDoubleArray(Double[] chromosome, IDoubleArrayDecoder<Double[]> decoder) {
                super(chromosome, decoder);
            }
            
            public TestDoubleArray(double[] values, IDoubleArrayDecoder<Double[]> decoder) {
                super(values, decoder);
            }
            
            @Override
            public void calculateFitness(ITestData<Double> data) {
                // Not needed here
            }
            
            @Override
            public double getFitness() {
                
                return 0.0;
            }
            
            @Override
            public AbstractDoubleArrayChromosome<Double, Double[]> newLikeThis(double[] values) {
                
                return new TestDoubleArray(values, decoder);
            }
        }
        
        class TestDecoder implements IDoubleArrayDecoder<Double[]> {
            
            @Override
            public double[] encode(Double[] chromosome) {
                
                double[] output = new double[chromosome.length];
                
                for (int i = 0; i < chromosome.length; i++) {
                    output[i] = chromosome[i];
                }
                
                return output;
            }
            
            @Override
            public void encode(Double[] chromosome, double[] values) {
                
                for (int i = 0; i < chromosome.length; i++) {
                    values[i] = chromosome[i];
                }
            }
            
            @Override
            public Double[] decode(double[] values) {
                
                Double[] output = new Double[values.length];
                
                for (int i = 0; i < values.length; i++) {
                    output[i] = values[i];
                }
                
                return output;
            }
        }
        
        SortedSet<AbstractDoubleArrayChromosome<Double, Double[]>> parents = new TreeSet<>();
        IDoubleArrayDecoder<Double[]> decoder = new TestDecoder();
        
        parents.add(new TestDoubleArray(new double[] { 1.0, 1.0 }, decoder));
        parents.add(new TestDoubleArray(new double[] { 2.0, 2.0 }, decoder));
        parents.add(new TestDoubleArray(new double[] { 3.0, 3.0 }, decoder));
        
        ICrossoverOperator<Double, AbstractDoubleArrayChromosome<Double, Double[]>> cop =
                new DoubleArrayArithmeticMeanCrossover<>();
                
        cop.performCrossover(parents);
    }
}
