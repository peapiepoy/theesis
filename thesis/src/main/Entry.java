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
import segmentation.Clustering;
import segmentation.SplitAndMerge;
import view.TargetSelection;
import view.SegmentationPanel;
import controller.MouseHandler;

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
	public Clustering clustering;
	// segmentation RegionGrowing
	
	Entry(){
		
	}
	public void segmentationProcess() {
		this.spam = new SplitAndMerge(img);
		this.clustering = new Clustering(img);
		
		SegmentationPanel.getInstance().spam.setDisplayImg(this.spam.getSegmentedImage());
		
		SegmentationPanel.getInstance().clustering.segmenting = true;
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
	public TargetAreaSelection getTargetAreaSelection() {
		return targetArea;
	}
	/*
	 * creates representation of the image to a 2d array
	 * and sidelining setting the max and min pixel :)
	 */
	public void toArray() {
		int iw = img.getWidth();
		int ih = img.getHeight();
		int pixel, a, r, g, b, ave;
		int minR, minG, minB, maxR, maxG, maxB;
		int []pixels = new int[iw * ih];
		PixelGrabber pg = new PixelGrabber(img, 0, 0, iw, ih, pixels, 0, iw);
		
		this.pixelmap = new int[ih][iw];
		minR = 255;
		minG = 255;
		minB = 255;
		maxR = 0;
		maxG = 0;
		maxB = 0;
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		int i, j=0;
		for (i = 0; i < ih; i++) {
			for (j = 0; j < iw; j++) {
				this.pixelmap[i][j] = pixels[i * iw + j];
				pixel = pixelmap[i][j];
				a = 0xff & pixel >> 24;
				r = 0xff & pixel >> 16; // Obtain the red Component
				g = 0xff & pixel >> 8; // Obtain the green Component
				b = 0xff & pixel; // Obtain the blue Component
				
				maxR = maxR>r? maxR: r;
				maxG = maxG>g? maxG: g;
				maxB = maxB>b? maxB: b;
				
				minR = minR<r? minR: r;
				minG = minG>g? minG: g;
				minB = minB>b? minB: b;
			}
		}
		this.minTH = (minR + minG + minB) / 3;
		this.maxTH = (maxR + maxG + maxB) / 3;
	}
	
	public BufferedImage grayscaling() {
		int iw = img.getWidth();
		int ih = img.getHeight();
		int pixel, a, r, g, b, ave;
		grayscaled = new BufferedImage(iw, ih, BufferedImage.TYPE_BYTE_GRAY);
		
		
		for(int j=0; j<ih;j++){
			for(int i=0;i<iw;i++){
				pixel = this.pixelmap[j][i];
				a = 0xff & pixel >> 24;
				r = 0xff & pixel >> 16; // Obtain the red Component
				g = 0xff & pixel >> 8; // Obtain the green Component
				b = 0xff & pixel; // Obtain the blue Component
				
				ave = (r+g+b)/3;
				
				int pix = (a<<24) | (ave<<16) | (ave<<8) | ave;
				this.grayscaled.setRGB(i,j,pix);
			}
		}
		return grayscaled;
	}
	
	public static Entry getInstance() {
		if(instance == null)
			instance = new Entry();
		return instance;
	}
	
}
