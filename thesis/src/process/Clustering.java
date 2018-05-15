package process;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import main.Entry;

public class Clustering {
	private int width, height;
	private int max, min;
	private int pcsMatrix[][];
	BufferedImage img = null;
	BufferedImage pcsImg = null;
	BufferedImage segmentedImage = null;
	BufferedImage grayscaled = null;
	
	public Clustering(BufferedImage img) {
		this.img = img;
		this.width = img.getWidth();
		this.height = img.getHeight();
																//		grayscaled = new BufferedImage(width, height, 
																//			      BufferedImage.TYPE_BYTE_GRAY);
		pcsImg = partialContrastStretching();
	}
	
	public void setImage(BufferedImage displayImage) {
		this.segmentedImage = displayImage;
	}
	
	
	public BufferedImage partialContrastStretching() {
		pcsImg =  new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		pcsMatrix = new int[width][height];
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
				pcsMatrix[y][x] = pixelVal; 
			}
		}
		
		return pcsImg;
	}
		
	public void subtractiveClusteringAlgorithm() {
		
	}
	
	public BufferedImage getSegmentedImage() {
		return pcsImg;
	}

}
