package algorithms.inpainting;

import main.Entry;
import java.awt.Point;
import java.util.ArrayList;

public class ImageInpainting {
	public int[][] pixelmap, maskedmap;
	private int[][] confidence, dataTerm, convolved;
	private int row, col;
	private int []maskColor;
	ArrayList <Point> dTR;
	
	public ImageInpainting(int [][]pixelmap, int[][] maskedmap) {
		this.row = pixelmap.length;
		this.col = pixelmap[0].length;
		this.pixelmap = pixelmap;
		this.maskedmap = maskedmap;
		this.confidence = new int[row][col];
		this.dataTerm = new int[row][col];
		this.convolved = new int[row][col];
		this.maskColor = Entry.getInstance().maskColor;
	}
	
	public void inpaintingProcess() {
		//defineBoundaries();
		// calculate confidence terms
		// calculate data term
		// find patch toFill to be inpainted (max prio patch)
		// find best exemplar from source region (segmentation shits)
		// fill the toFill patch and fill it wityh the best exemplar
		// update confidence term
		// check if inpainting is done
		
		
	}
	
	public void  defineBoundaries() {
		for(int i = 0; i < )
	}
	
	public void initialize() {
		for(int i=0; i < row; i++) {
			for(int j=0; j < col; j++) {
				this.dataTerm[j][i] = -1;
				int rgb[] = rgbValues(pixelmap[j][i]);
				confidence[j][i] = 1;
				if(maskColor[0] == rgb[0] && maskColor[1] == rgb[1] && maskColor[2] == rgb[2]) { 		// this pixel is a hole
					confidence[j][i] = 0;
				}
				
			}
		}
	}
	
	public int[] rgbValues(int pixel) {
		int []rgb = new int[3]; 
		rgb[0] = 0xff & pixel >> 16; // Obtain the red Component
		rgb[1] = 0xff & pixel >> 8; // Obtain the green Component
		rgb[2] = 0xff & pixel; // Obtain the blue Component
		
		return rgb;
	}
}
