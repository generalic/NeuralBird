package hr.fer.zemris.network;

import java.util.Random;




public class SimpleMutation {
	double sigma;
	public SimpleMutation(Double sigma) {
		this.sigma=sigma;
	}
	/**
	 * Metoda implementira mutaciju neuronske mreze.
	 * Svaka tezina mutira se za mali odmak prema normalnoj razdiobi
	 * @param sol neuronska mreza koja se mutira
	 * @return mutirana mreza
	 */
	void mutate(NeuralNetwork sol){

		Random rand = new Random();

		double[] weights = sol.getWeights();

		for (int i = 0; i < weights.length; i++) {

			weights[i]+=rand.nextGaussian()*sigma;
		}
		sol.setWeights(weights);


	}

	public void setSigma(double sigma) {
		this.sigma = sigma;
	}
}
