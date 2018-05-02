package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import inpainting_utils.TargetAreaSelection;
import view.TargetSelection;
import view.SegmentationPanel;
import controller.MouseHandler;
import process.Clustering;
import process.SplitAndMerge;

public class Entry {
	private static Entry instance;
	private BufferedImage img, grayscaled;
	private Image oImg;
	private TargetAreaSelection targetArea;
	private Polygon targetRegion;
	public int [][]pixelmap;
	public int maxTH, minTH;
	// segmentation Split and Merge
	private SplitAndMerge spam;
	private Clustering clustering;
	// segmentation RegionGrowing
	
	Entry(){
		
	}
	public void segmentationProcess() {
		this.spam = new SplitAndMerge(img);
		this.clustering = new Clustering(img);
		
		SegmentationPanel.getInstance().spam.setDisplayImg(this.spam.getSegmentedImage());
		SegmentationPanel.getInstance().clustering.setDisplayImg(this.clustering.getSegmentedImage());
		
		System.out.println("Entry.segmentationProcess() ends");
	}
	
	
	public void setImage(String path) {
		try {
			this.oImg = ImageIO.read(new File(""+path));
		} catch (IOException e) {
			System.out.println("File not loaded!");
			System.exit(0);
		}
		this.img = new BufferedImage(oImg.getWidth(null), oImg.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		Graphics2D bGr = img.createGraphics();
	    bGr.drawImage(oImg, 0, 0, null);
	    bGr.dispose();
	    
	    toArray();
	    
		this.targetArea = new TargetAreaSelection(img);
		TargetSelection.getInstance().addImageToScrollpane();
		
		MouseHandler.getInstance();
	}
	public BufferedImage getImage() {
		return img;
	}
	public void setPolygon() {
		Vector xx = getTargetAreaSelection().getPointsX();
		Vector yy = getTargetAreaSelection().getPointsY();
		int[] x = new int[xx.size()];
		int[] y = new int[yy.size()];
		for(int h = 0; h< x.length; h++) {
			x[h] = (Integer) xx.get(h);
			y[h] = (Integer) yy.get(h);
		}
		this.targetRegion = new Polygon(x, y, x.length);
	}
	
	public BufferedImage grayScaling(){
		int w = img.getWidth();
		int h = img.getHeight();
		int minR, minG, minB, maxR, maxG, maxB;
		
		minR = 255;
		minG = 255;
		minB = 255;
		maxR = 0;
		maxG = 0;
		maxB = 0;
		grayscaled = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_GRAY);
		System.out.println("grayscaling...");
		int pixel, r,g, b, a;
		
		for(int y=0;y<h;y++){
			for(int x=0;x<w;x++){
				pixel = this.img.getRGB(x, y);
//				pixelMap[x][y] = rgb;
//				pixel = this.pixelmap[x][y];
				r = 0xff & pixel >> 16; // Obtain the red Component
				g = 0xff & pixel >> 8; // Obtain the green Component
				b = 0xff & pixel; // Obtain the blue Component
				
				a = 0xff & pixel >> 24;
//				int r = (rgb>>16)&0xff;
//				int g = (rgb>>8)&0xff;
//				int b = rgb&0xff;
				int ave = (r+b+g) / 3;
				maxR = maxR>r? maxR: r;
				maxG = maxG>g? maxG: g;
				maxB = maxB>b? maxB: b;
				
				minR = minR<r? minR: r;
				minG = minG>g? minG: g;
				minB = minB>b? minB: b;
				
				int pix = (a<<24) | (ave<<16) | (ave<<8) | ave;
				this.grayscaled.setRGB(x,y,pix);
			}
		}
		this.minTH = (minR + minG + minB) / 3;
		this.maxTH = (maxR + maxG + maxB) / 3;
		return grayscaled;
	}
	
	public TargetAreaSelection getTargetAreaSelection() {
		return targetArea;
	}
	
	public void toArray() {
		int iw = img.getWidth();
		int ih = img.getHeight();
		this.pixelmap = new int[ih][iw];
		
		int []pixels = new int[iw * ih];
		PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels, 0, iw);
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		for (int i = 0; i < ih; i++) {
			for (int j = 0; j < iw; j++) {
				
				this.pixelmap[i][j] = pixels[i * iw + j];
				if(i>(iw-6) && j<5) {
					int r = 0xff & pixelmap[i][j] >> 16;
					System.out.print(r+" ");
				}
			}
			if(i>(iw-6))
				System.out.println();
		}
	}
	
	public static Entry getInstance() {
		if(instance == null)
			instance = new Entry();
		return instance;
	}
	
}
