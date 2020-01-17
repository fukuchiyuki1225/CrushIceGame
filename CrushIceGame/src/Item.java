import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class Item {
	private JLabel itemLabel;
	private String name;
	private int location;

	public Item(String imgPass, String name, int location) {
		this.itemLabel = new JLabel(new ImageIcon(ImageLoader.loadImage(imgPass)));
		this.name = name;
		this.location = location;
	}

	public void initialize(GameScreen gs, Ices ices) {
		int j = location / ices.getIcesX();
		int i = location % ices.getIcesX();
		if (i % 2 == 0) {
			gs.addComponent(itemLabel, 120, i * 75 + 50, j * 86 + 43 + 120, 100, 100);
		} else {
			gs.addComponent(itemLabel, 120, i * 75 + 50, j * 86 + 120, 100, 100);
		}
		itemLabel.setVisible(false);
	}

	public void getItem() {
		itemLabel.setVisible(true);
	}

	public abstract void use();

	public String getName() {
		return name;
	}

	public int getLocation() {
		return location;
	}
}
