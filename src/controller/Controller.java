package controller;

import model.*;
import view.PreviewFrame;

import java.awt.Color;
import java.util.concurrent.TimeUnit;

public class Controller {
	public Controller(String[] args) {
		ProcessableColor[] colors = new ProcessableColor[] { ProcessableColor.YELLOW, ProcessableColor.GREEN };
		ProcessableColor color = ProcessableColor.RED;

		int[] com = new int[2];
		Frame pic = new Frame("/home/ben/Scripts/webcamShot.jpg");
		PreviewFrame window = new PreviewFrame(pic.getPixels2D());
		while (true) {
			try {
				System.out.println("New Frame");
				pic = new Frame("/home/ben/Scripts/webcamShot.jpg");
				pic.cutoffBottom(30);
				pic.colorIsolate(color, 1, 1);
				com = pic.getCOM();
				pic.drawBox(com[0], com[1], Color.MAGENTA, 10);
				if (com[0] == 0) {
					window.updatePicture(new Frame("/home/ben/Scripts/webcamShot.jpg").getPixels2D());
				} else {
					window.updatePicture(pic.getPixels2D());
				}
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
				window.updatePicture(new Frame("/home/ben/Scripts/webcamShot.jpg").getPixels2D());
			}
		}
	}
}
