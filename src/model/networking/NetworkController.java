package model.networking;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import controller.Controller;
import edu.wpi.first.networktables.*;
import model.vision.ProcessableColor;
import model.vision.VisionFrameController;

public class NetworkController extends Thread {
	Socket client;
	DataOutputStream out;
	InputStream in;
	VisionFrameController controller;

	String ipAddress;
	int port;

	public NetworkController(String ipAddress, int port, VisionFrameController controller) {
		boolean isConnected = false;
		while (!isConnected) {
			try {
				client = new Socket(ipAddress, port);
				in = client.getInputStream();
				out = new DataOutputStream(client.getOutputStream());
				this.controller = controller;
				this.ipAddress = ipAddress;
				this.port = port;

				isConnected = true;
			} catch (UnknownHostException e) {
				System.out.println("unknown host: " + ipAddress);
				try {
					TimeUnit.MILLISECONDS.sleep(250);
				} catch (InterruptedException e1) {
				}
				// e.printStackTrace();
				System.out.println("Unknown host: " + ipAddress + ":" + port);
			} catch (ConnectException e) {
				System.out.println("Connection failed to " + ipAddress + ":" + port);
				try {
					TimeUnit.MILLISECONDS.sleep(250);
				} catch (InterruptedException e1) {
				}

			} catch (IOException e) {

			}
		}

	}

	public void run() {
		// while thread is running
		while (!this.isInterrupted()) {
			// while has connection to the client
			while (!client.isClosed()) {
				try {

					// read message from the server
					boolean isDoneReading = false;
					String message = "";
					int currentByte = 0;
					while (!isDoneReading) {
						currentByte = in.read();
						if (currentByte != -1) {
							message += (char) currentByte;
						} else {
							isDoneReading = true;
						}
					}

					// point object to send to the server if required
					int[] point;

					// process server request and respond with an accurate response
					switch (Character.getNumericValue(message.charAt(0))) {
					case (Requests.COM):
						point = controller.getCOM();
						out.writeUTF(point[0] + "," + point[1]);
						break;
					case (Requests.HEIGHT):
						out.writeUTF(Integer.toString(controller.getHeight()));
						break;
					case (Requests.WIDTH):
						out.writeUTF(Integer.toString(controller.getWidth()));
						break;
					case (Requests.NEAREST_CUBE):
						point = controller.getColoredFrame(ProcessableColor.YELLOW).getCOM();
						out.writeUTF(point[0] + "," + point[1]);
						break;
					default:
						System.out.println(
								"Invalid request from Server: " + Character.getNumericValue(message.charAt(0)));
						break;
					}

				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("lost connection to Server");
				}
			}
			try {
				client = new Socket(ipAddress, port);
				in = client.getInputStream();
				out = new DataOutputStream(client.getOutputStream());
				System.out.println("Successfully reconnected");
			} catch (IOException e) {
				System.out.println("Connection Failed, retrying");
				try {
					TimeUnit.MILLISECONDS.sleep(250);
				} catch (InterruptedException e1) {
				}
			}
		}
	}

}
