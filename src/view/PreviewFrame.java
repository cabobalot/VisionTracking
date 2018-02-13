package view;

import javax.swing.JFrame;

import controller.Controller;
import model.vision.Pixel;

public class PreviewFrame extends JFrame {
	private PreviewPanel panel;

	public PreviewFrame(Pixel[][] pixels, Controller controller) {
		super();

		this.panel = new PreviewPanel(pixels, controller);

		setupFrame();
	}
	
	private void setupFrame() {
		this.setContentPane(panel);
		this.setTitle("Preview");
		this.setSize(1280, 720);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}
	
	public void updatePicture(Pixel[][] pixels) {
		panel.setImage(pixels);
	}
}
