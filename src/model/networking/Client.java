package model.networking;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

import controller.Controller;
import model.vision.hsvIsolate.HSVIsolateController;

public class Client extends Thread {
	
	private PrintWriter out;
	private BufferedReader in;
	private HSVIsolateController visionFrameController;
	private Socket socket;
	private Controller controller;
	
	public Client(Controller controller, HSVIsolateController visionFrameController, Socket socket) {
		super();
		try {
			this.socket = socket;
			this.visionFrameController = visionFrameController;
			this.controller = controller;
			out = new PrintWriter(socket.getOutputStream());
			in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			socket.setSoTimeout(100);
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
						out.println(Integer.toString(visionFrameController.getHeight()));
						out.flush();
						break;
					case (Requests.WIDTH):
						out.println(Integer.toString(visionFrameController.getWidth()));
						out.flush();
						break;
					case (Requests.NEAREST_CUBE_DISTANCE):
						double distance = visionFrameController.getColoredFrame(controller.yellowHue).getLargestObject().getDistanceFeet(13, 10.5);
						if (distance < 50)
							out.println(distance);
						else
							out.println("0");
						out.flush();
						break;
					case (Requests.NEAREST_CUBE):
						point = visionFrameController.getColoredFrame(controller.yellowHue).getLargestObject().getCOM();
						String cubeNum = Integer.toString(visionFrameController.getColoredFrame(controller.yellowHue).getObjects().size());
						out.println(point[0] + "," + point[1] + ";" + cubeNum);
						out.flush();
						break;
					case (Requests.NEAREST_TAPE):
						point = visionFrameController.getColoredFrame(controller.greenHue).getLargestObject().getCOM();
						out.println(point[0] + "," + point[1]);
						out.flush();
						break;
					case (Requests.AMOUNT_CUBES):
						out.println(Integer.toString(visionFrameController.getColoredFrame(controller.yellowHue).getObjects().size()));
						out.flush();
						break;
					case (Requests.AMOUNT_TAPE):
						out.println(Integer.toString(visionFrameController.getColoredFrame(controller.greenHue).getObjects().size()));
						out.flush();
						break;
					case (Requests.AVERAGE_BRIGHTNESS):
						//maps from 0 to 100
						out.println(Integer.toString((int) (visionFrameController.getAverageBrightness() * 100)));
						out.flush();
						break;
					case (Requests.ANGLE_OFF_CENTER_CUBE):
						double[] angles = visionFrameController.getColoredFrame(controller.yellowHue).getLargestObject().getAngleOffCenter(90);
						out.println(angles[0] + "," + angles[1]);
						out.flush();
						break;
					default:
						break;
				}
			}
			
		} catch (IOException e) {
			e.printStackTrace();
			// System.out.println("lost connection to Client");
			try {
				socket.close();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} catch (NumberFormatException e) {
			// System.out.println("Invalid Request: " + message);
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
