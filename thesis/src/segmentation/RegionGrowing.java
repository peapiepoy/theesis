package segmentation;

import java.awt.image.BufferedImage;

import main.Entry;

public class RegionGrowing {
	public BufferedImage img, grayscaled, segmentedImage;
	private int width, height;
	
	public RegionGrowing(BufferedImage img) {
		this.img = img;
		this.width = img.getWidth();
		this.height = img.getHeight();
		grayscaled = new BufferedImage(width, height, 
			      BufferedImage.TYPE_BYTE_GRAY);
		
		grayscaled = Entry.getInstance().grayscaling(Entry.getInstance().original_pixel);
	}
	
	public void setImage(BufferedImage displayImage) {
		this.segmentedImage = displayImage;
	}
	public BufferedImage getSegmentedImage() {
		return grayscaled;
	}
}
