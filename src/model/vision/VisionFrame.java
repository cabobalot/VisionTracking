package model.vision;

import java.awt.Color;

public class VisionFrame extends Frame {

	final double cameraCoeff = 1; // used to calibrate distance

	protected ProcessableColor colorToIsolate = ProcessableColor.GREEN;

	public VisionFrame(String fileName) {
		super(fileName);

	}

	public VisionFrame(String fileName, ProcessableColor colorToIsolate) {
		super(fileName);
		this.colorToIsolate = colorToIsolate;

	}

	public VisionFrame(Pixel[][] pixels) {
		super(pixels);

	}

	public VisionFrame(Pixel[][] pixels, ProcessableColor colorToIsolate) {
		super(pixels);
		this.colorToIsolate = colorToIsolate;

	}

	public void setColorToIsolate(ProcessableColor color) {
		this.colorToIsolate = color;
	}

	// entry point
	public void run() {
		this.colorIsolate(colorToIsolate, .7, 1.2);
		this.drawCOM(Color.MAGENTA, .25);
	}

	public double getDistanceFeet(double widthInches, double heightInches) {
		return (Math.sqrt(pixels[0].length * pixels.length) / Math.sqrt(getArea()) * ((widthInches + heightInches) / 2) * .095 * cameraCoeff);
	}

}
