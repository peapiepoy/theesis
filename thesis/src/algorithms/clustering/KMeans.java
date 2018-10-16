package algorithms.clustering;

import model.clustering.Cluster;

import java.awt.Color;
import java.awt.Graphics2D;
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
	public static BufferedImage segmented;
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
		
		//this.segmented = kmeans_helper();
	}
	
	public void process() {
		
	}
	
	private static BufferedImage kmeans_helper(BufferedImage originalImage, int k){
		int w=originalImage.getWidth();
		int h=originalImage.getHeight();
		BufferedImage kmeansImage = new BufferedImage(w,h,originalImage.getType());
		Graphics2D g = kmeansImage.createGraphics();
		g.drawImage(originalImage, 0, 0, w,h , null);
		// Read rgb values from the image
		int[] rgb=new int[w*h];
		int count=0;
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		for(int i=0;i<w;i++){
		    for(int j=0;j<h;j++){
				int rgbVal = kmeansImage.getRGB(i,j);			
				rgb[count++] = rgbVal;
				if (rgbVal < min){
					min = rgbVal;
				}
				if(rgbVal > max){
					max = rgbVal;
				}
		    }
		}
		// Call kmeans algorithm: update the rgb values
		kmeans(rgb,k, min, max);
	
		// Write the new rgb values to the image
		count=0;
		for(int i=0;i<w;i++){
		    for(int j=0;j<h;j++){
			kmeansImage.setRGB(i,j,rgb[count++]);
		    }
		}
		return kmeansImage;
	}
	
	// Your k-means code goes here
    // Update the array rgb by assigning each entry in the rgb array to its cluster center
    private static void kmeans(int[] rgb, int k, int min, int max){

    	double[][] means = new double[k+1][4];
    	//initializing k mean values
    	long[] sum = new long[k+1];
    	long[] count = new long[k+1];
    	double avg=(min+max)/2.0;
    	for(int i = 1; i<= k; i++){
    		Random rand = new Random();
    		means[i][0] = rand.nextDouble()*255;
    		means[i][1] = rand.nextDouble()*255;
    		means[i][2] = rand.nextDouble()*255;
    		means[i][3] = rand.nextDouble()*255;
    		sum[i] = 0;
    		count[i] = 0;
    	}
    	int[] red = new int[rgb.length];
    	int[] blue = new int[rgb.length];
    	int[] green = new int[rgb.length];
    	int[] alpha = new int[rgb.length];
    	for (int i=0; i<rgb.length; i++){
        	Color c = new Color(rgb[i]);
        	red[i] = c.getRed();
        	blue[i] = c.getBlue();
        	green[i] = c.getGreen();
        	alpha[i]=c.getAlpha();
    	}
    	int[] curAssignments = new int[rgb.length];
    	boolean change = true;
    	while(change){
    		//finding the closest mean
    		change = false;
    		int[] tempAssignments=new int[rgb.length];
	    	for(int x = 0; x < rgb.length; x++){
	    		double closest = Double.MAX_VALUE;
	    		for(int m = 1; m <= k; m++){
	    			
	    			double distance = ( Math.pow((double)(red[x] - means[m][0]), 2) + Math.pow((double)(blue[x] - means[m][1]), 2) + Math.pow((double)(green[x] - means[m][2]), 2)+ Math.pow((double)(alpha[x] - means[m][3]), 2));
	    			if (distance < closest){
	    				tempAssignments[x]=m;
	    				closest = distance;
		    			}
	    			}		    			
	     		}
	    	//update step
	    	double changeVal=0;
	    	for(int x=0;x<rgb.length;x++){
	    		if(curAssignments[x]!=tempAssignments[x])changeVal+=1;
	    	}
	    	if(changeVal>0){
	    		change=true;
	    		for(int x=0;x<rgb.length;x++){
		    		curAssignments[x]=tempAssignments[x];
		    	}
	    		for(int m=1; m<= k; m++){
		        	sum[m] = 0;
		        	count[m] = 0;
	    		}
	    		
		    	for(int i=0; i< rgb.length; i++){
		    			int meanInd = tempAssignments[i];
		    			count[meanInd] += 1;
		    			if(count[meanInd]==1){
		    				means[meanInd][0]=red[i];
		    				means[meanInd][1]=blue[i];
		    				means[meanInd][2]=green[i];
		    				means[meanInd][3]=alpha[i];
		    			}
		    			else{
		    				double prevC=(double)count[meanInd]-1.0;
			    			means[meanInd][0]=(prevC/(prevC+1.0))*(means[meanInd][0]+(red[i]/prevC));
			    			means[meanInd][1]=(prevC/(prevC+1.0))*(means[meanInd][1]+(blue[i]/prevC));
			    			means[meanInd][2]=(prevC/(prevC+1.0))*(means[meanInd][2]+(green[i]/prevC));
			    			means[meanInd][3]=(prevC/(prevC+1.0))*(means[meanInd][3]+(alpha[i]/prevC));
			    			
		    			}
		    					    			
		    	}
	    	}
				
		}
    	for(int i=0; i<rgb.length; i++){    		
    		rgb[i]=(int)means[curAssignments[i]][3]<<24|(int)means[curAssignments[i]][0]<<16 | (int)means[curAssignments[i]][2]<<8 | (int)means[curAssignments[i]][1]<<0;
    	}
}
	
}