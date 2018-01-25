package controller;

import model.*;
import view.PictureExplorer;

import java.util.concurrent.TimeUnit;

public class Controller {
	public Controller(String[] args) {
		ProcessableColor[] colors = new ProcessableColor[] { ProcessableColor.YELLOW, ProcessableColor.GREEN };

		int[] com = new int[2];
		VisionFrameController pic = new VisionFrameController("/home/ben/Scripts/webcamShot.jpg", colors);
		PictureExplorer window = new PictureExplorer(pic);
		while (true) {
			try {
				pic = new VisionFrameController("/home/ben/Scripts/webcamShot.jpg", colors);
				pic.cutoffBottom(30);
				pic.process();
				com = pic.getCOM();
				window.displayPixelInformation(String.valueOf(com[0]), String.valueOf(com[1]));
				if (com[0] == 0) {
					window.updatePicture(new VisionFrame("/home/ben/Scripts/webcamShot.jpg"));
				} else {
					window.updatePicture(pic);
				}
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
				window.updatePicture(new VisionFrame("/home/ben/Scripts/webcamShot.jpg"));
			}
		}
	}
}
