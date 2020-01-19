import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Hammer {
	private static Hammer hammer = new Hammer();
	private ImageIcon hammerIcon, hammerIcon2;
	private JLabel hammerLabel;

	private Hammer() {
		hammerIcon = new ImageIcon(ImageLoader.loadImage("img/pick_hammer.png"));
		hammerIcon2 = new ImageIcon(ImageLoader.loadImage("img/pick_hammer_2.png"));
		hammerLabel = new JLabel(hammerIcon);
	}

	public static Hammer getInstance() {
		return hammer;
	}

	public void changeHammerIcon(GameScreen gs) {
		if (!gs.isMyTurn() && gs.getCurrentScreen().equals("game")) return;
		if (hammerLabel.getIcon() == hammerIcon) {
			hammerLabel.setIcon(hammerIcon2);
		} else if (hammerLabel.getIcon() == hammerIcon2){
			hammerLabel.setIcon(hammerIcon);
		}
	}

	public void setHammerLocation(Point p) {
		hammerLabel.setLocation(p.x - 50, p.y - 120);
	}

	public void cleanHammerIcon() {
		hammerLabel.setIcon(hammerIcon);
	}

	public JLabel getHammerLabel() {
		return hammerLabel;
	}
}
