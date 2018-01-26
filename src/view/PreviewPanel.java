package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import model.Pixel;

public class PreviewPanel extends JPanel {
	
	private JLabel imageLabel;
	
	private SpringLayout layout;
	
	private Pixel[][] pixels;
	
	public PreviewPanel(Pixel[][] pixels) {
		super();
		
		this.pixels = pixels;
		this.imageLabel = new JLabel(new ImageIcon(pixelsToBufferedImage(pixels)));
		
		layout = new SpringLayout();
		this.setLayout(layout);
		
		setImage(pixels);
		
		this.add(imageLabel);
	}
	
	
	public void setImage(Pixel[][] pixels) {
		this.pixels = pixels;
		this.imageLabel.setIcon(new ImageIcon(pixelsToBufferedImage(pixels)));
	}
	
	
	
	private BufferedImage pixelsToBufferedImage(Pixel[][] pixels) {
		BufferedImage retBuffer = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_INT_RGB);
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				retBuffer.setRGB(col, row, pixels[row][col].getRGB());
			}
		}
		
		return retBuffer;
	}

}
