import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Penguin {
	private JLabel penguin;
	private Ices ices;

	public Penguin(Ices ices) {
		penguin = new JLabel(new ImageIcon("img/penguin.png"));
		this.ices = ices;
		Game.j.setLayer(penguin, 500);
		Game.j.add(penguin);
		penguin.setBounds(350, 375, 100, 100);
	}
}
