import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Penguin extends JLabel {
	private JLabel penguin;

	public Penguin(GameScreen gs) {
		penguin = new JLabel(new ImageIcon("img/penguin.png"));
		GameScreen.j.setLayer(penguin, 500);
		GameScreen.j.add(penguin);
		penguin.setBounds(350, 375, 100, 100);
	}

	public double lerp(double x0, double y0, double x1, double y1, double x) {
		return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
	}

	public int calcDistance(double x0, double y0, double x1, double y1) {
		return (int) Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));
	}

	public void penguinFall(Ices ices) {
		if (penguin.getX() < 10 || penguin.getX() > 700 || penguin.getY() < 90 || penguin.getY() > 700) {
			penguin.setVisible(false);
		}
		loop : for (JButton[] icesArray : ices.getIces()) {
			for (JButton ice : icesArray) {
				if (ice.getIcon() == ices.getBrokenIceIcon()) {
					int x0, x1, y0, y1;
					if (penguin.getX() < ice.getX()) {
						x0 = ice.getX();
						x1 = penguin.getX();
					} else {
						x0 = penguin.getX();
						x1 = ice.getX();
					}
					if (penguin.getY() < ice.getY()) {
						y0 = ice.getY();
						y1 = penguin.getY();
					} else {
						y0 = penguin.getY();
						y1 = ice.getY();
					}
					if (calcDistance(x0, y0, x1, y1) < 40) {
						penguin.setVisible(false);
						break loop;
					}
				}
			}
		}
	}

	public JLabel getPenguin() {
		return penguin;
	}
}
