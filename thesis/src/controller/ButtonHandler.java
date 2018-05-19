package controller;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import view.FirstPagePanel;
import view.SegmentationPanel;
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
		SegmentationPanel.getInstance().segment.addActionListener(this);
		SegmentationPanel.getInstance().next.addActionListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent e) {
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
			
			SegmentationPanel.getInstance().setImage();
			SegmentationPanel.getInstance().displaySegmentationBoxes(); // displays image for segmentation
		}
		
		/*
		 * enable defining the target region for inpainting
		 */
		else if(e.getSource() == TargetSelection.getInstance().setTAB) {
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
			Entry.getInstance().setPolygon(); //saves polygon in the Entry class
//			SegmentationPanel.getInstance().setImage();
//			SegmentationPanel.getInstance().displaySegmentationBoxes(); // displays image for segmentation
			Main.getInstance().nextCard(); // next page
		}
		else if(e.getSource() == SegmentationPanel.getInstance().segment) {
			Entry.getInstance().segmentationProcess();
			SegmentationPanel.getInstance().flipButtons();
			
		}
		else if(e.getSource() == SegmentationPanel.getInstance().next) {
			Main.getInstance().nextCard();
		}
	}
	
	public static ButtonHandler getInstance() {
		if(instance == null) 
			instance = new ButtonHandler();
		return instance;
	}
}
