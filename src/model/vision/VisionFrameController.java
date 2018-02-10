package model.vision;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.github.sarxos.webcam.Webcam;

public class VisionFrameController extends Frame {
	ProcessableColor[] colors;
	VisionFrame[] colorFrames;
	int blurAmount;

	public VisionFrameController(String file, ProcessableColor[] colors, int blurAmount, boolean swapBlueRed) {
		super(file);
		this.colors = colors;
		this.colorFrames = new VisionFrame[colors.length];
		this.blurAmount = blurAmount;

		if (swapBlueRed) {
			this.swapColor(ProcessableColor.BLUE, ProcessableColor.RED);
		}

		if (blurAmount > 0) {
			long startTime = System.currentTimeMillis();
			fastBlur(blurAmount);
			System.out.println("Blurred image");
			System.out.println(System.currentTimeMillis() - startTime);
		}
		populateVisionFrames();
		process();
		concatenateColors();

	}

	public VisionFrameController(BufferedImage image, ProcessableColor[] colors, int blurAmount, boolean swapBlueRed) {
		super(image);
		this.colors = colors;
		this.colorFrames = new VisionFrame[colors.length];
		this.blurAmount = blurAmount;

		if (swapBlueRed) {
			this.swapColor(ProcessableColor.BLUE, ProcessableColor.RED);
		}

		if (blurAmount > 0) {
			long startTime = System.currentTimeMillis();
			fastBlur(blurAmount);
			System.out.println("Blurred image");
			System.out.println(System.currentTimeMillis() - startTime);
		}
		populateVisionFrames();
		process();
		concatenateColors();

	}

	// //written in order to save time by not creating a new object every time
	// public void setBufferedImage(BufferedImage image) {
	// FastRGB img = new FastRGB(image);
	//
	// try {
	// for (int row = 0; row < pixels.length; row++) {
	// for (int col = 0; col < pixels[0].length; col++) {
	// pixels[row][col] = new Pixel(img.getRGB(col, row));
	// }
	// }
	// } catch (ArrayIndexOutOfBoundsException e) {
	//
	// this.pixels = new Pixel[image.getHeight()][image.getWidth()];
	// for (int row = 0; row < pixels.length; row++) {
	// for (int col = 0; col < pixels[0].length; col++) {
	// pixels[row][col] = new Pixel(img.getRGB(col, row));
	// }
	// }
	// }
	// if (blurAmount > 0) {
	// long startTime = System.currentTimeMillis();
	// fastBlur(blurAmount);
	// System.out.println("Blurred image");
	// System.out.println(System.currentTimeMillis() - startTime);
	// }
	// populateVisionFrames();
	// process();
	// concatenateColors();
	// }

	private void populateVisionFrames() {
		long startTime = System.currentTimeMillis();
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
					if (!frame.pixels[row][col].isBlack()) {
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
		return new VisionFrame(getWidth(), getHeight(), color);
	}

}
