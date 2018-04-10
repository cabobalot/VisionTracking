package model.vision;

import javax.imageio.ImageIO;

import model.util.FastRGB;

import java.awt.image.DataBufferByte;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.awt.color.ColorSpace;
import java.io.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.awt.geom.*;

public class Frame extends Thread {
	
	/*
	 * buffered image to hold pixels for the simple picture
	 */
	// protected BufferedImage bufferedImage;
	
	public Pixel[][] pixels;
	
	private Integer area;
	
	private Integer[] com = new Integer[] { null, null };
	
	/////////////////////// Constructors /////////////////////////
	
	/**
	 * A Constructor that takes a file name and uses the file to create a picture
	 * 
	 * @param fileName
	 *            the file name to use in creating the picture
	 */
	public Frame(String fileName) {
		long startTime = System.currentTimeMillis();
		
		load(fileName);
		System.out.println("loaded File\n" + (System.currentTimeMillis() - startTime));
		
	}
	
	public Frame(Pixel[][] pixels) {
		this.pixels = new Pixel[pixels.length][pixels[0].length];
		// Arrays.fill(pixels, new Pixel(0));
		// deep copy, slow as hell. required as not to use a refrence to pixels
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				this.pixels[row][col] = new Pixel(pixels[row][col].getHSV());
			}
		}
	}
	
	public Frame(int rows, int cols) {
		this.pixels = new Pixel[rows][cols];
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				this.pixels[row][col] = new Pixel(0);
			}
		}
	}
	
	public Frame(BufferedImage image) {
		long startTime = System.currentTimeMillis();
		FastRGB img = new FastRGB(image);
		
		this.pixels = new Pixel[image.getHeight()][image.getWidth()];
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				pixels[row][col] = new Pixel(img.getRGB(col, row));
			}
		}
		System.out.println("Read image\n" + (System.currentTimeMillis() - startTime));
	}
	
	////////////////////////// Methods //////////////////////////////////
	
	public void run() {
	}
	
	/**
	 * Method to get the width of the picture in pixels
	 * 
	 * @return the width of the picture in pixels
	 */
	public int getWidth() {
		return pixels[0].length;
	}
	
	/**
	 * Method to get the height of the picture in pixels
	 * 
	 * @return the height of the picture in pixels
	 */
	public int getHeight() {
		return pixels.length;
	}
	
	/**
	 * Method to return the pixel value as an int for the given x and y location
	 * 
	 * @param x
	 *            the x coordinate of the pixel
	 * @param y
	 *            the y coordinate of the pixel
	 * @return the pixel value as an integer (alpha, red, green, blue)
	 */
	public int getPixelRGB(int x, int y) {
		return pixels[y][x].getRGB();
	}
	
	/**
	 * Method to get a pixel object for the given x and y location
	 * 
	 * @param x
	 *            the x location of the pixel in the picture
	 * @param y
	 *            the y location of the pixel in the picture
	 * @return a Pixel object for this location
	 */
	public Pixel getPixel(int x, int y) {
		// create the pixel object for this picture and the given x and y location
		return pixels[y][x];
	}
	
	/**
	 * Method to get a two-dimensional array of Pixels for this simple picture
	 * 
	 * @return a two-dimensional array of Pixel objects in row-major order.
	 */
	public Pixel[][] getPixels2D() {
		return pixels;
	}
	
	public void add(Pixel[][] newPixels) {
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				pixels[row][col].setHue(pixels[row][col].getHue() + newPixels[row][col].getHue());
				pixels[row][col].setSaturation(pixels[row][col].getSaturation() + newPixels[row][col].getSaturation());
				pixels[row][col].setValue(pixels[row][col].getValue() + newPixels[row][col].getValue());
			}
		}
	}
	
	/**
	 * Method to force the picture to repaint itself. This is very useful after you
	 * have changed the pixels in a picture and you want to see the change.
	 */
	/**
	 * Method to load the picture from the passed file name
	 * 
	 * @param fileName
	 *            the file name to use to load the picture from
	 * @throws IOException
	 *             if the picture isn't found
	 */
	
	/**
	 * Method to read the contents of the picture from a filename without throwing
	 * errors
	 * 
	 * @param fileName
	 *            the name of the file to write the picture to
	 * @return true if success else false
	 */
	public BufferedImage load(String fileName) {
		BufferedImage img;
		try {
			
			img = ImageIO.read(new File(fileName));
			
		} catch (IOException e) {
			e.printStackTrace();
			try {
				TimeUnit.MILLISECONDS.sleep(25);
			} catch (InterruptedException e1) {
			}
			img = load(fileName);
		}
		this.pixels = new Pixel[img.getHeight()][img.getWidth()];
		
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				pixels[row][col] = new Pixel(img.getRGB(col, row));
			}
		}
		return img;
	}
	
	///////////////////// Filters //////////////////////////////
	
	public float getAverageBrightness() {
		float retBuffer = 0;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				retBuffer += pixels[row][col].getValue();
			}
		}
		return retBuffer / (pixels.length * pixels[0].length);
	}
	
	public void edgeDetection(int threshold) {
		Pixel[][] thisPixels = new Pixel[getHeight()][getWidth()];
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				thisPixels[row][col] = new Pixel(pixels[row][col].getRGB());
			}
		}
		float currentColorVal;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				currentColorVal = pixels[row][col].getHue();
				try {
					if (currentColorVal - pixels[row][col - 1].getHue() > threshold || currentColorVal - pixels[row][col + 1].getHue() > threshold
							|| currentColorVal - pixels[row - 1][col].getHue() > threshold || currentColorVal - pixels[row + 1][col].getHue() > threshold) {
						
						thisPixels[row][col].setColor(Color.WHITE);
					} else {
						thisPixels[row][col].setColor(Color.BLACK);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		pixels = thisPixels;
	}
	
	public void contrast(double power) {
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				pixels[row][col].setValue((int) (Math.pow((pixels[row][col].getValue() / 255.0), power) * 255));
			}
		}
	}
	
	public void colorIsolate(float hue, float hueSpread, float threshold) {
		
		Color color = Color.getHSBColor(hue, 1f, 1f);
		
		int blockSize = 2;
		int thisRow;
		int thisCol;
		for (int row = 0; row < (pixels.length) / blockSize; row++) {
			thisRow = row * blockSize;
			for (int col = 0; col < (pixels[0].length) / blockSize; col++) {
				thisCol = col * blockSize;
				if (pixels[thisRow][thisCol].getSaturation() > threshold && pixels[thisRow][thisCol].getValue() > 0.4 && Math.abs(pixels[thisRow][thisCol].getHue() - hue) < hueSpread) {
					this.drawBox(thisCol, thisRow, color, blockSize);
				} else {
					// pixels[row][col].setColor(Color.BLACK);
					this.drawBox(thisCol, thisRow, Color.BLACK, blockSize);
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
	
	public void drawBlackBorder() {
		for (int row = 0; row < pixels.length; row++) {
			pixels[row][0].setColor(Color.black);
			pixels[row][pixels[0].length - 1].setColor(Color.black);
		}
		
		for (int col = 0; col < pixels[0].length; col++) {
			pixels[0][col].setColor(Color.black);
			pixels[pixels.length - 1][col].setColor(Color.black);
		}
	}
	
	public int[] getCOM() {
		if (com[0] == null) {
			double colTotal = 0, rowTotal = 0, massTotal = 0;
			for (int row = 0; row < pixels.length; row++) {
				for (int col = 0; col < pixels[0].length; col++) {
					massTotal += (double) pixels[row][col].getValue();
					rowTotal += (double) pixels[row][col].getValue() * row;
					colTotal += (double) pixels[row][col].getValue() * col;
				}
			}
			try {
				colTotal /= massTotal;
				rowTotal /= massTotal;
				this.com = new Integer[] { (int) colTotal, (int) rowTotal };
			} catch (Exception e) {
				this.com = new Integer[] { 0, 0 };
			}
		}
		return new int[] { this.com[0], this.com[1] };
	}
	
	public double[] getAngle(double FOV) {
		double[] angles = new double[2];
		
		angles[0] = ((double) getCOM()[0] * FOV) / (double) getWidth();
		angles[1] = ((double) getCOM()[1] * FOV) / (double) getHeight();
		
		return angles;
	}
	
	public double[] getAngleOffCenter(double FOV) {
		double[] angles = getAngle(FOV);
		
		if (getCOM()[0] == 0 && getCOM()[1] == 0)
			return new double[] { 0, 0 };
		
		angles[0] -= FOV / 2;
		angles[1] -= FOV / 2;
		angles[1] *= -1;
		
		return angles;
		
	}
	
	public void blur(int amount) {
		float saturation, value;
		for (int i = 0; i < amount; i++) {
			for (int row = 0; row < pixels.length; row++) {
				for (int col = 0; col < pixels[0].length; col++) {
					try {
						saturation = (pixels[row - 1][col].getSaturation() + pixels[row + 1][col].getSaturation() + pixels[row][col - 1].getSaturation() + pixels[row][col + 1].getSaturation()
								+ pixels[row][col].getSaturation()) / 5;
						value = (pixels[row - 1][col].getValue() + pixels[row + 1][col].getValue() + pixels[row][col - 1].getValue() + pixels[row][col + 1].getValue() + pixels[row][col].getValue())
								/ 5;
						pixels[row][col].setSaturation(saturation);
						pixels[row][col].setValue(value);
					} catch (ArrayIndexOutOfBoundsException e) {
						
					}
				}
			}
		}
	}
	
	public void fastBlur(int amount) {
		amount = (int) Math.sqrt(amount);
		float saturation, value, hue;
		for (int i = 0; i < amount; i++) {
			for (int row = 0; row < pixels.length; row++) {
				for (int col = 0; col < pixels[0].length; col++) {
					try {
						saturation = (pixels[row - amount][col].getSaturation() + pixels[row + amount][col].getSaturation() + pixels[row][col - amount].getSaturation()
								+ pixels[row][col + amount].getSaturation() + pixels[row][col].getSaturation()) / 5;
						value = (pixels[row - amount][col].getValue() + pixels[row + amount][col].getValue() + pixels[row][col - amount].getValue() + pixels[row][col + amount].getValue()
								+ pixels[row][col].getValue()) / 5;
//						hue = ((pixels[row - amount][col].getHue()+1f + pixels[row + amount][col].getHue()+1f + pixels[row][col - amount].getHue()+1f + pixels[row][col + amount].getHue()+1f
//								+ pixels[row][col].getHue()+1f) / 5)-1f;
						
						pixels[row][col].setSaturation(saturation);
						pixels[row][col].setValue(value);
//						pixels[row][col].setHue(hue);
//						System.out.println(hue);
					} catch (ArrayIndexOutOfBoundsException e) {
						
					}
				}
			}
		}
	}
	
	/*
	 * gets the number of pixels that are not black
	 */
	public int getArea() {
		if (this.area == null) {
			int thisArea = 0;
			for (int row = 0; row < pixels.length; row++) {
				for (int col = 0; col < pixels[0].length; col++) {
					if (!pixels[row][col].isBlack()) {
						thisArea++;
					}
				}
			}
			this.area = thisArea;
		}
		return this.area;
	}
	
	public void drawBox(int x, int y, Color color, int radius) {
		try {
			for (int row = y - radius; row < y + radius; row++) {
				for (int col = x - radius; col < x + radius; col++) {
					pixels[row][col].setColor(color);
				}
			}
		} catch (ArrayIndexOutOfBoundsException e) {
			drawBox(x, y, color, radius - 1);
		}
	}
	
	public void drawCOM(Color color, double sizeCoeff) {
		int[] com = getCOM();
		drawBox(com[0], com[1], color, (int) (sizeCoeff * Math.sqrt(getArea()) / 2));
	}
	
	//Fun code!!!
	public void confetti(int amount) {
		for (int i = 0; i < amount; i++) {
			drawBox((int) (getWidth() * Math.random()), (int) (getHeight() * Math.random()), Color.getHSBColor((float) Math.random(), 1f, 1f), (int) (Math.random() * 20));
		}
	}
	
	public void addStatic(float amount) {
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				if (Math.random() < amount) {
					pixels[row][col].setValue((float) Math.random());
					pixels[row][col].setHue((float) Math.random());
					pixels[row][col].setSaturation((float) Math.random());
				}
			}
		}
	}
	
	public void fillSaturation(float value) {
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				pixels[row][col].setSaturation(value);
			}
		}
	}
	
} // end of SimplePicture class
