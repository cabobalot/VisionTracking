package model.networking;


import controller.Controller;
import model.vision.hsvIsolate.HSVIsolateController;

import edu.wpi.first.networktables.NetworkTable;
import edu.wpi.first.networktables.NetworkTableEntry;
import edu.wpi.first.networktables.NetworkTableInstance;

public class NetworkTableController extends Thread {
	HSVIsolateController visionFrameController;
	Controller controller;
	
	public NetworkTableController(Controller controller, HSVIsolateController frameController) {
		this.visionFrameController = frameController;
		this.controller = controller;
		
	}
	
	public void setVisionFrameController(Controller controller, HSVIsolateController visionFrameController) {
		this.visionFrameController = visionFrameController;
		this.controller = controller;
	}
	
	public void run() {
		
//		NetworkTableInstance inst = NetworkTableInstance.getDefault();
		NetworkTableInstance inst = NetworkTableInstance.create();
	    NetworkTable table = inst.getTable("vision");
	    NetworkTableEntry nearestCube = table.getEntry("nearestCube");
	    inst.startDSClient();  // recommended if running on DS computer; this gets the robot IP from the DS
		
		while (!this.isInterrupted()) {
			
			int[] point = visionFrameController.getColoredFrame(controller.yellowHue).getLargestObject().getCOM();
			nearestCube.setString(point[0] + ", " + point[1]);
			
		}
	}
}


/*
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
		out.println(point[0] + "," + point[1]);
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
//*/



