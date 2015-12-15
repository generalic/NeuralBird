package hr.fer.zemris.gen.operators.abstracts;

import java.util.Random;
import hr.fer.zemris.gen.operators.interfaces.IPopulationGenerator;
import hr.fer.zemris.gen.population.interfaces.IChromosome;

public abstract class AbstractPopulationGenerator<T, U extends IChromosome<T>> extends AbstractParameterReciever<T, U>
		implements IPopulationGenerator<T, U> {
		
	protected Random rand;
	
	public AbstractPopulationGenerator() {
		rand = new Random();
	}
}
