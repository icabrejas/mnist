package ui;

import java.awt.BorderLayout;

import javax.swing.JFrame;

public class AppUI {

	public static void main(String[] args) {
		
		NumberCanvas numberCanvas = new NumberCanvas();
		numberCanvas.setSize(100, 100);

		JFrame mainFrame = new JFrame();
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLayout(new BorderLayout());
		mainFrame.add(numberCanvas, BorderLayout.CENTER);
		mainFrame.setExtendedState(JFrame.MAXIMIZED_BOTH);
		mainFrame.setVisible(true);
		
	}

}
