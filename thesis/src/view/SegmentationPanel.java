package view;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;
import main.Entry;
import inpainting_utils.SegmentationDisplay;

public class SegmentationPanel extends JPanel{
	public static SegmentationPanel instance;
	public JButton segment;
	private BufferedImage img;
	public SegmentationDisplay clustering, regionGrowing, spam; 
	
	public SegmentationPanel() {
		setLayout(null);
		
		segment = new JButton("Segmentation Process");
		segment.setBounds(400,650, 200,20);
		add(segment);
		
		spam = new SegmentationDisplay(20, 20, "SPA,");	// 100 tanan
		clustering = new SegmentationDisplay(350, 222, "KMeans");
		regionGrowing = new SegmentationDisplay(680, 424, "Region Growing");
		add(spam);
		add(clustering);
		add(regionGrowing);
	}
	
	public void setImage() {
		this.img = Entry.getInstance().getImage();
	}
	public BufferedImage getImage() {
		return this.img;
	}
	public void displaySegmentationBoxes() {
		spam.setDisplayImg(img);
		clustering.setDisplayImg(img);
		regionGrowing.setDisplayImg(img);
	}
	public static SegmentationPanel getInstance() {
		if(instance==null)
			instance = new SegmentationPanel();
		return instance;
	}
}
