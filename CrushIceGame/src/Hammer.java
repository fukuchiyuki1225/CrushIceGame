import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Hammer {
	private ImageIcon hammerIcon, hammerIcon2;
	private JLabel hammer;

	public Hammer() {
		hammerIcon = new ImageIcon("img/pick_hammer.png");
		hammerIcon2 = new ImageIcon("img/pick_hammer_2.png");
		hammer = new JLabel(hammerIcon);
		Game.j.setLayer(hammer, 1000);
		Game.j.add(hammer);
		hammer.setBounds(0, 0, 150, 170);
	}

	public void changeHammerIcon() {
		if (hammer.getIcon() == hammerIcon) {
			hammer.setIcon(hammerIcon2);
		} else {
			hammer.setIcon(hammerIcon);
		}
	}

	public void setHammerLocation(Point p) {
		hammer.setLocation(p.x - 50, p.y - 70);
	}
}
