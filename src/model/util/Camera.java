package model.util;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.ds.ipcam.IpCamDeviceRegistry;
import com.github.sarxos.webcam.ds.ipcam.IpCamDriver;
import com.github.sarxos.webcam.ds.ipcam.IpCamMode;
import java.awt.image.ColorConvertOp;

public class Camera {
	Webcam webcam;
	boolean isIpCamera;
	BufferedImage image;
	String url;

	public Camera(int width, int height) {
		this.url = null;
		image = new BufferedImage(1, 1, 1);
		Dimension d = new Dimension(width, height);
		webcam = Webcam.getDefault();
		webcam.setViewSize(d);
		isIpCamera = false;
		webcam.open();
	}

	public Camera(String url) throws MalformedURLException {
		this.url = url;
		image = new BufferedImage(1, 1, 1);
		IpCamDeviceRegistry.register("Camera", url, IpCamMode.PUSH);
		Webcam.setDriver(new IpCamDriver());
		webcam = Webcam.getDefault();
		isIpCamera = true;
		webcam.open();
	}

	public BufferedImage getImage() {
		try {
			image = webcam.getImage();
		} catch (Exception e) {
			try {
				IpCamDeviceRegistry.register("Camera", url, IpCamMode.PUSH);
			} catch (MalformedURLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			image = webcam.getImage();
		}
		return image;
	}

	public boolean isIpCamera() {
		return isIpCamera;
	}
}
