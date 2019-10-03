package Examples;
import AbstractClasses.HyperHeuristic;
import AbstractClasses.ProblemDomain;
import BinPacking.BinPacking;
import SAT.SAT;

/**
 * This class shows how to run a selected hyper-heuristic on a selected problem domain.
 * It shows the minimum that must be done to test a hyper heuristic on a problem domain, and it is 
 * intended to be read before the ExampleRun2 class, which provides an example of a more complex set-up
 */
public class ExampleHillClimberRun {

	public static void main(String[] args) {

		

		//creates an ExampleHyperHeuristic object with a seed for the random number generator
		HyperHeuristic hyper_heuristic_object = new ExampleHillClimbingHeuristic(5678);

		// loop through all instances
		//we must load an instance within the problem domain, in this case we choose instance 2
		int instanceCounter;

		System.out.println("Started");
		
		for (instanceCounter = 0;instanceCounter < 10; instanceCounter++){
			

			//create a ProblemDomain object with a seed for the random number generator
			ProblemDomain problem =  new BinPacking(1234);;

			problem.loadInstance(instanceCounter);
		

			//we must set the time limit for the hyper-heuristic in milliseconds, in this example we set the time limit to 1 minute
			hyper_heuristic_object.setTimeLimit(10000);

			//a key step is to assign the ProblemDomain object to the HyperHeuristic object. 
			//However, this should be done after the instance has been loaded, and after the time limit has been set
			hyper_heuristic_object.loadProblemDomain(problem);

			//now that all of the parameters have been loaded, the run method can be called.
			//this method starts the timer, and then calls the solve() method of the hyper_heuristic_object.
			hyper_heuristic_object.run();

			//obtain the best solution found within the time limit
			System.out.println("INSTANCE " + instanceCounter + " BEST FOUND " + hyper_heuristic_object.getBestSolutionValue());
		}
	}
}
