package view;

import java.awt.*;
import java.awt.image.BufferedImage;

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
		
		layout = new SpringLayout();
		this.setLayout(layout);
		
		setImage(pixels);
		
		this.add(imageLabel);
	}
	
	
	public void setImage(Pixel[][] pixels) {
		BufferedImage image = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_INT_RGB);
		this.imageLabel = new JLabel(new ImageIcon(image));;
	}
	
	
	
	private BufferedImage pixelsToBufferedImage(Pixel[][] pixels) {
		BufferedImage retBuffer = new BufferedImage(pixels[0].length, pixels.length, BufferedImage.TYPE_INT_RGB);
		
		return retBuffer;
	}

}
