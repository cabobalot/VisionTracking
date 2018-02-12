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
	public Camera(int width, int height) {
		Dimension d = new Dimension(width, height);
		webcam = Webcam.getDefault();
		webcam.setViewSize(d);
		isIpCamera = false;
		webcam.open();
	}
	public Camera(String url) throws MalformedURLException {
		IpCamDeviceRegistry.register("Camera", url, IpCamMode.PUSH);
		Webcam.setDriver(new IpCamDriver());
		webcam = Webcam.getDefault();
		isIpCamera = true;
		webcam.open();
	}
	
	public BufferedImage getImage() {
		return webcam.getImage();
	}
	public boolean isIpCamera() {
		return isIpCamera;
	}
}





