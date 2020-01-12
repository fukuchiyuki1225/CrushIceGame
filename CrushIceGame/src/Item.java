import javax.swing.ImageIcon;
import javax.swing.JLabel;

public abstract class Item {
	public JLabel itemlabel;
	public String name;

	public Item(String imgPass, String name) {
		this.itemlabel = new JLabel(new ImageIcon(ImageLoader.loadImage(imgPass)));
		this.name = name;
	}

	public abstract void use();

	public String getName() {
		return name;
	}
}
