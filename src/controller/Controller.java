package controller;

import view.PreviewFrame;

import java.awt.Color;
import java.awt.Dimension;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.ipcam.*;

import model.networking.NetworkServerController;
import model.vision.*;

public class Controller {

	// VisionFrameController pic;
	VisionFrameController pic;
	PreviewFrame window;
	GarbageCollector garbageCollector;
	NetworkServerController rioResponder;
	Webcam webcam;
	boolean isIpCam = false;

	public Controller(String[] args) {

		long startTime;
		
		try {
			IpCamDeviceRegistry.register("Camera", "http://10.45.85.2:5800/stream.mjpg", IpCamMode.PUSH);
			Webcam.setDriver(new IpCamDriver());
			webcam = Webcam.getDefault();
			isIpCam = true;
			webcam.open();
		} catch (Exception e1) {
			// TODO Auto-generated catch block
//			Dimension d = new Dimension(640, 480);
//			webcam = Webcam.getDefault();
//			webcam.setViewSize(d);
			e1.printStackTrace();
		}
		
//		webcam.open();

		ProcessableColor[] colors = new ProcessableColor[] { ProcessableColor.GREEN, ProcessableColor.YELLOW};

		pic = new VisionFrameController(webcam.getImage(), colors, 10, isIpCam);
		window = new PreviewFrame(pic.getPixels2D());

		rioResponder = new NetworkServerController(4585, pic);
		rioResponder.start();

		long iterations = 0;
		long timeAccumulator = 0;
		long timeTaken = 0;
		while (true) {
			try {

				startTime = System.currentTimeMillis();

				pic = new VisionFrameController(webcam.getImage(), colors, 10, isIpCam);
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
