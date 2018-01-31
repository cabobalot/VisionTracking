package model.vision;

import java.awt.Color;

public class VisionObject extends Frame{

	public ProcessableColor color;

	/*
	 * the default constructor for an object, assumes that pixels is a continuous object
	 * 
	 * @param pixels the black and white image with white being the image
	 * @param the color of the object
	 */
	public VisionObject(Pixel[][] pixels, ProcessableColor color) {
		super(pixels);
		this.color = color;
	}
	
	
}