package hr.fer.zemris.gen.operators.interfaces;

import java.util.SortedSet;
import hr.fer.zemris.gen.population.interfaces.IChromosome;

public interface ISelectionOperator<T, U extends IChromosome<T>> extends IParameterReciever<T, U> {
	
	public void setNumOfSelections(int numOfSelections);
	
	public SortedSet<U> performSelection(SortedSet<U> population);
}