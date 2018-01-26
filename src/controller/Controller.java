package controller;

import model.*;
import view.PictureExplorer;

import java.util.concurrent.TimeUnit;

public class Controller {
	public Controller(String[] args) {
		ProcessableColor[] colors = new ProcessableColor[] { ProcessableColor.YELLOW, ProcessableColor.GREEN };
		ProcessableColor color = ProcessableColor.GREEN;

		int[] com = new int[2];
		Frame pic = new Frame("/home/ben/Scripts/webcamShot.jpg");//, colors);
		PictureExplorer window = new PictureExplorer(pic);
		while (true) {
			try {
				pic = new Frame("/home/ben/Scripts/webcamShot.jpg");//, colors);
				pic.cutoffBottom(30);
				pic.colorIsolate(color, 1, 1);
				com = pic.getCOM();
				window.displayPixelInformation(String.valueOf(com[0]), String.valueOf(com[1]));
				if (com[0] == 0) {
					window.updatePicture(new Frame("/home/ben/Scripts/webcamShot.jpg"));
				} else {
					window.updatePicture(pic);
				}
				TimeUnit.MILLISECONDS.sleep(50);
			} catch (Exception e) {
				e.printStackTrace();
				window.updatePicture(new Frame("/home/ben/Scripts/webcamShot.jpg"));
			}
		}
	}
}
