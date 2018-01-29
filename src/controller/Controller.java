package controller;

import model.*;
import view.PreviewFrame;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

public class Controller {
	public Controller(String[] args) {

		long startTime;

//		String fileName = "/home/ben/Scripts/webcamShot.jpg";
		String fileName = "image.jpg";

		ProcessableColor[] colors = new ProcessableColor[] {ProcessableColor.GREEN, ProcessableColor.RED, ProcessableColor.BLUE, ProcessableColor.YELLOW, ProcessableColor.CYAN, ProcessableColor.MAGENTA};

		int[] com = new int[2];
		VisionFrameController pic = new VisionFrameController(fileName, colors);
		PreviewFrame window = new PreviewFrame(pic.getPixels2D());
		long iterations = 0;
		long timeAccumulator = 0;
		long timeTaken = 0;
		while (true) {
			try {
				
				startTime = System.currentTimeMillis();

				pic = new VisionFrameController(fileName, colors);
//				com = pic.getCOM();

				timeTaken = System.currentTimeMillis() - startTime;
				timeAccumulator+=timeTaken;
				iterations++;
				System.out.println("Milliseconds taken: " + timeTaken);
				System.out.println("Average: " + timeAccumulator/iterations + "\n");

//				if (com[0] == 0) {
//					window.updatePicture(new Frame(fileName).getPixels2D());
//				} else {
					window.updatePicture(pic.getPixels2D());
//				}
				TimeUnit.MILLISECONDS.sleep(250);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
