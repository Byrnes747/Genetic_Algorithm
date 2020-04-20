/*
    Ryan Byrnes
    Class that stores information about a route in the traveling salesman problem.
    Java 11
 */
import java.util.Random;

public class route {

    private boolean[][] path;

    public route(int points, boolean randomize)
    {
        // Create a path of the correct size.
        path = new boolean[points][points];
        // Set all values in path to false.
        for(int i = 0; i < points; i++)
        {
            for(int j = 0; j < points; j++)
            {
                path[i][j] = false;
            }
        }
        if(randomize) {
            // Create an array that keeps track of what points have been visited in the path.
            boolean[] not_taken = new boolean[points];
            for (int i = 0; i < points; i++) {
                not_taken[i] = true;
            }
            // For every row in the path...
            for (int i = 0; i < points; i++) {
                // Create a random number. This number will be used to determine which point to go to from point i
                Random rand = new Random();
                int rand_point = rand.nextInt(points - i);
                // Create a variable to store where the point that the point i will travel to next.
                int index_point = 0;
                for (int j = 0; j < points && rand_point > -1; j++) {
                    if (not_taken[j] && i != j) {
                        rand_point--;
                        index_point = j;
                    }
                }
                // Set the correct value in not_taken to false, and update path to tell what point i will go to.
                not_taken[index_point] = false;
                path[i][index_point] = true;
            }
            remove_cycles();
        }
    }

    // Function to see the path a route will take.
    public void view_route()
    {
        System.out.println("___________________");
        for(int i = 0; i < path.length; i++)
        {
            for(int j = 0; j < path.length; j++)
            {
                if(path[i][j])
                    System.out.print(1 + " ");
                else
                    System.out.print(0 + " ");
            }
            System.out.println();
        }
    }

    // Function to remove extra cycles.
    // Example of cycles with 5 points(a,b,c,d,e):
    // Cycle 1: a>c>d>a
    // Cycle 2: b>e>b
    public void remove_cycles()
    {
        int index_point = 0;
        int cycle_length = 1;
        boolean cycle_over = false;
        do {
            for(int i = 0; i < path.length; i++)
            {
                if(path[index_point][i])
                {
                    if(i == 0)
                    {
                        cycle_over = true;
                    }
                    else
                    {
                        cycle_length++;
                        index_point = i;
                    }
                }
            }
        } while(!cycle_over);

        // If the cycle did not hit every point, we must combine cycles.
        if(cycle_length < path.length)
        {
            // Create an ordered list of the points in the main cycle
            int[] points_in_cycle = new int[cycle_length];
            points_in_cycle[0] = 0;
            for(int i = 1; i < cycle_length; i++)
            {
                for(int j = 0; j < path.length; j++)
                {
                    if(path[points_in_cycle[i-1]][j])
                    {
                        points_in_cycle[i] = j;
                    }
                }
            }

            //for(int i = 0; i < points_in_cycle.length; i++)
            //    System.out.println(points_in_cycle[i]);

            // Create an ordered list of the points NOT in the main cycle
            int[] points_not_in_cycle = new int[path.length-cycle_length];
            for(int i = 0; i < points_not_in_cycle.length; i++) {
                for (int j = 0; j < path.length; j++) {
                    boolean in_cycle = false;
                    for (int k = 0; k < points_in_cycle.length; k++) {
                        for(int l = 0; l < points_not_in_cycle.length; l++)
                            if (j == points_in_cycle[k] || j == points_not_in_cycle[l]) {
                                in_cycle = true;
                            }
                    }
                    if (!in_cycle) {
                        points_not_in_cycle[i] = j;
                    }
                }
            }
            //for(int i = 0; i < points_not_in_cycle.length; i++)
            //    System.out.println(points_not_in_cycle[i]);

            // Select a random point not in the main cycle and add it to the main cycle.
            Random rand = new Random();
            int rand_point = points_not_in_cycle[rand.nextInt(points_not_in_cycle.length)];
            int last_point_in_cycle = points_in_cycle[points_in_cycle.length-1];
            path[rand_point][0] = true;
            for(int i = 1; i < path.length; i++)
            {
                path[rand_point][i] = false;
            }
            for(int i = 0; i < path.length; i++)
            {
                path[last_point_in_cycle][i] = false;
            }
            path[last_point_in_cycle][rand_point] = true;

            // Recurse until there is only one cycle.
            remove_cycles();
        }

    }

    public route create_offspring(route mate, double mut_rate)
    {
        int[] my_path = new int[path.length];
        int[] mate_path = new int[path.length];
        my_path[0] = 0;
        mate_path[0] = 0;

        for(int i = 1; i < path.length; i++)
        {
            for(int j = 0; j < path.length; j++)
            {
                if(path[my_path[i-1]][j])
                {
                    my_path[i] = j;
                }
                if(mate.path[mate_path[i-1]][j])
                {
                    mate_path[i] = j;
                }
            }
        }

        /*for(int i = 0 ; i < path.length; i++)
            System.out.println(mate_path[i]);*/

        int[] child_path = new int[path.length];

        for(int i = 0; i < path.length; i++)
        {
            child_path[i] = 0;
        }

        for(int i = 1; i < path.length; i++)
        {
            boolean using_p1_gene = false;
            boolean using_p2_gene = false;
            for(int j = 0; j < child_path.length; j++)
            {
                if(my_path[i] == child_path[j])
                    using_p1_gene = true;
                if(mate_path[i] == child_path[j])
                    using_p2_gene = true;
            }

            if(!using_p1_gene && using_p2_gene)
            {
                child_path[i] = my_path[i];
            }
            else if(using_p1_gene && !using_p2_gene)
            {
                child_path[i] = mate_path[i];
            }
            else if(!using_p1_gene && !using_p2_gene)
            {
                Random rand = new Random();
                int rand_parent = rand.nextInt(2);
                if(rand_parent == 0)
                {
                    child_path[i] = my_path[i];
                }
                else
                {
                    child_path[i] = mate_path[i];
                }
            }
        }

        int num_unused_points = 0;
        for(int i = 1; i < path.length; i++)
        {
            if(child_path[i] == 0)
                num_unused_points++;
        }

        /*System.out.println("P1  P2  Child Path");
        for(int i = 0 ; i < path.length; i++)
        {
            System.out.println(my_path[i] + "   " + mate_path[i] + "   " + child_path[i]);
        }*/

        //System.out.println("Number of Unused Points: " + num_unused_points);

        if(num_unused_points > 0)
        {
            int[] unused_points = new int[num_unused_points];
            int[] unused_point_indexes = new int[num_unused_points];
            for(int i = 0 ; i < num_unused_points; i++)
            {
                unused_points[i] = 0;
                unused_point_indexes[i] = 0;
            }

            for(int i = 1; i < child_path.length; i++)
            {
                if(child_path[i] == 0)
                {
                    for(int j = 0; j < unused_point_indexes.length; j++)
                    {
                        if(unused_point_indexes[j] == 0)
                        {
                            unused_point_indexes[j] = i;
                            j = unused_point_indexes.length;
                        }
                    }
                }
            }
            //System.out.println(unused_points.length);
            for(int i = 1 ; i < path.length; i++)
            {
                boolean uses_i = false;
                for(int j = 1 ; j < child_path.length; j++)
                {
                    if (child_path[j] == i)
                    {
                        uses_i = true;
                    }
                }
                if(!uses_i)
                {
                    for(int j = 0; j < unused_points.length; j++)
                    {
                        if(unused_points[j] == 0)
                        {
                            unused_points[j] = i;
                            j = unused_points.length;
                        }
                    }
                }
            }

            /*System.out.println("Unused points       Indexes ");
            for(int i = 0 ; i < unused_points.length; i++)
            {
                System.out.println(unused_points[i] + "                 " + unused_point_indexes[i]);
            }*/

            for(int i = 0; i < unused_point_indexes.length; i++)
            {
                Random rand = new Random();
                int rand_swap = rand.nextInt(unused_point_indexes.length);
                int temp = unused_points[i];
                unused_points[i] = unused_points[rand_swap];
                unused_points[rand_swap] = temp;
            }
            for(int i = 0; i < unused_point_indexes.length; i++)
            {
                child_path[unused_point_indexes[i]] = unused_points[i];
            }

            /*System.out.println("Child Path");
            for(int i = 0 ; i < path.length; i++)
            {
                System.out.println(child_path[i]);
            }*/

        }

        // Mutate
        for(int i = 1; i < child_path.length; i++)
        {
            Random rand = new Random();
            int rand_mut = rand.nextInt(100);
            if(rand_mut < mut_rate)
            {
                int temp = child_path[i];
                int rand_swap = rand.nextInt(child_path.length-1)+1;
                child_path[i] = child_path[rand_swap];
                child_path[rand_swap] = temp;
            }
        }

        route child_route = new route(child_path.length, false);
        for(int i = 0; i < child_path.length-1; i++)
        {
            child_route.path[child_path[i]][child_path[i+1]] = true;
        }
        child_route.path[child_path[child_path.length-1]][0] = true;

        /*System.out.println("P1  P2  Child Path");
        for(int i = 0 ; i < path.length; i++)
        {
            System.out.println(my_path[i] + "   " + mate_path[i] + "   " + child_path[i]);
        }*/

        return child_route;
    }

    public double calc_fit(point[] p)
    {
        double total_distance = 0;
        for(int i = 0; i < path.length; i++)
        {
            for(int j = 0; j < path.length; j++)
            {
                if(path[i][j])
                {
                    total_distance += p[i].calc_distance(p[j]);
                }
            }
        }
        //System.out.println("Route Distance: " + total_distance);
        return 1/total_distance;
    }
}
