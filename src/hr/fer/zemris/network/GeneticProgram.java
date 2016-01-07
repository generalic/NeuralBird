package hr.fer.zemris.network;

import hr.fer.zemris.game.model.GameModelAI;
import hr.fer.zemris.network.transfer_functions.ITransferFunction;
import hr.fer.zemris.network.transfer_functions.SigmoidTransferFunction;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class GeneticProgram {
    
    /** Max number of generations. */
    public static final int MAX_GENERATIONS = 50;
    /** Size of population. */
    public static final int POPULATION_SIZE = 50;

    public static final int SCORE_LIMIT = 600;
    public static final int MAX_NUM_OF_PIPES = 50;
    /** Number of neuronsPerLayer */

    private static final int[] neuronsPerLayer = { 9, 30, 1 };
    
    public NeuralNetwork train() {
        
        int numberOfGenerations = 0;
        int n = 10;
        
        TournamentSelection selectionO = new TournamentSelection();
        
        BLXAlphaCrossover crossoverO = new BLXAlphaCrossover(0.5);
        SimpleMutation mutationO = new SimpleMutation(0.01);
        
        // Napravi inicijalnu populaciju
        Solution[] population = createInitialPopulation(POPULATION_SIZE);
        
        while (numberOfGenerations < MAX_GENERATIONS) {
            
            Solution[] newPopulation = new Solution[POPULATION_SIZE];
            
            Solution[] bestTwo = getTwoBestNets(population.clone());
            newPopulation[0] = bestTwo[0];
            newPopulation[1] = bestTwo[1];
            // Napravi novu populaciju
            for (int i = 0; i < POPULATION_SIZE; i++) {
                NeuralNetwork parent1 = selectionO.select(population, n);
                NeuralNetwork parent2 = selectionO.select(population, n);
                Solution child = new Solution(crossoverO.doCrossover(parent1, parent2));
                
                mutationO.mutate(child.network);
                child.fitness = calculateFitness(child.network);
                newPopulation[i] = child;
                
            }
            
            population = newPopulation;
            numberOfGenerations++;
            double currentFit = evaluatePopulation(population);
            
            // NeuronNetwork net2 = getBestNet(population);
            // System.out.println(compareLists(net2.getWeights(), tmpNet[0].network.getWeights()));
            //
            System.out.println(currentFit + "   " + numberOfGenerations);
            
            mutationO.setSigma(currentFit < 3 ? 0.03 : 0.01);
            
        }
        NeuralNetwork bestOne = getBestNet(population);
        
        System.out.println(calculateFitness(bestOne));
        // Engine testGame = new Engine(DIMENSION, DIMENSION, (new Random()).nextInt());
        
        
        return bestOne;
    }
    
    /**
     * Metoda kreira inicijalnu populaciju
     * 
     * @param popSize velicina populacije
     * @return polje neuronskih mreza (populacija)
     */
    static Solution[] createInitialPopulation(int popSize) {
        
        Solution[] result = new Solution[popSize];
        
        ITransferFunction[] transferFunction = new ITransferFunction[neuronsPerLayer.length - 1];
        Arrays.fill(transferFunction, new SigmoidTransferFunction());
        
        for (int i = 0; i < popSize; i++) {
            Solution sol = new Solution(new NeuralNetwork(neuronsPerLayer, transferFunction, new GameModelAI()));
            double[] weights = new double[sol.network.getWeightsCount()];
            Arrays.fill(weights, new Random().nextDouble());
            sol.network.setWeights(weights);
            sol.fitness = calculateFitness(sol.network);
            result[i] = sol;
        }
        return result;
    }
    
    /**
     * Metoda vraca najmanju gresku (1/brojFrame-ova) u populaciji
     * 
     * @param population populacija
     * @return najmanja greska u populaciji
     */
    static double evaluatePopulation(Solution[] population) {
        
        double maxValue = population[0].fitness;
        for (Solution solution : population) {
            if (solution.fitness > maxValue) {
                maxValue = solution.fitness;
            }
        }
        return maxValue;
    }
    
    /**
     * Metoda vraca neuronsku mrezu koja je dosla najdalje u igrici
     * 
     * @param population populacija
     * @return najbolja neuronska mreza
     */
    static NeuralNetwork getBestNet(Solution[] population) {
        
        double maxValue = population[0].fitness;
        NeuralNetwork bestNet = population[0].network;
        for (Solution solution : population) {
            if (solution.fitness > maxValue) {
                bestNet = solution.network;
                maxValue = solution.fitness;
            }
        }
        return bestNet;
    }
    
    /**
     * Metoda vraca 2 najbolje mreze u populaciji
     * 
     * @param population populacija
     * @return 2 najbolje mreze
     */
    static Solution[] getTwoBestNets(Solution[] population) {
        
        Solution[] bestNets = new Solution[2];
        Arrays.sort(population);
        bestNets[0] = population[population.length - 1];
        bestNets[1] = population[population.length - 2];
        return bestNets;
        
    }
    
    /**
     * Metoda za racunanje greske. Greska se racuna tako da se ptica pokrene u NUMBER_OF_GAMES igara. Zatim se izracuna
     * se prosjecan broj frame-ova u NUMBER_OF_GAMES igara koji je ptica presla, a greska se racuna prema formuli
     * 1/prosjecanBrojFrame-ova
     * 
     * @param net neuronska mreza koju testiramo (ptica)
     * @return 1/prosjecanBrojFrame-ova
     */
    public static final int NUMBER_OF_GAMES = 5;
    
    static double calculateFitness(NeuralNetwork net) {
        
        double counter = 0;
        for (int i = 0; i < NUMBER_OF_GAMES; i++) {
            GameModelAI model = new GameModelAI();
            model.addEnvironmentListener(net);
            int tmpScore = 0;
            
            while (true) {
                
                if (!model.update(1) || model.getScore() > SCORE_LIMIT || model.getNumOfPipesPassed()> MAX_NUM_OF_PIPES) {
                    break;
                }
                
                tmpScore = model.getScore();
                
            }
            counter += tmpScore * 1.0;
            
        }
        counter /= NUMBER_OF_GAMES;
        return counter;
        
    }
    
    boolean compareLists(List<Double> list1, List<Double> list2) {
        
        for (int i = 0; i < list1.size(); i++) {
            if (!list1.get(i).equals(list2.get(i))) {
                return false;
            }
        }
        return true;
    }
    
    
    
}
