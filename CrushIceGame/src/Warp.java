import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;

public class Warp extends Item {

	public Warp(int location) {
		super("img/warp.png", "warp", location);
	}

	public void use() {
		System.out.println("warp");
		Ices ices = GameScreen.getInstance().getIces();
		List<Integer> icesNum = new ArrayList<Integer>();
		for (JButton[] icesArray : ices.getIces()) {
			for (JButton ice : icesArray) {
				if (ice.getIcon() != ices.getBrokenIceIcon()) {
					icesNum.add(Integer.parseInt(ice.getActionCommand()));
				}
			}
		}
		Random random = new Random();
		int rand = icesNum.get(random.nextInt(icesNum.size()));
		MesgSend.getInstance().send("move" + " " + ices.getIces()[rand / ices.getIcesX()][rand % ices.getIcesX()].getX() + " " + ices.getIces()[rand / ices.getIcesX()][rand % ices.getIcesX()].getY());
		Sound.getInstance().play("item");
	}
}
