package model;

import java.awt.Color;

public class VisionObject extends Frame{

	public Pixel[][] pixels;
	public ProcessableColor color;

	/*
	 * the default constructor for an object, assumes that pixels is a continuous object
	 * 
	 * @param pixels the black and white image with white being the image
	 * @param the color of the object
	 */
	public VisionObject(Pixel[][] pixels, ProcessableColor color) {
		this.color = color;
		this.pixels = pixels;
	}
	
	/*
	 * gets the number of pixels that are not white
	 */
	public int getArea() {
		int area = 0;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				if(pixels[row][col].getAverage() != 0) {
					area++;
				}
			}
		}
		return area;
	}
	
	public int[] getCOM() {
		double colTotal = 0, rowTotal = 0, massTotal = 0;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				massTotal += (double) pixels[row][col].getAverage();
				rowTotal += (double) pixels[row][col].getAverage() * row;
				colTotal += (double) pixels[row][col].getAverage() * col;
			}
		}
		try {
			colTotal /= massTotal;
			rowTotal /= massTotal;
			return new int[] { (int) colTotal, (int) rowTotal };
		} catch (Exception e) {
			return new int[] { 0, 0 };
		}
	}
	
	/**
	 * Method to show large changes in color
	 * 
	 * @param edgeDist
	 *            the distance for finding edges
	 */
	public void edgeDetectionHorizontal(int edgeDist) {
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		Color rightColor = null;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length - 1; col++) {
				leftPixel = pixels[row][col];
				rightPixel = pixels[row][col + 1];
				rightColor = rightPixel.getColor();
				if (leftPixel.colorDistance(rightColor) > edgeDist)
					leftPixel.setColor(Color.WHITE);
				else
					leftPixel.setColor(Color.BLACK);
			}
		}
	}
}
