import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class Item {
	private JLabel itemlabel;
	private String name;
	private int location;

	public Item(String imgPass, String name, int location) {
		this.itemlabel = new JLabel(new ImageIcon(ImageLoader.loadImage(imgPass)));
		this.name = name;
	}

	public void initialize(GameScreen gs, Ices ices) {
		Random random = new Random();
		location = random.nextInt(63);
		int icesX = ices.getIcesX();
		gs.addComponent(itemlabel, 800, ices.getIces()[location / icesX][location % icesX].getX(), ices.getIces()[location / icesX][location % icesX].getY(), 80, 80);
		itemlabel.setVisible(false);
	}

	public abstract void use();

	public String getName() {
		return name;
	}
}
