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
	private RollingTimer requestTimer = new RollingTimer(.1);
	private RollingTimer frameTimer = new RollingTimer(.05);
	
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
		while (true) {
			frameTimer.startTimer();
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
			frameTimer.stopTimer();
			try {
				if ((int)requestTimer.getAverage() - (frameTimer.getLastTimeTaken()) > 0) {
					TimeUnit.MILLISECONDS.sleep((int)requestTimer.getAverage() - (frameTimer.getLastTimeTaken()));
				}
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public BufferedImage getImage() {
		try {
		requestTimer.stopTimer();
		} catch(IllegalStateException e) {
			
		}
		requestTimer.startTimer();
		return image;
	}
	
	public int getMaxFramerate() {
		return (int)frameTimer.getOpsPerSecond();
	}
	public int getFrameRate() {
		return (int)requestTimer.getOpsPerSecond();
	}
	
	public boolean isIpCamera() {
		return isIpCamera;
	}
}
