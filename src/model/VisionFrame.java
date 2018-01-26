package model;

import java.awt.*;
//import java.awt.font.*;
//import java.awt.geom.*;
import java.awt.image.BufferedImage;
//import java.text.*;
//import java.util.*;
//import java.util.List; // resolves problem with java.awt.List and java.util.List
import java.util.ArrayList;

public class VisionFrame extends Frame {
	
	public ArrayList<VisionObject> objects = new ArrayList();
	///////////////////// constructors //////////////////////////////////

	public VisionFrame(String fileName) {
		super(fileName);
	}

	public VisionFrame(BufferedImage image) {
		super(image);
	}
	public VisionFrame(Pixel[][] pixels) {
		super(pixels);
	}

	////////////////////// methods ///////////////////////////////////////

	public ArrayList<VisionObject> getVisionObjects(){
		return objects;
	}
	
	public VisionObject getVisionObject(int index) {
		return objects.get(index);
	}
	

}
