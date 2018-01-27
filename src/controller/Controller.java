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

		ProcessableColor[] colors = new ProcessableColor[] { ProcessableColor.YELLOW, ProcessableColor.GREEN };
		ProcessableColor color = ProcessableColor.YELLOW;

		int[] com = new int[2];
		Frame pic = new Frame(fileName);
		PreviewFrame window = new PreviewFrame(pic.getPixels2D());
		while (true) {
			try {
				startTime = System.currentTimeMillis();

				pic = new Frame(fileName);
				pic.cutoffBottom(30);
				pic.colorIsolate(color, 1, 1);
				pic.drawCOM(Color.MAGENTA, .25);
				com = pic.getCOM();

				System.out.println("Milliseconds taken: " + (System.currentTimeMillis() - startTime));
				//
				if (com[0] == 0) {
					window.updatePicture(new Frame(fileName).getPixels2D());
				} else {
					window.updatePicture(pic.getPixels2D());
				}
				TimeUnit.MILLISECONDS.sleep(250);
			} catch (Exception e) {
				e.printStackTrace();
//				window.updatePicture(new Frame(fileName).getPixels2D());
			}
		}
	}
}
