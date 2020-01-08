package frame;

import java.awt.Dimension;
import javax.swing.JFrame;
import panel.GamePanel;

public class MainFrame extends JFrame {

	private GamePanel gamePanel;
	
	public MainFrame() {
		gamePanel = new GamePanel();
		addMouseListener(gamePanel);
		addMouseMotionListener(gamePanel);
		addKeyListener(gamePanel);
		add(gamePanel);
		setTitle("AIG_PROJECT");
		gamePanel.setPreferredSize(new Dimension(1000, 600));
		pack();
		setDefaultCloseOperation(3);
		setLocationRelativeTo(null);
		setVisible(true);
	}

	public static void main(String[] args) {
		new MainFrame();
	}

}
