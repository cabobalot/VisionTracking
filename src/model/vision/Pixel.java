package model.vision;

import java.awt.Color;

public class Pixel {
	float hue, saturation, value;
	
	public Pixel(float hue, float saturation, float value) {
		this.hue = hue;
		this.saturation = saturation;
		this.value = value;
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
		float[] values = Color.RGBtoHSB((RGB >> 16) & 0x000000FF, (RGB >> 8) & 0x000000FF, (RGB >> 0) & 0x000000FF, null);
		setHue(values[0]);
		setSaturation(values[1]);
		setValue(values[2]);
		values = null;
		
	}
	
}
