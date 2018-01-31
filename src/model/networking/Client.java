package model.networking;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import model.vision.ProcessableColor;
import model.vision.VisionFrameController;

public class Client {

	private PrintWriter out;
	private BufferedReader in;
	private VisionFrameController controller;
	private Socket socket;

	public Client(VisionFrameController controller, Socket socket) {
		super();
		try {
			this.socket = socket;
			this.controller = controller;
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}
	
	public boolean isClosed() {
		return socket.isClosed();
	}

	public void respond() {
		try {

			// read message from the client
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

			// point object to send to the client if required
			int[] point;

			// process client request and respond with an accurate response
			switch (Character.getNumericValue(message.charAt(0))) {
			case (Requests.COM):
				point = controller.getCOM();
				out.write(point[0] + "," + point[1]);
				break;
			case (Requests.HEIGHT):
				out.write(Integer.toString(controller.getHeight()));
				break;
			case (Requests.WIDTH):
				out.write(Integer.toString(controller.getWidth()));
				break;
			case (Requests.NEAREST_CUBE):
				point = controller.getColoredFrame(ProcessableColor.YELLOW).getCOM();
				out.write(point[0] + "," + point[1]);
				break;
			default:
				System.out.println("Invalid request from Server: " + Character.getNumericValue(message.charAt(0)));
				break;
			}

		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("lost connection to Client");
		}
	}

}
