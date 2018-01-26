package model;

import java.awt.Color;

public class VisionFrameController extends Frame {
	ProcessableColor[] colors;
	VisionFrame[] colorFrames;
	String file;

	public VisionFrameController(String file, ProcessableColor[] colors) {
		super(file);
		this.file = file;
		this.colors = colors;
		this.colorFrames = new VisionFrame[colors.length];

		populateVisionFrames();
		System.out.println("Populated frames");
		process();
		System.out.println("processed");
		// concatenateColors();
		System.out.println("Concatenated Color frames");

	}

	private void populateVisionFrames() {
		for (int i = 0; i < colorFrames.length; i++) {
			colorFrames[i] = new VisionFrame(getPixels2D());
		}
	}

	private void process() {
		for (int i = 0; i < colorFrames.length; i++) {
			colorFrames[i].colorIsolate(colors[i], 1, 1);
		}
	}

	private void concatenateColors() {

		for (VisionFrame frame : colorFrames) {
			System.out.println("frame");
			for (int row = 0; row < getPixels2D().length; row++) {
				for (int col = 0; col < getPixels2D()[0].length; col++) {
					if (frame.getPixels2D()[row][col].getAverage() != 0) {
						System.out.println(col + " " + row);
						getPixels2D()[row][col] = new Pixel(0,0,0);// frame.getPixels2D()[col][row];
					}
				}
			}
		}
	}

	public Frame getColoredFrame(ProcessableColor color) {
		for (int i = 0; i < colorFrames.length; i++) {
			if(color==colors[i]) {
				return colorFrames[i];
			}
		}
		return new Frame(file);
	}
}
