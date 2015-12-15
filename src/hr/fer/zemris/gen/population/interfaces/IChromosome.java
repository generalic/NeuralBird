package hr.fer.zemris.gen.population.interfaces;

public interface IChromosome<T> extends Comparable<IChromosome<T>> {
	
	public void evaluateFitness(ITestData<T> data);
	
	public double getFitness();
}