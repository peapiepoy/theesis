package process;

import java.awt.image.BufferedImage;

import main.Entry;

public class SplitAndMerge {
	public BufferedImage img, grayscaled, segmentedImage;
	private int width, height;
	
	public SplitAndMerge(BufferedImage img) {
		this.img = img;
		this.width = img.getWidth();
		this.height = img.getHeight();
		grayscaled = new BufferedImage(width, height, 
			      BufferedImage.TYPE_BYTE_GRAY);
		
		grayscaled = Entry.getInstance().grayscaling();
	}
	
	public void setImage(BufferedImage displayImage) {
		this.segmentedImage = displayImage;
	}
	public BufferedImage getSegmentedImage() {
		return grayscaled;
	}
}
