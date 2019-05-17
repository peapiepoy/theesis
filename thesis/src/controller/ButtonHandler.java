package controller;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import view.FirstPagePanel;
import view.DisplayThreePanel;
import view.TargetSelection;
import main.Entry;
import main.Main;

public class ButtonHandler implements ActionListener{
	private static ButtonHandler instance;
	protected Image entryImage;
	
	public ButtonHandler() {
		FirstPagePanel.getInstance().uploadB.addActionListener(this);
		
		TargetSelection.getInstance().setTAB.addActionListener(this);
		TargetSelection.getInstance().reset.addActionListener(this);
		TargetSelection.getInstance().submit.addActionListener(this);
		
		Entry.getInstance().segmenting.process.addActionListener(this);
		Entry.getInstance().segmenting.next.addActionListener(this);
		Entry.getInstance().inpainting_panels.process.addActionListener(this);
		Entry.getInstance().inpainting_panels.next.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

/*
 * 		FirstPagePanel buttons		
 */
		if(e.getSource() == FirstPagePanel.getInstance().uploadB) {
			JFileChooser jfc = new JFileChooser();
			jfc.setCurrentDirectory(new File(System.getProperty("user.home")+"/git/theesis/thesis/images"));
			FileNameExtensionFilter filter = new FileNameExtensionFilter("*.Images", "jpg","png");
			jfc.addChoosableFileFilter(filter);
			jfc. setAcceptAllFileFilterUsed(false);
			int result = jfc.showOpenDialog(FirstPagePanel.getInstance());
			File selectedFile;
			
			if (result == JFileChooser.APPROVE_OPTION) {
				
			    selectedFile = jfc.getSelectedFile();
			    
			    Entry.getInstance().setImage(selectedFile.getAbsolutePath()+"");
			    System.out.println("Selected file: " + selectedFile.getAbsolutePath());
			}
			Main.getInstance().nextCard();
			
			Entry.getInstance().segmenting.setImage(Entry.getInstance().getImage());
			Entry.getInstance().segmenting.displaySegmentationBoxes(); // displays image for segmentation
		}
		
/*
 * 			E N T R Y class buttons		
 */
		else if(e.getSource() == Entry.getInstance().segmenting.process) {
			Entry.getInstance().segmentationProcess();
			Entry.getInstance().segmenting.flipButtons();			// making "process" button to "next" button
		}
		
		else if(e.getSource() == Entry.getInstance().segmenting.next) {
			Main.getInstance().nextCard();
		}
		
/*
 * 			TargetSelection class buttons
 */
		else if(e.getSource() == TargetSelection.getInstance().setTAB) { //TAB = target area button
			Entry.getInstance().getTargetAreaSelection().enableSelection(true);
			TargetSelection.getInstance().setTAB.setEnabled(false);
			TargetSelection.getInstance().setTAB.setVisible(false);
		}
		else if(e.getSource() == TargetSelection.getInstance().reset) {
			Entry.getInstance().getTargetAreaSelection().reset();
			Entry.getInstance().getTargetAreaSelection().showImage();
			Entry.getInstance().getTargetAreaSelection().enableSelection(true);
		}
		else if(e.getSource() == TargetSelection.getInstance().submit) {
			Entry.getInstance().submitTargetRegion();
			Main.getInstance().nextCard(); // next page
			
		}
/*
 * entry na pud		
 */
		else if(e.getSource() == Entry.getInstance().inpainting_panels.process) {
			Entry.getInstance().inpainting_panels.flipButtons();
			Entry.getInstance().inpaintingProcess();
			
		}
		else if(e.getSource() == Entry.getInstance().inpainting_panels.next) {
			System.out.println("dead end yet");
		}
		


	}
	
	public static ButtonHandler getInstance() {
		if(instance == null) 
			instance = new ButtonHandler();
		return instance;
	}
}
