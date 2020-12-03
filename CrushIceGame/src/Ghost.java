
public class Ghost extends Item {

	public Ghost(String imgPass, String name, int location) {
		super("img/ghost.png", "ghost", location);
	}

	@Override
	public void use() {
		MesgSend.send("useGhostItem");
		GameScreen.getInstance().getItemManager().getItems();

	}

}
