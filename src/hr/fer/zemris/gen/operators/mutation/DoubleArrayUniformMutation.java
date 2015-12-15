package hr.fer.zemris.gen.operators.mutation;

import hr.fer.zemris.gen.operators.abstracts.AbstractDoubleArrayDistributionMutator;

public class DoubleArrayUniformMutation<T, C> extends AbstractDoubleArrayDistributionMutator<T, C> {
	
	public DoubleArrayUniformMutation(double delta, double mutationChance) {
		super(delta, mutationChance);
	}
	
	@Override
	protected double randomNumber() {
		
		return 2.0 * rand.nextDouble() - 1.0;
	}
}