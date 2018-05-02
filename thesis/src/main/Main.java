package main;
import java.awt.CardLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JPanel;
import controller.ButtonHandler;
import view.FirstPagePanel;
import view.TargetSelection;
import view.SegmentationPanel;


public class Main extends JFrame {
	private JPanel cards;
	private JPanel fpp;
	private JPanel ts;
	private JPanel sp;
	private static Main instance;
	private Entry entry;
	private CardLayout cardLayout;
	public final int sizeX=1000, sizeY=700;
	ButtonHandler bh;
	
	public Main() {
		setBackground(Color.cyan);
		this.cardLayout = new CardLayout();
		cards = new JPanel(cardLayout);
		
		fpp = FirstPagePanel.getInstance();
		ts = TargetSelection.getInstance();
		sp = SegmentationPanel.getInstance();
		
		entry = new Entry();
		bh = ButtonHandler.getInstance();
		
		frameSetup();
		
		cards.add(fpp, "first");
		cards.add(ts, "second");
		cards.add(sp, "third");
		
		add(cards);
		setResizable(false);
		setVisible(true);
	}
	
		public static void main(String args[]) {
			getInstance();
		}
		
		private void frameSetup() {
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setTitle("Thesis Attempt (:");
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
