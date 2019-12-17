import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;

public class Penguin extends JLabel {
	private JLabel penguin;

	public Penguin() {
		penguin = new JLabel(new ImageIcon("img/penguin.png"));
		Game.j.setLayer(penguin, 500);
		Game.j.add(penguin);
		penguin.setBounds(350, 375, 100, 100);
	}

	public double lerp(double x0, double y0, double x1, double y1, double x) {
		return y0 + (y1 - y0) * (x - x0) / (x1 - x0);
	}

	public void penguinFall(Ices ices) {
		loop : for (JButton[] icesArray : ices.getIces()) {
			for (JButton ice : icesArray) {
				if (ice.getIcon() == ices.getBrokenIceIcon()) {
					int x1, x2, y1, y2;
					if (penguin.getX() < ice.getX()) {
						x1 = ice.getX();
						x2 = penguin.getX();
					} else {
						x1 = penguin.getX();
						x2 = ice.getX();
					}
					if (penguin.getY() < ice.getY()) {
						y1 = ice.getY();
						y2 = penguin.getY();
					} else {
						y1 = penguin.getY();
						y2 = ice.getY();
					}
					if (Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) < 40) {
						System.out.println("fall");
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
