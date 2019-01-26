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
	public float threshold = .02f;
	public int blur = 10;
	public int framerate = 24;
	
	public float yellowHue = .16f;
	public float greenHue = .33f;
	private float testHue = .43f;
	
//	private float[] colors = new float[] { yellowHue, greenHue};
	private float[] colors = new float[] {yellowHue};
	
	public Controller(String[] args) {
		try {
			try {
				webcam = new Camera(args[0]);
			} catch (Exception e) {
				webcam = new Camera(640, 480);
//												 webcam = new Camera("http://10.45.85.2:5800/stream.mjpg");
//				 webcam = new Camera("http://10.0.0.56:8080/video");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		webcam.start();
		frameTimer = new RollingTimer(.05);
		pic = new HSVIsolateController(webcam.getImage(), colors, blur, threshold, hueSpread);
		window = new PreviewFrame(pic.getPixels2D(), this);
		
		rioResponder = new NetworkServerController(5801, this, pic);
		rioResponder.start();
		while (true) {
			try {
				
				frameTimer.startTimer();
				
				pic = new HSVIsolateController(webcam.getImage(), colors, blur, threshold, hueSpread);
				rioResponder.setVisionFrameController(this, pic);
				window.update(pic.getPixels2D());
				
//				 Frame frame = new Frame(webcam.getImage());
//				 frame.addStatic((float)blur/100);
//				 window.update(frame.getPixels2D());
				
				frameTimer.stopTimer();
				
				System.out.println("Milliseconds taken: " + frameTimer.getLastTimeTaken());
				System.out.println("Average: " + (int) frameTimer.getAverage() + "\n");
				
				TimeUnit.MILLISECONDS.sleep((1000 / framerate) - frameTimer.getLastTimeTaken() > 0 ? (1000 / framerate) - frameTimer.getLastTimeTaken() : 0);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	//methods to interact with the GUI
	
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
	
	public int getMaxFramerate() {
		return (int) frameTimer.getOpsPerSecond();
	}
	
	public int getMaxCameraFramerate() {
		return webcam.getMaxFramerate();
	}
	
	public int getCameraFramerate() {
		return webcam.getFrameRate();
	}
}
