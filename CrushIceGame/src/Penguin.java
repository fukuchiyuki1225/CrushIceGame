import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Penguin {
	private JLabel penguin;

	public Penguin() {
		penguin = new JLabel(new ImageIcon("img/penguin.png"));
		Game.j.setLayer(penguin, 500);
		Game.j.add(penguin);
		penguin.setBounds(350, 375, 100, 100);
	}

	public void penguinFall() {
		System.out.println("fall");
		penguin.setVisible(false);
	}

	public int getPenguinX() {
		return penguin.getX();
	}

	public int getPenguinY() {
		return penguin.getY();
	}

	// checkFallˆÚA
}
