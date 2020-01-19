
public class Warp extends Item {

	public Warp(int location) {
		super("img/warp.png", "warp", location);
	}

	public void use() {
		System.out.println("warp");
	}

}
