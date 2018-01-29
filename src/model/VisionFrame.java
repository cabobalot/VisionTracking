package model;

import java.awt.Color;

public class VisionFrame extends Frame {
	
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
	
	//entry point
	public void run() {
		this.colorIsolate(colorToIsolate, 1, 1);
		this.drawCOM(Color.MAGENTA, .25);
	}

}
