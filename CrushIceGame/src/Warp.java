import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JButton;
import javax.swing.Timer;

public class Warp extends Item {
	public Warp(int location) {
		super("img/warp.png", "warp", location);
	}

	public void use() {
		MesgSend.send("changePenguinIcon" + " " + 5);
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
		Timer timer = new Timer(1000,  new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MesgSend.send("useItem");
				MesgSend.send("move" + " " + ices.getIces()[rand / ices.getIcesX()][rand % ices.getIcesX()].getX() + " " + ices.getIces()[rand / ices.getIcesX()][rand % ices.getIcesX()].getY());
				MesgSend.send("changePenguinIcon" + " " + 0);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}
}
