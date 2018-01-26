package view;

import javax.swing.JFrame;
import model.Pixel;

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
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
		
	}
	
	public void updatePicture(Pixel[][] pixels) {
		panel.setImage(pixels);
	}
}
