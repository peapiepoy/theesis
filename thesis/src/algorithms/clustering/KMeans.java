package algorithms.clustering;

import model.clustering.Cluster;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Random;

public class KMeans {
	private ArrayList<Point> clusterCenters;
	private int [][]pixelmap;
	private int [][]pixelCluster;
	private int row, col;
	private Cluster[] clusters;
	public BufferedImage segmented;
	private int k;
	
	
//	public KMeans(ArrayList<Point> clusterCenters, int [][]pixelmap) {
	public KMeans( int [][]pixelmap, int k) {
//		this.clusterCenters = clusterCenters;
		this.pixelmap = pixelmap;
		this.row = pixelmap.length;
		this.col = pixelmap[0].length;
		this.k = k;
		this.pixelCluster = new int[row][col];
		System.out.println("CONSTRUCTOR r: "+row+ " col: "+col);
		segmented = new BufferedImage(col, row, BufferedImage.TYPE_INT_ARGB);
		
		process();
	}
	
	public void process() {
		// compute euclidean distance of each centroids from every pixel
		assignClustersRandomly();
		rebuildClusters();
		generateClusters();
	}
	
	/**
	 * assign all pixels with a random cluster
	 * [O(p) : p for # of pixels] time complexity
	 */
	private void assignClustersRandomly() {
		Random random = new Random();
		
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				this.pixelCluster[i][j] = random.nextInt(this.k);
			}
		}
		
		System.out.println("Completed assigning clusters randomly to all pixels (stored in cluster array in ImageMatrix).");	
	}
	
	/**
	 * Two actions:
	 * 1)adds all the points associated with a certain cluster into that respective cluster
	 * 2)updates the centroids of all of the clusters
	 * [O(p*c + p) : p for # of pixels and c for # of clusters] time complexity
	 */
	private void rebuildClusters() {
		clusters = new Cluster[this.k];
		System.out.print("rebuildcluster() r: "+row+ " col: "+col);
		//[O(p) : p for # of pixels] time complexity, since iterating through all pixels
		for (int i = 0; i < row; i++) {
			for (int j = 0; j < col; j++) {
				
				int clusterIndex = this.pixelCluster[i][j];
				
				if (clusters[clusterIndex] == null) {
					clusters[clusterIndex] = new Cluster(pixelmap);
				}
				// the order is switched because the x coordinate is i (width iteration) 
				//and the y coordinate is j (height iteration)
				clusters[clusterIndex].addPoint(new Point(j, i));
			}
		}
		
		//[O(p*c) p for # of pixels and c for # of clusters] time complexity
		for (int i = 0; i < clusters.length; i++) {
			if (clusters[i] == null)
				continue;
			clusters[i].updateCentroid();
		}
	}
	
	/**
	 * uses the euclidean distance to find the nearest centroid to a point (for all points)
	 * if a change occurred, then the algorithm must be repeated again until no change occurs (indication of the clusters stabilizing)
	 */
	private void generateClusters() {
		boolean changeOccurred;
		
		// do while loop is used to allow one iteration of the program before checking for a change
		do {
			changeOccurred = false;
			
			for (int i = 0; i < row; i++) {
				for (int j = 0; j < col; j++) {
					double minDistance = Double.MAX_VALUE;
					int bestClusterIndex = -1;
					
					for (int clusterIndex = 0; clusterIndex < clusters.length; clusterIndex++) {
						
						// an empty cluster (best to use this instead of looking
						// for another cluster, since we can make this the closest
						if (clusters[clusterIndex] == null) {
							clusters[clusterIndex] = new Cluster(pixelmap);
							minDistance = 0;
							bestClusterIndex = clusterIndex;
							break;
						} else {
							double computedDistance = euclideanDistance(this.pixelmap[i][j],
																		clusters[clusterIndex].getCentroid());

							if (computedDistance < minDistance) {
								minDistance = computedDistance;
								bestClusterIndex = clusterIndex;
							}
						}
					}
					
					if (this.pixelCluster[i][j] != bestClusterIndex) {
						this.pixelCluster[i][j] = bestClusterIndex;
						changeOccurred = true;
					}
					
				}
			}
			
			if (changeOccurred)
				rebuildClusters();
			
			
		} while (changeOccurred);
		
		System.out.println("Completed generating the clusters for the current image.");	
	}
	
	/*
	 * Computes the euclidean distance between two points
	 * [O(length of the vectors) : length of the vectors represent the depth of the data (in this case: 3 *RGB*)] time complexity
	 */
	private double euclideanDistance(int pixel, int[] centroid) {
		double distance = 0;
		int rp = (pixel>>16)&0xff;
		int gp = (pixel>>8)&0xff;
		int bp = pixel&0xff;
		
		distance = Math.pow((rp - centroid[0]), 2) + Math.pow((gp - centroid[1]), 2) 
						+ Math.pow((bp - centroid[2]), 2);
		
		return Math.sqrt(distance);
	}
	
	public BufferedImage segmentedImage() {
		int[] wood = new int[]{222,184,135};
		int[] indigo = new int[]{75,0,130};
		int[] lawngreen = new int[]{124,252,0};
		int[] pxl = new int[3];
		
		for(int y = 0; y < pixelCluster.length; y++) {
			for( int x=0; x < pixelCluster[0].length; x++) {
				if(pixelCluster[y][x] == 0)
					pxl = wood;
				else if(pixelCluster[y][x] == 1)
					pxl =indigo;
				else if(pixelCluster[y][x] == 2)
					pxl =lawngreen;
				
				int pix = (80<<24) | (pxl[0]<<16) | (pxl[1]<<8) | pxl[2];
				this.segmented.setRGB(x,y,pix);
			}
		}
		
		return segmented;
	}
}
