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

	public Controller(String[] args) {

		long startTime;
		
		Webcam.setDriver(new IpCamDriver());
		try {
			IpCamDeviceRegistry.register("Camera", "http://10.224.41.80:8080/video", IpCamMode.PULL);
		} catch (MalformedURLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		webcam = Webcam.getDefault();
		Dimension d = new Dimension(640, 480);
		webcam.setViewSize(d);
		webcam.open();

		ProcessableColor[] colors = new ProcessableColor[] { ProcessableColor.GREEN, ProcessableColor.YELLOW};

		pic = new VisionFrameController(webcam.getImage(), colors, 10);
		window = new PreviewFrame(pic.getPixels2D());

		rioResponder = new NetworkServerController(4585, pic);
		rioResponder.start();

		long iterations = 0;
		long timeAccumulator = 0;
		long timeTaken = 0;
		while (true) {
			try {

				startTime = System.currentTimeMillis();

				pic = new VisionFrameController(webcam.getImage(), colors, 10);
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
