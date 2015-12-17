package hr.fer.zemris.gen.operators.crossover;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import hr.fer.zemris.gen.decoders.IDoubleArrayDecoder;
import hr.fer.zemris.gen.exceptions.InvalidNumberOfParentsException;
import hr.fer.zemris.gen.operators.abstracts.AbstractPointCrossover;
import hr.fer.zemris.gen.operators.interfaces.ICrossoverOperator;
import hr.fer.zemris.gen.population.abstracts.AbstractDoubleArrayChromosome;
import hr.fer.zemris.gen.population.interfaces.ITestData;

public class DoubleArrayMultiPointCrossover<T, C extends AbstractDoubleArrayChromosome<T, ?>>
        extends AbstractPointCrossover<T, C> {
        
    private int numOfCrossoverPoints;
    
    public DoubleArrayMultiPointCrossover(int numOfCrossoverPoints) {
        
        if (numOfCrossoverPoints < 1) {
            throw new IllegalArgumentException("Invalid number of crossover points: " + numOfCrossoverPoints);
        }
        
        this.numOfCrossoverPoints = numOfCrossoverPoints;
    }
    
    @Override
    public C performCrossover(SortedSet<C> parents) {
        
        int chromosomeLength = parents.first().getArraySize();
        
        if (parents.size() != 2) {
            throw new InvalidNumberOfParentsException("Invalid number of parents was provided: " + parents.size());
        }
        
        Set<Integer> crossoverPoints = getCrossoverPoints(numOfCrossoverPoints, chromosomeLength);
        List<C> shuffledParents = getShuffledParents(parents);
        
        double[] childArray = new double[chromosomeLength];
        int i = 0;
        int j = 0;
        
        Iterator<Integer> iter = crossoverPoints.iterator();
        while (iter.hasNext()) {
            int point = iter.next();
            
            if (!iter.hasNext()) {
                point = childArray.length - 1;
            }
            
            i = mergeArrays(childArray, shuffledParents.get(j).readValues(), i, point);
            j = 1 - j;
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
        
        ICrossoverOperator<Double, AbstractDoubleArrayChromosome<Double, Double[]>> cop =
                new DoubleArrayMultiPointCrossover<>(3);
                
        cop.performCrossover(parents);
    }
}
