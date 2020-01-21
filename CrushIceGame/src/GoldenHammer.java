import javax.swing.ImageIcon;

public class GoldenHammer extends Item {
	private ImageIcon[] ghIcons;

	public GoldenHammer(int location) {
		super("img/goldenHammer.png", "goldenHammer", location);
		ImageLoader il = ImageLoader.getInstance();
		ghIcons = new ImageIcon[] {
				new ImageIcon(il.load("img/golden_hammer.png")),
				new ImageIcon(il.load("img/golden_hammer_2.png"))
		};
	}

	public void use() {
		System.out.println("goldenHammer");
		Hammer.getInstance().changeHammer(ghIcons);
	}

}
