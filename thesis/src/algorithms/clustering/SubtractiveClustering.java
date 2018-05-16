package algorithms.clustering;


import java.awt.Point;
import java.util.ArrayList;

public class SubtractiveClustering {
	private int[][] pixelmap;
	private double[][]potentialMap;
	
	private final double ALPHA = 4.0;
	private final double RA = 0.3;			// JAVASOURCE = 0.001
	private final double RB = RA * 1.15;	// javasource = 1.5 * RA
	public static double ACCEPT_RATIO = 0.70d;	// .98 sya originally
    public static double REJECT_RATIO = 0.16d;
	
	public ArrayList<Point> clusterCenter;
	private int kCluster;
	
	public SubtractiveClustering(int [][]pixelmap, int kCluster) {
		pixelmap = new int[pixelmap.length][pixelmap[0].length];
		this.pixelmap = pixelmap;
		potentialMap = new double[pixelmap.length][pixelmap[0].length];
		this.kCluster = kCluster;
		clusterCenter = new ArrayList<>();
		process();
	}
	
	private void process() {
		// calculate potential for every pixel of the image
		// find max potential and set as the first center cluster
		// update potential value of other remaining pixels based on the first cluster center
		// repeat finding max
		System.out.println("sca processing...");
		calculatePotential();	// initial pixel potentials
		clusterCenter.add(maxPotentialPoint());
		
		
		for(int index=0; index < kCluster; index++) {
			 // if potential ni (candidate) centercluster is > accept_ratio
				// add to the cluster center arraylist
				// update potentials
			// else if 
			System.out.println(index+" cluster centroid... ");
			updatePotential(clusterCenter.get(index));
			clusterCenter.add(maxPotentialPoint());
		}
		
		
	}

	// calculates the potential for every pixel of the image (pcs processed)
	public void calculatePotential() {
		double potential = 0.0;
		for(int x1 = 0; x1< pixelmap.length; x1++) {
			System.out.print(".");
			for(int y1=0; y1 < pixelmap[0].length; y1++) {
				
				int rgb1 = pixelmap[x1][y1];
//				int a1 = (rgb1>>24)&0xff;
				int r1 = (rgb1>>16)&0xff;
				int g1 = (rgb1>>8)&0xff;
				int b1 = rgb1&0xff;
				
				for(int x = 0; x< pixelmap.length; x++) {
					for(int y=0; y < pixelmap[0].length; y++) {
						int rgb = pixelmap[x][y];
//						int a = (rgb>>24)&0xff;
						int r = (rgb>>16)&0xff;
						int g = (rgb>>8)&0xff;
						int b = rgb&0xff;
						
						potential = Math.exp(-ALPHA * ( Math.pow(r1-r , 2) + 
														Math.pow(g1-g , 2) + 
														Math.pow(b1-b , 2) ) ) / (Math.pow(RA,2));
						
						potentialMap[x][y] = potential;
					}
				}
			}
		}
		System.out.println();
	}
	
	public void updatePotential(Point thisCentroid) {
		System.out.println("update potential ...");
		double newPotential = 0.0;
		
		int pixelC, rC, gC, bC;
		double potentialC = potentialMap[(int)thisCentroid.getX()][(int)thisCentroid.getY()];
		pixelC = pixelmap[(int)thisCentroid.getX()][(int)thisCentroid.getY()];
		rC = (pixelC>>16)&0xff;
		gC = (pixelC>>8)&0xff;
		bC = pixelC&0xff;
		
		for(int x = 0; x< pixelmap.length; x++) {
			for(int y=0; y < pixelmap[0].length; y++) {
				int rgb = pixelmap[x][y];
				int r = (rgb>>16)&0xff;
				int g = (rgb>>8)&0xff;
				int b = rgb&0xff;
				
				newPotential = potentialMap[x][y] * ( potentialC * Math.exp( -ALPHA * ( Math.pow(r-rC , 2) + 
																						Math.pow(g-gC , 2) + 
																						Math.pow(b-bC , 2) ) ) /
																						Math.pow(RB,2)
													);
				
				potentialMap[x][y] = newPotential;
			}
		}
		
	}
	
	public Point maxPotentialPoint() {
		double maxPotential = potentialMap[0][0];
		Point maxPoint = new Point(0,0);
		
		for(int x = 0; x< potentialMap.length; x++) {
			for(int y=0; y < potentialMap[0].length; y++) {
				if(maxPotential < potentialMap[x][y]) {
					maxPotential = potentialMap[x][y];
					maxPoint = new Point(x, y);
				}
			}
		}
		System.out.println("maxpotential: "+maxPotential);
		return maxPoint;
	}
	
	
}


class Cell{
	public int x, y;
	public double potential;
	
	public Cell(int x, int y, double potential){
		this.x = x;
		this.y = y;
		this.potential = potential;
	}
}