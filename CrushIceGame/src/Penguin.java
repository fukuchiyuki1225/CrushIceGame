import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Penguin {
	private ImageIcon[] penguinIcons;
	private JLabel penguinLabel;
	private boolean fallFlag;

	public Penguin() {
		ImageLoader il = ImageLoader.getInstance();
		penguinIcons = new ImageIcon[] {
				new ImageIcon(il.load("img/penguin.png")),
				new ImageIcon(il.load("img/penguin_2.png")),
				new ImageIcon(il.load("img/penguin_3.png"))
		};
		penguinLabel = new JLabel(penguinIcons[0]);
		fallFlag = false;
		GameScreen.getInstance().addComponent(penguinLabel, 500, 350, 375, 100, 100);
	}

	public void penguinFall(Ices ices, Icon brokenIce) {
		if (fallFlag) return;
		if (penguinLabel.getX() < 10 || penguinLabel.getX() > 700 || penguinLabel.getY() < 90 || penguinLabel.getY() > 700) {
			penguinLabel.setVisible(false);
			return;
		}
		for (JButton[] icesArray : ices.getIces()) {
			for (JButton ice : icesArray) {
				if (ice.getIcon() == brokenIce) {
					int x0, x1, y0, y1;
					x0 = getPenguinX()< ice.getX() ? ice.getX() : getPenguinX();
					x1 = getPenguinX() < ice.getX() ? getPenguinX() : ice.getX();
					y0 = getPenguinY() < ice.getY() ? ice.getY() : getPenguinY();
					y1 = getPenguinY() < ice.getY() ? getPenguinY() : ice.getY();
					if (Calculation.calcDistance(x0, y0, x1, y1) < 40) {
						MesgSend.getInstance().send("fall");
						fallFlag = true;
						return;
					}
				}
			}
		}
	}

	public void changePenguinIcon(int state) {
		penguinLabel.setIcon(penguinIcons[state]);
	}

	public void penguinMove(int x, int y) {
		penguinLabel.setLocation(x, y);
	}

	public int getPenguinX() {
		return penguinLabel.getX();
	}

	public int getPenguinY() {
		return penguinLabel.getY();
	}

	public JLabel getPenguinLabel() {
		return penguinLabel;
	}
}
