package main;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.awt.image.PixelGrabber;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

import javax.imageio.ImageIO;

import inpainting_utils.TargetAreaSelection;
import segmentation.Clustering;
import segmentation.SpAM;
import algorithms.inpainting.ImageInpaint;
import view.TargetSelection;
import view.DisplayThreePanel;
import controller.MouseHandler;

public class Entry {
	
	private static Entry instance;
	private BufferedImage image, grayscaled, masked, inpaintedImage;
	private Image oimage;
	private TargetAreaSelection targetAreaSelection;
	public DisplayThreePanel segmenting, inpainting_panels;
	private Polygon targetRegionPoly;
	public int[][] original_pixel, masked_pixel, masked_binary;
	public int maxTH, minTH;					// im afraid we wont use this anymore
	// segmentation Split and Merge
	private SpAM spam;
	public Clustering clustering;
	public ImageInpaint imageInpainting;
	public int k_clustering, msd_spam, ssd_spam, ms_spam;
	public int[] maskColor = {255, 16, 245, 238, 0}; //argb	a=transparency
	
	// segmentation RegionGrowing
	
	Entry(){
		this.segmenting = new DisplayThreePanel(true);
		this.inpainting_panels = new DisplayThreePanel(false);
		
	}
	
/*
 *  image set for target selection in the inpainting process
 *  called upon selection of the image file
 */
	public void setImage(String path) {
		try {
			this.oimage = ImageIO.read(new File(""+path));
		} catch (IOException e) {
			System.out.println("File not loaded!");
			System.exit(0);
		}
		this.image = new BufferedImage(oimage.getWidth(null), oimage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		this.inpaintedImage = new BufferedImage(oimage.getWidth(null), oimage.getHeight(null), BufferedImage.TYPE_INT_ARGB);
		
		
		Graphics2D bGr = image.createGraphics();
	    bGr.drawImage(oimage, 0, 0, null);
	    bGr.dispose();
	    
	    this.original_pixel = toPixelArray(image);
	    
		this.targetAreaSelection = new TargetAreaSelection();
		TargetSelection.getInstance().addImageToScrollpane();
		
		MouseHandler.getInstance();
	}
	
	public BufferedImage getImage() {
		return this.image;
	}
	
	public void TASdisplayImage() {
		this.targetAreaSelection.setImage(this.image);
	}
	
	
/*
 *  calls each segmentation process and display the output on DisplayThreePanels	
 */
	public void segmentationProcess() {
		this.k_clustering = this.segmenting.kSpinnerValue();
		this.msd_spam = this.segmenting.msdSpinnerValue();
		this.ssd_spam = this.segmenting.ssdSpinnerValue();
		this.ms_spam = this.segmenting.msSpinnerValue();
		
		this.spam = new SpAM(image, ssd_spam, msd_spam, ms_spam);
		this.clustering = new Clustering(image, k_clustering);
		
		this.segmenting.spam.setDisplayImage(this.spam.getSegmentedImage());
		this.segmenting.clustering.setDisplayImage(this.clustering.getSegmentedImage());
		
	}
	
/*
 * 	when inpainting button is clicked	
 */
	public void inpaintingProcess() {
		this.imageInpainting = new ImageInpaint(image, masked); //is this true???? 4 26
		this.inpaintedImage = imageInpainting.getInpaintedImage();
		// display this inpaintedImage
		System.out.println("\n\ndisplaying segmented image....");
		this.inpainting_panels.setImage(this.inpaintedImage);
		this.inpainting_panels.displaySegmentationBoxes();
	}

	public TargetAreaSelection getTargetAreaSelection() {
		return targetAreaSelection;
	}
	
	public int[][] toPixelArray(BufferedImage image){
		int [][]pixelmap;
		int iw = image.getWidth();
		int ih = image.getHeight();
		int pixels[] = new int[iw * ih];
		PixelGrabber pg = new PixelGrabber(image, 0, 0, iw, ih, pixels, 0, iw);
		
		pixelmap = new int[ih][iw];
		
		try {
			pg.grabPixels();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < ih; i++) {
			for (int j = 0; j < iw; j++) {
				pixelmap[i][j] = pixels[i * iw + j];
			}
		}
		
		return pixelmap;
	}
	
/*toArray()
 * creates representation of the image to a 2d array
 * and setting the max and min pixel
 * toArray() method is removed and replaced with a generic method toPixelArray(BufferedImage image)
 * to see the method, review at docs>198.2>code collections>toArray().txt 
 */
	
	
	public BufferedImage grayscaling(int[][] pixelmap) {
		int iw = image.getWidth();
		int ih = image.getHeight();
		int pixel, a, r, g, b, ave;
		grayscaled = new BufferedImage(iw, ih, BufferedImage.TYPE_BYTE_GRAY);
		
		
		for(int j=0; j<ih;j++){
			for(int i=0;i<iw;i++){
				pixel = pixelmap[j][i];
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
	
	/*
	 * returns image with colored TARGET REGION
	 * masked_pixel is set
	 * masked_binary 1: source region. -1: target region
	 */
	
	public BufferedImage imageMasking() {
		int iw = original_pixel[0].length;
		int ih = original_pixel.length;
		System.out.println(" --------- Entry.image masking() ---------");
		
		this.masked = new BufferedImage(iw, ih, BufferedImage.TYPE_INT_ARGB);
		this.masked_pixel = new int[ih][iw];
		this.masked_binary = new int[ih][iw];
		
		for(int i=0; i < ih; i++) {
			for(int j=0; j< iw; j++) {
				
				this.masked.setRGB(j, i, original_pixel[i][j]);
				this.masked_binary[i][j] = 1;
				this.masked_pixel[i][j] = original_pixel[i][j];
				// checks if pixel is in the target region
				if(this.targetRegionPoly.contains(new Point(j, i))) {
					
					int rgb = (maskColor[0]<<24) | (maskColor[1]<<16) | (maskColor[2]<<8) | maskColor[3];
					this.masked.setRGB(j, i, rgb);
					this.masked_binary[i][j] = 0;			
					this.masked_pixel[i][j] = rgb;
					
				}
//				System.out.print(masked_binary[i][j]+"");
			}
//			System.out.println();
		}
		return this.masked;
	}
/*
 * called when SUBMIT button in TargetAreaSelection is pressed	
 */
	public void submitTargetRegion() {
		setPolygon(); //saves polygon in the Entry class
		this.masked = imageMasking();							
		
		this.inpainting_panels.setImage(this.image);
		this.inpainting_panels.displaySegmentationBoxes();
				
		// display the selected target region to the DisplayThreePanels
		this.inpainting_panels.process.setText("Inpainting Process");
		this.inpainting_panels.spam.setDisplayImage(masked);
		this.inpainting_panels.clustering.setDisplayImage(masked);
		// kulang pa for region growing oops
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
		this.targetRegionPoly = new Polygon(x, y, x.length);
	}
	
	public static Entry getInstance() {
		if(instance == null)
			instance = new Entry();
		return instance;
	}
	
}
