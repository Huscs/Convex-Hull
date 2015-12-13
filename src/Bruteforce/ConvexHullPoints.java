
/**
 * @author Hussam I. Alduraiei
 * Supervisor Prof.Abdelfettah Belghith
 * KSU - CS512 Algorithm Analysis and Design
 * @version 0.1
 * 
 * Definitions.
 * -A set of points S is convex if for any two points in S, 
 *   the line segment joining them is also inside the set.
 * -A point p is an extreme point of a convex set S if p 
 *   is not interior to any line segment connecting two points in the set.
 *
 */

package Bruteforce;

import java.awt.Point;
import java.util.ArrayList;


public interface ConvexHullPoints
{
	ArrayList<Point> execute(ArrayList<Point> points);
	
	
}

