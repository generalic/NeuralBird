package hr.fer.zemris.gen.operators.abstracts;

import java.util.Random;
import hr.fer.zemris.gen.operators.interfaces.IMutationOperator;
import hr.fer.zemris.gen.population.interfaces.IChromosome;

public abstract class AbstractMutationOperator<T, U extends IChromosome<T>> extends AbstractParameterReciever<T, U>
		implements IMutationOperator<T, U> {
		
	protected Random rand;
	
	public AbstractMutationOperator() {
		rand = new Random();
	}
}