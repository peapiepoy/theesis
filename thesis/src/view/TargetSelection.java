package view;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import main.Entry;
import main.Main;

/*
 * the class for the selection of target for inpainting
 */
public class TargetSelection extends JPanel {
	private static TargetSelection instance;
	private static BufferedImage image;
	public JButton submit, setTAB, reset;
	public JScrollPane scrollPane;
	public boolean enabled;
	
	
	TargetSelection(){
		setLayout(null);
		
		scrollPane = new JScrollPane();
		scrollPane.setBackground(Color.BLUE);
		scrollPane.setAlignmentY(CENTER_ALIGNMENT);
		scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		scrollPane.setBounds(20,20, 500,600);
		
		submit = new JButton("Submit");
		submit.setBounds(620, 200, 100, 20);
		setTAB = new JButton("Set Target Area");
		setTAB.setBounds(620, 100, 150, 20);
		reset = new JButton("Reset");
		reset.setBounds(620, 100, 100, 20);
		submit.setEnabled(false);
		
		add(scrollPane);
		add(submit);
		add(setTAB);
		add(reset);
		
		reset.setEnabled(false);
		reset.setVisible(false);
		submit.setEnabled(false);
		submit.setVisible(false);
		this.enabled = false;
	}	
	
	public void addImageToScrollpane() {
		scrollPane.setViewportView(Entry.getInstance().getTargetAreaSelection());
	}
	
	public static TargetSelection getInstance() {
		if(instance == null)
			instance = new TargetSelection();
		return instance;
	}
}
