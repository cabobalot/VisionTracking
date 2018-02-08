package model.vision;

import java.awt.Color;

public class VisionObject extends Frame{

	private ProcessableColor color;
	
	private final double cameraCoeff = 1.1; // used to calibrate distance

	/*
	 * the default constructor for an object, assumes that pixels is a continuous object
	 * 
	 * @param pixels the black and white image with white being the image
	 * @param color the color of the object
	 */
	public VisionObject(Pixel[][] pixels, ProcessableColor color) {
		super(pixels);
		this.color = color;
	}
	public VisionObject(int rows, int cols, ProcessableColor color) {
		super(rows, cols);
		this.color = color;
	}
	public ProcessableColor getColor() {
		return color;
	}
	
	public double getDistanceFeet(double widthInches, double heightInches) {
		return (Math.sqrt(pixels[0].length * pixels.length) / Math.sqrt(getArea()) * ((widthInches + heightInches) / 2)
				* .095 * cameraCoeff);
	}
	
	
}