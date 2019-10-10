package tspsolver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;


public class SolveTSP{	
		
	
	int SIMULATED_ANNEALING = 1;
	int HILL_CLIMBER = 2;
	double startTemp = 200;
	double endTemp = 0;
	double coolingRate = 0.995;
	boolean verbose = false;
	int iterations = 10000;
	String fileName  = "berlin52.tsp";
	int solver = HILL_CLIMBER;
	int numRuns=1;
	boolean opSwap = true;
	
	@SuppressWarnings("static-access")
	public SolveTSP(String[] args) {
		final Options options = new Options();
		options.addOption(new Option("h", "help", false,
			"print this help message"));
		
		Option option1 = OptionBuilder.withArgName("int").hasArg().withLongOpt("iterations").withDescription("iterations (default: 10000)").create("i");
		options.addOption(option1);
		
		Option option2 = OptionBuilder.withArgName("double").hasArg().withLongOpt("startTemp").withDescription("start temperature(default: 200)").create("s");
		options.addOption(option2);
		
		Option option3 = OptionBuilder.withArgName("double").hasArg().withLongOpt("final temperature (default: 0)").create("e");
		options.addOption(option3);
		
		Option option4 = OptionBuilder.withArgName("double").hasArg().withLongOpt("coolingRate").withDescription("cooling rate (default: 0.995)").create("r");
		options.addOption(option4);
		
		Option option5 = OptionBuilder.withArgName("string").hasArg().withLongOpt("file").withDescription("file name (default: berlin52.tsp)").create("f");
		options.addOption(option5);
		
		Option option6 = OptionBuilder.withArgName("int").hasArg().withLongOpt("numRuns").withDescription("number of run (default: 1)").create("N");
		options.addOption(option6);
		
		options.addOption("v", false, "verbose (display intermediate output");
		options.addOption("SA", false, "use simulatedAnnealing");
		options.addOption("H", false,"use hill climber");
		options.addOption("mS", false,"use swap operator");
		options.addOption("mT", false,"use two-opt operator");
		
		final CommandLineParser parser = new GnuParser();
		try {
			final CommandLine line = parser.parse(options, args);
			if (line.hasOption("h")) {
				final HelpFormatter formatter = new HelpFormatter();
				formatter.printHelp("TSP Solver", options);
				System.exit(1);
			}
			if (line.hasOption("N"))
				numRuns = Integer.parseInt(line.getOptionValue("N"));
			if (line.hasOption("i"))
				iterations = Integer.parseInt(line.getOptionValue("i"));
			if (line.hasOption("s"))
				startTemp = Double.parseDouble(line.getOptionValue("s"));
			if (line.hasOption("e"))
				endTemp = Double.parseDouble(line.getOptionValue("e"));
			if (line.hasOption("r"))
				coolingRate = Double.parseDouble(line.getOptionValue("r"));
			if (line.hasOption("v"))
				verbose = true;
			if (line.hasOption("f"))
				fileName = line.getOptionValue("f");
			if (line.hasOption("SA"))
				solver = SIMULATED_ANNEALING;
			if (line.hasOption("H"))
				solver = HILL_CLIMBER;
			if (line.hasOption("mS"))
				opSwap = true;
			if (line.hasOption("mT"))
				opSwap = false;
			
		} catch (final ParseException exp) {
			System.out.println("Parsing failed. Reason: " + exp.getMessage());
			System.exit(1);
		}
		
		if (solver == SIMULATED_ANNEALING){
			simanneal mySA = new simanneal(fileName, iterations, startTemp, endTemp, coolingRate, verbose,opSwap );
			for (int i=0;i<numRuns;i++)
				mySA.runSA();
		} else {
			hillclimber myHC = new hillclimber(fileName, iterations, verbose, opSwap);
			for (int i=0;i<numRuns;i++)
				myHC.runHC();
		}
	}



	
	public static void main(String[] args) {	
		@SuppressWarnings("unused") SolveTSP app = new SolveTSP(args);	
	}

}
