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
	VisionFrameController pic;
	PreviewFrame window;
	GarbageCollector garbageCollector;
	NetworkServerController rioResponder;
	Camera webcam;

	public Controller(String[] args) {

		long startTime;
		try {
			try {
				webcam = new Camera(args[0]);
			} catch (Exception e) {
				webcam = new Camera(640, 480);
//				webcam = new Camera("http://10.45.85.2:5800/stream.mjpg");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ProcessableColor[] colors = new ProcessableColor[] { ProcessableColor.GREEN, ProcessableColor.YELLOW };

		pic = new VisionFrameController(webcam.getImage(), colors, 10, webcam.isIpCamera());
		window = new PreviewFrame(pic.getPixels2D());

		rioResponder = new NetworkServerController(5801, pic);
		rioResponder.start();

		long iterations = 0;
		long timeAccumulator = 0;
		long timeTaken = 0;
		while (true) {
			try {

				startTime = System.currentTimeMillis();

				pic = new VisionFrameController(webcam.getImage(), colors, 10, webcam.isIpCamera());
				rioResponder.setVisionFrameController(pic);
				timeTaken = System.currentTimeMillis() - startTime;
				iterations++;
				if (iterations > 10) {
					timeAccumulator += timeTaken;
				}

				System.out.println("Milliseconds taken: " + timeTaken);
				System.out.println("Average: " + timeAccumulator / (iterations - 9) + "\n");

				window.updatePicture(pic.getPixels2D());

				// TimeUnit.MILLISECONDS.sleep(1000);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
