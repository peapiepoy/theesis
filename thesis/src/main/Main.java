package main;

import java.awt.CardLayout;
import java.awt.Color;
import javax.swing.JFrame;
import javax.swing.JPanel;

import controller.ButtonHandler;
import view.FirstPagePanel;
import view.TargetSelection;

public class Main extends JFrame {
	private static Main instance;
	private JPanel cards;
	private JPanel fpp;
	private JPanel ts;
	private JPanel sp, ip;
	private Entry entry;
	private CardLayout cardLayout;
	public int sizeX, sizeY;
	ButtonHandler bh;
	
	public Main() {
		setBackground(Color.cyan);
		this.sizeX = 1000;
		this.sizeY = 700;
		
		this.entry = Entry.getInstance();
		
		this.cardLayout = new CardLayout();
		cards = new JPanel(cardLayout);
		
		fpp = FirstPagePanel.getInstance();
		ts = TargetSelection.getInstance();
		sp = this.entry.segmenting;
		ip = this.entry.inpainting;
		
		bh = ButtonHandler.getInstance();
		
		frameSetup();
		
		cards.add(fpp, "first");
		cards.add(sp, "second");
		cards.add(ts, "third");
		cards.add(ip, "fourth");
		
		add(cards);
		setResizable(false);
		setVisible(true);
	}
	
		public static void main(String args[]) {
			getInstance();
		}
		
		private void frameSetup() {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("Segmentation X Inpainting");
			setSize(sizeX, sizeY);
			setLocationRelativeTo(null);
			setUndecorated(true);
		}
		
		public void nextCard() {
			cardLayout.next(cards);
		}
		
		public static Main getInstance() {
			if(instance == null)
				instance = new Main();
			return instance;
		}
}
