import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Penguin extends JLabel {
	private JLabel penguin;
	private GameScreen gs;

	public Penguin(GameScreen gs) {
		penguin = new JLabel(new ImageIcon(ImageLoader.readImage("img/penguin.png")));
		this.gs = gs;
		gs.addComponent(penguin, 500, 350, 375, 100, 100);
	}

	public void penguinFall(Ices ices, Icon brokenIce) {
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
						// penguin.setVisible(false);
						gs.send("fall");
						return;
					}
				}
			}
		}
	}

	public void penguinMove(double x, double y) {
		penguin.setLocation((int)Math.ceil(x), (int)Math.ceil(y));
		gs.send("move" + " " + (int)Math.ceil(x) + " " + (int)Math.ceil(y));
	}

	public void penguinMove(int x, int y) {
		if (gs.isMyTurn()) return;
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
