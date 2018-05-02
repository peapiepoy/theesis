package view;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;


import javax.swing.JButton;

public class FirstPagePanel extends JPanel{
	public JButton uploadB;
	
	private static FirstPagePanel instance;
	
	public FirstPagePanel() {
		setLayout(null);
		setButtons();
		
	}
	
	private void setButtons() {
		uploadB = new JButton("upload photo");
		uploadB.setBounds(400, 330, 200, 20);
		add(uploadB);
	}

	public static FirstPagePanel getInstance() {
		if(instance == null) {
			return instance = new FirstPagePanel();
		}
		return instance;
	}
}
