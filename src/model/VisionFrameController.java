package model;

public class VisionFrameController extends Frame{
	Pixel[][] pixels;
	ProcessableColor[] colors;
	VisionFrame[] colorFrames;

	public VisionFrameController(String file, ProcessableColor[] colors) {
		super(file);
		this.pixels = this.getPixels2D();
		this.colors = colors;
		colorFrames = new VisionFrame[colors.length];
		
		process();
		concatenateColors();

	}

	private void process() {
		for (int i = 0; i<colors.length; i++) {
			colorFrames[i].colorIsolate(colors[i], .8, .7);
		}
	}
	private void concatenateColors() {
		
	}
}
