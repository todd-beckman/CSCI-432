package grid;

import pathplanning.util.Point;

/**
 * A grid initialized with a certain number of random obstacles
 * @author Todd
 * 
 */
public class RandomGrid extends Grid {
    
    /**
     * Constructs a grid with numObstacles obstacles placed in it randomly
     * @param minX The farthest west legal coordinate
     * @param maxX The farthest east legal coordinate
     * @param minY The farthest south legal coordinate
     * @param maxY The farthest north legal coordinate
     * @param numObstacles The number of random obstacles to be placed in the grid upon initialization
     */
    public RandomGrid(int minX, int maxX, int minY, int maxY, int numObstacles) {
        super(minX, maxX, minY, maxY);

        start = randomLocation();
        do {
            end = randomLocation();
        }
        while (start.equals(end));
        
        for (int i = 0; i < numObstacles; i++) {
            Point p = start;
            do {
                p = randomLocation();
            }
            while (obstacles.contains(p) || start.equals(p) || end.equals(p));
            addObstacle(p);
        }
    }
    
    private Point randomLocation() {
        return new Point((int)(Math.random() * (maxX - minX + 1) + minX), (int)(Math.random() * (maxY - minY + 1) + minY));
    }
}
