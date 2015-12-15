package hr.fer.zemris.gen.operators.selection;

import java.text.DecimalFormat;
import java.util.SortedSet;
import java.util.TreeSet;
import hr.fer.zemris.gen.operators.abstracts.AbstractSelectionOperator;
import hr.fer.zemris.gen.operators.interfaces.ISelectionOperator;
import hr.fer.zemris.gen.population.abstracts.AbstractChromosome;
import hr.fer.zemris.gen.population.interfaces.IChromosome;
import hr.fer.zemris.gen.population.interfaces.ITestData;

public class RouletteWheelSelection<T, U extends IChromosome<T>> extends AbstractSelectionOperator<T, U> {
	
	public RouletteWheelSelection(int numOfSelections) {
		super(numOfSelections);
	}
	
	@Override
	public SortedSet<U> performSelection(SortedSet<U> population) {
		
		// TODO: improve
		double minFitness = Double.MAX_VALUE;
		double maxFitness = -Double.MAX_VALUE;
		
		for (U chromosome : population) {
			double fitness = chromosome.getFitness();
			
			minFitness = fitness < minFitness ? fitness : minFitness;
			maxFitness = fitness > maxFitness ? fitness : maxFitness;
		}
		
		minFitness /= 4.0;
		maxFitness /= 4.0;
		
		int popSize = population.size();
		double range = maxFitness - minFitness;
		double sum = 0.0;
		double[] weights = new double[popSize];
		
		int i = 0;
		for (U chromosome : population) {
			weights[i] = (chromosome.getFitness() / 4.0 + range) / popSize;
			sum += weights[i];
			
			if (i != 0) {
				weights[i] += weights[i - 1];
			}
			
			i++;
		}
		
		SortedSet<U> selected = new TreeSet<>();
		
		for (i = 0; i < numOfSelections; i++) {
			double number = rand.nextDouble() * sum;
			
			int j = 0;
			for (U chromosome : population) {
				if (weights[j] >= number) {
					selected.add(chromosome);
					
					break;
				}
				
				j++;
			}
		}
		
		return selected;
	}
	
	public static void main(String[] args) {
		
		class TestChromosome extends AbstractChromosome<Double> {
			
			private double value;
			private String label;
			
			public TestChromosome(double value, String label) {
				this.value = value;
				this.label = label;
			}
			
			@Override
			public double getFitness() {
				
				return value;
			}
			
			@Override
			public void calculateFitness(ITestData<Double> data) {
				
				fitness = value;
			}
			
			@Override
			public String toString() {
				
				return label;
			}
		}
		
		SortedSet<TestChromosome> testData = new TreeSet<>();
		
		testData.add(new TestChromosome(1, "a"));
		testData.add(new TestChromosome(2, "b"));
		testData.add(new TestChromosome(3, "c"));
		
		int size = 1000;
		ISelectionOperator<Double, TestChromosome> selection = new RouletteWheelSelection<>(size);
		
		int a = 0;
		int b = 0;
		int c = 0;
		
		for (TestChromosome sel : selection.performSelection(testData)) {
			switch (sel.toString()) {
				case "a":
					a++;
					
					break;
				case "b":
					b++;
					
					break;
				case "c":
					c++;
					
					break;
				default:
					System.out.println("err: " + sel);
					
					break;
			}
		}
		
		DecimalFormat formatter = new DecimalFormat("0.00");
		System.out.println("a: " + formatter.format(((double) a) / size * 100) + "%, b: "
				+ formatter.format(((double) b) / size * 100) + "%,  c: " + formatter.format(((double) c) / size * 100)
				+ "%");
	}
}