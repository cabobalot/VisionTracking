package controller;

import view.PreviewFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.util.concurrent.TimeUnit;

import com.github.sarxos.webcam.Webcam;

//import model.networking.NetworkClientController;
import model.networking.NetworkServerController;
import model.vision.*;

public class Controller {

	// VisionFrameController pic;
	VisionFrameController pic;
	PreviewFrame window;
	GarbageCollector garbageCollector;
	NetworkServerController rioResponder;
	Webcam webcam;

	public Controller(String[] args) {

		long startTime;
		webcam = Webcam.getDefault();
		Dimension d = new Dimension(640, 480);
		webcam.setViewSize(d);
		webcam.open();

		ProcessableColor[] colors = new ProcessableColor[] { ProcessableColor.GREEN };// , ProcessableColor.YELLOW};

		pic = new VisionFrameController(webcam.getImage(), colors, 0);
		// pic = new Frame(webcam.getImage());
		window = new PreviewFrame(pic.getPixels2D());

		rioResponder = new NetworkServerController(4585, pic);
		rioResponder.start();

		garbageCollector = new GarbageCollector();

		long iterations = 0;
		long timeAccumulator = 0;
		long timeTaken = 0;
		while (true) {
			try {

				startTime = System.currentTimeMillis();

				pic = new VisionFrameController(webcam.getImage(), colors, 20);

				timeTaken = System.currentTimeMillis() - startTime;
				iterations++;
				if (iterations > 10) {
					timeAccumulator += timeTaken;
				}

				rioResponder.setVisionFrameController(pic);
				System.out.println("Milliseconds taken: " + timeTaken);
				System.out.println("Average: " + timeAccumulator / (iterations - 10) + "\n");

				window.updatePicture(pic.getPixels2D());

//				try {
//					garbageCollector.start();
//				} catch (IllegalThreadStateException e) {
//
//				}

//				 TimeUnit.MILLISECONDS.sleep(1000);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
