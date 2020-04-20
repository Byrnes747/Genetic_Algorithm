/*
    Ryan Byrnes
    Main function for genetic algorithm class. For testing purposes.
    Java 11
 */

public class main {
    public static void main(String[] args) {
        int n_points = 12;
        int point_range = 10;
        point[] p = new point[n_points];
        /*//Randomized points for Testing without validation
        for(int i = 0; i < n_points; i++)
        {
            p[i] = new point(true, point_range);
            for(int j = 0; j < i; j++)
            {
                while(p[i].equals(p[j]))
                {
                    p[i] = new point(true, point_range);
                }
            }
            p[i].view_point();
        }
        */

        // Validation Problem: Optimal Solution Distance = number of points
        p[0] = new point(0,0);
        p[1] = new point(0,1);
        p[2] = new point(0,2);
        p[3] = new point(0,3);
        p[4] = new point(0,4);
        p[5] = new point(1,4);
        p[6] = new point(1,3);
        p[7] = new point(1,2);
        p[8] = new point(1,1);
        p[9] = new point(1,0);
        p[10] = new point(0,5);
        p[11] = new point(1,5);



        int n_routes = 100;
        genetic_algorithm ga = new genetic_algorithm(n_routes, 5, p);

        // Genetic Algorithm
        for(int i = 0; i < 10000; i++)
        {
            ga.next_generation(false);
        }

    }
}
