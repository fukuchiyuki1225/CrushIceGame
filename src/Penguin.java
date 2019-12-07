import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Penguin {
	private JLabel penguin;

	public Penguin() {
		penguin = new JLabel(new ImageIcon("img/penguin.png"));
		Game.c.add(penguin);
		penguin.setBounds(350, 375, 100, 100);
	}
}
