package hr.fer.zemris.gen.operators.mutation;

import hr.fer.zemris.gen.operators.abstracts.AbstractDoubleArrayDistributionMutator;

public class DoubleArrayGaussianMutation<T, C> extends AbstractDoubleArrayDistributionMutator<T, C> {
	
	public DoubleArrayGaussianMutation(double delta, double mutationChance) {
		super(delta, mutationChance);
	}
	
	@Override
	protected double randomNumber() {
		
		return rand.nextGaussian();
	}
}