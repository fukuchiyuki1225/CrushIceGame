import javax.swing.ImageIcon;

public class GoldenHammer extends Item {
	private ImageIcon[] ghIcons;

	public GoldenHammer(int location) {
		super("img/goldenHammer.png", "goldenHammer", location);
		ghIcons = new ImageIcon[] {
				new ImageIcon(ResourceLoader.load("img/golden_hammer.png")),
				new ImageIcon(ResourceLoader.load("img/golden_hammer_2.png"))
		};
	}

	public void use() {
		Hammer.getInstance().changeHammer(ghIcons);
		GameScreen.getInstance().getIces().setGhFlag(true);
		MesgSend.send("changePenguinIcon" + " " + 2);
	}

}
