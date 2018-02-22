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

	public float hueSpread = .051f;
	public float threshold = .3f;
	public int blur = 0;
	public int framerate = 24;
	public int maxFramerate = 0;
	
	public float yellowHue = .25f;
	public float greenHue = .35f;
	public float testHue = .35f;

	public Controller(String[] args) {

		long startTime;
		try {
			try {
				webcam = new Camera(args[0]);
			} catch (Exception e) {
				webcam = new Camera(640, 480);
//				 webcam = new Camera("http://10.45.85.2:5800/stream.mjpg");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
//		if(webcam==null)
			

		float[] colors = new float[] {testHue};

		pic = new VisionFrameController(webcam.getImage(), colors, blur, webcam.isIpCamera(), threshold,
				hueSpread);
		window = new PreviewFrame(pic.getPixels2D(), this);

//		rioResponder = new NetworkServerController(5801, pic);
//		rioResponder.start();

		long timeTaken = 0;
		int averageTimeTaken = 0;
		while (true) {
			try {

				startTime = System.currentTimeMillis();

				pic = new VisionFrameController(webcam.getImage(), colors, blur, webcam.isIpCamera(), threshold,
						hueSpread);
//				rioResponder.setVisionFrameController(pic);
				window.update(pic.getPixels2D());

				// Frame frame = new Frame(webcam.getImage());
				// frame.contrast(5);
				// window.updatePicture(frame.getPixels2D());
				
				timeTaken = System.currentTimeMillis() - startTime;
				averageTimeTaken = (int) (timeTaken*.1 + (averageTimeTaken*.9));
//				maxFramerate = (int) (((1000 / (double) timeTaken) * .1) + ((double) maxFramerate * .9));
				maxFramerate = (int)(1000/averageTimeTaken);

				System.out.println("Milliseconds taken: " + timeTaken);
				System.out.println("Average: " + averageTimeTaken + "\n");

				TimeUnit.MILLISECONDS.sleep((1000 / framerate) - timeTaken > 0 ? (1000 / framerate) - timeTaken : 0);

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
}
