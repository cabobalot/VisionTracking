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

public class Client extends Thread{

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
			socket.setSoTimeout(250);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public boolean isClosed() {
		return socket.isClosed();
	}
	
	public void run() {
		respond();
	}

	public void respond() {
		String message = "";
		try {

			// read message from the client
			message = in.readLine();
			if (message != null) {
				System.out.println("Request from " + socket.getInetAddress() + ": " + message);

				// point object to send to the client if required
				int[] point;

				// process client request and respond with an accurate response
				switch (Integer.parseInt(message)) {
				case (Requests.HEIGHT):
					out.println(Integer.toString(controller.getHeight()));
					out.flush();
					break;
				case (Requests.WIDTH):
					out.println(Integer.toString(controller.getWidth()));
					out.flush();
					break;
				case (Requests.COM):
					point = controller.getCOM();
					out.println(point[0] + "," + point[1]);
					out.flush();
					break;
				case (Requests.NEAREST_CUBE):
					point = controller.getColoredFrame(ProcessableColor.YELLOW).getCOM();
					out.println(point[0] + "," + point[1]);
					out.flush();
					break;
				case (Requests.NEAREST_TAPE):
					point = controller.getColoredFrame(ProcessableColor.GREEN).getCOM();
					out.println(point[0] + "," + point[1]);
					out.flush();
					break;
				default:
					break;
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
//			System.out.println("lost connection to Client");
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (NumberFormatException e) {
//			System.out.println("Invalid Request: " + message);
		}

		// close socket
		try {
			socket.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
