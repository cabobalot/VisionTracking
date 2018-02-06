package model.vision;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class VisionFrame extends Frame {

	final double cameraCoeff = 1; // used to calibrate distance

	private List<VisionObject> objects;

	protected ProcessableColor colorToIsolate = ProcessableColor.GREEN;

	public VisionFrame(String fileName, ProcessableColor colorToIsolate) {
		super(fileName);
		this.colorToIsolate = colorToIsolate;
		this.objects = new ArrayList<VisionObject>();

	}

	public VisionFrame(Pixel[][] pixels, ProcessableColor colorToIsolate) {
		super(pixels);
		this.colorToIsolate = colorToIsolate;
		this.objects = new ArrayList<VisionObject>();
	}

	public void setColorToIsolate(ProcessableColor color) {
		this.colorToIsolate = color;
	}

	// entry point
	public void run() {
		this.colorIsolate(colorToIsolate, .7, 1.2);
		this.drawBlackBorder();
		this.breakIntoObjects(.1);
		this.concatenateObjects();

		// this.colorIsolate(colorToIsolate, .7, 1.2);
		// this.drawCOM(Color.MAGENTA, .25);
		// System.out.println(this.getArea());
	}

	private void concatenateObjects() {
//		System.out.println("size: " + objects.size());
		for (VisionObject frame : objects) {
			for (int row = 0; row < pixels.length; row++) {
				for (int col = 0; col < pixels[0].length; col++) {
					if (frame.pixels[row][col].getAverage() != 0) {
						this.pixels[row][col] = frame.pixels[row][col];
					}
				}
			}
		}
	}

	private void breakIntoObjects(double minimumArea) {
		VisionObject object;
		for (int row = 0; row < pixels.length; row++) {
			for (int col = 0; col < pixels[0].length; col++) {
				try {
					if (pixels[row][col].getAverage() != 0) {
						object = findObject(row, col, 50);
//						if ((object.getArea() / (this.getHeight() * this.getWidth())) > minimumArea) {
							object.drawCOM(Color.MAGENTA, .25);
							objects.add(object);
//						}
					}
				} catch (NullPointerException e) {
				}
			}
		}
	}

	private VisionObject findObject(int startRow, int startCol, int fudgeFactor) {
		int col = startCol;
		int row = startRow;
		int maxRow = startRow;
		int maxCol = startCol;
		int minCol = startCol;
		int minRow = startRow;
		// new empty VisionObject
		VisionObject object = new VisionObject(pixels.length, pixels[0].length, colorToIsolate);
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
				if (pixels[row][col - 1].getColor(ProcessableColor.GREEN) > 127) {// turn left
					direction = 3;
					col--;
				} else if (pixels[row - 1][col].getColor(ProcessableColor.GREEN) > 127) {// go straight
					row--;
				} else {
					direction++;
				}
				break;
			case 1: // East
				if (pixels[row - 1][col].getColor(ProcessableColor.GREEN) > 127) {// turn left
					direction--;
					row--;
				} else if (pixels[row][col + 1].getColor(ProcessableColor.GREEN) > 127) {// go straight
					col++;
				} else {
					direction++;
				}
				break;
			case 2: // South
				if (pixels[row][col + 1].getColor(ProcessableColor.GREEN) > 127) {// turn left
					direction--;
					col++;
				} else if (pixels[row + 1][col].getColor(ProcessableColor.GREEN) > 127) {// go straight
					row++;
				} else {
					direction++;
				}
				break;
			case 3: // West
				if (pixels[row + 1][col].getColor(ProcessableColor.GREEN) > 127) {// turn left
					direction--;
					row++;
				} else if (pixels[row][col - 1].getColor(ProcessableColor.GREEN) > 127) {// go straight
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

		// find pixels within bounding box
		for (row = minRow - fudgeFactor; row < maxRow + fudgeFactor; row++) {
			for (col = minCol - fudgeFactor; col < maxCol + fudgeFactor; col++) {
				try {
					if (pixels[row][col].getColor(colorToIsolate) > 127) {
						pixels[row][col].setColor(Color.black);
						object.pixels[row][col].setColor(colorToIsolate);
					}
				} catch (ArrayIndexOutOfBoundsException e) {
				}
			}
		}
		
		int[] center = new int[] {(maxRow + minRow)/2, (maxCol + minCol)/2};

		return object;

	}

	public double getDistanceFeet(double widthInches, double heightInches) {
		return (Math.sqrt(pixels[0].length * pixels.length) / Math.sqrt(getArea()) * ((widthInches + heightInches) / 2)
				* .095 * cameraCoeff);
	}

	public List<VisionObject> getObjects() {
		return objects;
	}

}
