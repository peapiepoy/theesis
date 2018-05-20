package inpainting_utils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;


public class ScaledImagePanel extends JPanel{
	private BufferedImage displayImg = null;
	private int x, y;
	private String label;
	private final String one = "SPAM", two = "KMeans", three = "Region Growing";
	public boolean segmenting = false;
	
	public ScaledImagePanel(int x, int y, String label){
		this.x = x;
		this.y = y;
		this.label = label;
		init();
	}
	public void setDisplayImg(BufferedImage displayImg) {
		
		this.displayImg = displayImg;
		this.displayImg = scaleImage(displayImg, 300, 660);
		repaint();  
	}
	
	public BufferedImage scaleImage(BufferedImage image,
	        int newWidth, int newHeight) {
	        // Make sure the aspect ratio is maintained, so the image is not distorted
	        double thumbRatio = (double) newWidth / (double) newHeight;
	        int imageWidth = image.getWidth(null);
	        int imageHeight = image.getHeight(null);
	        double aspectRatio = (double) imageWidth / (double) imageHeight;

	        if (thumbRatio < aspectRatio) {
	            newHeight = (int) (newWidth / aspectRatio);
	        } else {
	            newWidth = (int) (newHeight * aspectRatio);
	        }

	        // Draw the scaled image
	        BufferedImage newImage = new BufferedImage(newWidth, newHeight,
	        								BufferedImage.TYPE_INT_RGB);
	        Graphics2D graphics2D = newImage.createGraphics();
	        graphics2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION,
	            RenderingHints.VALUE_INTERPOLATION_BILINEAR);
	        graphics2D.drawImage(image, 0, 0, newWidth, newHeight, null);
	        
//	        if(label.equals(two) && segmenting) { // if kmeans X SCA
//				System.out.println("\nyoooooooowssss");
//				int kCluster = Entry.getInstance().clustering.kCluster;
//				ArrayList<Point> centroids = Entry.getInstance().clustering.subtractiveClustering.clusterCenter;
//					for(int z = 0; z < kCluster; z++) {
//						System.out.println(centroids.get(z).getX()+","+centroids.get(z).getY());
//						graphics2D.setColor(Color.red);
//						graphics2D.drawRect((int)centroids.get(z).getX() - 5, (int)centroids.get(z).getY() - 5, 10, 10);
//						graphics2D.drawRect((int)centroids.get(z).getX() - 4, (int)centroids.get(z).getY() - 4, 8, 8);
//					}
//			}
	        
	        return newImage;
	    }
	
	public void init() {
		setBounds(x, 50, 300, 580);
	}
	public void paint(Graphics g) {
		
		
		

		g.drawImage(displayImg, 0, 0, this);
	}
}
