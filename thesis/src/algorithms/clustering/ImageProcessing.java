package algorithms.clustering;

import model.clustering.*;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageProcessing {
	private BufferedImage image;
	int height;
	int width;
	Color imageMatrix1[][];
	
	/**
	 * Loads image from file path and initialize height and width of imageMatrix and the matrix 
	 * @param filePath
	 */
	public ImageProcessing(BufferedImage image, int k){
		this.image = image;
		this.height = this.image.getHeight();
		this.width = this.image.getWidth();
		this.imageMatrix1 = new Color[width][height];
		convertIntoArray();
		doKmeans(k);
		System.out.println("--------end of kmeans; k: "+k);
	}
	/**
	 * Converts image into an 2d array of pixels to work with
	 */
	public void convertIntoArray(){
		int x,y, red, green, blue;
		Color c, c1;
		for(y=0;y<height;y++){
			for(x=0;x<width;x++){
				c = new Color(this.image.getRGB(x, y));
				red = c.getRed();
				green = c.getGreen();
				blue = c.getBlue();
				imageMatrix1[x][y] = new Color(red,green,blue);
				c1 = new Color(0,0,0); //na pocz�tku obraz jest wype�niany na czarno in the beginning, the image is filled in black
				this.image.setRGB(x, y, c1.getRGB());
			}
		}
	}
	
	public BufferedImage getOutput() {
		return this.image;
	}
	/**
	 * Converts color to gray level 
	 * @param color
	 * @return the integer representation of grey level
	 */
	public int colorToGray(Color color){
		return (color.getBlue()+color.getGreen()+color.getRed())/3;
	}
	/**
	 * Does Kmeans algorithm on given image
	 * @param clusterCount - number of clusters to recognize
	 */
	public void doKmeans(int clusterCount){
		Cluster center[] = new Cluster[clusterCount];
		boolean globalChange = true;
		boolean centerChange[] = new boolean[clusterCount];
		Random random = new Random();
		//for(int i=1;i<=clusterCount;i++) center[i-1] = new Cluster(new Pixel(255/clusterCount*(i)));
		for(int i=0;i<clusterCount;i++) 
			center[i] = new Cluster(new Pixel(random.nextInt(256),random.nextInt(256),random.nextInt(256))); //losuj� warto�ci centroid�w w skali szaro�ci

		while(globalChange){
			for(int i=0;i<clusterCount;i++){ //resetuj� tablice pikseli przynale��cych do centroid�w --reset pixel tables belonging to centroids
				System.out.println(center[i].getCentroidColor()+" getCentroidColor()");
				center[i].clusterArray = new ArrayList<Pixel>();
			}
			System.out.println("");
			for(int y=0;y<height;y++){
				for(int x=0;x<width;x++){
					int minDist =255; //minimalny dystans piksela do najbli�szego centroidu --the minimum distance of the pixel to the nearest centroid
					int nearestCenter=0; //a number representing the nearest centroid
					for(int i=0;i<clusterCount;i++){
						int dist = Math.abs(colorToGray(imageMatrix1[x][y]) - center[i].getCentroidGray()); //obliczam dystans od itego centroidu
						if(minDist>dist){ //Compare the calculated distance with the minimum hitherto
							minDist = dist;
							nearestCenter = i;
						}
					}
					center[nearestCenter].addPixel(x,y,imageMatrix1[x][y]);
				}
			}
			for(int i=0;i<clusterCount;i++){
				int cent = center[i].newCentroid();
				if(cent == 1) centerChange[i] = true;
				else centerChange[i] = false;
			}
			for(int i=0;i<clusterCount;i++){
				if(centerChange[i] == true) break;
				globalChange = false;
			}
			
		}
		for(int i=0;i<clusterCount;i++){
			center[i].setClusterColor();
			System.out.println("clustercolor done");
			for(int j=0;j<center[i].clusterArray.size();j++){
				this.image.setRGB(center[i].clusterArray.get(j).xPos, center[i].clusterArray.get(j).yPos, center[i].clusterColor.getRGB());
			}
		}
	}
	/**
	 * Main method to test out the algorithm
	 * @param args
	 * @throws IOException
	 */
//	public static void main(String []args) throws IOException{
//		File output = new File("outputImage9"
//				+ ".png");
//		ImageProcessing ip = new ImageProcessing("colorful-play-balls.jpg");
//		ip.convertIntoArray();
//		ip.doKmeans(9);
//		ImageIO.write(ip.image, "PNG", output);
//	}
	
}
