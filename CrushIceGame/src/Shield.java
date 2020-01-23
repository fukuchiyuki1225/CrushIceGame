
public class Shield extends Item {

	public Shield(int location) {
		super("img/shield.png", "shield", location);
	}

	public void use() {
		System.out.println("shield");
		GameScreen.getInstance().getIces().setShieldFlag(true);
		MesgSend.getInstance().send("changePenguinIcon" + " " + 3);
	}

}
