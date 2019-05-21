package model.clustering;
import java.awt.Color;
import java.util.ArrayList;
/**
 * Representation of cluster with all necessary and unnecessary constructors, getters, setters. It aslo contains methods to compute new Centroids and new clusterColor
 */
public class Cluster {
	public Pixel centroid;							// CHANGED from protected-> public
	public ArrayList<Pixel> clusterArray;
	public Color clusterColor;
	
	public Cluster(Pixel centroid){
		this.centroid = centroid;
		clusterArray = new ArrayList<Pixel>();
	}
	
	public Cluster(int centroid){
		this.centroid.color = new Color(centroid, centroid,centroid);
		clusterArray = new ArrayList<Pixel>();
	}
	
	public Cluster(Color centroid){
		this.centroid.color = centroid;
		clusterArray = new ArrayList<Pixel>();
	}
	
	public void addPixel(Pixel pixel){
		clusterArray.add(pixel);
	}
	
	public void addPixel(int xPos, int yPos, int R, int G, int B){
		clusterArray.add(new Pixel(xPos,yPos,new Color(R,G,B)));
	}
	
	public void addPixel(int xPos, int yPos,int grey){
		clusterArray.add(new Pixel(xPos,yPos,new Color(grey,grey,grey)));
	}
	
	public void addPixel(int xPos, int yPos,Color color){
		clusterArray.add(new Pixel(xPos,yPos,color));
	}
	
	public int getCentroidComponent(String component){
		switch(component){
		case"R":
			return this.centroid.getPixelR();
		case"G":
			return this.centroid.getPixelG();
		case"B":
			return this.centroid.getPixelB();
		}
		return 0;
	}
	
	public int getCentroidGray(){
		return ((this.centroid.getPixelR()+this.centroid.getPixelG()+this.centroid.getPixelB())/3);
	}
	
	public Color getCentroidGrayColor(){
		int col = (this.centroid.getPixelR()+this.centroid.getPixelG()+this.centroid.getPixelB())/3;
		return new Color(col,col,col);
	}
	
	public Color getCentroidColor(){
		return centroid.color;
	}
	/**
	 * Computes new centroid grayscale value 
	 * @return if centroid grayscale value change return 1 if not returns 0
	 */
	public int newCentroid(){
		int newR = 0,newG = 0, newB = 0;
		for(int i=0;i<this.clusterArray.size();i++){
			//newCentroid = newCentroid + clusterArray.get(i).getPixelGrey();
			newR = newR + clusterArray.get(i).getPixelR();
			newG = newG + clusterArray.get(i).getPixelG();
			newB = newB + clusterArray.get(i).getPixelB();
		}
//		newCentroid = newCentroid/this.clusterArray.size();
		try{
			newR = newR/this.clusterArray.size();
		}catch(ArithmeticException e){
			newR = 0;
		}
		try{
			newG = newG/this.clusterArray.size();
		}catch(ArithmeticException e){
			newG = 0;
		}	
		try{
			newB = newB/this.clusterArray.size();
		}catch(ArithmeticException e){
			newB = 0;
		}
		
		if(newR != centroid.color.getRed() && newG != centroid.color.getGreen() && newB != centroid.color.getBlue()){
			centroid.color = new Color(newR,newG,newB);
			return 1;
		}else
			return 0;
		
//		if(newCentroid != centroid.color.getRed()){
//			centroid.color = new Color(newCentroid,newCentroid,newCentroid);
//			return 1;
//		}else
//			return 0;
		
	}
	

	/**
	 * Sets the clusterColor. That value should be an output representaton of cluster color on output image
	 * this is of no use anymore. no random colors anymore. this frequently gives black bc of the catch method 52119
	 */
	public void setsClusterColor(){
		Color newCol;
		int R=0,G=0,B=0, r,g,b;
		for(int i=0;i<this.clusterArray.size();i++){
			R = R+this.clusterArray.get(i).getPixelR();
			G = G+this.clusterArray.get(i).getPixelG();
			B = B+this.clusterArray.get(i).getPixelB();
			//System.out.println(clusterArray.get(i).xPos+"  "+clusterArray.get(i).yPos+"  "+clusterArray.get(i).getPixelR()+"  "+clusterArray.get(i).getPixelG()+"  "+clusterArray.get(i).getPixelB());
		}
		try{
			newCol = new Color(R/this.clusterArray.size(),G/this.clusterArray.size(),B/this.clusterArray.size());
		}catch(ArithmeticException e){

			r = (int) (Math.random() * ((200) + 1)) + 5;
			g = (int) (Math.random() * ((155) + 50)) + 12;
			b = (int) (Math.random() * ((125) + 25)) + 15;
			newCol = new Color(r,g,b);

//			r = (int) Math.random()*255 + 125;
//			g = (int) Math.random()*130;
//			b = (int) Math.random()*255;
//			newCol = new Color(r, g, b);

		}
		
		System.out.println(newCol.getRed()+"   "+newCol.getGreen()+"   "+newCol.getBlue()+"   ");
		this.clusterColor = newCol;
		
	}
	
	public void setClusterColor(int i) {
		Color newCol = new Color(0,0,0);
		int r=0,g=0,b=0;
		if(i == 0) {
			r=255;g=0;b=0;
		}else if ( i == 1) {
			r=0;g=255;b=0;
		}else if( i==2) {
			r=0;g=0;b=255;
		}else if(i==3) {
			r=255;g=255;b=0;
		}else if(i==4) {
			r=0;g=255;b=255;
		}else if(i==5) {
			r=255;g=0;b=255;
		}
		newCol = new Color(r,g,b);

		System.out.println(newCol.getRed()+"   "+newCol.getGreen()+"   "+newCol.getBlue()+"   ");
		this.clusterColor = newCol;	
			
	}
}
