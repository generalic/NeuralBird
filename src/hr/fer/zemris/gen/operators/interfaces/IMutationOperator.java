package hr.fer.zemris.gen.operators.interfaces;

import hr.fer.zemris.gen.population.interfaces.IChromosome;

public interface IMutationOperator<T, U extends IChromosome<T>> extends IParameterReciever<T, U> {
	
	public void performMutation(U unit);
}