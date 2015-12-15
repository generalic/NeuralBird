package hr.fer.zemris.gen.alg;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.SortedSet;
import java.util.function.Function;
import hr.fer.zemris.gen.operators.interfaces.ICrossoverOperator;
import hr.fer.zemris.gen.operators.interfaces.IMutationOperator;
import hr.fer.zemris.gen.operators.interfaces.IPopulationGenerator;
import hr.fer.zemris.gen.operators.interfaces.ISelectionOperator;
import hr.fer.zemris.gen.operators.interfaces.ITestDataProvider;
import hr.fer.zemris.gen.population.interfaces.IChromosome;
import hr.fer.zemris.gen.population.interfaces.ITestData;

public abstract class AbstractGeneticAlgorithm<T, U extends IChromosome<T>> {
	
	protected int numOfIterations;
	protected int popSize;
	protected double maxFitness;
	protected double averageFitness;
	protected Random rand;
	protected SortedSet<U> population;
	protected IPopulationGenerator<T, U> popGenerator;
	protected ISelectionOperator<T, U> selectionOp;
	protected ICrossoverOperator<T, U> crossoverOp;
	protected IMutationOperator<T, U> mutationOp;
	protected ITestDataProvider<T> testDataProvider;
	protected Function<AbstractGeneticAlgorithm<T, U>, Boolean> mutationFormula;
	protected Function<AbstractGeneticAlgorithm<T, U>, Boolean> terminationCriterion;
	
	public AbstractGeneticAlgorithm(int popSize, IPopulationGenerator<T, U> popGenerator,
			ISelectionOperator<T, U> selectionOp, ICrossoverOperator<T, U> crossoverOp,
			IMutationOperator<T, U> mutationOp, ITestDataProvider<T> testDataProvider,
			Function<AbstractGeneticAlgorithm<T, U>, Boolean> mutationFormula,
			Function<AbstractGeneticAlgorithm<T, U>, Boolean> terminationCriterion) {
			
		this.popSize = popSize;
		this.popGenerator = popGenerator;
		this.selectionOp = selectionOp;
		this.crossoverOp = crossoverOp;
		this.mutationOp = mutationOp;
		this.testDataProvider = testDataProvider;
		this.mutationFormula = mutationFormula;
		this.terminationCriterion = terminationCriterion;
		
		numOfIterations = 0;
		maxFitness = 0.0;
		averageFitness = 0.0;
		rand = new Random();
	}
	
	public abstract void run();
	
	public List<U> getNBest(int n) {
		
		if (n <= 0 || n > popSize) {
			throw new IllegalArgumentException("Invalid argument for number of best chromosomes was provided: " + n
					+ ", range of acceptable values: [0, " + popSize + "].");
		}
		
		List<U> best = new LinkedList<>();
		Iterator<U> iter = population.iterator();
		
		for (int i = 0; i < n; i++) {
			best.add(iter.next());
		}
		
		return best;
	}
	
	public U getBest() {
		
		List<U> best = getNBest(1);
		
		return best.isEmpty() ? null : best.get(0);
	}
	
	public void validateOn(ITestData<T> validationData) {
		
		for (U pop : population) {
			pop.evaluateFitness(validationData);
		}
	}
	
	public int getNumOfIterations() {
		
		return numOfIterations;
	}
	
	public void setNumOfIterations(int numOfIterations) {
		
		this.numOfIterations = numOfIterations;
	}
	
	public int getPopSize() {
		
		return popSize;
	}
	
	public void setPopSize(int popSize) {
		
		this.popSize = popSize;
	}
	
	public double getMaxFitness() {
		
		return maxFitness;
	}
	
	public double getAverageFitness() {
		
		return averageFitness;
	}
	
	public Set<U> getPopulation() {
		
		return population;
	}
	
	public void setPopGenerator(IPopulationGenerator<T, U> popGenerator) {
		
		this.popGenerator = popGenerator;
	}
	
	public void setSelectionOp(ISelectionOperator<T, U> selectionOp) {
		
		this.selectionOp = selectionOp;
	}
	
	public void setCrossoverOp(ICrossoverOperator<T, U> crossoverOp) {
		
		this.crossoverOp = crossoverOp;
	}
	
	public void setMutationOp(IMutationOperator<T, U> mutationOp) {
		
		this.mutationOp = mutationOp;
	}
	
	public void setMutationFormula(Function<AbstractGeneticAlgorithm<T, U>, Boolean> mutationFormula) {
		
		this.mutationFormula = mutationFormula;
	}
	
	public void setTerminationFormula(Function<AbstractGeneticAlgorithm<T, U>, Boolean> terminationCriterion) {
		
		this.terminationCriterion = terminationCriterion;
	}
	
	public void setTestDataProvider(ITestDataProvider<T> testDataProvider) {
		
		this.testDataProvider = testDataProvider;
	}
}