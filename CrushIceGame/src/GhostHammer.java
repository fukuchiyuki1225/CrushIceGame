import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class GhostHammer extends Hammer {
	private static GhostHammer hammer = new GhostHammer();

	private GhostHammer() {
		ResourceLoader rl = ResourceLoader.getInstance();
		nhIcons = new ImageIcon[] {
				new ImageIcon(rl.load("img/ghost_hammer.png")),
				new ImageIcon(rl.load("img/ghost_hammer_2.png"))
		};
		hammerLabel = new JLabel();
		changeHammer();
	}

	public static GhostHammer getInstance() {
		return hammer;
	}

	public void changeHammerIcon(int myNum, int iconNum) {
		if (myNum == GameScreen.getInstance().getMyNum()) return;
		hammerLabel.setIcon(hammerIcons[iconNum]);
	}

	public void setHammerLocation(int myNum, int x, int y) {
		if (myNum == GameScreen.getInstance().getMyNum()) return;
		hammerLabel.setLocation(x, y);
	}
}