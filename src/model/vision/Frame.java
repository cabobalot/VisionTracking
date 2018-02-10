package model.vision;

import javax.imageio.ImageIO;
import java.awt.image.DataBufferByte;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.awt.geom.*;

public class Frame extends Thread {

	/*
	 * buffered image to hold pixels for the simple picture
	 */
	// protected BufferedImage bufferedImage;

	protected Pixel[][] pixels;

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
				this.pixels[row][col] = new Pixel(pixels[row][col].getRGB());
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
				pixels[row][col].setRed(pixels[row][col].getRed() + newPixels[row][col].getRed());
				pixels[row][col].setGreen(pixels[row][col].getGreen() + newPixels[row][col].getGreen());
				pixels[row][col].setBlue(pixels[row][col].getBlue() + newPixels[row][col].getBlue());
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

	///////////////////// My Code///////////////////////////////

	public void swapColor(ProcessableColor color1, ProcessableColor color2) {
		int color1Val, color2Val;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				color1Val = pixels[row][col].getColor(color1);
				color2Val = pixels[row][col].getColor(color2);

				pixels[row][col].setColor(color1, color2Val);
				pixels[row][col].setColor(color2, color1Val);
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

	public void colorIsolate(ProcessableColor color, double thresholdCoeff, double requiredIntensity) {
		thresholdCoeff *= 1.45;
		requiredIntensity *= .7;
		ProcessableColor color1, color2;
		switch (color) {
		case RED:
			color1 = ProcessableColor.BLUE;
			color2 = ProcessableColor.GREEN;
			requiredIntensity *= .5;
			thresholdCoeff *= 1;
			break;
		case BLUE:
			color1 = ProcessableColor.RED;
			color2 = ProcessableColor.GREEN;
			requiredIntensity *= 1;
			thresholdCoeff *= 1;
			break;
		case GREEN:
			color1 = ProcessableColor.BLUE;
			color2 = ProcessableColor.RED;
			break;
		case CYAN:
			color1 = ProcessableColor.MAGENTA;
			color2 = ProcessableColor.YELLOW;
			requiredIntensity *= .8;
			thresholdCoeff *= 1.2;
			break;
		case MAGENTA:
			color1 = ProcessableColor.YELLOW;
			color2 = ProcessableColor.CYAN;
			requiredIntensity *= 1;
			thresholdCoeff *= 1;
			break;
		case YELLOW:
			color1 = ProcessableColor.CYAN;
			color2 = ProcessableColor.MAGENTA;
			requiredIntensity *= 1;
			thresholdCoeff *= 1.25;
			break;
		default:
			color1 = color;
			color2 = color;
		}

		int threshold = (int) (thresholdCoeff * (getAverage(color) + 5));

		int blockSize = 2;
		int thisRow;
		int thisCol;
		for (int row = 0; row < (pixels.length) / blockSize; row++) {
			thisRow = row * blockSize;
			for (int col = 0; col < (pixels[0].length) / blockSize; col++) {
				thisCol = col * blockSize;
				if (pixels[thisRow][thisCol].getColor(color) > threshold
						&& pixels[thisRow][thisCol].getColor(color1) < pixels[thisRow][thisCol].getColor(color)
								* requiredIntensity
						&& pixels[thisRow][thisCol].getColor(color2) < pixels[thisRow][thisCol].getColor(color)
								* requiredIntensity) {
					// pixels[row][col].setColor(color);
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
					massTotal += (double) pixels[row][col].getAverage();
					rowTotal += (double) pixels[row][col].getAverage() * row;
					colTotal += (double) pixels[row][col].getAverage() * col;
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

	public void blur(int amount) {
		int red, green, blue;
		for (int i = 0; i < amount; i++) {
			for (int row = 0; row < pixels.length; row++) {
				for (int col = 0; col < pixels[0].length; col++) {
					try {
						red = (pixels[row - 1][col].getRed() + pixels[row + 1][col].getRed()
								+ pixels[row][col - 1].getRed() + pixels[row][col + 1].getRed()
								+ pixels[row][col].getRed()) / 5;
						green = (pixels[row - 1][col].getGreen() + pixels[row + 1][col].getGreen()
								+ pixels[row][col - 1].getGreen() + pixels[row][col + 1].getGreen()
								+ pixels[row][col].getGreen()) / 5;
						blue = (pixels[row - 1][col].getBlue() + pixels[row + 1][col].getBlue()
								+ pixels[row][col - 1].getBlue() + pixels[row][col + 1].getBlue()
								+ pixels[row][col].getBlue()) / 5;

						pixels[row][col].setRed(red);
						pixels[row][col].setGreen(green);
						pixels[row][col].setBlue(blue);
					} catch (ArrayIndexOutOfBoundsException e) {

					}
				}
			}
		}
	}

	public void fastBlur(int amount) {
		amount /= 4;
		int red, green, blue;
		for (int i = 0; i < amount; i++) {
			for (int row = 0; row < pixels.length; row++) {
				for (int col = 0; col < pixels[0].length; col++) {
					try {
						red = (pixels[row - amount][col].getRed() + pixels[row + amount][col].getRed()
								+ pixels[row][col - amount].getRed() + pixels[row][col + amount].getRed()
								+ pixels[row][col].getRed()) / 5;
						green = (pixels[row - amount][col].getGreen() + pixels[row + amount][col].getGreen()
								+ pixels[row][col - amount].getGreen() + pixels[row][col + amount].getGreen()
								+ pixels[row][col].getGreen()) / 5;
						blue = (pixels[row - amount][col].getBlue() + pixels[row + amount][col].getBlue()
								+ pixels[row][col - amount].getBlue() + pixels[row][col + amount].getBlue()
								+ pixels[row][col].getBlue()) / 5;

						pixels[row][col].setRed(red);
						pixels[row][col].setGreen(green);
						pixels[row][col].setBlue(blue);
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

	public void drawBox(int x, int y, ProcessableColor color, int radius) {
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

} // end of SimplePicture class
