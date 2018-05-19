package segmentation;

import java.awt.image.BufferedImage;
import algorithms.clustering.SubtractiveClustering;

import main.Entry;
import algorithms.clustering.KMeans;


public class Clustering {
	private int width, height;
	public int pcsMatrix[][];
	
	BufferedImage img = null;
	BufferedImage pcsImg = null;
	BufferedImage segmentedImage = null;
	BufferedImage grayscaled = null;
	
	public SubtractiveClustering subtractiveClustering;
	public KMeans kmeans;
	
	public final int kCluster = 3;
	
	public Clustering(BufferedImage img) {
		this.img = img;
		this.width = img.getWidth();
		this.height = img.getHeight();
																//		grayscaled = new BufferedImage(width, height, 
																//			      BufferedImage.TYPE_BYTE_GRAY);
		pcsImg = partialContrastStretching();
//		pcsMatrixToString();
//		subtractiveClustering = new SubtractiveClustering(pcsMatrix, kCluster);
		kmeans = new KMeans(pcsMatrix, kCluster);
		kmeans.process();
		this.segmentedImage = kmeans.segmentedImage();
	}
	
	public void pcsMatrixToString() {
		System.out.println("\t\tPCS Matrix from CLustering.java");
		for(int b=40; b<60; b++) {
			for (int n=40; n<60; n++) {
				int rgb = pcsMatrix[b][n];
				int r = (rgb>>16)&0xff;
				int g = (rgb>>8)&0xff;
				int z = rgb&0xff;
				
				System.out.print(" "+ r+","+g+","+z);
			}
			System.out.println();
		}
	}
	
	public void setImage(BufferedImage displayImage) {
		this.segmentedImage = displayImage;
	}
	
	
	public BufferedImage partialContrastStretching() {
		pcsImg =  new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		pcsMatrix = new int[height][width];
		int [][]pixelmap = Entry.getInstance().pixelmap;
		int maxTH = Entry.getInstance().maxTH;
		int minTH = Entry.getInstance().minTH;
		int fmin, upperGap, fmax;
		int pixelr, pixelg, pixelb;
		double pixel1;
		
		upperGap = 255-maxTH;
		fmax = 255;
		fmin = minTH - upperGap;
		pixel1 = ( (fmax-fmin)/ (maxTH - minTH) );
		
		System.out.println("maxTH: "+maxTH+"  minTH: "+minTH+" fmin:"+fmin+" pixel1:"+pixel1);
		
		for(int y=0; y< img.getHeight(); y++) {
			for(int x = 0; x< img.getWidth(); x++) {
				int rgb = pixelmap[y][x];
				int a = (rgb>>24)&0xff;
				int r = (rgb>>16)&0xff;
				int g = (rgb>>8)&0xff;
				int b = rgb&0xff;
				
				int pcs = (fmax-fmin) / (maxTH-minTH);
				
				pixelr = (int) (pcs * (r-minTH)) + fmin;
				pixelg = (int) (pcs * (g-minTH)) + fmin;
				pixelb = (int) (pcs * (b-minTH)) + fmin;

				int pixelVal = (a<<24) | (pixelr<<16) | (pixelg <<8) | pixelb;
				pcsImg.setRGB(x,y,pixelVal);
				this.pcsMatrix[y][x] = pixelVal;  
			}
		}
		
		return pcsImg;
	}
	
	
	public BufferedImage getSegmentedImage() {
		return segmentedImage;
	}

}
