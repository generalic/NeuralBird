package hr.fer.zemris.network;

public class Solution  implements Comparable<Solution>{
	NeuralNetwork network;
	double fitness;
	
	public Solution(NeuralNetwork net) {
		network= net;
	}

	@Override
	public int compareTo(Solution o) {
		return Double.compare(fitness, o.fitness);
	}

}
