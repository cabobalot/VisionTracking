package model;

import java.awt.*;
//import java.awt.font.*;
//import java.awt.geom.*;
import java.awt.image.BufferedImage;
//import java.text.*;
//import java.util.*;
//import java.util.List; // resolves problem with java.awt.List and java.util.List
import java.util.ArrayList;

/**
 * A class that represents a picture. This class inherits from SimplePicture and
 * allows the student to add functionality to the Picture class.
 * 
 * @author Barbara Ericson ericson@cc.gatech.edu
 */
public class VisionFrame extends Frame {
	
	Pixel[][] pixels;
	public ArrayList<VisionObject> objects = new ArrayList();
	///////////////////// constructors //////////////////////////////////

	public VisionFrame(String fileName) {
		super(fileName);
		pixels = this.getPixels2D();
	}

	public VisionFrame(BufferedImage image) {
		super(image);
		pixels = this.getPixels2D();
	}

	////////////////////// methods ///////////////////////////////////////

	public ArrayList<VisionObject> getVisionObjects(){
		return objects;
	}
	
	public VisionObject getVisionObject(int index) {
		return objects.get(index);
	}

	public void swapColor(ProcessableColor color1, ProcessableColor color2) {
		int color1Val, color2Val;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				color1Val = pixels[row][col].getColor(color1);
				color2Val = pixels[row][col].getColor(color2);

				pixels[row][col].setColor(color1, color1Val);
				pixels[row][col].setColor(color2, color2Val);
			}
		}
	}

	public int getAverage(ProcessableColor color) {
		int retBuffer = 0;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				retBuffer += pixels[row][col].getColor(color);
			}
		}
		return retBuffer / (pixels.length * pixels[0].length);
	}

	public void colorIsolate(ProcessableColor color, double thresholdCoeff, double requiedIntensity) {
		ProcessableColor color1, color2;
		int threshold = (int) (thresholdCoeff * getAverage(color));
		switch (color) {
		case RED:
			color1 = ProcessableColor.BLUE;
			color2 = ProcessableColor.GREEN;
			break;
		case BLUE:
			color1 = ProcessableColor.RED;
			color2 = ProcessableColor.GREEN;
			break;
		case GREEN:
			color1 = ProcessableColor.BLUE;
			color2 = ProcessableColor.RED;
			break;
		case CYAN:
			color1 = ProcessableColor.MAGENTA;
			color2 = ProcessableColor.YELLOW;
			break;
		case MAGENTA:
			color1 = ProcessableColor.YELLOW;
			color2 = ProcessableColor.CYAN;
			break;
		case YELLOW:
			color1 = ProcessableColor.CYAN;
			color2 = ProcessableColor.MAGENTA;
			break;
		default:
			color1 = color;
			color2 = color;
		}

		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				if (pixels[row][col].getColor(color) > threshold
						&& pixels[row][col].getColor(color1) < pixels[row][col].getColor(color) * requiedIntensity
						&& pixels[row][col].getColor(color2) < pixels[row][col].getColor(color) * requiedIntensity) {
					pixels[row][col].setColor(color);
				} else {
					pixels[row][col].setColor(Color.BLACK);
				}
			}
		}
	}

	public void cutoffBottom(int numOfPixels) {
		for (int row = pixels.length - numOfPixels; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				pixels[row][col].setColor(Color.BLACK);
			}
		}

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
	

}
