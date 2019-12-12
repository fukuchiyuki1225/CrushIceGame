import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class Ices extends JFrame implements MouseListener, MouseMotionListener {
	private ImageIcon whiteIce, blueIce, whiteHover, blueHover, brokenIce;
	private JButton ices[][];
	private JLabel breakIce;
	private int countWhite = 0, countBlue = 0, roulette, breakWhite = 0, breakBlue = 0;
	private Hammer hammer;
	private Penguin penguin;
	private Random random;

	public Ices(Hammer hammer, Penguin penguin) {
		whiteIce = new ImageIcon("img/white_ice.png");
		blueIce = new ImageIcon("img/blue_ice.png");
		whiteHover = new ImageIcon("img/white_hover.png");
		blueHover = new ImageIcon("img/blue_hover.png");
		brokenIce = new ImageIcon("img/broken_ice.png");
		ices = new JButton[7][9];
		this.hammer = hammer;
		this.penguin = penguin;
		random = new Random();
		for (int j = 0; j < 7; j++) {
			for (int i = 0; i < 9; i++) {
				if (random.nextInt(2) == 0) {
					ices[j][i] = new JButton(whiteIce);
					countWhite++;
				} else {
					ices[j][i] = new JButton(blueIce);
					countBlue++;
				}
				Game.j.setLayer(ices[j][i], 100);
				Game.j.add(ices[j][i]);
				ices[j][i].setBorderPainted(false);
				if (i % 2 == 0) {
					ices[j][i].setBounds(i * 75 + 50, j * 86 + 43 + 100, 100, 100);
				} else {
					ices[j][i].setBounds(i * 75 + 50, j * 86 + 100, 100, 100);
				}
				ices[j][i].addMouseListener(this);
				ices[j][i].addMouseMotionListener(this);
				ices[j][i].setContentAreaFilled(false);
				ices[j][i].setActionCommand(Integer.toString(i + j * 9));
			}
		}
		breakIce = new JLabel("���F" + breakWhite + "�@�F" + breakBlue);
		Game.j.add(breakIce);
		breakIce.setBounds(900, 300, 200, 200);
		spinTheRoulette();
		System.out.println("���F" + countWhite + "�@�F" + countBlue);
		System.out.println("0:0 " + ices[0][0].getLocation());
		System.out.println("6:8 " + ices[6][8].getLocation());
	}

	public void spinTheRoulette() {
		roulette = random.nextInt(7);
		switch (roulette) {
		case 0:
			breakWhite = 0;
			breakBlue = 2;
			break;
		case 1:
			breakWhite = 2;
			breakBlue = 0;
			break;
		case 2:
			breakWhite = 1;
			breakBlue = 1;
		case 3:
			breakWhite = 1;
			breakBlue = 0;
			break;
		case 4:
			breakWhite = 0;
			breakBlue = 1;
			break;
		case 5:
			breakWhite = 0;
			breakBlue = 0;
			break;
		case 6:
			breakWhite = 5;
			breakBlue = 5;
			break;
		default:
			break;
		}
		breakIce.setText("���F" + breakWhite + "�@�F" + breakBlue);
	}

	public void breakIce(JButton jb) {
		Icon icon = jb.getIcon();
		if (icon == whiteHover && breakWhite > 0) {
			jb.setIcon(brokenIce);
			breakWhite--;
			countWhite--;
		} else if (icon == blueHover && breakBlue > 0) {
			jb.setIcon(brokenIce);
			breakBlue--;
			countBlue--;
		}
		if (breakWhite == 0 && breakBlue == 0) {
			spinTheRoulette();
		}
		breakIce.setText("���F" + breakWhite + "�@�F" + breakBlue);
		checkPenguinFall(penguin);
	}

	public void checkPenguinFall(Penguin penguin) {
		loop: for (JButton[] icesArray : ices) {
			for (JButton ice : icesArray) {
				if (ice.getIcon() == brokenIce) {
					int x1, x2, y1, y2;
					if (penguin.getPenguinX() < ice.getX()) {
						x1 = ice.getX();
						x2 = penguin.getPenguinX();
					} else {
						x1 = penguin.getPenguinX();
						x2 = ice.getX();
					}
					if (penguin.getPenguinY() < ice.getY()) {
						y1 = ice.getY();
						y2 = penguin.getPenguinY();
					} else {
						y1 = penguin.getPenguinY();
						y2 = ice.getY();
					}
					if (Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2)) < 40) {
						penguin.penguinFall();
						break loop;
					}
				}
			}
		}
	}

	public void mouseReleased(MouseEvent e) {
		hammer.changeHammerIcon();
	}

	public void mousePressed(MouseEvent e) {
		hammer.changeHammerIcon();
	}

	public void mouseExited(MouseEvent e) {
		JButton jb = (JButton) e.getComponent();
		if (jb.getIcon() == whiteHover) {
			jb.setIcon(whiteIce);
		} else if (jb.getIcon() == blueHover) {
			jb.setIcon(blueIce);
		}
	}

	public void mouseEntered(MouseEvent e) {
		JButton jb = (JButton) e.getComponent();
		if (jb.getIcon() == whiteIce) {
			jb.setIcon(whiteHover);
		} else if (jb.getIcon() == blueIce) {
			jb.setIcon(blueHover);
		}
	}

	public void mouseClicked(MouseEvent e) {
		JButton jb = (JButton) e.getComponent();
		System.out.println(jb.getActionCommand());
		breakIce(jb);
	}

	public void mouseDragged(MouseEvent e) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		SwingUtilities.convertPointToScreen(p, e.getComponent());
		SwingUtilities.convertPointFromScreen(p, e.getComponent().getParent());
		hammer.setHammerLocation(p);
	}
}
