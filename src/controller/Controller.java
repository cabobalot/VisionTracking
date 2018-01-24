package controller;

import model.*;
import java.util.concurrent.TimeUnit;

public class Controller {
	public Controller(String[] args) {
		
		RGB color = RGB.GREEN;
		
		int[] com = new int[2];
		try {
			Picture pic = new Picture("/home/ben/Scripts/webcamShot.jpg");
			PictureExplorer window = new PictureExplorer(pic);
			while (true) {
				try {
					pic = new Picture("/home/ben/Scripts/webcamShot.jpg");
					pic.cutoffBottom(30);
					pic.colorPick(color, (int)(pic.getAverage(color)*1.8), .7);
					com = pic.getCOM();
					pic.mirrorTemple();
//					pic.edgeDetection(120);
//					pic.edgeDetection(120);
//					pic.edgeDetection(120);
//					pic.edgeDetection(120);
					window.displayPixelInformation(String.valueOf(com[0]), String.valueOf(com[1]));
					if(com[0] == 0){
						window.updatePicture(new Picture("/home/ben/Scripts/webcamShot.jpg"));
					}
					else{
						window.updatePicture(pic);
					}
					TimeUnit.MILLISECONDS.sleep(100);
				}
				catch (Exception e) {
					e.printStackTrace();
					window.updatePicture(new Picture("/home/ben/Scripts/webcamShot.jpg"));
				}
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}
