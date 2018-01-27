package model;

import java.awt.Color;

public class VisionFrameController extends Frame {
	ProcessableColor[] colors;
	Frame[] colorFrames;
	String file;

	public VisionFrameController(String file, ProcessableColor[] colors) {
		super(file);
		this.file = file;
		this.colors = colors;
		this.colorFrames = new Frame[colors.length];
		this.pixels = this.getPixels2D();

		populateVisionFrames();
		System.out.println("Populated frames");
		process();
		System.out.println("processed");
		// concatenateColors();
		System.out.println("Concatenated Color frames");

	}

	private void populateVisionFrames() {
		for (int i = 0; i < colorFrames.length; i++) {
			colorFrames[i] = new Frame(pixels);
		}
	}

	private void process() {
		for (int i = 0; i < colorFrames.length; i++) {
			colorFrames[i].colorIsolate(colors[i], 1, 1);
		}
	}

	private void concatenateColors() {
		for (Frame frame : colorFrames) {
			System.out.println("frame");
			for (int row = 0; row < pixels.length; row++) {
				for (int col = 0; col < pixels[0].length; col++) {
					if (frame.getPixels2D()[row][col].getAverage() != 0) {
						System.out.println(col + " " + row);
						pixels[row][col] = new Pixel(0,0,0);// frame.getPixels2D()[col][row];
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
