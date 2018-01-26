package view;

import javax.swing.JFrame;
import model.Pixel;
import model.VisionFrameController;

public class PreviewFrame extends JFrame {

	private PreviewPanel panel;

	public PreviewFrame(Pixel[][] pixels) {
		super();

		this.panel = new PreviewPanel(pixels);

		setupFrame();
	}

	public void setupFrame() {
		this.setContentPane(panel);
		this.setTitle("Preview");
		this.setSize(900, 500);
		this.setResizable(false);
		this.setDefaultCloseOperation(this.EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}
}
