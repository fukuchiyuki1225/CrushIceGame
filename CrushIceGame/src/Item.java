import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class Item {
	private JLabel itemLabel;
	private String name;
	private int location;

	public Item(String imgPass, String name, int location) {
		this.itemLabel = new JLabel(new ImageIcon(ImageLoader.getInstance().load(imgPass)));
		this.name = name;
		this.location = location;
	}

	public void initItem() {
		GameScreen gs = GameScreen.getInstance();
		int j = location / gs.getIces().getIcesX();
		int i = location % gs.getIces().getIcesX();
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

	public JLabel getItemLabel() {
		return itemLabel;
	}

	public String getName() {
		return name;
	}

	public int getLocation() {
		return location;
	}
}
