package controller;

import view.PreviewFrame;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

//import model.networking.NetworkClientController;
import model.networking.NetworkServerController;
import model.vision.*;

public class Controller {

	VisionFrameController pic;
	PreviewFrame window;
	GarbageCollector garbageCollector;
	NetworkServerController rioResponder;

	public Controller(String[] args) {

		long startTime;
		String fileName = "image.jpg";

		ProcessableColor[] colors = new ProcessableColor[] { ProcessableColor.GREEN, ProcessableColor.YELLOW,};
//				ProcessableColor.BLUE, ProcessableColor.RED, ProcessableColor.CYAN, ProcessableColor.MAGENTA };

		int[] com = new int[2];
		pic = new VisionFrameController(fileName, colors);
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

				pic = new VisionFrameController(fileName, colors);
				
				rioResponder.setVisionFrameController(pic);

				timeTaken = System.currentTimeMillis() - startTime;
				timeAccumulator += timeTaken;
				iterations++;
				
				System.out.println("Milliseconds taken: " + timeTaken);
				System.out.println("Average: " + timeAccumulator / iterations + "\n");
				System.out.println(pic.getColoredFrame(ProcessableColor.GREEN).getCOM());

				window.updatePicture(pic.getPixels2D());
				
				if (garbageCollector.isAlive()) {
					garbageCollector.start();
				}
				
				TimeUnit.MILLISECONDS.sleep(100);
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
