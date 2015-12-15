package hr.fer.zemris.gen.operators.abstracts;

import java.util.Random;
import hr.fer.zemris.gen.operators.interfaces.ICrossoverOperator;
import hr.fer.zemris.gen.population.interfaces.IChromosome;

public abstract class AbstractCrossoverOperator<T, U extends IChromosome<T>> extends AbstractParameterReciever<T, U>
		implements ICrossoverOperator<T, U> {
		
	protected Random rand;
	
	public AbstractCrossoverOperator() {
		rand = new Random();
	}
}
