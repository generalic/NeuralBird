package hr.fer.zemris.gen.operators.abstracts;

import hr.fer.zemris.gen.alg.AbstractGeneticAlgorithm;
import hr.fer.zemris.gen.operators.interfaces.IParameterReciever;
import hr.fer.zemris.gen.population.interfaces.IChromosome;

public abstract class AbstractParameterReciever<T, U extends IChromosome<T>> implements IParameterReciever<T, U> {
	
	protected AbstractGeneticAlgorithm<T, U> parameters;
	
	@Override
	public void recieveParameters(AbstractGeneticAlgorithm<T, U> parameters) {
		
		this.parameters = parameters;
	}
}