/*
    Ryan Byrnes
    Class that stores x and y coordinate of a point.
    Java 11
 */

import java.util.Random;
import java.lang.Math;

public class point {
    public int x;
    public int y;

    public point(boolean randomize, int range)
    {
        if(randomize)
        {
            Random rand = new Random();
            x = rand.nextInt(range);
            y = rand.nextInt(range);
        }
        else
        {
            x = 0;
            y = 0;
        }
    }

    public point(int x1, int y1)
    {
        x = x1;
        y = y1;
    }

    public void view_point()
    {
        System.out.println(x + ", " + y);
    }

    public boolean equals(point p) {
        if(p.x ==x && p.y == y)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public double calc_distance(point p)
    {
        double x_dis = Math.abs(x-p.x);
        double y_dis = Math.abs(y-p.y);
        return Math.sqrt((x_dis*x_dis)+(y_dis*y_dis));
    }
}
