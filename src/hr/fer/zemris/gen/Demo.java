package hr.fer.zemris.gen;

import hr.fer.zemris.game.model.GameModel;
import hr.fer.zemris.gen.alg.AbstractGeneticAlgorithm;
import hr.fer.zemris.gen.alg.NElitismGeneticAlgorithm;
import hr.fer.zemris.gen.impl.FlappyBirdTestDataProivder;
import hr.fer.zemris.gen.impl.NeuralNetworkChromosome;
import hr.fer.zemris.gen.impl.NeuralNetworkDecoder;
import hr.fer.zemris.gen.impl.NeuralNetworkPopulationGenerator;
import hr.fer.zemris.gen.operators.crossover.DoubleArrayArithmeticMeanCrossover;
import hr.fer.zemris.gen.operators.interfaces.IPopulationGenerator;
import hr.fer.zemris.gen.operators.interfaces.ISelectionOperator;
import hr.fer.zemris.gen.operators.interfaces.ITestDataProvider;
import hr.fer.zemris.gen.operators.mutation.DoubleArrayGaussianMutation;
import hr.fer.zemris.gen.operators.selection.TournamentSelection;

public class Demo {
    
    public static void main(String[] args) {
        
        IPopulationGenerator<GameModel[], NeuralNetworkChromosome> popGen =
                new NeuralNetworkPopulationGenerator(new NeuralNetworkDecoder());
        ISelectionOperator<GameModel[], NeuralNetworkChromosome> selOp = new TournamentSelection<>(2, 10);
        DoubleArrayArithmeticMeanCrossover crossOp = new DoubleArrayArithmeticMeanCrossover<>();
        DoubleArrayGaussianMutation mutOp = new DoubleArrayGaussianMutation<>(0.01, 1.0);
        ITestDataProvider<GameModel[]> provider = new FlappyBirdTestDataProivder();
        
        AbstractGeneticAlgorithm<GameModel[], NeuralNetworkChromosome> alg = new NElitismGeneticAlgorithm<>(100, 0,
                popGen, selOp, crossOp, mutOp, provider, null, (genAlg) -> genAlg.getAverageFitness() >= 1000.0);
                
        alg.run();
    }
}
