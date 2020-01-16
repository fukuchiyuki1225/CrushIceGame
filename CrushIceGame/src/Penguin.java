import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Penguin extends JLabel {
	private JLabel penguin;
	private ImageIcon[] penguinIcon;
	private boolean fallFlag;
	private GameScreen gs;

	public Penguin(GameScreen gs) {
		penguinIcon = new ImageIcon[] {
				new ImageIcon(ImageLoader.loadImage("img/penguin.png")),
				new ImageIcon(ImageLoader.loadImage("img/penguin_2.png"))
		};
		penguin = new JLabel(penguinIcon[0]);
		fallFlag = false;
		this.gs = gs;
		gs.addComponent(penguin, 500, 350, 375, 100, 100);
	}

	public void penguinFall(Ices ices, Icon brokenIce) {
		if (fallFlag) return;
		if (penguin.getX() < 10 || penguin.getX() > 700 || penguin.getY() < 90 || penguin.getY() > 700) {
			penguin.setVisible(false);
			return;
		}
		for (JButton[] icesArray : ices.getIces()) {
			for (JButton ice : icesArray) {
				if (ice.getIcon() == brokenIce) {
					int x0, x1, y0, y1;
					x0 = penguin.getX() < ice.getX() ? ice.getX() : penguin.getX();
					x1 = penguin.getX() < ice.getX() ? penguin.getX() : ice.getX();
					y0 = penguin.getY() < ice.getY() ? ice.getY() : penguin.getY();
					y1 = penguin.getY() < ice.getY() ? penguin.getY() : ice.getY();
					if (Calculation.calcDistance(x0, y0, x1, y1) < 40) {
						gs.send("fall");
						fallFlag = true;
						return;
					}
				}
			}
		}
	}

	public void setPenguinIcon() {
		if (penguin.getIcon() == penguinIcon[0]) {
			penguin.setIcon(penguinIcon[1]);
		} else {
			penguin.setIcon(penguinIcon[0]);
		}
	}

	public void penguinMove(int x, int y) {
		penguin.setLocation(x, y);
	}

	public int getPenguinX() {
		return penguin.getX();
	}

	public int getPenguinY() {
		return penguin.getY();
	}

	public JLabel getPenguin() {
		return penguin;
	}
}
