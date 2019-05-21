package algorithms.clustering;

import model.clustering.*;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;

public class ImageProcessing {
	private BufferedImage image, output;
	int height;
	int width;
	Color imageMatrix1[][];
	WritableRaster raster;
	
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
		return this.output;
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
		output = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		raster = this.output.getRaster();
		
		
		//for(int i=1;i<=clusterCount;i++) center[i-1] = new Cluster(new Pixel(255/clusterCount*(i)));
		for(int i=0;i<clusterCount;i++) // draw centroid values ​​on a gray scale
			center[i] = new Cluster(new Pixel(random.nextInt(this.image.getWidth()),random.nextInt(this.image.getHeight()),random.nextInt(256))); //losuj� warto�ci centroid�w w skali szaro�ci

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
			System.out.println("global change cycle "+globalChange);
		}
		for(int i=0;i<clusterCount;i++){
			center[i].setClusterColor(i);
			System.out.println("clustercolor done: "+center[i].clusterArray.size());
			int clusterSize = center[i].clusterArray.size();
			for(int j=0;j<clusterSize;j++){
				//this.image.setRGB(center[i].clusterArray.get(j).xPos, center[i].clusterArray.get(j).yPos, center[i].clusterColor.getRGB());
//				int[] color = new int[4];
//				color[0] = 0xff & 255 >> 24;
//				color[1] = 0xff & center[i].clusterColor.getRed() >> 16;
//				color[2] = 0xff & center[i].clusterColor.getGreen() >> 8;
//				color[3] = 0xff & center[i].clusterColor.getBlue();
//				raster.setPixel(center[i].clusterArray.get(j).xPos, center[i].clusterArray.get(j).yPos, color);
				
				int x = center[i].clusterArray.get(j).xPos;
				int y = center[i].clusterArray.get(j).yPos;
//				if(j < 300)
//					System.out.println(x+","+y);
				
		         int r = center[i].clusterColor.getRed(); //red
		         int g = center[i].clusterColor.getGreen(); //green
		         int b = center[i].clusterColor.getBlue(); //blue
		 
		         int p = (255<<24) | (r<<16) | (g<<8) | b; //pixel
		 
		         output.setRGB(x, y, p);
		         
		         Graphics2D graphics2D = output.createGraphics();
		         graphics2D.drawImage(output, 0, 0, image.getWidth(), image.getHeight(), null);
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
