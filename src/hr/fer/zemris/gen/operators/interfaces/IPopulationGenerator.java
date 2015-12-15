package hr.fer.zemris.gen.operators.interfaces;

import java.util.SortedSet;
import hr.fer.zemris.gen.population.interfaces.IChromosome;

public interface IPopulationGenerator<T, U extends IChromosome<T>> extends IParameterReciever<T, U> {
	
	public SortedSet<U> generatePopulation(int size);
}