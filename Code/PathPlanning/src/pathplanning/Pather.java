/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pathplanning;

import pathplanning.util.Point;

/**
 *
 * @author alexhuleatt
 */
public interface Pather {
    Point[] pathfind(Point a, Point b);
}
