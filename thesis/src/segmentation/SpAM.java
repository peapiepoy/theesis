package segmentation;

import java.awt.image.BufferedImage;

import main.Entry;
import algorithms.spam.SplitAndMerge;


public class SpAM {
	public BufferedImage img, grayscaled, segmentedImage;
	
	public SpAM(BufferedImage img) {
		this.img = img;
		SplitAndMerge spam = new SplitAndMerge(this.img);
		
		setImage(spam.getOutput());
		
//		grayscaled = new BufferedImage(width, height, 
//			      BufferedImage.TYPE_BYTE_GRAY);
//		
//		grayscaled = Entry.getInstance().grayscaling();
	}
	
	public void setImage(BufferedImage displayImage) {
		this.segmentedImage = displayImage;
	}
	
	
	public BufferedImage getSegmentedImage() {
		return segmentedImage;
	}
}
