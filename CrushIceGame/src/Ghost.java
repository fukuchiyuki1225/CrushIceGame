import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Ghost extends Item {

	public Ghost(int location) {
		super("img/ghost.png", "ghost", location);
	}

	public void use() {
		JLabel ghost = new JLabel(new ImageIcon(ResourceLoader.load("img/ghost.png")));
		GameScreen.getInstance().addComponent(ghost, 500, 1050, 640, 100, 100);
		MesgSend.send("useGhost");
	}
}
