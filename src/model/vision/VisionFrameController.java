package model.vision;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.concurrent.TimeUnit;

import com.github.sarxos.webcam.Webcam;

public class VisionFrameController extends Frame {
	float[] hues;
	VisionFrame[] colorFrames;
	int blurAmount;
	private float thresholdCoeff;
	private float hueSpread;
	
	public VisionFrameController(String file, float[] hues, int blurAmount, float thresholdCoeff, float hueSpread) {
		super(file);
		this.hues = hues;
		this.colorFrames = new VisionFrame[hues.length];
		this.blurAmount = blurAmount;
		this.hueSpread = hueSpread;
		
		// filters
		
		if (blurAmount > 0) {
//			blur(blurAmount);
			 fastBlur(blurAmount);
			
		}
		
		populateVisionFrames();
		process();
		concatenateColors();
		
	}
	
	public VisionFrameController(BufferedImage image, float[] hues, int blurAmount, float thresholdCoeff, float hueSpread) {
		super(image);
		this.hues = hues;
		this.colorFrames = new VisionFrame[hues.length];
		this.blurAmount = blurAmount;
		this.thresholdCoeff = thresholdCoeff;
		this.hueSpread = hueSpread;
		
		// filters
		
		if (blurAmount > 0) {
//			blur(blurAmount);
			fastBlur(blurAmount);
			
		}
		
		populateVisionFrames();
		process();
		concatenateColors();
		
	}
	
	private void populateVisionFrames() {
		long startTime = System.currentTimeMillis();
		for (int i = 0; i < colorFrames.length; i++) {
			colorFrames[i] = new VisionFrame(pixels, hues[i], thresholdCoeff, hueSpread);
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
	
	public VisionFrame getColoredFrame(float hue) {
		for (int i = 0; i < colorFrames.length; i++) {
			if (hue == hues[i]) {
				return colorFrames[i];
			}
		}
		return new VisionFrame(getWidth(), getHeight(), hue, thresholdCoeff, hueSpread);
	}
	
}
