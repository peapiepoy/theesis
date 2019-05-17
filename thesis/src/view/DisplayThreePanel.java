package view;

import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

import main.Entry;
import inpainting_utils.ScaledImagePanel;

public class DisplayThreePanel extends JPanel{
	
	public static DisplayThreePanel instance;
	public JButton process, next;
	private JSpinner kspinner, ms, ssd, msd;
	private BufferedImage image;
	public ScaledImagePanel clustering, regionGrowing, spam; 
	public boolean segmenting;
	//private final String one = "SPAM", two = "KMeans", three = "Region Growing";
	
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
		
		SpinnerNumberModel snm = new SpinnerNumberModel(3, 1, 10, 1);
		kspinner = new JSpinner(snm);
		kspinner.setBounds(450, 450, 100, 30);
		add(kspinner);
		
		SpinnerNumberModel ssdx = new SpinnerNumberModel(5, 1, 10, 1);
		ssd = new JSpinner(ssdx);
		ssd.setBounds(70, 450, 100, 30);
		add(ssd);
		
		SpinnerNumberModel msdx = new SpinnerNumberModel(40, 10, 100, 5);
		msd = new JSpinner(msdx);
		msd.setBounds(70, 490, 100, 30);
		add(msd);
		
		SpinnerNumberModel msx = new SpinnerNumberModel(3, 1, 10, 1);
		ms = new JSpinner(msx);
		ms.setBounds(70, 530, 100, 30);
		add(ms);
		
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
	
	public void setImage(BufferedImage img) {
		this.image = img;
	}
	public BufferedImage getImage() {
		return this.image;
	}
	public void displaySegmentationBoxes() {
		spam.setDisplayImg(image);
		clustering.setDisplayImg(image);
		regionGrowing.setDisplayImg(image);
	}
	
	public int kSpinnerValue() {
		String ss =  kspinner.getValue().toString();
		return Integer.parseInt(ss);
	}
	
	public int msSpinnerValue() {
		return Integer.parseInt(ms.getValue().toString());
	}
	
	public int msdSpinnerValue() {
		return Integer.parseInt(msd.getValue().toString());
	}
	
	public int ssdSpinnerValue() {
		return Integer.parseInt(ssd.getValue().toString());
	}
	
}
