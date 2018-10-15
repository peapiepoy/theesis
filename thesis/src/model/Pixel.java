package model;

import java.awt.Color;

public class Pixel {
	private Color color;
	private int xPos, yPos;
	
	public Pixel (int xPos, int yPos, Color rgb) {
		this.setColor(rgb);
		this.xPos = xPos;
		this.yPos = yPos;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}
	
	
}
