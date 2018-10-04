package model.clustering;

import java.awt.Point;
import java.util.ArrayList;

public class Cluster {
	private ArrayList<Point> points; // all the pixels
	private int[] centroid; // represented by RGB (3 values)
	private int [][]pixelmap;
	
	public Cluster(int [][]pixelmap) {
		this.points = new ArrayList<Point>();
		this.pixelmap = pixelmap;
		this.centroid = new int[3];
	}
	
	public void addPoint(Point point) {
		points.add(point);
	}
	
	/**
	 * Supposed to be used when there is a change in the points
	 * This updates the centroid to fit the mean of all the values of of the points
	 * O(p) : p for # of points
	 */
	public void updateCentroid() {
		if (this.points.size() > 0) {
			
			int red = 0, green = 0, blue = 0;
		
			for (Point point: points) {
				
				int rgb = pixelmap[point.x][point.y];
				red += (rgb>>16)&0xff;
				green += (rgb>>8)&0xff;
				blue += rgb&0xff;
				
			}
		
			// updating the centroid based on the average of all the values in RGB
			centroid[0] = red/points.size();
			centroid[1] = green/points.size();
			centroid[2] = blue/points.size();
		}
	}
	
	public ArrayList<Point> getPoints() {
		return this.points;
	}
	
	public int[] getCentroid() {
		return this.centroid;
	}
}
