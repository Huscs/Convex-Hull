package QuickHull;

import java.awt.Point;
import java.util.ArrayList;

//import convexHullImpl.XYPoint;
import Bruteforce.ConvexHullPoints;

public class QuickHullAlgorithm implements ConvexHullPoints {	 
	@SuppressWarnings("unchecked")
	@Override
	public ArrayList<Point> execute(ArrayList<Point> points) {
	    ArrayList<Point> convexHull = new ArrayList<Point>();
	    if (points.size() < 3) return (ArrayList<Point>)points.clone();
	    // find extremal
	    int minPoint = -1, maxPoint = -1;
	    int minX = Integer.MAX_VALUE;
	    int maxX = Integer.MIN_VALUE;
	    for (int i = 0; i < points.size(); i++) {
	      if (points.get(i).x < minX) {
	        minX = points.get(i).x;
	        minPoint = i;
	      } 
	      if (points.get(i).x > maxX) {
	        maxX = points.get(i).x;
	        maxPoint = i;       
	      }
	    }
	    Point p1 = points.get(minPoint);
	    Point pn = points.get(maxPoint);
	    convexHull.add(p1);
	    convexHull.add(pn);
//	    points.remove(p1); // if removed, we can no longer re-render the same convex hull when hitting the devide and conq. button
//	    points.remove(pn);
	    
	    ArrayList<Point> s1 = new ArrayList<Point>();
	    ArrayList<Point> s2 = new ArrayList<Point>();
	    
	    for (int i = 0; i < points.size(); i++) {
	      Point p = points.get(i);
	      if (determinantMagintude(p1,pn,p) < 0) // if p is on the right(negative means right) of line p1, pn then add to s2, else add to s1
	        s2.add(p);
	      else
	        s1.add(p);
	    }
	    // Construct S1, and add its convex hull to the complete convex hull
	    hullSet(p1,pn,s1,convexHull);
	    
	    // Construct S2, and add its convex hull to the complete convex hull
	    hullSet(pn,p1,s2,convexHull);
	    
	    return convexHull;
	  }
	  
	  public void hullSet(Point A, Point B, ArrayList<Point> set, ArrayList<Point> hull) {
	    int insertPosition = hull.indexOf(B);
	    if (set.size() == 0) return;
	    if (set.size() == 1) {
	      Point p = set.get(0);
	      set.remove(p);
	      hull.add(insertPosition,p);
	      return;
	    }
	    int dist = Integer.MIN_VALUE;
	    int furthestPoint = -1;
	    for (int i = 0; i < set.size(); i++) {
	      Point p = set.get(i);
	      int distance  = Math.abs(determinantMagintude(A, B, p));
	      if (distance > dist) {
	        dist = distance;
	        furthestPoint = i;
	      }
	    }
	    Point pMax = set.get(furthestPoint);
	    set.remove(furthestPoint);
	    hull.add(insertPosition,pMax);
	    
	    // Determine who's to the left of A-Pmax
	    ArrayList<Point> leftSetAP = new ArrayList<Point>();
	    for (int i = 0; i < set.size(); i++) {
	      Point M = set.get(i);
	      if (determinantMagintude(A,pMax,M)  > 0) {
	        leftSetAP.add(M);
	      }
	    }
	    
	    // Determine who's to the left of Pmax-B
	    ArrayList<Point> leftSetPB = new ArrayList<Point>();
	    for (int i = 0; i < set.size(); i++) {
	      Point M = set.get(i);
	      if (determinantMagintude(pMax,B,M) > 0) {
	        leftSetPB.add(M);
	      }
	    }
	    
	    //Recursively call hullset with newly calculated  points and sets
	    hullSet(A,pMax,leftSetAP,hull);
	    hullSet(pMax,B,leftSetPB,hull);
	    
	  }
	  
	  private int determinantMagintude(Point p1, Point p2, Point p3){
			// x1y2 + x3y1 + x2 y3 − x3y2 − x2y1 − x1y3
			return (p1.x * p2.y) + (p3.x * p1.y) + (p2.x *  p3.y) - (p3.x * p2.y) - (p2.x * p1.y) - (p1.x * p3.y);
		}
}
