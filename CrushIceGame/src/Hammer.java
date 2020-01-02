import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Hammer {
	private ImageIcon hammerIcon, hammerIcon2;
	private JLabel hammer;
	private MyTurn myTurn;

	public Hammer(MyTurn myTurn, GameScreen gs) {
		hammerIcon = new ImageIcon(ImageLoader.readImage("img/pick_hammer.png"));
		hammerIcon2 = new ImageIcon(ImageLoader.readImage("img/pick_hammer_2.png"));
		hammer = new JLabel(hammerIcon);
		gs.addComponent(hammer, 1000, 0, 0, 200, 170);
		this.myTurn = myTurn;
	}

	public void changeHammerIcon() {
		if (!myTurn.isMyTurn()) return;
		if (hammer.getIcon() == hammerIcon) {
			hammer.setIcon(hammerIcon2);
		} else if (hammer.getIcon() == hammerIcon2){
			hammer.setIcon(hammerIcon);
		}
	}

	public void setHammerLocation(Point p) {
		hammer.setLocation(p.x - 50, p.y - 120);
	}
}
