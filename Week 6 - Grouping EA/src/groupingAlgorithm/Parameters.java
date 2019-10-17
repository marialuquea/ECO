package groupingAlgorithm;

import java.util.Random;

public class Parameters {
	// running time - adjust this based on your own hardware
	static long runningTime = 60000;
	
	//	 filename of the problem to be used
	static String fileName = "problems/input1n.epp";
	// filename of the output file
	static String output = "output.txt";
	// default population size
	static int populationSize = 10;
	// default mutation rate
	static double mutationRate = 0.05;
	// default tournament size
	static int tournamentSize = 2;
	// default crossover method used
	static int crossoverMethod = 1;
	// default maximum iterations 
	static int maxIterations = 100000;
	// default number of runs
	static int numberOfRuns = 1;
	
	
	// default of -1 means use system clock to seed
	static long seed = -1;

	static int numberOfGroups = 9;
}
