package Bruteforce;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Observable;




public class State extends Observable
{
	private ArrayList<Point> points;
	private ArrayList<Point> convexHull;
	
	public State()
	{
		points = new ArrayList<Point>();
		convexHull = new ArrayList<Point>();
	}
	
	public void addPoint(int x, int y)
	{
		points.add(new Point(x, y));
		
		setChanged();
		notifyObservers();
	}
	
	public ArrayList<Point> getPoints()
	{
		return points;
	}
	
	public ArrayList<Point> getConvexHull()
	{
		return convexHull;
	}
	
	public void removeAllPoints()
	{
		points.clear();
		convexHull.clear();
		
		setChanged();
		notifyObservers();
	}
	
	public void closeWindow()
	{
		System.exit(0);
		
	}
	
	public void computeConvexHull(ConvexHullPoints bruteforce)
	{
		convexHull = bruteforce.execute(points);
		setChanged();
		notifyObservers();
	}
	
	public void applyQuickHull(ConvexHullPoints hullPoints){
		convexHull = hullPoints.execute(points);
		setChanged();
		notifyObservers();
	}
}

