
public class Shield extends Item {

	public Shield(int location) {
		super("img/shield.png", "shield", location);
	}

	public void use() {
		GameScreen.getInstance().getIces().setShieldFlag(true);
		MesgSend.getInstance().send("changePenguinIcon" + " " + 4);
	}

}
