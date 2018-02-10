package model.vision;

import java.awt.Color;

public class Pixel {
	int red, green, blue;

	public Pixel(int red, int green, int blue) {
		this.red = red;
		this.green = green;
		this.blue = blue;
	}

	public Pixel(int RGB) {
		setRGB(RGB);
	}

	public int getRed() {
		return red;
	}

	public int getGreen() {
		return green;
	}

	public int getBlue() {
		return blue;
	}

	public void setRed(int value) {
		this.red = value;
	}

	public void setGreen(int value) {
		this.green = value;
	}

	public void setBlue(int value) {
		this.blue = value;
	}

	public int getAverage() {
		return (getRed() + getGreen() + getBlue()) / 3;
	}

	public int getYellow() {
		return (getRed() + getGreen()) / 2;
	}

	public int getCyan() {
		return (getBlue() + getGreen()) / 2;
	}

	public int getMagenta() {
		return (getRed() + getBlue()) / 2;
	}

	public int getColor(ProcessableColor color) {
		switch (color) {
		case RED:
			return getRed();
		case GREEN:
			return getGreen();
		case BLUE:
			return getBlue();
		case CYAN:
			return getCyan();
		case MAGENTA:
			return getMagenta();
		case YELLOW:
			return getYellow();
		default:
			return 0;
		}

	}

	public void setColor(Color newColor) {
		// set the red, green, and blue values
		red = newColor.getRed();
		green = newColor.getGreen();
		blue = newColor.getBlue();

	}

	public void setColor(ProcessableColor color) {
		switch (color) {
		case RED:
			setColor(Color.RED);
			break;
		case GREEN:
			setColor(Color.GREEN);
			break;
		case BLUE:
			setColor(Color.BLUE);
			break;
		case CYAN:
			setColor(Color.CYAN);
			break;
		case MAGENTA:
			setColor(Color.MAGENTA);
			break;
		case YELLOW:
			setColor(Color.YELLOW);
			break;
		}
	}
	
	public boolean isBlack() {
		if(getGreen()!=0 || getBlue()!=0 || getRed()!=0)
			return false;
		return true;
	}

	public int getRGB() {
		return (getRed() << 16) + (getGreen() << 8) + (getBlue());
	}

	public void setRGB(int RGB) {
		this.red = (RGB >> 16) & 0x000000FF;
		this.green = (RGB >> 8) & 0x000000FF;
		this.blue = (RGB) & 0x000000FF;
	}

}
