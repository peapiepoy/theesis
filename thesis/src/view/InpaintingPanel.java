package view;

import javax.swing.JPanel;
import javax.swing.JButton;

public class InpaintingPanel extends JPanel{
	public static InpaintingPanel instance;
	public JButton inpaint;
	
	
	public InpaintingPanel() {
		setLayout(null);
		
		inpaint = new JButton("Inpainting Process");
		inpaint.setBounds(400, 650, 200, 20);
		
		
	}

}
