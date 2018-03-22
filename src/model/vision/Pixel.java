package model.vision;

import java.awt.Color;

public class Pixel {
	float hue, saturation, value;
	
	public Pixel(float hue, float saturation, float value) {
		this.hue = hue;
		this.saturation = saturation;
		this.value = value;
	}
	public Pixel(float[] HSV){
		this.hue = HSV[0];
		this.saturation = HSV[1];
		this.value = HSV[2];
	}
	
	public Pixel(int RGB) {
		setRGB(RGB);
	}
	
	public Pixel(Color newColor) {
		setColor(newColor);
	}
	
	public float getHue() {
		return hue;
	}
	
	public float getSaturation() {
		return saturation;
	}
	
	public float getValue() {
		return value;
	}
	
	public void setHue(float value) {
		this.hue = value;
	}
	
	public void setSaturation(float value) {
		this.saturation = value;
	}
	
	public void setValue(float value) {
		this.value = value;
	}
	
	public void setColor(Color newColor) {
		// set the red, green, and blue values
		float[] values = Color.RGBtoHSB(newColor.getRed(), newColor.getGreen(), newColor.getBlue(), null);
		hue = values[0];
		saturation = values[1];
		value = values[2];
		values = null;
		
	}
	
	public boolean isBlack() {
		if (value != 0)
			return false;
		return true;
	}
	
	public int getRGB() {
		return Color.HSBtoRGB(hue, saturation, value);
	}
	
	public void setRGB(int RGB) {
		int r = (RGB >> 16) & 0x000000FF;
		int g = (RGB >> 8) & 0x000000FF;
		int b = (RGB >> 0) & 0x000000FF;
		
		float hue, saturation, brightness;
		float cmax = (r > g) ? r : g;
		if (b > cmax)
			cmax = b;
		float cmin = (r < g) ? r : g;
		if (b < cmin)
			cmin = b;
		
		brightness = (cmax) / 255.0f;
		if (cmax != 0)
			saturation = ((cmax - cmin)) / (cmax);
		else
			saturation = .5f;
		if (saturation == 0)
			hue = 0;
		else {
			float redc = ((cmax - r)) / ((cmax - cmin));
			float greenc = ((cmax - g)) / ((cmax - cmin));
			float bluec = ((cmax - b)) / ((cmax - cmin));
			if (r == cmax)
				hue = bluec - greenc;
			else if (g == cmax)
				hue = 2.0f + redc - bluec;
			else
				hue = 4.0f + greenc - redc;
			hue = hue / 6.0f;
			if (hue < 0)
				hue = hue + 1.0f;
		}
		
		setHue(hue);
		setSaturation(saturation);
		setValue(brightness);
	}
	
	public float[] getHSV() {
		return new float[]{hue, saturation, value};
	}
	
	//	public void setRGB(int RGB) {
	//		float[] values = Color.RGBtoHSB((RGB >> 16) & 0x000000FF, (RGB >> 8) & 0x000000FF, (RGB >> 0) & 0x000000FF, null);
	//		setHue(values[0]);
	//		setSaturation(values[1]);
	//		setValue(values[2]);		
	//	}
	
}
