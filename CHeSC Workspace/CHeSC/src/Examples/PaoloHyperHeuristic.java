package Examples;


import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;

/**
 * This class presents an example hyper-heuristic which demonstrates how to use the different types of low level heuristic 
 * in each Problem Domain. it is intended to be read after the ExampleHyperHeuristic2 class has been understood.
 * There are four types of low level heuristic within each domain of the competition software:
 * <br>
 * 1) Local Search<br>
 * 2) Mutation<br>
 * 3) Ruin-Recreate<br>
 * 4) Crossover<br>
 * <p>
 * the methods to retrieve the indices of the heuristics of each type are given in the example below.
 * if there are no heuristics of a certain type, then the method returns null. Using the information about the type of the heuristic 
 * is potentially powerful, but it is important to check that the problem domain contains heuristics of that type, by checking 
 * for equality with null. Examples of such checks can be seen in this class.
 * <p>
 * to apply a crossover heuristic, a different method must be used, which supplies two input solutions instead of one.
 * it is possible to supply the index of a crossover heuristic to the method which applies a non-crossover heuristic.
 * if this happens, the method will return the input solution unmodified. Therefore, the hyper-heuristic
 * strategy implemented in ExampleHyperHeuristic1.java occasionally chooses a crossover heuristic which does nothing, as it
 * is applied with only one input solution.
 * 
 */

public class PaoloHyperHeuristic extends HyperHeuristic {

	/**
	 * creates a new ExampleHyperHeuristic object with a random seed
	 */
	public PaoloHyperHeuristic(long seed) {
		super(seed);
	}

	/**
	 * This method defines the strategy of the hyper-heuristic
	 * @param problem the problem domain to be solved
	 */
	public void solve(ProblemDomain problem) {
		//obtain arrays of the indices of the ow level heuristics heuristics which correspond to the different types.
		//the arrays will be set to 'null' if there are no low level heuristics of that type
		
		int[] local_search_heuristics = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.LOCAL_SEARCH);
		int[] mutation_heuristics = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.MUTATION);
		int[] crossover_heuristics = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.CROSSOVER);
		int[] ruin_recreate_heuristics = problem.getHeuristicsOfType(ProblemDomain.HeuristicType.RUIN_RECREATE);

		// Number of element in memory
		int solutionmemorysize = 11;
		
		// Number of element in the population
		int populationSize = solutionmemorysize -1;
		
		// Child memory place
		int childPlace = solutionmemorysize - 1;
		
		// Allocate space in memory for the population
		problem.setMemorySize(solutionmemorysize);
		
		double[] current_obj_function_values = new double[solutionmemorysize];

		// Determining the best element in the population
		int best_solution_index = 0;
		double best_solution_value = Double.POSITIVE_INFINITY;
		for (int x = 0; x < populationSize; x++) {
			problem.initialiseSolution(x);
			current_obj_function_values[x] = problem.getFunctionValue(x);
			if (current_obj_function_values[x] < best_solution_value) {
				best_solution_value = current_obj_function_values[x];
				best_solution_index = x;
			}
		}

		//the main loop of any hyper-heuristic, which checks if the time limit has been reached
		while (!hasTimeExpired()) {

/* ---> ByPaolo
 * 
			//this hyper-heuristic first randomly chooses a mutation heuristic to apply
			int heuristic_to_apply = 0;
			//we must check that there are some mutational heuristics in this problem domain
			if (mutation_heuristics != null) {
				heuristic_to_apply = mutation_heuristics[rng.nextInt(mutation_heuristics.length)];
			} else {//we apply a randomly selected heuristic if there are no mutational heuristics
				heuristic_to_apply = rng.nextInt(problem.getNumberOfHeuristics());
			}

			//apply the randomly chosen heuristic to the current best solution in the memory
			//we accept every move, so the new solution can be immediately written to the same index in memory
			problem.applyHeuristic(heuristic_to_apply, best_solution_index, best_solution_index);
<---
*/
			// ---------------------------------------------------------------------------------------
			// Apply 		LOCAL SEARCH HEURISTIC
			//----------------------------------------------------------------------------------------
			
			int heuristic_to_apply = 0;
			//we must check that there are some local search heuristics in this problem domain
			if (local_search_heuristics != null) {
				heuristic_to_apply = local_search_heuristics[rng.nextInt(local_search_heuristics.length)];
			} else {//we apply a randomly selected heuristic if there are no local searchers
				heuristic_to_apply = rng.nextInt(problem.getNumberOfHeuristics());
			}

/*
---> ByPaolo
			current_obj_function_values[best_solution_index] = problem.applyHeuristic(heuristic_to_apply, best_solution_index, best_solution_index);

			//if the solution has got worse, we must find the new best solution in the memory
			if (current_obj_function_values[best_solution_index] > best_solution_value) {
				best_solution_value = Double.POSITIVE_INFINITY;
				for (int x = 0; x < solutionmemorysize; x++) {
					if (current_obj_function_values[x] < best_solution_value) {
						best_solution_value = current_obj_function_values[x];
						best_solution_index = x;
					}
				}
			}
<---			
*/			
			// ---> ByPaolo
			double new_obj_function_new_value = problem.applyHeuristic(heuristic_to_apply, best_solution_index, childPlace);
			
			if ((best_solution_value - new_obj_function_new_value) > 0)
			{
				// we found a solution better the the best : accept the new solution
				problem.copySolution(childPlace, best_solution_index);
				current_obj_function_values[best_solution_index] = new_obj_function_new_value;
			}
			
			
/* ByPaolo
			//it is important to check first that there are some crossover heuristics to use
			if (crossover_heuristics != null) {

				//we now perform a crossover heuristic on two randomly selected solutions
				int solution_index_1 = rng.nextInt(solutionmemorysize);
				int solution_index_2 = rng.nextInt(solutionmemorysize);
				//if the solutions are the same, choose a different one
				while (true) {
					if (solution_index_1 == solution_index_2) {
						solution_index_2 = rng.nextInt(solutionmemorysize);
					} else {break;}}

				//we select a random crossover heuristic to use
				heuristic_to_apply = crossover_heuristics[rng.nextInt(crossover_heuristics.length)];

				
				//the method to apply crossover heuristics involves specifying two indices of solutions in the memory, and an index into which to put the result of the crossover.
				//in this example we give the randomly selected indices as input, and we overwrite the first parent with the resulting solution
				current_obj_function_values[solution_index_1] = problem.applyHeuristic(heuristic_to_apply, solution_index_1, solution_index_2, solution_index_1);

 				
				//if we have overwritten the best solution with the result of the crossover, we must check for the new best solution
				if (solution_index_1 == best_solution_index) {
					//we only need to check for a new best solution if the new solution that replaced the best solution is worse
					if (current_obj_function_values[solution_index_1] > best_solution_value) {
						best_solution_value = Double.POSITIVE_INFINITY;
						for (int x = 0; x < solutionmemorysize; x++) {
							if (current_obj_function_values[x] < best_solution_value) {
								best_solution_value = current_obj_function_values[x];
								best_solution_index = x;
							}
						}
					}
				} 
				//if the result of the crossover is better than the best solution, then update the best solution record
				else if (current_obj_function_values[solution_index_1] < best_solution_value) {
					best_solution_value = current_obj_function_values[solution_index_1];
					best_solution_index = solution_index_1;
				}

			} //  
 ByPaolo
*/
			// ---------------------------------------------------------------------------------------
			// Apply 		CROSSOVER + MUTATION 
			//----------------------------------------------------------------------------------------
			

			//it is important to check first that there are some crossover heuristics to use
			if (crossover_heuristics != null) {

				//we now perform a crossover heuristic on two randomly selected solutions
				int solution_index_1 = rng.nextInt(populationSize);
				int solution_index_2 = rng.nextInt(populationSize);
				//if the solutions are the same, choose a different one
				while (true) {
					if (solution_index_1 == solution_index_2) {
						solution_index_2 = rng.nextInt(populationSize);
					} else {break;}}

				
				//we select a random crossover heuristic to use
				heuristic_to_apply = crossover_heuristics[rng.nextInt(crossover_heuristics.length)];

				problem.applyHeuristic(heuristic_to_apply, solution_index_1, solution_index_2, childPlace);
 				
				// applying the mutation heuristic
				if (mutation_heuristics != null)
				{heuristic_to_apply = mutation_heuristics[rng.nextInt(mutation_heuristics.length)];}
				else
				{ heuristic_to_apply = rng.nextInt(problem.getNumberOfHeuristics());}

				// calculating the child fitness
				double childFitness = problem.applyHeuristic(heuristic_to_apply, childPlace, childPlace);
				
				// determining the element of the population with the worst fitness
				double worstFitness = problem.getFunctionValue(0);
				int worst_index_solution = 0 ;
				double solutionFitness;
				
				for (int x=0;x<populationSize;x++)
				{
					solutionFitness = problem.getFunctionValue(x);
					if (solutionFitness > worstFitness)
					{
						worstFitness = solutionFitness;
						worst_index_solution = x;
					}
				}
				
				// if child is "better" than the worst element of the population => accept the child !!!
				if (childFitness < worstFitness)
				{
					problem.copySolution(childPlace, worst_index_solution);
					current_obj_function_values [worst_index_solution] = childFitness;
				}	
				
			} // CROSSOVER 
			
			
			// ---------------------------------------------------------------------------------------
			// Apply 		RUIN - RECREATE
			//----------------------------------------------------------------------------------------
			if (ruin_recreate_heuristics != null)
			{
				heuristic_to_apply = ruin_recreate_heuristics[rng.nextInt(ruin_recreate_heuristics.length)];
				
				int index = rng.nextInt(populationSize);
				problem.applyHeuristic(heuristic_to_apply, index, index);
				
			}
			
			
			
			//one iteration has been completed, so we return to the start of the main loop and check if the time has expired 
		} // WHILE LOOP
	}

	/**
	 * this method must be implemented, to provide a different name for each hyper-heuristic
	 * @return a string representing the name of the hyper-heuristic
	 */
	public String toString() {
		return "Paolo Hyper Heuristic";
	}
}
