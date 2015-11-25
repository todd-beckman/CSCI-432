package grid;

import java.util.HashSet;
import path2.structs.Point;

/**
 * A grid containing a start, end, and list of obstacles.
 *
 * @author Todd
 */
public abstract class Grid {

    /**
     * The list of locations in this grid that are illegal
     */
    protected HashSet<Point> obstacles;

    /**
     * The intended starting point of the agent
     */
    protected Point start;

    /**
     * The intended ending point of the agent
     */
    protected Point end;

    /**
     * The farthest west legal coordinate
     */
    public final int minX;

    /**
     * The farthest east legal coordinate
     */
    public final int maxX;

    /**
     * The farthest south legal coordinate
     */
    public final int minY;

    /**
     * The farthest north legal coordinate
     */
    public final int maxY;

    /**
     * Constructs a grid
     *
     * @param minX The farthest west legal coordinate
     * @param maxX The farthest east legal coordinate (exclusive)
     * @param minY The farthest south legal coordinate
     * @param maxY The farthest north legal coordinate (exclusive)
     */
    public Grid(int minX, int maxX, int minY, int maxY) {
        obstacles = new HashSet<Point>();
        this.minX = minX;
        this.maxX = maxX;
        this.minY = minY;
        this.maxY = maxY;
    }

    /**
     * Gets the list of illegal locations in the current grid
     *
     * @return the list
     */
    public HashSet<Point> getObstacles() {
        return obstacles;
    }

    /**
     * Gets the location where the agent should start
     *
     * @return the location
     */
    public Point getStart() {
        return start;
    }

    /**
     * Gets the location where the agent should try to find
     *
     * @return the location
     */
    public Point getEnd() {
        return end;
    }

    /**
     * Inserts the location
     *
     * @param location
     */
    public void addObstacle(Point location) {
        obstacles.add(location);
    }

    /**
     * Removes the obstacle at the location
     *
     * @param location
     */
    public void removeObstacle(Point location) {
        obstacles.remove(location);
    }

    /**
     * Illustrates this grid in a string with one row per line
     *
     * @return
     */
    public String visualize() {
        char[][] grid = new char[maxX - minX][maxY - minY];
        if (start != null) {
            grid[start.x][start.y] = 'S';
        }
        if (end != null) {
            grid[end.x][end.y] = 'E';
        }
        String output = "";
        for (int r = 0; r <= maxX - minX - 1; r++) {
            for (int c = 0; c <= maxY - minY - 1; c++) {
                Point p = new Point(r, c);
                if ((start == null || !start.equals(p)) && (end == null || !end.equals(p))) {
                    if (obstacles.contains(p)) {
                        grid[r][c] = 'X';
                    } else {
                        grid[r][c] = ' ';
                    }
                }
            }
            output += "|" + new String(grid[r]) + "|\n";
        }
        return output;
    }

    /**
     * Finds a random point in the grid
     *
     * @return The point
     */
    protected Point randomLocation() {
        return new Point((int) (Math.random() * (maxX - minX) + minX), (int) (Math.random() * (maxY - minY) + minY));
    }

}
