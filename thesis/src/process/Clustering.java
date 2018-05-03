package process;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;

import main.Entry;

public class Clustering {
	private int width, height;
	private int max, min;
	BufferedImage img = null;
	BufferedImage pcs = null;
	BufferedImage segmentedImage = null;
	BufferedImage grayscaled = null;
	
	public Clustering(BufferedImage img) {
		this.img = img;
		this.width = img.getWidth();
		this.height = img.getHeight();
		grayscaled = new BufferedImage(width, height, 
			      BufferedImage.TYPE_BYTE_GRAY);
		
		grayscaled = Entry.getInstance().grayScaling();
		//partialContrastStretching();
	}
	
	public void setImage(BufferedImage displayImage) {
		this.segmentedImage = displayImage;
	}
	
	public void japplyPCS() { // applying partial contrast stretching
		int pixelSize = img.getColorModel().getPixelSize();
		double mp = Math.pow(2, pixelSize) - 1;
		pcs = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
		int range = max - min;
		System.out.println("range: "+range+"   max: "+max+"   min:"+min+"   mp: "+mp);
		
		for(int y=0;y<height; y++) {
			for(int x = 0; x < width; x++) {
				int rgb = grayscaled.getRGB(x,y);
				int a = (rgb>>24)&0xff;
				int r = (rgb>>16)&0xff; // red lang ako gikuha kay pareho ra man silag values
				
				
				int pcsVal = ((r - min) / range) * (int) mp;
				//int pcsVal = ((r - min)* (0-255/(max-min))+255);
				int pcsPixel = (a<<24) | (pcsVal<<16) | (pcsVal<<8) | pcsVal;
				pcs.setRGB(x,y,pcsPixel);
				if(x<30 && y<30)
					System.out.print("-"+pcsPixel);
				
			}
			if(y<30)
				System.out.println();
		}
	}
	
	public BufferedImage partialContrastStretching() {
		pcs =  new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		
		int maxTH = Entry.getInstance().maxTH;
		int minTH = Entry.getInstance().minTH;
		int fmin = 0, fmax = 255;
		System.out.println("maxTH: "+maxTH+"  minTH: "+minTH);
		
		
		for(int y=0; y< img.getHeight(); y++) {
			for(int x = 0; x< img.getWidth(); x++) {
				int rgb = img.getRGB(x,y);
				int a = (rgb>>24)&0xff;
				int r = (rgb>>16)&0xff;
				int g = (rgb>>8)&0xff;
				int b = rgb&0xff;
//				
//				//int pixel = (fmax - fmin) * (maxTH - minTH) * ((r+g+b)-(minTH * 3)) + fmin;
			
				//int pixel;
				int upperGap = 250-maxTH;
				fmin = upperGap + minTH;
				int pixel1 = ( (fmax-fmin)/ (maxTH - minTH) );
				int pixelr = pixel1 * (r-minTH);
				int pixelg = pixel1 * (g-minTH);
				int pixelb = pixel1 * (b-minTH);
				
				
//				
				
				
				//int pixel = ((fmax - fmin) * (maxTH - minTH) * (rgb&0xff - fmin)) + fmin;
//				pixel = pixel>255?255:pixel;
//				pixel = pixel < 0? 0: pixel;
				
				
				
				int pixelVal = (a<<24) | (pixelr<<16) | (pixelg <<8) | pixelb;
				pcs.setRGB(x,y,pixelVal);
//				if(x<5&&y<5)
//					System.out.print("  "+pixel+"|"+(pixel<<16+","+pixel<<8);
				
			}
			if(y<5)
				System.out.println();
		}
		return pcs;
	}
		
	
	public BufferedImage getSegmentedImage() {
		return grayscaled;
	}

}
