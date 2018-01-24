package model;

import java.awt.*;
//import java.awt.font.*;
//import java.awt.geom.*;
import java.awt.image.BufferedImage;
//import java.text.*;
//import java.util.*;
//import java.util.List; // resolves problem with java.awt.List and java.util.List

/**
 * A class that represents a picture. This class inherits from SimplePicture and
 * allows the student to add functionality to the Picture class.
 * 
 * @author Barbara Ericson ericson@cc.gatech.edu
 */
public class Picture extends SimplePicture {
	///////////////////// constructors //////////////////////////////////
	
	/**
	 * Constructor that takes no arguments
	 */
	public Picture() {
		/*
		 * not needed but use it to show students the implicit call to super() child
		 * constructors always call a parent constructor
		 */
		super();
	}
	
	/**
	 * Constructor that takes a file name and creates the picture
	 * 
	 * @param fileName
	 *            the name of the file to create the picture from
	 */
	public Picture(String fileName) {
		// let the parent class handle this fileName
		super(fileName);
	}
	
	/**
	 * Constructor that takes the width and height
	 * 
	 * @param height
	 *            the height of the desired picture
	 * @param width
	 *            the width of the desired picture
	 */
	public Picture(int height, int width) {
		// let the parent class handle this width and height
		super(width, height);
	}
	
	/**
	 * Constructor that takes a picture and creates a copy of that picture
	 * 
	 * @param copyPicture
	 *            the picture to copy
	 */
	public Picture(Picture copyPicture) {
		// let the parent class do the copy
		super(copyPicture);
	}
	
	/**
	 * Constructor that takes a buffered image
	 * 
	 * @param image
	 *            the buffered image to use
	 */
	public Picture(BufferedImage image) {
		super(image);
	}
	
	////////////////////// methods ///////////////////////////////////////
	
	/**
	 * Method to return a string with information about this picture.
	 * 
	 * @return a string with information about the picture such as fileName, height
	 *         and width.
	 */
	public String toString() {
		String output = "Picture, filename " + getFileName() + " height " + getHeight() + " width " + getWidth();
		return output;
		
	}
	
	/** Method to set the blue to 0 */
	public void zeroBlue() {
		Pixel[][] pixels = this.getPixels2D();
		for (Pixel[] rowArray : pixels) {
			for (Pixel pixelObj : rowArray) {
				pixelObj.setBlue(0);
			}
		}
	}
	
	/**
	 * Method that mirrors the picture around a vertical mirror in the center of the
	 * picture from left to right
	 */
	public void mirrorVertical() {
		Pixel[][] pixels = this.getPixels2D();
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		int width = pixels[0].length;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < width / 2; col++) {
				leftPixel = pixels[row][col];
				rightPixel = pixels[row][width - 1 - col];
				rightPixel.setColor(leftPixel.getColor());
			}
		}
	}
	
	/** Mirror just part of a picture of a temple */
	public void mirrorTemple() {
		int mirrorPoint = 276;
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		int count = 0;
		Pixel[][] pixels = this.getPixels2D();
		
		// loop through the rows
		for (int row = 27; row < 97; row++) {
			// loop from 13 to just before the mirror point
			for (int col = 13; col < mirrorPoint; col++) {
				
				leftPixel = pixels[row][col];
				rightPixel = pixels[row][mirrorPoint - col + mirrorPoint];
				rightPixel.setColor(leftPixel.getColor());
			}
		}
	}
	
	/**
	 * copy from the passed fromPic to the specified startRow and startCol in the
	 * current picture
	 * 
	 * @param fromPic
	 *            the picture to copy from
	 * @param startRow
	 *            the start row to copy to
	 * @param startCol
	 *            the start col to copy to
	 */
	public void copy(Picture fromPic, int startRow, int startCol) {
		Pixel fromPixel = null;
		Pixel toPixel = null;
		Pixel[][] toPixels = this.getPixels2D();
		Pixel[][] fromPixels = fromPic.getPixels2D();
		for (int fromRow = 0, toRow = startRow; fromRow < fromPixels.length && toRow < toPixels.length; fromRow++, toRow++) {
			for (int fromCol = 0, toCol = startCol; fromCol < fromPixels[0].length && toCol < toPixels[0].length; fromCol++, toCol++) {
				fromPixel = fromPixels[fromRow][fromCol];
				toPixel = toPixels[toRow][toCol];
				toPixel.setColor(fromPixel.getColor());
			}
		}
	}
	
	/** Method to create a collage of several pictures */
	public void createCollage() {
		Picture flower1 = new Picture("flower1.jpg");
		Picture flower2 = new Picture("flower2.jpg");
		this.copy(flower1, 0, 0);
		this.copy(flower2, 100, 0);
		this.copy(flower1, 200, 0);
		Picture flowerNoBlue = new Picture(flower2);
		flowerNoBlue.zeroBlue();
		this.copy(flowerNoBlue, 300, 0);
		this.copy(flower1, 400, 0);
		this.copy(flower2, 500, 0);
		this.mirrorVertical();
		this.write("collage.jpg");
	}
	
	/**
	 * Method to show large changes in color
	 * 
	 * @param edgeDist
	 *            the distance for finding edges
	 */
	public void edgeDetection(int edgeDist) {
		Pixel leftPixel = null;
		Pixel rightPixel = null;
		Pixel[][] pixels = this.getPixels2D();
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
	
	// ----------------------------------------------------------------------------------------------------------
	// My code
	
	public void swapRedAndBlue() {
		Pixel[][] pixels = this.getPixels2D();
		int redVal;
		int blueVal;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				redVal = pixels[row][col].getRed();
				blueVal = pixels[row][col].getBlue();
				
				pixels[row][col].setBlue(redVal);
				pixels[row][col].setRed(blueVal);
			}
		}
	}
	
	public void swapRedAndGreen() {
		Pixel[][] pixels = this.getPixels2D();
		int redVal;
		int greenVal;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				redVal = pixels[row][col].getRed();
				greenVal = pixels[row][col].getGreen();
				
				pixels[row][col].setGreen(redVal);
				pixels[row][col].setRed(greenVal);
			}
		}
	}
	
	public void swapGreenAndBlue() {
		Pixel[][] pixels = this.getPixels2D();
		int greenVal;
		int blueVal;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				greenVal = pixels[row][col].getGreen();
				blueVal = pixels[row][col].getBlue();
				
				pixels[row][col].setBlue(greenVal);
				pixels[row][col].setGreen(blueVal);
			}
		}
	}
	
	public int getAverage(RGB color) {
		int retBuffer = 0;
		Pixel[][] pixels = this.getPixels2D();
		switch (color) {
			case RED:
				for (int row = 0; row < pixels.length; row++) {
					for (int col = 0; col < pixels[0].length; col++) {
						retBuffer += pixels[row][col].getRed();
					}
				}
				break;
			case BLUE:
				for (int row = 0; row < pixels.length; row++) {
					for (int col = 0; col < pixels[0].length; col++) {
						retBuffer += pixels[row][col].getBlue();
					}
				}
				break;
			case GREEN:
				for (int row = 0; row < pixels.length; row++) {
					for (int col = 0; col < pixels[0].length; col++) {
						retBuffer += pixels[row][col].getGreen();
					}
				}
			case YELLOW:
				for (int row = 0; row < pixels.length; row++) {
					for (int col = 0; col < pixels[0].length; col++) {
						retBuffer += pixels[row][col].getYellow();
					}
				}
		}
		return retBuffer / (pixels.length * pixels[0].length);
		
	}
	
	public void colorPick(RGB color, int threshold, double requiedIntensity) {
		Pixel[][] pixels = this.getPixels2D();
		switch (color) {
			case RED:
				for (int row = 0; row < pixels.length; row++) {
					for (int col = 0; col < pixels[0].length; col++) {
						if (pixels[row][col].getRed() > threshold && pixels[row][col].getGreen() < pixels[row][col].getRed() * requiedIntensity && pixels[row][col].getBlue() < pixels[row][col].getRed() * requiedIntensity) {
							pixels[row][col].setColor(Color.WHITE);
						}
						else {
//							pixels[row][col].setColor(Color.BLACK);
						}
					}
				}
				break;
			
			case BLUE:
				for (int row = 0; row < pixels.length; row++) {
					for (int col = 0; col < pixels[0].length; col++) {
						if (pixels[row][col].getBlue() > threshold && pixels[row][col].getGreen() < pixels[row][col].getBlue() * requiedIntensity && pixels[row][col].getRed() < pixels[row][col].getBlue() * requiedIntensity) {
							pixels[row][col].setColor(Color.WHITE);
						}
						else {
//							pixels[row][col].setColor(Color.BLACK);
						}
					}
				}
				break;
			
			case GREEN:
				for (int row = 0; row < pixels.length; row++) {
					for (int col = 0; col < pixels[0].length; col++) {
						if (pixels[row][col].getGreen() > threshold && pixels[row][col].getBlue() < pixels[row][col].getGreen() * requiedIntensity && pixels[row][col].getRed() < pixels[row][col].getGreen() * requiedIntensity) {
							pixels[row][col].setColor(Color.WHITE);
						}
						else {
//							pixels[row][col].setColor(Color.BLACK);
						}
					}
				}
				break;
			case YELLOW:
				for (int row = 0; row < pixels.length; row++) {
					for (int col = 0; col < pixels[0].length; col++) {
						if (pixels[row][col].getYellow() > threshold && pixels[row][col].getCyan() < pixels[row][col].getYellow() * requiedIntensity && pixels[row][col].getMagenta() < pixels[row][col].getYellow() * requiedIntensity) {
							pixels[row][col].setColor(Color.WHITE);
						}
						else {
//							pixels[row][col].setColor(Color.BLACK);
						}
					}
				}
				break;
		}
		
	}
	
	public void cutoffBottom(int numOfPixels) {
		Pixel[][] pixels = this.getPixels2D();
		for (int row = pixels.length - numOfPixels; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				pixels[row][col].setColor(Color.BLACK);
			}
		}
		
	}
	
	public int[] getCOM() {
		double colTotal = 0, rowTotal = 0, massTotal = 0;
		Pixel[][] pixels = this.getPixels2D();
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				massTotal += (double)pixels[row][col].getAverage();
				rowTotal += (double)pixels[row][col].getAverage() * row;
				colTotal += (double)pixels[row][col].getAverage() * col;
			}
		}
		try {
			colTotal /= massTotal;
			rowTotal /= massTotal;
			return new int[] {(int)colTotal, (int)rowTotal };
		}
		catch (Exception e) {
			return new int[] { 0, 0 };
		}
	}
	
} // this } is the end of class Picture, put all new methods before this
