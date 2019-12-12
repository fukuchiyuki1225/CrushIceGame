import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Penguin extends JLabel {
	private JLabel penguin;

	public Penguin() {
		penguin = new JLabel(new ImageIcon("img/penguin.png"));
		Game.j.setLayer(penguin, 500);
		Game.j.add(penguin);
		penguin.setBounds(350, 375, 100, 100);
		Timer timer = new Timer(10, new PenguinMove(this));
		timer.start();
	}

	public double lerp(double x0, double y0, double x1, double y1, double x) {
		return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
	}

	public void penguinFall() {
		System.out.println("fall");
		penguin.setVisible(false);
	}

	public int getPenguinX() {
		return penguin.getX();
	}

	public int getPenguinY() {
		return penguin.getY();
	}

	// checkFallà⁄êA

	public static class PenguinMove implements ActionListener {
		private Penguin penguin;
		private double startTime, diff, time;
		private double x0, x1, y0, y1;
		public PenguinMove(Penguin penguin) {
			this.penguin = penguin;
			startTime = System.currentTimeMillis();
			x0 = 50;
			x1 = 650;
			y0 = 143;
			y1 = 659;
		}

		public void actionPerformed(ActionEvent e) {
			diff = System.currentTimeMillis() - startTime;
			time = 2000;
			if (x1 * (diff / time) < x1) {
				penguin.penguin.setLocation((int)(Math.ceil(x1 * (diff / time))), (int)(Math.ceil(penguin.lerp(x0, y0, x1, y1, x1 * (diff / time)))));
			}
		}
	}
}
