package controller;

import model.*;
import view.PreviewFrame;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

public class Controller {

	VisionFrameController pic;
	PreviewFrame window;
	GarbageCollector garbageCollector;

	public Controller(String[] args) {

		long startTime;

		// String fileName = "/home/ben/Scripts/webcamShot.jpg";
		String fileName = "image.jpg";

		ProcessableColor[] colors = new ProcessableColor[] { ProcessableColor.GREEN, ProcessableColor.YELLOW };
//				ProcessableColor.BLUE, ProcessableColor.RED, ProcessableColor.CYAN, ProcessableColor.MAGENTA };

		int[] com = new int[2];
		pic = new VisionFrameController(fileName, colors);
		window = new PreviewFrame(pic.getPixels2D());

		garbageCollector = new GarbageCollector();

		long iterations = 0;
		long timeAccumulator = 0;
		long timeTaken = 0;
		while (true) {
			try {

				startTime = System.currentTimeMillis();

				pic = new VisionFrameController(fileName, colors);

				timeTaken = System.currentTimeMillis() - startTime;
				timeAccumulator += timeTaken;
				iterations++;
				System.out.println("Milliseconds taken: " + timeTaken);
				System.out.println("Average: " + timeAccumulator / iterations + "\n");

				window.updatePicture(pic.getPixels2D());
				TimeUnit.MILLISECONDS.sleep(10);
				if (garbageCollector.isAlive()) {
					garbageCollector.start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
