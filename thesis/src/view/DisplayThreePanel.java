package view;

import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;
import main.Entry;
import inpainting_utils.ScaledImagePanel;

public class DisplayThreePanel extends JPanel{
	public static DisplayThreePanel instance;
	public JButton process, next;
	private BufferedImage img;
	public ScaledImagePanel clustering, regionGrowing, spam; 
	public boolean segmenting;
	private final String one = "SPAM", two = "KMeans", three = "Region Growing";
	
	public DisplayThreePanel(boolean segmenting) {
		setLayout(null);
		this.segmenting = segmenting;
		next = new JButton("Next");
		next.setBounds(400,650, 200,20);
		add(next);
		next.setEnabled(false);
		next.setVisible(false);
		
		process = new JButton("Segmentation Process");
		process.setBounds(400,650, 200,20);
		add(process);
		
		spam = new ScaledImagePanel(20, 20, "Split-and-Merge");	
		clustering = new ScaledImagePanel(350, 222, "KMeans");
		regionGrowing = new ScaledImagePanel(680, 424, "Region Growing");
		
		add(spam);
		add(clustering);
		add(regionGrowing);
	}
	
	public void flipButtons() {
		this.next.setEnabled(true);
		this.next.setVisible(true);
		
		this.process.setEnabled(false);
		this.process.setVisible(false);
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
//	public static DisplayThreePanel getInstance() {
//		if(instance==null)
//			instance = new DisplayThreePanel();
//		return instance;
//	}
}
