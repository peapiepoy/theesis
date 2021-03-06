package inpainting_utils;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.Vector;

import javax.swing.JPanel;

import view.TargetSelection;

public class TargetAreaSelection extends JPanel {
	private BufferedImage img;
	public int xSize, ySize;
//	public int x, y;
	public boolean enabled, createPoly;
	public Vector PolygonX;
	public Vector PolygonY;
	
	
	public TargetAreaSelection(BufferedImage img){
		setLayout(null);
		this.img= img;
		this.enabled = false;
		this.createPoly = false;
		this.xSize = img.getWidth(this);
		this.ySize = img.getHeight(this);
		this.PolygonX = new Vector<>();
		this.PolygonY = new Vector<>();
		init();
	}
	public void reset() {
		this.enabled = false;
		this.enabled = false;
		this.createPoly = false;
		this.xSize = img.getWidth(this);
		this.ySize = img.getHeight(this);
		this.PolygonX.clear();
		this.PolygonY.clear();
		this.PolygonX = new Vector<>();
		this.PolygonY = new Vector<>();
	}
	public void init() {
		this.setPreferredSize(new Dimension(xSize, ySize));
		repaint();
	}
	
	public void enableSelection(boolean x) {
		this.enabled = x;
	}
	public void showImage() {
		repaint();
	}
	public void paint(Graphics g) {
		g.drawImage(img, 0, 0, this);
		
		for(int p = 0; p < PolygonX.size(); p++) {
			if(p==0) {
				g.setColor(Color.red);
				g.drawRect((Integer)PolygonX.get(0) - 5, (Integer)PolygonY.get(0) - 5, 10, 10);
				g.drawRect((Integer)PolygonX.get(0) - 4, (Integer)PolygonY.get(0) - 4, 8, 8);
			}
			else {
				if(!createPoly)
					g.setColor(Color.green);
				g.drawRect((Integer)PolygonX.get(p) - 5, (Integer)PolygonY.get(p) - 5, 10, 10);
				g.drawRect((Integer)PolygonX.get(p) - 4, (Integer)PolygonY.get(p) - 4, 8, 8);
				// draw lines as the SIDES of the polygon
					g.setColor(Color.blue);
					g.drawLine((Integer)PolygonX.get(p-1), (Integer)PolygonY.get(p-1),
									(Integer)PolygonX.get(p), (Integer) PolygonY.get(p));
				
			}
		}
		
		if(createPoly) {
			int[] polyX = new int[PolygonX.size()];
			int[] polyY = new int[PolygonY.size()];
			for(int u=0; u<PolygonX.size(); u++) {
				polyX[u] = (Integer) PolygonX.get(u);
				polyY[u] = (Integer) PolygonY.get(u);
			}
			g.setColor(new Color(255,255,255,80));
			g.fillPolygon(polyX, polyY, PolygonX.size());
			
			
		}
		
		
	}
	public Vector getPointsX() {
		return PolygonX;
	}
	public Vector getPointsY() {
		return PolygonY;
	}
	public void addToPolyCoords(int x, int y) {
		if(PolygonX.size()>2 && ( (x >= (Integer)PolygonX.get(0)-5) && (x <= (Integer)PolygonX.get(0)+5) ) &&
				( (y >= (Integer)PolygonY.get(0)-5) && (y <= (Integer)PolygonY.get(0)+5) )	){
					createPoly = true;
					TargetSelection.getInstance().submit.setVisible(true);
					TargetSelection.getInstance().submit.setEnabled(true);
					TargetSelection.getInstance().reset.setVisible(true);
					TargetSelection.getInstance().reset.setEnabled(true);
					this.enabled = false;
				}
		
		PolygonX.add(x);
		PolygonY.add(y);
	}
	
	
}
