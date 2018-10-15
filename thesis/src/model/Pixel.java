package model;

import java.awt.Color;
/**
 * A class representation of single Pixel. Every pixel has its own position X and Y and Color
 */
public class Pixel {
	protected Color color;
	protected int xPos;
	protected int yPos;
	
	public Pixel(int xPos, int yPos, Color rgb){
		this.color = rgb;
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public Pixel(Color rgb){
		this.color = rgb;
		this.xPos = 0;
		this.yPos = 0;
	}
	
	public Pixel(int xPos, int yPos, int gray){
		this.color = new Color(gray,gray,gray);
		this.xPos = xPos;
		this.yPos = yPos;
	}
	
	public Pixel(int gray){
		this.color = new Color(gray,gray,gray);
		this.xPos = 0;
		this.yPos = 0;
	}
	
	public int getPixelR(){
		return this.color.getRed();       
	}
	
	public int getPixelG(){
		return this.color.getGreen();
	}
	
	public int getPixelB(){
		return this.color.getBlue();
	}
	
	public int getPixelGrey(){
		return (this.color.getBlue()+this.color.getGreen()+this.color.getRed())/3;
	}
	
	public int getPixelColor(){
		return this.color.getRGB();
	}
}
