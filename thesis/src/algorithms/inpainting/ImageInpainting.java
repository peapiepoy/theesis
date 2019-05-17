package algorithms.inpainting;

import main.Entry;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Vector;

public class ImageInpainting {
	
	public int[][] pixelmap, maskedmap;
	private int[][] confidence, dataTerm, convolved;
	private int row, col;
	private int []maskColor;
	ArrayList <Point> boundaryPixels;
	
	double toFillRegion[][];
	double con[][] = new double[][] { { 1, 1, 1 }, { 1, -8, 1 }, { 1, 1, 1 } }; // Laplacian filter used in founding
																				// boundary of target region
	GradientCalculator gc;
	
	double[][] gradientX, gradientY, normalizedXY;
	
	
	
	public ImageInpainting(int [][]pixelmap, int[][] maskedmap) {
		this.row = pixelmap.length;
		this.col = pixelmap[0].length;
		this.pixelmap = pixelmap;
		this.maskedmap = maskedmap;
		this.confidence = new int[row][col];
		this.dataTerm = new int[row][col];
		this.convolved = new int[row][col];
		this.confidence = new int[row][col];
		this.maskColor = Entry.getInstance().maskColor;
		this.gc = new GradientCalculator();
	}
	
	public void inpaintingProcess() {
		initializeData();
		defineBoundaries();
		// calculate confidence terms
		// calculate data term
		// find patch toFill to be inpainted (max prio patch)
		// find best exemplar from source region (segmentation shits)
		// fill the toFill patch and fill it wityh the best exemplar
		// update confidence term
		// check if inpainting is done
		
		
	}
	/*
	 * initialize toFillRegion, dataTerm, confidenceTerm
	 * 				gradientX, gradientY, normalizedXY
	 */


	public void initializeData() {
		/*
		 * intialize GRADIENTS??
		 */
		gradientX = new double[row][col];
		gradientY = new double[row][col];
		normalizedXY = new double[row][col];
		this.gc.calculateGradient(pixelmap, col, row);
		
		
		for(int i=0; i < row; i++) {
			for(int j=0; j < col; j++) {
				
				this.toFillRegion[j][i] = 0;
				this.dataTerm[j][i] = -1;
				int rgb[] = rgbValues(pixelmap[j][i]);
				this.confidence[j][i] = 1;
				
				/* target region pixel */
				if(maskColor[0] == rgb[0] && maskColor[1] == rgb[1] && maskColor[2] == rgb[2]) { 		// this pixel is a hole
					this.confidence[j][i] = 0;
					this.toFillRegion[j][i] = 1;
				}
				
			}
		}
		
		
	}
	
	public void  defineBoundaries() {
		int conRow = toFillRegion.length + con.length - 1;
		int conCol = toFillRegion[0].length + con[0].length - 1; 
		
		double convolution[][] = new double[conRow][conCol];
		
		for(int i = 0; i < con.length; i++) {
			for(int j = 0; j < con[0].length; j++) {
				
				int endX = i + toFillRegion.length - 1;
				int endY = j + toFillRegion[0].length - 1;
				
				for(int conX = i; conX < endX; conX++) {
					for(int conY = j; conY < endY; conY++) {
						convolution[conX][conY] = convolution[conX][conY] + con[i][j] * toFillRegion[conX-i+1][conY-j+1]; 
					}
				}
				
			}
		}
		
		double boundsMatrix[][] = new double[toFillRegion.length][toFillRegion[0].length];
		int startX = con.length / 2;
		int endX = startX + toFillRegion.length - 1;
		int startY = con[0].length / 2;
		int endY = startY + toFillRegion[0].length - 1;
		
		for(int i = startX; i < endX; i++) {
			for(int j = startY; j < endY; j++) {
				int x = i-startX + 1;
				int y = j-startX + 1;
				boundsMatrix[x][y] = convolution[i][j];
			}
		}
		
		gc.calculateGradientFromImage(pixelmap, row, col);
		
		
		// add the boundary pixels to dR and their gradients (not so sure why include the gradients)
		
		for(int i=0; i < boundsMatrix[0].length; i++) {
			for(int j = 0; j<boundsMatrix.length; j++) {
				if(boundsMatrix[j][i] > 0) {
					boundaryPixels.add(new Point(i, j));
					gradientX[j][i] = gc.gradientX[j][i];
					gradientY[j][i] = gc.gradientY[j][i];
				}
			}
		}
		
	}
	
	public double[][] normalize(double[][] x, double [][]y){
		int xSize = x.length * x[0].length;
		double[][] norm = new double[xSize][2];
		int ndx = 0;
		double t1, t2, t3;
		for(int yy=0; yy < x.length; yy++) {
			for(int xx=0; xx < x[0].length; xx++) {
				t1 = gradientX[xx][yy];
				t2 = gradientY[xx][yy];
				
				if(t1+t2 == 0)
					t3=0;
				else
					t3 = Math.sqrt(1/(t1*t1 + t2*t2));
				norm[ndx][0] = t3 * t1;
				norm[ndx][1] = t3 * t2;
				
				ndx++;
			}  
		}
		
		
		return norm;
	}
	
	
	public int[] rgbValues(int pixel) {
		int []rgb = new int[3]; 
		rgb[0] = 0xff & pixel >> 16; // Obtain the red Component
		rgb[1] = 0xff & pixel >> 8; // Obtain the green Component
		rgb[2] = 0xff & pixel; // Obtain the blue Component
		
		return rgb;
	}
}
