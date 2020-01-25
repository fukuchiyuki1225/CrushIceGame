import javax.swing.ImageIcon;

public class GoldenHammer extends Item {
	private ImageIcon[] ghIcons;

	public GoldenHammer(int location) {
		super("img/goldenHammer.png", "goldenHammer", location);
		ResourceLoader rl = ResourceLoader.getInstance();
		ghIcons = new ImageIcon[] {
				new ImageIcon(rl.load("img/golden_hammer.png")),
				new ImageIcon(rl.load("img/golden_hammer_2.png"))
		};
	}

	public void use() {
		Hammer.getInstance().changeHammer(ghIcons);
		GameScreen.getInstance().getIces().setGhFlag(true);
		MesgSend.getInstance().send("changePenguinIcon" + " " + 2);
	}

}
