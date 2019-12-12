import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Penguin extends JLabel {
	private JLabel penguin;
	private Ices ices;

	public Penguin(Ices ices) {
		penguin = new JLabel(new ImageIcon("img/penguin.png"));
		this.ices = ices;
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
		loop : for (JButton[] icesArray : ices.getIces()) {
			for (JButton ice : icesArray) {
				if (ice.getIcon() == ices.getBrokenIceIcon()) {
					int x1, x2, y1, y2;
					if (penguin.getX() < ice.getX()) {
						x1 = ice.getX();
						x2 = penguin.getX();
					} else {
						x1 = penguin.getX();
						x2 = ice.getX();
					}
					if (penguin.getY() < ice.getY()) {
						y1 = ice.getY();
						y2 = penguin.getY();
					} else {
						y1 = penguin.getY();
						y2 = ice.getY();
					}
					if (Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) < 40) {
						System.out.println("fall");
						penguin.setVisible(false);
						break loop;
					}
				}
			}
		}
	}

	public static class PenguinMove implements ActionListener {
		private Penguin penguin;
		private double startTime, diff, time;
		private double x0, x1, y0, y1;
		private boolean inertiaFlag;
		private int inertia;
		public PenguinMove(Penguin penguin) {
			this.penguin = penguin;
			startTime = System.currentTimeMillis();
			x0 = 50;
			x1 = 650 - 100;
			y0 = 143;
			y1 = 659 - 100;
			inertiaFlag = true;
			inertia = 100;
		}

		public void actionPerformed(ActionEvent e) {
			diff = System.currentTimeMillis() - startTime;
			time = 5000;
			if (x1 * (diff / time) < x1) {
				penguin.penguin.setLocation((int)(Math.ceil(x1 * (diff / time))), (int)(Math.ceil(penguin.lerp(x0, y0, x1, y1, x1 * (diff / time)))));
				penguin.penguinFall();
			}

			if (x1 * (diff / time) >= x1 && inertiaFlag) {
				penguin.penguin.setLocation(penguin.penguin.getX() + 1, penguin.penguin.getY() + 1);
				if (inertia == 0) {
					inertiaFlag = false;
				} else {
					inertia--;
				}
				penguin.penguinFall();
			}
		}
	}
}
