import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Penguin extends JLabel {
	private JLabel penguin;

	public Penguin(GameScreen gs) {
		penguin = new JLabel(new ImageIcon(ImageLoader.readImage("img/penguin.png")));
		GameScreen.j.setLayer(penguin, 500);
		GameScreen.j.add(penguin);
		penguin.setBounds(350, 375, 100, 100);
	}

	public void penguinFall(Ices ices) {
		if (penguin.getX() < 10 || penguin.getX() > 700 || penguin.getY() < 90 || penguin.getY() > 700) {
			penguin.setVisible(false);
		}
		loop : for (JButton[] icesArray : ices.getIces()) {
			for (JButton ice : icesArray) {
				if (ice.getIcon() == ices.getBrokenIceIcon()) {
					int x0, x1, y0, y1;
					x0 = penguin.getX() < ice.getX() ? ice.getX() : penguin.getX();
					x1 = penguin.getX() < ice.getX() ? penguin.getX() : ice.getX();
					y0 = penguin.getY() < ice.getY() ? ice.getY() : penguin.getY();
					y1 = penguin.getY() < ice.getY() ? penguin.getY() : ice.getY();
					if (Calculation.calcDistance(x0, y0, x1, y1) < 40) {
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
