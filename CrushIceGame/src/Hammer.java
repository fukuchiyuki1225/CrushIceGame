import java.awt.Point;

import javax.swing.Icon;
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

	public void changeHammerIcon() {
		GameScreen gs = GameScreen.getInstance();
		if (!gs.isMyTurn() && gs.getCurrentScreen().equals("game")) return;
		Icon icon = (hammerLabel.getIcon() == hammerIcon) ? hammerIcon2 : hammerIcon;
		hammerLabel.setIcon(icon);
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
