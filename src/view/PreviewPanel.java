package view;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.SpringLayout;

import controller.Controller;
import model.vision.Pixel;

public class PreviewPanel extends JPanel {

	private JLabel imageLabel;
	private JSlider framerateSlider;
	private JLabel framerateLabel;
	private JSlider thresholdSlider;
	private JLabel thresholdLabel;
	private JSlider spreadSlider;
	private JLabel spreadLabel;
	private JSlider blurSlider;
	private JLabel blurLabel;
	private Controller controller;

	private SpringLayout layout;

	private Pixel[][] pixels;

	public PreviewPanel(Pixel[][] pixels, Controller controller) {
		super();

		this.controller = controller;

		this.pixels = pixels;

		this.imageLabel = new JLabel(new ImageIcon(pixelsToBufferedImage(pixels)));
		this.framerateSlider = new JSlider(0, 1, 60, controller.framerate);
		this.thresholdSlider = new JSlider(0, 0, 100, (int)(controller.threshold*100));
		this.spreadSlider = new JSlider(0, 0, 100, (int)(controller.hueSpread*100));
		this.blurSlider = new JSlider(0, 0, 50, controller.blur);
		this.framerateLabel = new JLabel("Framerate: 0");
		this.thresholdLabel = new JLabel("Threshold: 0");
		this.spreadLabel = new JLabel("Intensity: 0");
		this.blurLabel = new JLabel("Blur: 0");

		layout = new SpringLayout();
		layout.putConstraint(SpringLayout.NORTH, imageLabel, 30, SpringLayout.SOUTH, blurSlider);
		layout.putConstraint(SpringLayout.NORTH, blurLabel, 0, SpringLayout.SOUTH, blurSlider);
		layout.putConstraint(SpringLayout.WEST, blurLabel, 10, SpringLayout.WEST, blurSlider);
		layout.putConstraint(SpringLayout.NORTH, blurSlider, 10, SpringLayout.SOUTH, framerateLabel);
		layout.putConstraint(SpringLayout.WEST, blurSlider, 0, SpringLayout.WEST, framerateSlider);
		layout.putConstraint(SpringLayout.NORTH, thresholdLabel, 0, SpringLayout.SOUTH, thresholdSlider);
		layout.putConstraint(SpringLayout.NORTH, framerateLabel, 0, SpringLayout.SOUTH, framerateSlider);
		layout.putConstraint(SpringLayout.NORTH, spreadLabel, 0, SpringLayout.SOUTH, spreadSlider);
		layout.putConstraint(SpringLayout.WEST, thresholdLabel, 0, SpringLayout.WEST, thresholdSlider);
		layout.putConstraint(SpringLayout.EAST, thresholdLabel, -10, SpringLayout.EAST, this);
		layout.putConstraint(SpringLayout.WEST, spreadLabel, 10, SpringLayout.WEST, spreadSlider);
		layout.putConstraint(SpringLayout.WEST, framerateLabel, 10, SpringLayout.WEST, framerateSlider);
		layout.putConstraint(SpringLayout.WEST, imageLabel, 0, SpringLayout.WEST, framerateSlider);
		layout.putConstraint(SpringLayout.NORTH, thresholdSlider, 0, SpringLayout.NORTH, framerateSlider);
		layout.putConstraint(SpringLayout.WEST, thresholdSlider, 6, SpringLayout.EAST, spreadSlider);
		layout.putConstraint(SpringLayout.NORTH, spreadSlider, 0, SpringLayout.NORTH, framerateSlider);
		layout.putConstraint(SpringLayout.WEST, spreadSlider, 6, SpringLayout.EAST, framerateSlider);
		layout.putConstraint(SpringLayout.NORTH, framerateSlider, 10, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WEST, framerateSlider, 10, SpringLayout.WEST, this);
		this.setLayout(layout);

		update(pixels);

		this.add(imageLabel);
		this.add(framerateSlider);
		this.add(thresholdSlider);
		this.add(spreadSlider);
		this.add(blurSlider);

		this.add(spreadLabel);
		this.add(thresholdLabel);
		this.add(framerateLabel);
		this.add(blurLabel);

	}

	public void update(Pixel[][] pixels) {
		this.pixels = pixels;
		this.imageLabel.setIcon(new ImageIcon(pixelsToBufferedImage(pixels)));

		// update controller values
		
		spreadLabel.setText("Hue Spread: " + (double)spreadSlider.getValue()/100);
		framerateLabel.setText("Framerate: " + framerateSlider.getValue() + "/" + controller.maxFramerate);
		thresholdLabel.setText("Threshold: " + (double)thresholdSlider.getValue()/100);
		blurLabel.setText("Blur: " + blurSlider.getValue());
		
		controller.setFrameRate(framerateSlider.getValue());
		controller.setHueSpread((float)spreadSlider.getValue() / 100);
		controller.setThresholdCoeff((float)thresholdSlider.getValue() / 100);
		controller.setBlur(blurSlider.getValue());

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
