package hr.fer.zemris.gen.operators.abstracts;

import java.util.Random;
import hr.fer.zemris.gen.operators.interfaces.ISelectionOperator;
import hr.fer.zemris.gen.population.interfaces.IChromosome;

public abstract class AbstractSelectionOperator<T, U extends IChromosome<T>> extends AbstractParameterReciever<T, U>
		implements ISelectionOperator<T, U> {
		
	protected int numOfSelections;
	protected Random rand;
	
	public AbstractSelectionOperator(int numOfSelections) {
		rand = new Random();
		
		checkSize(numOfSelections);
		
		this.numOfSelections = numOfSelections;
	}
	
	public void setNumOfSelections(int numOfSelections) {
		
		checkSize(numOfSelections);
		
		this.numOfSelections = numOfSelections;
	}
	
	private void checkSize(int numOfSelections) {
		
		if (numOfSelections < 2) {
			throw new IllegalArgumentException("Invalid number of selections: " + numOfSelections);
		}
	}
}