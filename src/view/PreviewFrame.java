package view;

import javax.swing.JFrame;

import model.vision.Pixel;

public class PreviewFrame extends JFrame {
	private PreviewPanel panel;

	public PreviewFrame(Pixel[][] pixels) {
		super();

		this.panel = new PreviewPanel(pixels);

		setupFrame();
	}
	
	private void setupFrame() {
		this.setContentPane(panel);
		this.setTitle("Preview");
		this.setSize(900, 500);
		this.setResizable(true);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}
	
	public void updatePicture(Pixel[][] pixels) {
		panel.setImage(pixels);
	}
}
