import java.awt.Point;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Hammer {
	private static Hammer hammer = new Hammer();
	private ImageIcon[] hammerIcons, nhIcons;
	private JLabel hammerLabel;

	private Hammer() {
		ImageLoader il = ImageLoader.getInstance();
		nhIcons = new ImageIcon[] {
				new ImageIcon(il.load("img/pick_hammer.png")),
				new ImageIcon(il.load("img/pick_hammer_2.png"))
		};
		hammerLabel = new JLabel();
		changeHammer();
	}

	public static Hammer getInstance() {
		return hammer;
	}

	public void changeHammerIcon() {
		GameScreen gs = GameScreen.getInstance();
		if (!gs.isMyTurn() && gs.getCurrentScreen().equals("game")) return;
		Icon icon = (hammerLabel.getIcon() == hammerIcons[0]) ? hammerIcons[1] : hammerIcons[0];
		hammerLabel.setIcon(icon);
	}

	public void changeHammer() {
		hammerIcons = nhIcons;
		hammerLabel.setIcon(hammerIcons[0]);
	}

	public void changeHammer(ImageIcon[] ghIcons) {
		hammerIcons = ghIcons;
		hammerLabel.setIcon(hammerIcons[0]);
	}

	public void setHammerLocation(Point p) {
		hammerLabel.setLocation(p.x - 50, p.y - 120);
	}

	// 画面遷移時やゲーム中に、ハンマー画像が叩いた状態のままになることがあるのを元に戻すメソッド
	public void cleanHammerIcon() {
		hammerLabel.setIcon(hammerIcons[0]);
	}

	public JLabel getHammerLabel() {
		return hammerLabel;
	}
}
