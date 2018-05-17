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
		pcsToString();
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
		potentialToString();
		
		for(int index=1; index < kCluster; index++) {
			 // if potential ni (candidate) centercluster is > accept_ratio
				// add to the cluster center arraylist
				// update potentials
			// else if 
			System.out.println(index+" cluster centroid... ");
			updatePotential(clusterCenter.get(index));
			clusterCenter.add(maxPotentialPoint());
			potentialToString();
		}
		
		
	}

	// calculates the potential for every pixel of the (pcs processed) image
	public void calculatePotential() {
		double potential = 0.0;
		
		for(int xn = 0; xn< pixelmap.length; xn++) {
			for(int yn=0; yn < pixelmap[0].length; yn++) {
				
				int rgbn = pixelmap[xn][yn];
				int rn = (rgbn>>16)&0xff;
				int gn = (rgbn>>8)&0xff;
				int bn = rgbn&0xff;
				
					double diff = 0;
					for(int xi = 0; xi < xn; xi++) {
						for(int yi=0; yi < yn; yi++) {
							
							int rgbi = pixelmap[xi][yi];
							int ri = (rgbi>>16)&0xff;
							int gi = (rgbi>>8)&0xff;
							int bi = rgbi&0xff;
							
							diff += Math.pow(rn-ri , 2) + 
									Math.pow(gn-gi , 2) + 
									Math.pow(bn-bi , 2) ;
						}
					}
					
				potential = Math.exp( (-ALPHA * diff ) / (Math.pow(RA,2)) );
				
				potentialMap[xn][yn] = potential;
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
		
		for(int xn = 0; xn < pixelmap.length; xn++) {
			for(int yn=0; yn < pixelmap[0].length; yn++) {
				int rgb = pixelmap[xn][yn];
				int r = (rgb>>16)&0xff;
				int g = (rgb>>8)&0xff;
				int b = rgb&0xff;
				
				newPotential = potentialMap[xn][yn] - ( potentialC * Math.exp( -ALPHA * ( Math.pow(r-rC , 2) + 
																						Math.pow(g-gC , 2) + 
																						Math.pow(b-bC , 2) ) ) /
																						Math.pow(RB,2)
													);
				
				potentialMap[xn][yn] = newPotential;
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
		System.out.println("maxpotential: "+maxPotential+ " maxpoint:"+maxPoint.getX()+","+maxPoint.getY());
		return maxPoint;
	}
	
	public void potentialToString() {
		for(int x = 40; x< 50; x++) {
			for(int y=40; y < 50; y++) {
				System.out.printf("%.0f",potentialMap[x][y]);
				System.out.print(" ");
			}
			System.out.println();
		}
	}
	
	public void pcsToString() {
		for(int x = 40; x< 50; x++) {
			for(int y=40; y < 50; y++) {
//				System.out.printf("%.0f",potentialMap[x][y]);
				System.out.print(" "+pixelmap[x][y]);
			}
			System.out.println();
		}
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