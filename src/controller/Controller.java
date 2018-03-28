package controller;

import view.PreviewFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.ipcam.*;

import model.networking.NetworkServerController;
import model.util.Camera;
import model.util.GarbageCollector;
import model.util.RollingTimer;
import model.vision.*;
import model.vision.hsvIsolate.HSVIsolateController;

public class Controller {
	
	// VisionFrameController pic;
	private HSVIsolateController pic;
	private PreviewFrame window;
	private NetworkServerController rioResponder;
	private Camera webcam;
	private RollingTimer frameTimer;
	
	public float hueSpread = .05f;
	public float threshold = .4f;
	public int blur = 10;
	public int framerate = 24;
	public int maxFramerate = 0;
	
	public float yellowHue = .16f;
	public float greenHue = .43f;
	private float testHue = .43f;
	
	private float[] colors = new float[] { yellowHue };
	
	public Controller(String[] args) {
		try {
			try {
				webcam = new Camera(args[0]);
			} catch (Exception e) {
				webcam = new Camera(640, 480);
//								 webcam = new Camera("http://10.45.85.2:5800/stream.mjpg");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		webcam.start();
		
		// if(webcam==null)
		
		//		colors = new float[] {};
		pic = new HSVIsolateController(webcam.getImage(), colors, blur, threshold, hueSpread);
		window = new PreviewFrame(pic.getPixels2D(), this);
		frameTimer = new RollingTimer(.05);
		
		rioResponder = new NetworkServerController(5801, this, pic);
		rioResponder.start();
		while (true) {
			try {
				
				frameTimer.startTimer();
				
				pic = new HSVIsolateController(webcam.getImage(), colors, blur, threshold, hueSpread);
				rioResponder.setVisionFrameController(this, pic);
				window.update(pic.getPixels2D());
				
				// Frame frame = new Frame(webcam.getImage());
				// frame.contrast(5);
				// window.updatePicture(frame.getPixels2D());
				
				frameTimer.stopTimer();
				maxFramerate = (int)frameTimer.getOpsPerSecond();
				
				System.out.println("Milliseconds taken: " + frameTimer.getLastTimeTaken());
				System.out.println("Average: " + (int)frameTimer.getAverage() + "\n");
				
				TimeUnit.MILLISECONDS.sleep((1000 / framerate) - frameTimer.getLastTimeTaken() > 0 ? (1000 / framerate) - frameTimer.getLastTimeTaken() : 0);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void setThresholdCoeff(float value) {
		this.threshold = value;
	}
	
	public void setHueSpread(float value) {
		this.hueSpread = value;
	}
	
	public void setFrameRate(int value) {
		this.framerate = value;
	}
	
	public void setBlur(int value) {
		this.blur = value;
	}
	
	public void setTestHue(float value) {
		this.testHue = value;
	}
	
	public int getMaxCameraFramerate() {
		return webcam.getMaxFramerate();
	}
}
