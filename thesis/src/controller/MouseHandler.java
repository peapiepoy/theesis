package controller;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import inpainting_utils.TargetAreaSelection;
import main.Entry;
/*
 * mouse matters for target selection
 */


public class MouseHandler implements MouseListener{
	private static MouseHandler instance;
	private TargetAreaSelection tas;
	
	public MouseHandler() {
		Entry.getInstance().getTargetAreaSelection().addMouseListener(this);
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		
	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		tas = Entry.getInstance().getTargetAreaSelection();
		if(tas.enabled) {
				tas.addToPolyCoords(e.getX(), e.getY());
				tas.showImage();
		}
		
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
	public static MouseHandler getInstance() {
		if(instance == null)
			instance = new MouseHandler();
		return instance;
	}

}
