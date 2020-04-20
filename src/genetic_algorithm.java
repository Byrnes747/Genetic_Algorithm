/*
    Ryan Byrnes
    Class that creates a genetic algorithm for the traveling salesman problem.
    Java 11
 */

import java.util.Random;
import java.util.concurrent.TimeUnit;

public class genetic_algorithm {

    private int population_size;
    private int mutation_rate;
    private route[] population;
    private double[] fitnesses;
    private int fittest_index;
    private int num_points;
    private point[] points;
    private double total_fitness;
    private double fittest_distance;
    private route fittest_route;
    private long startTime;
    private long endTime;
    private int computations;


    public genetic_algorithm(int pop_size, int mut_rate, point[] p)
    {
        population_size = pop_size;
        mutation_rate = mut_rate;
        population = new route[population_size];
        fitnesses = new double[population_size];
        for(int i = 0; i < population_size; i++)
        {
            population[i] = new route(p.length, true);
        }
        points = p;
        num_points = p.length;
        fittest_index = 0;
        fittest_route = population[0];
        fittest_distance = -1;
        startTime = -1;
        computations = 0;
    }

    public void view_population()
    {
        for(int i = 0; i < population_size; i++)
        {
            population[i].view_route();
        }
    }

    public void calc_fitnesses()
    {
        total_fitness = 0;
        double total_distance = 0;
        for(int i = 0; i < population_size; i++)
        {
            fitnesses[i] = population[i].calc_fit(points);
            computations++;
            total_fitness += fitnesses[i];
            total_distance += 1/fitnesses[i];
            if(fittest_distance == -1 || fittest_distance < fitnesses[i])
            {
                fittest_index = i;
                fittest_distance = fitnesses[i];
                fittest_route = population[i];
                System.out.println("Fittest Route Distance: " + 1/fittest_distance);
                endTime = System.nanoTime();
                System.out.println("Found after " + (endTime-startTime)/1000000 + " milliseconds");
                System.out.println("Found in " + computations + " computations");
                //fittest_route.view_route();
            }
        }
        for(int i = 0; i < population_size; i++)
        {
            fitnesses[i] /= total_fitness;
            //System.out.println("Route " + i + " fitness percentage: " + fitnesses[i]);
        }

        //System.out.println("Fittest Route Distance: " + 1/fittest_distance);
        //System.out.println("Average Route Distance: " + total_distance/population_size);
        //fittest_route.view_route();
    }

    public void next_generation(boolean solo_parent)
    {
        if(startTime == -1)
            startTime = System.nanoTime();
        calc_fitnesses();
        route[] next_gen = new route[population_size];
        for(int i = 0; i < population_size; i++)
        {
            fitnesses[i] = 1000 * fitnesses[i];
        }
        Random rand = new Random();
        route parent_1, parent_2;
        for(int i = 0; i < population_size; i++)
        {
            parent_1 = null;
            parent_2 = null;
            double rand_int1 = rand.nextInt(1000);
            //System.out.println(rand_int1);
            double rand_int2 = rand.nextInt(1000);
            //System.out.println(rand_int1);

            for(int j = 0; j < fitnesses.length; j++)
            {
                if(rand_int1 < fitnesses[j] && parent_1 == null)
                    parent_1 = population[j];
                if(rand_int2 < fitnesses[j] && parent_2 == null)
                    parent_2 = population[j];
                rand_int1 -= fitnesses[j];
                rand_int2 -= fitnesses[j];
            }

            if(parent_1 == null)
            {
                parent_1 = population[population_size-1];
                System.out.println("P1 Null");
            }
            if(parent_2 == null)
            {
                parent_2 = population[population_size-1];
                System.out.println("P2 Null");
            }
            if(solo_parent)
                next_gen[i] = parent_1.create_offspring(parent_1, mutation_rate);
            else
                next_gen[i] = parent_1.create_offspring(parent_2, mutation_rate);
        }
        population = next_gen;
    }

}
