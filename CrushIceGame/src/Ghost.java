
public class Ghost extends Item {

	public Ghost(int location) {
		super("img/ghost.png", "ghost", location);
	}

	public void use() {
		MesgSend.send("useGhost");
	}
}
