package model.util;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.awt.image.ColorConvertOp;
import java.net.MalformedURLException;
import java.util.concurrent.TimeUnit;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;

public class Camera extends Thread {
	private Webcam webcam;
	private boolean isIpCamera;
	private BufferedImage image;
	private String url;
	private int historicMillisBetweenFrameRequests = 0;
	private long lastTime = System.currentTimeMillis();
	private int maxFrameRate = 0;
	
	public Camera(int width, int height) {
		this.url = null;
		image = new BufferedImage(1, 1, 1);
		Dimension d = new Dimension(width, height);
		webcam = Webcam.getDefault();
		webcam.setViewSize(d);
		isIpCamera = false;
		webcam.open();
		image = webcam.getImage();
	}
	
	public Camera(String url) throws MalformedURLException {
		this.url = url;
		image = new BufferedImage(1, 1, 1);
		IpCamDeviceRegistry.register("Camera", url, IpCamMode.PUSH);
		Webcam.setDriver(new IpCamDriver());
		webcam = Webcam.getDefault();
		isIpCamera = true;
		webcam.open();
		image = webcam.getImage();
		//		if (!webcam.isImageNew() && isIpCamera) {
		//			try {
		//			IpCamDeviceRegistry.unregisterAll();
		//			IpCamDeviceRegistry.register("Camera", url, IpCamMode.PUSH);
		//		} catch (Exception e1) {
		//			e1.printStackTrace();
		//		}
		//		image = webcam.getImage();
		//	}
	}
	
	public void run() {
		long startTime;
		while (true) {
			startTime = System.currentTimeMillis();
			image = webcam.getImage();
			
			if (isIpCamera) {
				if (!webcam.isImageNew()) {
					try {
						IpCamDeviceRegistry.unregisterAll();
						IpCamDeviceRegistry.register("Camera", url, IpCamMode.PUSH);
					} catch (Exception e1) {
						e1.printStackTrace();
					}
					image = webcam.getImage();
				}
				ColorConvertOp convert = new ColorConvertOp(null);
				BufferedImage BGRImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_BGR);
				convert.filter(image, BGRImage);
				image = BGRImage;
			}
			try {
				if (historicMillisBetweenFrameRequests - (System.currentTimeMillis() - startTime) > 0) {
					TimeUnit.MILLISECONDS.sleep(historicMillisBetweenFrameRequests - (System.currentTimeMillis() - startTime));
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public BufferedImage getImage() {
		historicMillisBetweenFrameRequests = (int) (((System.currentTimeMillis() - lastTime) * .05) + (historicMillisBetweenFrameRequests * .95));
		lastTime = System.currentTimeMillis();
		return image;
	}
	
	public int getMaxFramerate() {
		return 1000 / historicMillisBetweenFrameRequests;
	}
	
	public boolean isIpCamera() {
		return isIpCamera;
	}
}
