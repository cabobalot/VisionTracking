package model;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.*;
import java.io.*;
import java.awt.geom.*;

/**
 * A class that represents a simple picture. A simple picture may have an
 * associated file name and a title. A simple picture has pixels, width, and
 * height. A simple picture uses a BufferedImage to hold the pixels. You can
 * show a simple picture in a PictureFrame (a JFrame). You can also explore a
 * simple picture.
 * 
 * @author Barb Ericson ericson@cc.gatech.edu
 */
public class Frame {

	/////////////////////// Fields /////////////////////////

	/**
	 * the file name associated with the simple picture
	 */
	private String fileName;

	/**
	 * the title of the simple picture
	 */
	private String title;

	/**
	 * buffered image to hold pixels for the simple picture
	 */
	private BufferedImage bufferedImage;

	/**
	 * extension for this file (jpg or bmp)
	 */
	private String extension;

	protected Pixel[][] pixels;

	/////////////////////// Constructors /////////////////////////

	/**
	 * A Constructor that takes a file name and uses the file to create a picture
	 * 
	 * @param fileName
	 *            the file name to use in creating the picture
	 */
	public Frame(String fileName) {

		// load the picture into the buffered image
		load(fileName);
		pixels = this.getPixels2D();

	}

	/**
	 * A constructor that takes a buffered image
	 * 
	 * @param image
	 *            the buffered image
	 */
	public Frame(BufferedImage image) {
		this.bufferedImage = image;
		title = "None";
		fileName = "None";
		extension = "jpg";
		pixels = this.getPixels2D();
	}

	public Frame(Pixel[][] pixels) {
		this.pixels = pixels;
		makeBufferedImage();
	}

	////////////////////////// Methods //////////////////////////////////

	private void makeBufferedImage() {
		bufferedImage = new BufferedImage(pixels.length, pixels[0].length, BufferedImage.TYPE_INT_RGB);
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				bufferedImage.setRGB(col, row, pixels[row][col].getRGB());
			}
		}
	}

	/**
	 * Method to get the extension for this picture
	 * 
	 * @return the extension (jpg, bmp, giff, etc)
	 */
	public String getExtension() {
		return extension;
	}

	/**
	 * Method to get the buffered image
	 * 
	 * @return the buffered image
	 */
	public BufferedImage getBufferedImage() {
		return bufferedImage;
	}

	/**
	 * Method to get a graphics object for this picture to use to draw on
	 * 
	 * @return a graphics object to use for drawing
	 */
	public Graphics getGraphics() {
		return bufferedImage.getGraphics();
	}

	/**
	 * Method to get a Graphics2D object for this picture which can be used to do 2D
	 * drawing on the picture
	 */
	public Graphics2D createGraphics() {
		return bufferedImage.createGraphics();
	}

	/**
	 * Method to get the file name associated with the picture
	 * 
	 * @return the file name associated with the picture
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * Method to set the file name
	 * 
	 * @param name
	 *            the full pathname of the file
	 */
	public void setFileName(String name) {
		fileName = name;
	}

	/**
	 * Method to get the title of the picture
	 * 
	 * @return the title of the picture
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * Method to get the width of the picture in pixels
	 * 
	 * @return the width of the picture in pixels
	 */
	public int getWidth() {
		return bufferedImage.getWidth();
	}

	/**
	 * Method to get the height of the picture in pixels
	 * 
	 * @return the height of the picture in pixels
	 */
	public int getHeight() {
		return bufferedImage.getHeight();
	}

	/**
	 * Method to get an image from the picture
	 * 
	 * @return the buffered image since it is an image
	 */
	public Image getImage() {
		return bufferedImage;
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
	public int getBasicPixel(int x, int y) {
		return bufferedImage.getRGB(x, y);
	}

	/**
	 * Method to set the value of a pixel in the picture from an int
	 * 
	 * @param x
	 *            the x coordinate of the pixel
	 * @param y
	 *            the y coordinate of the pixel
	 * @param rgb
	 *            the new rgb value of the pixel (alpha, red, green, blue)
	 */
	public void setBasicPixel(int x, int y, int rgb) {
		bufferedImage.setRGB(x, y, rgb);
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

	/**
	 * Method to get the coordinates of the enclosing rectangle after this
	 * transformation is applied to the current picture
	 * 
	 * @return the enclosing rectangle
	 */
	public Rectangle2D getTransformEnclosingRect(AffineTransform trans) {
		int width = getWidth();
		int height = getHeight();
		double maxX = width - 1;
		double maxY = height - 1;
		double minX, minY;
		Point2D.Double p1 = new Point2D.Double(0, 0);
		Point2D.Double p2 = new Point2D.Double(maxX, 0);
		Point2D.Double p3 = new Point2D.Double(maxX, maxY);
		Point2D.Double p4 = new Point2D.Double(0, maxY);
		Point2D.Double result = new Point2D.Double(0, 0);
		Rectangle2D.Double rect = null;

		// get the new points and min x and y and max x and y
		trans.deltaTransform(p1, result);
		minX = result.getX();
		maxX = result.getX();
		minY = result.getY();
		maxY = result.getY();
		trans.deltaTransform(p2, result);
		minX = Math.min(minX, result.getX());
		maxX = Math.max(maxX, result.getX());
		minY = Math.min(minY, result.getY());
		maxY = Math.max(maxY, result.getY());
		trans.deltaTransform(p3, result);
		minX = Math.min(minX, result.getX());
		maxX = Math.max(maxX, result.getX());
		minY = Math.min(minY, result.getY());
		maxY = Math.max(maxY, result.getY());
		trans.deltaTransform(p4, result);
		minX = Math.min(minX, result.getX());
		maxX = Math.max(maxX, result.getX());
		minY = Math.min(minY, result.getY());
		maxY = Math.max(maxY, result.getY());

		// create the bounding rectangle to return
		rect = new Rectangle2D.Double(minX, minY, maxX - minX + 1, maxY - minY + 1);
		return rect;
	}

	/**
	 * Method to get the coordinates of the enclosing rectangle after this
	 * transformation is applied to the current picture
	 * 
	 * @return the enclosing rectangle
	 */
	public Rectangle2D getTranslationEnclosingRect(AffineTransform trans) {
		return getTransformEnclosingRect(trans);
	}

	/**
	 * Method to return a string with information about this picture
	 * 
	 * @return a string with information about the picture
	 */
	public String toString() {
		String output = "Simple Picture, filename " + fileName + " height " + getHeight() + " width " + getWidth();
		return output;
	}

	///////////////////// My Code///////////////////////////////
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

	public void colorIsolate(ProcessableColor color, double thresholdCoeff, double requiredIntensity) {
		thresholdCoeff *= 1.5;
		requiredIntensity *= .7;
		ProcessableColor color1, color2;
		int threshold = (int) (thresholdCoeff * getAverage(color));
		switch (color) {
		case RED:
			color1 = ProcessableColor.BLUE;
			color2 = ProcessableColor.GREEN;
			requiredIntensity *= .67;
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
			requiredIntensity *= .8;
			thresholdCoeff *= 2;
			break;
		case MAGENTA:
			color1 = ProcessableColor.YELLOW;
			color2 = ProcessableColor.CYAN;
			requiredIntensity *= .8;
			thresholdCoeff *= 2;
			break;
		case YELLOW:
			color1 = ProcessableColor.CYAN;
			color2 = ProcessableColor.MAGENTA;
			requiredIntensity *= .8;
			thresholdCoeff *= 2;
			break;
		default:
			color1 = color;
			color2 = color;
		}

		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				if (pixels[row][col].getColor(color) > threshold
						&& pixels[row][col].getColor(color1) < pixels[row][col].getColor(color) * requiredIntensity
						&& pixels[row][col].getColor(color2) < pixels[row][col].getColor(color) * requiredIntensity) {
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

	/*
	 * gets the number of pixels that are not black
	 */
	public int getArea() {
		int area = 0;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				if (pixels[row][col].getAverage() != 0) {
					area++;
				}
			}
		}
		return area;
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
		drawBox(getCOM()[0], getCOM()[1], color, (int) (sizeCoeff * Math.sqrt(getArea()) / 2));
	}

} // end of SimplePicture class
