import java.awt.Point;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Hammer {
	private static Hammer hammer = new Hammer();
	protected ImageIcon[] hammerIcons;
	protected ImageIcon[] nhIcons;
	protected JLabel hammerLabel;

	protected Hammer() {
		nhIcons = new ImageIcon[] {
				new ImageIcon(ResourceLoader.load("img/pick_hammer.png")),
				new ImageIcon(ResourceLoader.load("img/pick_hammer_2.png"))
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
		if (!gs.getCurrentScreen().equals("game")) return;
		int iconNum = (hammerLabel.getIcon() == hammerIcons[0]) ? 0 : 1;
		MesgSend.send("ghostClick" + " " + gs.getMyNum() + " " + iconNum);
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
		GameScreen gs = GameScreen.getInstance();
		if (!gs.getCurrentScreen().equals("game")) return;
		MesgSend.send("ghostMove" + " " + gs.getMyNum() + " " + (p.x - 50) + " " + (p.y - 120));
	}

	// 画面遷移時やゲーム中に、ハンマー画像が叩いた状態のままになることがあるのを元に戻すメソッド
	public void cleanHammerIcon() {
		hammerLabel.setIcon(hammerIcons[0]);
	}

	public JLabel getHammerLabel() {
		return hammerLabel;
	}
}
