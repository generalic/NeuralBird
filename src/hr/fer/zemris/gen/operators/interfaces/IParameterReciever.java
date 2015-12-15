package hr.fer.zemris.gen.operators.interfaces;

import hr.fer.zemris.gen.alg.AbstractGeneticAlgorithm;
import hr.fer.zemris.gen.population.interfaces.IChromosome;

public interface IParameterReciever<T, U extends IChromosome<T>> {
	
	public void recieveParameters(AbstractGeneticAlgorithm<T, U> parameters);
}