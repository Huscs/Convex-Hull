package Bruteforce;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class ConvexHullAlgorithm implements ConvexHullPoints
{

	@Override
	public ArrayList<Point> execute(ArrayList<Point> points) 
	{
		int numberofpoints = points.size();
		Point currentPoint = new Point();
		boolean isConvex;
		ArrayList<Line> Edges = new ArrayList<Line>();
		
		
		for (int x = 0; x < numberofpoints; x++) /*O(N ^ 3)*/
		{
			for (int y = 0; y < numberofpoints; y++) 
			{
				if (x == y) continue; /* for each pair */
				
				isConvex = true;
				
				for (int xy = 0; xy < numberofpoints; xy++)
				{
					if (xy == x || xy == y) continue;
					
					currentPoint = points.get(xy);
					
					if (!IsRigth(currentPoint, new Line(points.get(x), points.get(y))))
					{	/* Check each point by drawing a new line,
					 		if the point on left then, this point on the hull */
						isConvex = false;
						break;
					}
				}
				
				if (isConvex)
				{
					Edges.add(new Line(points.get(x), points.get(y)));
				}
			}
		}
		
		return SortVertices(Edges);
		
	}
	

	private boolean IsRigth(Point p, Line line)
	{
		float x,y,x1,x2,y1,y2,c;
		x = p.x;
		y = p.y;
		x1 = line.p1.x;
		x2 = line.p2.x;
		y1 = line.p1.y;
		y2 = line.p2.y;
		c = (x2 - x1)*(y - y1) - (y2 - y1)*(x - x1);

		return  c < 0;
	}
	
	private boolean IsLeft(Point p, Line line)
	{
		float x,y,x1,x2,y1,y2,c;
		x = p.x;
		y = p.y;
		x1 = line.p1.x;
		x2 = line.p2.x;
		y1 = line.p1.y;
		y2 = line.p2.y;
		c = (x2 - x1)*(y - y1) - (y2 - y1)*(x - x1);
		
		return c > 0;
	}
	
	private ArrayList<Point> SortVertices(ArrayList<Line> lines)
	{
		@SuppressWarnings("unchecked")
		ArrayList<Line> Sort = (ArrayList<Line>)lines.clone();
		Collections.sort(Sort, new PointCompare());
		
		int n = Sort.size();
		
		Line baseLine = new Line(Sort.get(0).p1, Sort.get(n-1).p1);
		/*System.out.println("x: " + Sort.get(0).p1 + ", y: " + Sort.get(n-1).p1);*/
		
		ArrayList<Point> result = new ArrayList<Point>();
		
		result.add(Sort.get(0).p1);
		
		for (int i = 1; i < n; i++)
		{
			if (IsLeft(Sort.get(i).p1, baseLine))
			{
				result.add(Sort.get(i).p1);
			}
		}
		
		result.add(Sort.get(n-1).p1);
		
		for (int i = n-2; i > 0; i--)
		{
			if (IsRigth(Sort.get(i).p1, baseLine))
			{
				result.add(Sort.get(i).p1);
			}
		}
		
		return result;
	}
	
	private class Line
	{
		public Point p1, p2;
		
		// Line constructor 
		public Line(Point p1, Point p2)
		{
			this.p1 = p1;
			this.p2 = p2;
		}
	
	}
	
	private class PointCompare implements Comparator<Line>
	{
		@Override
		public int compare(Line o1, Line o2) 
		{
			return (new Integer(o1.p1.x)).compareTo(new Integer(o2.p1.x));
		}
	}
}
