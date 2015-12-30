package hr.fer.zemris.gen.operators.crossover;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import hr.fer.zemris.gen.decoders.IDoubleArrayDecoder;
import hr.fer.zemris.gen.exceptions.InvalidNumberOfParentsException;
import hr.fer.zemris.gen.operators.abstracts.AbstractPointCrossover;
import hr.fer.zemris.gen.operators.interfaces.ICrossoverOperator;
import hr.fer.zemris.gen.population.abstracts.AbstractDoubleArrayChromosome;
import hr.fer.zemris.gen.population.interfaces.ITestData;

public class DoubleArraySinglePointCrossover<T, C extends AbstractDoubleArrayChromosome<T, ?>>
        extends AbstractPointCrossover<T, C> {
        
    @Override
    public C performCrossover(SortedSet<C> parents) {
        
        int numOfCrossoverPoints = parents.size() - 1;
        int chromosomeLength = parents.first().getArraySize();
        
        if (numOfCrossoverPoints < 1 || numOfCrossoverPoints >= chromosomeLength) {
            throw new InvalidNumberOfParentsException(
                    "Invalid number of parents was provided: " + numOfCrossoverPoints);
        }
        
        SortedSet<Integer> crossoverPoints = getCrossoverPoints(numOfCrossoverPoints, chromosomeLength);
        List<C> shuffledParents = getShuffledParents(parents);
        
        double[] childArray = new double[chromosomeLength];
        int i = 0;
        
        Iterator<Integer> iter = crossoverPoints.iterator();
        for (C parent : shuffledParents) {
            int next = iter.hasNext() ? iter.next() : childArray.length - 1;
            i = mergeArrays(childArray, parent.readValues(), i, next);
        }
        
        return (C) parents.first().newLikeThis(childArray);
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
        
        parents.add(new TestDoubleArray(new double[] { 4.0, 4.0, 4.0, 4.0, 4.0, 4.0 }, decoder));
        parents.add(new TestDoubleArray(new double[] { 5.0, 5.0, 5.0, 5.0, 5.0, 5.0 }, decoder));
        parents.add(new TestDoubleArray(new double[] { 6.0, 6.0, 6.0, 6.0, 6.0, 6.0 }, decoder));
        parents.add(new TestDoubleArray(new double[] { 7.0, 7.0, 7.0, 7.0, 7.0, 7.0 }, decoder));
        parents.add(new TestDoubleArray(new double[] { 8.0, 8.0, 8.0, 8.0, 8.0, 8.0 }, decoder));
        parents.add(new TestDoubleArray(new double[] { 9.0, 9.0, 9.0, 9.0, 9.0, 9.0 }, decoder));
        
        // parents.add(new TestDoubleArray(new double[] { 8.0, 8.0, 8.0, 8.0, 8.0, 8.0 }, decoder));
        
        ICrossoverOperator<Double, AbstractDoubleArrayChromosome<Double, Double[]>> cop =
                new DoubleArraySinglePointCrossover<>();
                
        cop.performCrossover(parents);
    }
}
