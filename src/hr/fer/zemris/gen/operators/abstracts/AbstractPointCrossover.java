package hr.fer.zemris.gen.operators.abstracts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;
import hr.fer.zemris.gen.population.abstracts.AbstractDoubleArrayChromosome;

public abstract class AbstractPointCrossover<T, C extends AbstractDoubleArrayChromosome<T, ?>>
        extends AbstractCrossoverOperator<T, C> {
        
    protected SortedSet<Integer> getCrossoverPoints(int numOfCrossoverPoints, int chromosomeLength) {
        
        SortedSet<Integer> crossoverPoints = new TreeSet<>();
        
        while (crossoverPoints.size() < numOfCrossoverPoints) {
            crossoverPoints.add(rand.nextInt(chromosomeLength - 1));
        }
        
        return crossoverPoints;
    }
    
    protected List<C> getShuffledParents(SortedSet<C> parents) {
        
        List<C> shuffledParents = new ArrayList<>(parents);
        
        Collections.shuffle(shuffledParents, rand);
        
        return shuffledParents;
    }
    
    protected int mergeArrays(double[] childArray, double[] values, int i, int next) {
        
        for (; i <= next; i++) {
            childArray[i] = values[i];
        }
        
        return next + 1;
    }
}
