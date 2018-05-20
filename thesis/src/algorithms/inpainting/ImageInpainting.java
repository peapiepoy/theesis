package algorithms.inpainting;

public class ImageInpainting {
	public int[][] pixelmap, maskedmap;
	
	public ImageInpainting(int [][]pixelmap, int[][] maskedmap) {
		this.pixelmap = pixelmap;
		this.maskedmap = maskedmap;
	}
}
