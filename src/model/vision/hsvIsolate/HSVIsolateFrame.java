package model.vision.hsvIsolate;

import java.awt.Color;
import java.awt.color.ColorSpace;
import java.util.ArrayList;
import java.util.List;

import model.vision.Frame;
import model.vision.Pixel;

public class HSVIsolateFrame extends Frame {
	
	private List<HSVIsolateObject> objects;
	
	//	protected ProcessableColor colorToIsolate = ProcessableColor.GREEN;
	
	protected float hueToIsolate;
	
	private float threshold;
	private float hueSpread;
	
	public HSVIsolateFrame(Pixel[][] pixels, float hueToIsolate, float threshold, float hueSpread) {
		super(pixels);
		this.hueToIsolate = hueToIsolate;
		this.objects = new ArrayList<HSVIsolateObject>();
		this.threshold = threshold;
		this.hueSpread = hueSpread;
	}
	
	public HSVIsolateFrame(int rows, int cols, float hueToIsolate, float threshold, float hueSpread) {
		super(rows, cols);
		this.hueToIsolate = hueToIsolate;
		this.objects = new ArrayList<HSVIsolateObject>();
		this.threshold = threshold;
		this.hueSpread = hueSpread;
	}
	
	// entry point
	public void run() {
		this.colorIsolate(hueToIsolate, hueSpread, threshold);
		this.drawBlackBorder();
		this.breakIntoObjects(.0005);
		this.concatenateObjects();
		
		// this.colorIsolate(colorToIsolate, .7, 1.2);
		// this.drawCOM(Color.MAGENTA, .25);
		// System.out.println(this.getArea());
	}
	
	private void concatenateObjects() {
		for (HSVIsolateObject frame : objects) {
			
			for (int row = 0; row < pixels.length; row++) {
				for (int col = 0; col < pixels[0].length; col++) {
					if (!frame.pixels[row][col].isBlack()) {
						this.pixels[row][col] = frame.pixels[row][col];
					}
				}
			}
		}
	}
	
	private void breakIntoObjects(double minimumArea) {
		HSVIsolateObject object;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				try {
					if (!pixels[row][col].isBlack()) {
						object = findObject(row, col, getHeight() / 20);
						if (((double) pixels.length * (double) pixels[0].length) * minimumArea < ((double) object.getArea())) {
							object.drawCOM(Color.MAGENTA, .25);
							objects.add(object);
							
						}
					}
				} catch (NullPointerException e) {
				}
			}
		}
	}
	
	private HSVIsolateObject findObject(int startRow, int startCol, int fudgeFactor) {
		int col = startCol;
		int row = startRow;
		int maxRow = startRow;
		int maxCol = startCol;
		int minCol = startCol;
		int minRow = startRow;
		// new empty VisionObject
		HSVIsolateObject object = new HSVIsolateObject(pixels.length, pixels[0].length, new Color(hueToIsolate, 1f, 1f));
		/*
		 * 0: north, 1: East, 2: South, 3: West
		 */
		int direction = 1;
		
		// find boundingBox, an algorithm that always follows the left wall until
		// it comes back to it's original position
		// for my later self: https://github.com/jackrzhang/maze-solver
		do {
			/*
			 * row - 1 = North, row + 1 = South, col + 1 = East, col - 1 = West
			 */
			switch (direction) {
				case 0: // North
					if (!pixels[row][col - 1].isBlack()) {// turn left
						direction = 3;
						col--;
					} else if (!pixels[row - 1][col].isBlack()) {// go straight
						row--;
					} else {
						direction++;
					}
					break;
				case 1: // East
					if (!pixels[row - 1][col].isBlack()) {// turn left
						direction--;
						row--;
					} else if (!pixels[row][col + 1].isBlack()) {// go straight
						col++;
					} else {
						direction++;
					}
					break;
				case 2: // South
					if (!pixels[row][col + 1].isBlack()) {// turn left
						direction--;
						col++;
					} else if (!pixels[row + 1][col].isBlack()) {// go straight
						row++;
					} else {
						direction++;
					}
					break;
				case 3: // West
					if (!pixels[row + 1][col].isBlack()) {// turn left
						direction--;
						row++;
					} else if (!pixels[row][col - 1].isBlack()) {// go straight
						col--;
					} else {
						direction = 0;
					}
					break;
			}
			
			if (minRow > row)
				minRow = row;
			if (maxRow < row)
				maxRow = row;
			if (minCol > col)
				minCol = col;
			if (maxCol < col)
				maxCol = col;
			
		} while ((col != startCol) || (row != startRow));
		Color color = Color.getHSBColor(hueToIsolate, 1f, 1f);
		
		// find pixels within bounding box
		for (row = minRow - fudgeFactor; row < maxRow + fudgeFactor; row++) {
			for (col = minCol - fudgeFactor; col < maxCol + fudgeFactor; col++) {
				try {
					if (!pixels[row][col].isBlack()) {
						pixels[row][col].setColor(Color.black);
						object.pixels[row][col].setColor(color);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		
		int[] center = new int[] { (maxRow + minRow) / 2, (maxCol + minCol) / 2 };
		
		return object;
		
	}
	
	public HSVIsolateObject getLargestObject() {
		int largestIndex = 0;
		double largestSize = 0;
		double currentSize;
		try {
			for (int i = 0; i < objects.size(); i++) {
				currentSize = objects.get(i).getArea();
				if (currentSize > largestSize) {
					largestSize = currentSize;
					largestIndex = i;
				}
			}
			return objects.get(largestIndex);
		} catch (IndexOutOfBoundsException e) {
			return new HSVIsolateObject(pixels.length, pixels[0].length, new Color(hueToIsolate, 1, 1));
		}
	}
	
	public List<HSVIsolateObject> getObjects() {
		return objects;
	}
	
}
