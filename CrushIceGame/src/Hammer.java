import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Hammer {
	private ImageIcon hammerIcon, hammerIcon2;
	private JLabel hammer;

	public Hammer(GameScreen gs) {
		hammerIcon = new ImageIcon(ImageLoader.readImage("img/pick_hammer.png"));
		hammerIcon2 = new ImageIcon(ImageLoader.readImage("img/pick_hammer_2.png"));
		hammer = new JLabel(hammerIcon);
		gs.addComponent(hammer, 1000);
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
		hammer.setLocation(p.x - 50, p.y - 120);
	}
}
