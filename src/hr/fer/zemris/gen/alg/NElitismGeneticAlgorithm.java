package hr.fer.zemris.gen.alg;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.function.Function;
import hr.fer.zemris.gen.operators.interfaces.ICrossoverOperator;
import hr.fer.zemris.gen.operators.interfaces.IMutationOperator;
import hr.fer.zemris.gen.operators.interfaces.IPopulationGenerator;
import hr.fer.zemris.gen.operators.interfaces.ISelectionOperator;
import hr.fer.zemris.gen.operators.interfaces.ITestDataProvider;
import hr.fer.zemris.gen.population.interfaces.IChromosome;

public class NElitismGeneticAlgorithm<T, C extends IChromosome<T>> extends AbstractGeneticAlgorithm<T, C> {
    
    private int elitismSize;
    
    public NElitismGeneticAlgorithm(int popSize, int elitismSize, IPopulationGenerator<T, C> popGenerator,
            ISelectionOperator<T, C> selectionOp, ICrossoverOperator<T, C> crossoverOp,
            IMutationOperator<T, C> mutationOp, ITestDataProvider<T> testDataProvider,
            Function<AbstractGeneticAlgorithm<T, C>, Boolean> mutationFormula,
            Function<AbstractGeneticAlgorithm<T, C>, Boolean> terminationCriterion) {
        super(popSize, popGenerator, selectionOp, crossoverOp, mutationOp, testDataProvider, mutationFormula,
                terminationCriterion);
                
        this.elitismSize = elitismSize;
    }
    
    @Override
    public void run() {
        
        population = popGenerator.generatePopulation(popSize);
        
        for (C pop : population) {
            pop.evaluateFitness(testDataProvider.getTestData());
        }
        
        SortedSet<C> newPopulation = new TreeSet<>();
        
        int generation = 1;
        while (!terminationCriterion.apply(this)) {
            Iterator<C> iter = population.iterator();
            
            for (int i = 0; i < elitismSize && iter.hasNext(); i++) {
                newPopulation.add(iter.next());
            }
            
            for (int i = 0; i < popSize - elitismSize; i++) {
                SortedSet<C> parents = selectionOp.performSelection(population);
                
                C child = crossoverOp.performCrossover(parents);
                // TODO: dodati šansu
                mutationOp.performMutation(child);
                
                child.evaluateFitness(testDataProvider.getTestData());
                newPopulation.add(child);
            }
            
            population.clear();
            population.addAll(newPopulation);
            newPopulation.clear();
            
            maxFitness = population.first().getFitness();
            
            System.out.println("Generation: " + generation + ", current maximum fitness: " + maxFitness);
            
            generation++;
        }
    }
}
