import java.awt.Rectangle;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Penguin {
	private JLabel penguin;
	private Rectangle collision;

	public Penguin() {
		penguin = new JLabel(new ImageIcon("img/penguin.png"));
		Game.j.setLayer(penguin, 500);
		Game.j.add(penguin);
		penguin.setBounds(350, 375, 100, 100);
		collision = new Rectangle(penguin.getX(), penguin.getY() + 50, penguin.getWidth(), penguin.getHeight() - 50);
	}

	public Rectangle getCollision() {
		return collision;
	}

	public void penguinFall() {
		System.out.println("fall");
		penguin.setVisible(false);
	}

	// checkFallà⁄êA
}
