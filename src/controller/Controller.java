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
import model.vision.*;

public class Controller {

	// VisionFrameController pic;
	private VisionFrameController pic;
	private PreviewFrame window;
	private GarbageCollector garbageCollector;
	private NetworkServerController rioResponder;
	private Camera webcam;

	public double thresholdCoeff = .5;
	public double requiredIntensity = 1.25;
	public int blur = 10;
	public int framerate = 24;
	public int maxFramerate = 0;

	public Controller(String[] args) {

		long startTime;
		try {
			try {
				webcam = new Camera(args[0]);
			} catch (Exception e) {
//				webcam = new Camera(640, 480);
				 webcam = new Camera("http://10.45.85.2:5800/stream.mjpg");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		if(webcam==null)
			

		ProcessableColor[] colors = new ProcessableColor[] { ProcessableColor.GREEN, ProcessableColor.YELLOW };

		pic = new VisionFrameController(webcam.getImage(), colors, blur, webcam.isIpCamera(), thresholdCoeff,
				requiredIntensity);
		window = new PreviewFrame(pic.getPixels2D(), this);

		rioResponder = new NetworkServerController(5801, pic);
		rioResponder.start();

		long timeTaken = 0;
		int averageTimeTaken = 0;
		while (true) {
			try {

				startTime = System.currentTimeMillis();

				pic = new VisionFrameController(webcam.getImage(), colors, blur, webcam.isIpCamera(), thresholdCoeff,
						requiredIntensity);
				rioResponder.setVisionFrameController(pic);
				window.update(pic.getPixels2D());

				// Frame frame = new Frame(webcam.getImage());
				// frame.contrast(5);
				// window.updatePicture(frame.getPixels2D());
				
				timeTaken = System.currentTimeMillis() - startTime;
				averageTimeTaken = (int) (timeTaken*.05 + (averageTimeTaken*.95));
				maxFramerate = (int) (((1000 / (double) averageTimeTaken) * .1) + ((double) maxFramerate * .9));

				System.out.println("Milliseconds taken: " + averageTimeTaken);
				System.out.println("Average: " + averageTimeTaken + "\n");

				TimeUnit.MILLISECONDS.sleep((1000 / framerate) - timeTaken > 0 ? (1000 / framerate) - timeTaken : 0);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void setThresholdCoeff(double value) {
		this.thresholdCoeff = value;
	}

	public void setRequiredIntensity(double value) {
		this.requiredIntensity = value;
	}

	public void setFrameRate(int value) {
		this.framerate = value;
	}

	public void setBlur(int value) {
		this.blur = value;
	}
}
