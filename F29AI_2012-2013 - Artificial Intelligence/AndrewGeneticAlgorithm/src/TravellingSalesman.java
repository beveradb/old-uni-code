import org.jgap.Chromosome;
import org.jgap.Configuration;
import org.jgap.DefaultFitnessEvaluator;
import org.jgap.FitnessFunction;
import org.jgap.Gene;
import org.jgap.Genotype;
import org.jgap.IChromosome;
import org.jgap.Population;
import org.jgap.event.EventManager;
import org.jgap.impl.BestChromosomesSelector;
import org.jgap.impl.ChromosomePool;
import org.jgap.impl.GreedyCrossover;
import org.jgap.impl.IntegerGene;
import org.jgap.impl.StockRandomGenerator;
import org.jgap.impl.SwappingMutationOperator;

public class TravellingSalesman {
	static Configuration config; // Stores JGAP setup stuff
	static int maxEvolution = 100; // Maximum evolution for GA
	static int populationSize = 500; // Population size for GA
	static int numCities = 8; // Total number of cities we want to loop through

	// Array of city names, for nice printout at the end
	static String[] cityNames = {"Edinburgh","Aberdeen","Ayr","Fort William","Glasgow","Inverness","St Andrews","Stirling"};

	// Matrix of distances between cities 
	// In the format (where numbers are distances in miles to):
	// {edinburgh, aberdeen, ayr, fort william, glasgow, inverness, st andrews, stirling}
	// The order of the columns matters - edinburgh has been put first because it's our starting city
	static int cityDistances[][] =	{
		{0,129,79,131,43,154,50,36}, 		// Edinburgh
		{129,0,179,157,146,105,79,119}, 	// Aberdeen
		{79,179,0,141,33,207,118,64}, 		// Ayr
		{131,157,141,0,116,74,134,96}, 		// Fort William
		{43,146,33,116,0,175,81,27}, 		// Glasgow
		{154,105,207,64,175,0,145,143}, 	// Inverness
		{50,79,118,134,81,145,0,52}, 		// St Andrews
		{36,119,64,96,27,143,52,0} 			// Stirling
	};

	// Executes the genetic algorithm to determine the optimal path between the cities
	public static IChromosome findShortestPath() throws Exception {
		// Basic JGAP configuration
		config = new Configuration();
		BestChromosomesSelector bestCS = new BestChromosomesSelector(config, 1.0d);
		bestCS.setDoubletteChromosomesAllowed(false);
		config.addNaturalSelector(bestCS, true);
		config.setRandomGenerator(new StockRandomGenerator());
		config.setMinimumPopSizePercent(0);
		config.setEventManager(new EventManager());
		config.setFitnessEvaluator(new DefaultFitnessEvaluator());
		config.setChromosomePool(new ChromosomePool());
		config.addGeneticOperator(new GreedyCrossover(config));
		config.addGeneticOperator(new SwappingMutationOperator(config, 20));
		config.setFitnessFunction(new SalesmanFitnessFunction());
		
		// Now we need to tell the Configuration object how we want our Chromosomes to be setup. We do that by actually creating a
		// sample Chromosome and then setting it to the Configuration object.
		Gene[] sampleChromosomeGenes = new Gene[numCities];
		for (int i = 0; i < sampleChromosomeGenes.length; i++) {
			sampleChromosomeGenes[i] = new IntegerGene(config, 0, numCities - 1);
			sampleChromosomeGenes[i].setAllele(new Integer(i));
		}
		IChromosome sampleChromosome = new Chromosome(config, sampleChromosomeGenes);
		config.setSampleChromosome(sampleChromosome);
		
		// Finally, we need to tell the Configuration object how many Chromosomes we want in our population. The more Chromosomes,
		// the larger number of potential solutions (which is good for finding the answer), but the longer it will take to evolve
		// the population (which could be seen as bad). 
		config.setPopulationSize(populationSize);
		
		// Create random initial population of Chromosomes. As we cannot allow the normal mutations if this task,
		// we need multiple calls to createSampleChromosome.
		IChromosome[] chromosomes = new IChromosome[config.getPopulationSize()];
		Gene[] sampleGenes = sampleChromosome.getGenes();
		for (int i = 0; i < chromosomes.length; i++) {
			Gene[] genes = new Gene[sampleGenes.length];
			for (int k = 0; k < genes.length; k++) {
				genes[k] = sampleGenes[k].newGene();
				genes[k].setAllele(sampleGenes[k].getAllele());
			}
			chromosomes[i] = new Chromosome(config, genes);
		}
		
		// Create the genotype. We cannot use Genotype.randomInitialGenotype, because we need unique gene values 
		// (representing the indices of the cities of our problem).
		Genotype population = new Genotype(config, new Population(config, chromosomes));
		
		
		// Evolve the population. Since we don't know what the best answer is going to be, we just evolve the max number of times.
		IChromosome best = null;
		for (int i = 0; i < maxEvolution; i++) {
			population.evolve();
			best = population.getFittestChromosome();
		}

		// Return the best solution we found.
		return best;
	}
	
	public static void main(String[] args) throws Exception {
			// Actually run our genetic algorithm function and get the shortest path!
			IChromosome shortestPath = findShortestPath();
			
			// Print out various genetic algorithm-related statistics
			System.out.println("Population Size: " + populationSize);
			System.out.println("Max Evolution: " + maxEvolution);
			System.out.println("Fitness Value: " + shortestPath.getFitnessValue());
			
			// Fancy math stuff
			System.out.println("Shortest Distance: " + (Integer.MAX_VALUE / 2 - shortestPath.getFitnessValue()) + " miles");

			// Print array positions of shortest path
			System.out.print("Shortest Path (array positions): ");
			for (int i = 0; i < numCities; i++)
				System.out.print(shortestPath.getGene(i) + " ");
			System.out.println("0");

			// Print more informative shortest path with city names and
			// distances between
			System.out.print("Shortest Path (names / distances): ");
			for (int i = 0; i < numCities; i++) {
				int j;
				if (i == numCities - 1) {
					j = 0;
				} else {
					j = i + 1;
				}
				System.out.print("["
						+ cityNames[Integer.parseInt(shortestPath.getGene(i)
								.toString())]
										+ "] -"
										+ cityDistances[Integer.parseInt(shortestPath.getGene(i)
												.toString())][Integer.parseInt(shortestPath.getGene(
														j).toString())] + "- ");
			}
			System.out.println("[Edinburgh]");
	}
}

// Fitness function! Yay!
class SalesmanFitnessFunction extends FitnessFunction {
	protected double evaluate(final IChromosome chromosomeToEvaluate) {
		// Start with distance 0
		double distance = 0;
		
		// Get all the cities (er, I mean genes) in this path (er, I mean chromosome)
		Gene[] genes = chromosomeToEvaluate.getGenes();
		
		// Loop through them, add all the distances between them to our total distance
		for (int i = 0; i < genes.length - 1; i++) {
			int a = ((IntegerGene) genes[i]).intValue();
			int b = ((IntegerGene) genes[i + 1]).intValue();
			distance += TravellingSalesman.cityDistances[a][b];
		}
		
		// Add distance to last city (er, I mean gene) as part of the path (er, I mean chromosome)
		int a = ((IntegerGene) genes[genes.length - 1]).intValue();
		int b = ((IntegerGene) genes[0]).intValue();
		distance += TravellingSalesman.cityDistances[a][b];
		
		// Fancy math stuff
		return Integer.MAX_VALUE / 2 - distance;
	}
}