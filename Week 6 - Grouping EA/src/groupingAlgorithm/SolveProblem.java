package groupingAlgorithm;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StreamTokenizer;
import java.util.Arrays;
import java.util.Random;

/**
 * This class provides a basic evolutionary algorithm for solving the equal
 * grouping problem implemented in Java. A direct representation is used: each
 * item is assigned a group in which it is placed. The chromosome for each
 * individual includes an entry for each item in the problem. Basic
 * functionality is provided:
 * <ul>
 * <li>loading a problem file
 * <li>initialising a population
 * <li>an evaluation method for each individual
 * <li>a basic loop for the EA itself
 * <li>one basic crossover methods
 * <li>one basic mutation method
 * <li>a tournament selection method
 * */
public class SolveProblem{
	

	int[][] population;
	int[] fitness;
	int[] itemLength;
	int numberOfItems;  // set when file is read
	int numberOfGroups;
	
	String fileName;
	String output;
	int populationSize;
	double mutationRate;
	int tournamentSize;
	int crossoverMethod;
	int maxIterations;
	int numberOfRuns;
	long seed;
	long runningTime;
	Random random;
	

	
	/**
	 * Constructor for SolveProblem class.
	 */
	public SolveProblem() {
		// set parameters
		runningTime = Parameters.runningTime;
		fileName = Parameters.fileName;
		output = Parameters.output;
		populationSize = Parameters.populationSize;
		mutationRate = Parameters.mutationRate;
		tournamentSize = Parameters.tournamentSize;
		crossoverMethod = Parameters.crossoverMethod;
		maxIterations = Parameters.maxIterations;
		numberOfRuns=Parameters.numberOfRuns;
		seed = Parameters.seed;
		numberOfGroups = Parameters.numberOfGroups;

		if (seed == -1)
			seed = System.currentTimeMillis();
		random = new Random(seed);
	
	
		loadProblem(fileName);

		 // create random generator
		
	}

	
	/**
	 * Run method for EqualGroups class. Used to run the algorithm after
	 * construction.
	 */
	public void run() {
		// Initialise population
		initialisePopulation();
		
		// Start loop for Evolutionary Algorithm

		// start clock
		long startTime = System.currentTimeMillis(); 
		
		while (System.currentTimeMillis() - startTime < runningTime){
		//	for (int iteration = 0; iteration < maxIterations; iteration++) {
			// Select first parent
			int firstParentIndex = tournamentSelect();
			// Select second parent
			int secondParentIndex = tournamentSelect();
			// Create child
			int[] child;
			// Recombine child using both parents
			switch (crossoverMethod) {
			case 0:
				child = uniformCrossover(firstParentIndex, secondParentIndex);
				break;
			case 1:
				child = onepointCrossover(firstParentIndex, secondParentIndex);
				break;
			case 2:
				child = twopointCrossover(firstParentIndex, secondParentIndex);
				break;
			default:
				child = uniformCrossover(firstParentIndex, secondParentIndex);
				break;
			}
			
			// Mutate child
			child = mutate(child);

			// Evaluate child
			int childFitness = evaluate(child);
		
			// Replace parent with worst fitness value if child is better
			replaceWorstParent(child, childFitness, firstParentIndex,
				secondParentIndex);
		}
	}

	/**
	 * Method to output result both to the console and a file (default:
	 * output.txt). Note that all results are appended to the file if it already
	 * exists!
	 */
	public void outputResult() {
		int bestIndex = 0;
		int bestFitness = fitness[bestIndex];
		for (int index = 0; index < fitness.length; index++) {
			if (fitness[index] < bestFitness) {
				bestFitness = fitness[index];
				bestIndex = index;
			}
		}
		System.out.println("Best result found is: " + bestFitness);
		try {
			BufferedWriter logFile = new BufferedWriter(new FileWriter(output,
				true));
			logFile.write("Best Individual: " + printIndividual(bestIndex)
				+ "\n");
			logFile.close();
		} catch (IOException e) {
			System.out.println("log file error");
		}
	}

	/**
	 * Prints an individual and its fitness
	 * 
	 * @param aIndex
	 *            index of the individual
	 * @return String representation of the individual
	 */
	private String printIndividual(int aIndex) {
		StringBuilder result = new StringBuilder();
		result.append("[ ");
		for (int gene : population[aIndex]) {
			result.append(gene);
			result.append(", ");
		}
		result.replace(result.length() - 2, result.length(), " :");
		result.append(" " + fitness[aIndex] + " ]");
		return result.toString();
	}

	/**
	 * Tournament selection method for selecting a parent.
	 * 
	 * @return index to the selected parent
	 */
	private int tournamentSelect() {
		int index = random.nextInt(population.length);
		int pickedFitness = fitness[index];
		int bestFitness = pickedFitness;
		int bestIndex = index;
		for (int i = 0; i < tournamentSize - 1; i++) {
			index = random.nextInt(population.length);
			pickedFitness = fitness[index];
			if (pickedFitness < bestFitness) {
				bestFitness = pickedFitness;
				bestIndex = index;
			}
		}
		return bestIndex;
	}

	/**
	 * Mutation method for mutating a child.
	 * 
	 * @param aChild
	 *            child individual to mutate
	 * @return a mutated child individual
	 */
	private int[] mutate(int[] aChild) {
		int[] result = new int[aChild.length];
		for (int index = 0; index < aChild.length; index++)
			if (random.nextDouble() < mutationRate)
				result[index] = random.nextInt(numberOfGroups);
			else
				result[index] = aChild[index];
		return result;
	}

	/**
	 * Uniform crossover method for recombining two parent individuals into one
	 * child individual.
	 * 
	 * @param aFirstParentIndex
	 *            first parent individual index
	 * @param aSecondParentIndex
	 *            second parent individual index
	 * @return recombined child individual
	 */
	private int[] uniformCrossover(int aFirstParentIndex, int aSecondParentIndex) {
		int[] child = new int[population[aFirstParentIndex].length];
		for (int index = 0; index < population[aFirstParentIndex].length; index++) {
			if (random.nextBoolean())
				child[index] = population[aFirstParentIndex][index];
			else
				child[index] = population[aSecondParentIndex][index];
		}
		return child;
	}

	/**
	 * One point crossover method for recombining two parent individuals into
	 * one child individual.
	 * 
	 * @param aFirstParentIndex
	 *            first parent individual index
	 * @param aSecondParentIndex
	 *            second parent individual index
	 * @return recombined child individual
	 */
	private int[] onepointCrossover(int aFirstParentIndex,
			int aSecondParentIndex) {
		int[] child = new int[population[aFirstParentIndex].length];
		// doesn't do anything yet!!
		
		return child;
	}

	/**
	 * Two point crossover method for recombining two parent individuals into
	 * one child individual.
	 * 
	 * @param aFirstParentIndex
	 *            first parent individual index
	 * @param aSecondParentIndex
	 *            second parent individual index
	 * @return recombined child individual
	 */
	private int[] twopointCrossover(int aFirstParentIndex,
			int aSecondParentIndex) {
		int[] child = new int[population[aFirstParentIndex].length];
		
		// doesn't do anything yet!!
		
		return child;
	}

	/**
	 * Initialise population method for initialising the population.
	 */
	private void initialisePopulation() {
		population = new int[populationSize][numberOfItems];
		fitness = new int[populationSize];
		for (int individualIndex = 0; individualIndex < populationSize; individualIndex++) {
			for (int itemIndex = 0; itemIndex < numberOfItems; itemIndex++)
				population[individualIndex][itemIndex] = random.nextInt(numberOfGroups);
			fitness[individualIndex] = evaluate(population[individualIndex]);
		}
	}

	/**
	 * Evaluation method for evaluating an individual.
	 * 
	 * @param aIndividual
	 *            individual to be evaluated
	 * @return fitness/evaluation value for the individual
	 */
	private int evaluate(int[] aIndividual) {
		int result;
		int[] weight = new int[numberOfGroups];
		Arrays.fill(weight, 0);
		for (int itemCounter = 0; itemCounter < numberOfItems; itemCounter++) {
			weight[aIndividual[itemCounter]] += itemLength[itemCounter];
		}
		Arrays.sort(weight);
		result = weight[numberOfGroups - 1] - weight[0];
		return result;
	}

	/**
	 * Replacement method. Replaces the parent with the worst fitness value with
	 * the child if the child has a better fitness value.
	 * 
	 * @param aChild
	 *            child individual
	 * @param aChildFitness
	 *            child fitness
	 * @param aFirstParentIndex
	 *            first parent index
	 * @param aSecondParentIndex
	 *            second parent index
	 */
	private void replaceWorstParent(int[] aChild, int aChildFitness,
			int aFirstParentIndex, int aSecondParentIndex) {
		// Find worst parent
		int worstParentIndex = aFirstParentIndex;
		int worstFitness = fitness[aFirstParentIndex];
		if (worstFitness < fitness[aSecondParentIndex]) {
			worstParentIndex = aSecondParentIndex;
			worstFitness = fitness[aSecondParentIndex];
		}
		// Replace child if worst parent fitness is worse
		if (aChildFitness < worstFitness) {
			for (int itemIndex = 0; itemIndex <numberOfItems; itemIndex++)
				population[worstParentIndex][itemIndex] = aChild[itemIndex];
			fitness[worstParentIndex] = aChildFitness;
		}
	}

	/**
	 * Method for loading a problem
	 * 
	 * @param aFileName
	 *            filename of the problem file.
	 */
	private void loadProblem(String aFileName) {
		try {
			BufferedReader input = new BufferedReader(new InputStreamReader(
				new FileInputStream(aFileName)));
			StreamTokenizer streamTokenizer = new StreamTokenizer(input);
			streamTokenizer.parseNumbers();
			streamTokenizer.nextToken();
			numberOfItems = (int) streamTokenizer.nval;
			itemLength = new int[numberOfItems];
			for (int tokens = 0; tokens < numberOfItems; tokens++) {
				streamTokenizer.nextToken();
				itemLength[tokens] = (int) streamTokenizer.nval;
			}
		} catch (Exception e) {
			System.out.println("Error reading problem file " + aFileName);
		}
	}
}