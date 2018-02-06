package model.vision;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.github.sarxos.webcam.Webcam;

public class VisionFrameController extends Frame {
	ProcessableColor[] colors;
	VisionFrame[] colorFrames;

	public VisionFrameController(String file, ProcessableColor[] colors) {
		super(file);
		this.colors = colors;
		this.colorFrames = new VisionFrame[colors.length];

		populateVisionFrames();
		process();
		concatenateColors();

	}

	public VisionFrameController(BufferedImage image, ProcessableColor[] colors) {
		super(image);
		this.colors = colors;
		this.colorFrames = new VisionFrame[colors.length];

		populateVisionFrames();
		process();
		concatenateColors();

	}

	private void populateVisionFrames() {
		long startTime = System.currentTimeMillis();
		cutoffBottom(30);
		for (int i = 0; i < colorFrames.length; i++) {
			colorFrames[i] = new VisionFrame(pixels, colors[i]);
		}
		System.out.println("Populated frames");
		System.out.println(System.currentTimeMillis() - startTime);
	}

	private void process() {
		long startTime = System.currentTimeMillis();
		// assign threads
		for (int i = 0; i < colorFrames.length; i++) {
			colorFrames[i].start();
		}

		// wait till all threads complete
		for (int i = 0; i < colorFrames.length; i++) {
			while (colorFrames[i].isAlive()) {

			}
		}
		System.out.println("Processed Color frames");
		System.out.println(System.currentTimeMillis() - startTime);
	}

	private void concatenateColors() {
		long startTime = System.currentTimeMillis();
		for (Frame frame : colorFrames) {
			for (int row = 0; row < pixels.length; row++) {
				for (int col = 0; col < pixels[0].length; col++) {
					if (frame.pixels[row][col].getAverage() != 0) {
						pixels[row][col] = frame.pixels[row][col];
					}
				}
			}
		}
		System.out.println("Concatenated Color frames");
		System.out.println(System.currentTimeMillis() - startTime);
	}

	public VisionFrame getColoredFrame(ProcessableColor color) {
		for (int i = 0; i < colorFrames.length; i++) {
			if (color == colors[i]) {
				return colorFrames[i];
			}
		}
		return null;
	}

}
