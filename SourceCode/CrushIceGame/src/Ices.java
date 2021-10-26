import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Ices implements MouseListener, MouseMotionListener {
	private GameScreen gs;
	private ItemManager im;
	private Hammer hammer;
	private Penguin penguin;
	private JButton ices[][];
	private int[][] hitCount, mustHitNum;
	private int[] countIce, breakIce;
	private ImageIcon[][] numIcons, iceIcons, hoverIcons;
	private ImageIcon brokenIce;
	private final int icesX = 9, icesY = 7, white = 0, blue = 1;
	private JLabel[] numLabels;
	private boolean moveFlag, turnFlag, ghFlag, shieldFlag, startFlag;
	private Random random;
	private Timer timer;

	public Ices() {
		gs = GameScreen.getInstance();
		im = gs.getItemManager();
		hammer = Hammer.getInstance();
		penguin = gs.getPenguin();
		ices = new JButton[icesY][icesX];
		hitCount = new int[icesY][icesX];
		mustHitNum = new int[icesY][icesX];
		countIce = new int[2];
		breakIce = new int[2];
		moveFlag = false;
		turnFlag = false;
		startFlag = false;
		random = new Random();

		loadImage();

		initIceRand();

		gs.addComponent(new JLabel(new ImageIcon(ResourceLoader.load("img/white.png"))), 850, 900, 415, 100, 100);
		gs.addComponent(new JLabel(new ImageIcon(ResourceLoader.load("img/blue.png"))), 850, 900, 501, 100, 100);
		numLabels = new JLabel[] {
				new JLabel(numIcons[white][0]),
				new JLabel(numIcons[blue][0])
		};
		gs.addComponent(numLabels[white], 800, 975, 458, 100, 100);
		gs.addComponent(numLabels[blue], 800, 975, 544, 100, 100);

		spinTheRoulette();
	}

	private void loadImage() {
		iceIcons = new ImageIcon[][] {
			{
				new ImageIcon(ResourceLoader.load("img/white_ice.png")),
				new ImageIcon(ResourceLoader.load("img/white_ice_2.png")),
				new ImageIcon(ResourceLoader.load("img/white_ice_3.png"))
			},
			{
				new ImageIcon(ResourceLoader.load("img/blue_ice.png")),
				new ImageIcon(ResourceLoader.load("img/blue_ice_2.png")),
				new ImageIcon(ResourceLoader.load("img/blue_ice_3.png"))
			}
		};
		numIcons = new ImageIcon[][] {
			{
				new ImageIcon(ResourceLoader.load("img/white_0.png")),
				new ImageIcon(ResourceLoader.load("img/white_1.png")),
				new ImageIcon(ResourceLoader.load("img/white_2.png")),
				new ImageIcon(ResourceLoader.load("img/white_3.png"))
			},
			{
				new ImageIcon(ResourceLoader.load("img/blue_0.png")),
				new ImageIcon(ResourceLoader.load("img/blue_1.png")),
				new ImageIcon(ResourceLoader.load("img/blue_2.png")),
				new ImageIcon(ResourceLoader.load("img/blue_3.png"))
			}
		};
		hoverIcons = new ImageIcon[][] {
			{
				new ImageIcon(ResourceLoader.load("img/white_hover.png")),
				new ImageIcon(ResourceLoader.load("img/white_hover_2.png")),
				new ImageIcon(ResourceLoader.load("img/white_hover_3.png"))
			},
			{
				new ImageIcon(ResourceLoader.load("img/blue_hover.png")),
				new ImageIcon(ResourceLoader.load("img/blue_hover_2.png")),
				new ImageIcon(ResourceLoader.load("img/blue_hover_3.png"))
			}
		};
		brokenIce = new ImageIcon(ResourceLoader.load("img/broken_ice.png"));
	}

	public void initIceRand() {
		if (gs.isMyTurn()) return;
		for (int j = 0; j < icesY; j++) {
			for (int i = 0; i < icesX; i++) {
				int rand = random.nextInt(2);
				int mustHitNum = random.nextInt(5) + 1;
				MesgSend.send("initIces" + " " + j + " " + i + " " + rand + " " + mustHitNum);
			}
		}
	}

	public void initIces(int j, int i, int rand, int mustHitCount) {
		ices[j][i] = new JButton(iceIcons[rand][0]);
		countIce[rand]++;
		if (i % 2 == 0) {
			gs.addComponent(ices[j][i], 100, i * 75 + 50, j * 86 + 43 + 120, 100, 100);
		} else {
			gs.addComponent(ices[j][i], 100, i * 75 + 50, j * 86 + 120, 100, 100);
		}
		gs.setButton(ices[j][i], this, this);
		ices[j][i].setActionCommand(Integer.toString(i + j * icesX));
		mustHitNum[j][i] = mustHitCount;
		hitCount[j][i] = 0;
	}

	public void spinTheRoulette() {
		if (gs.isMyTurn()) return;
		int roulette = 0;
		if (countIce[white] > 0 && countIce[blue] > 0) {
			roulette = random.nextInt(6);
		} else if (countIce[white] == 0) {
			roulette = random.nextInt(2);
		} else if (countIce[blue] == 0) {
			roulette = random.nextInt(2) + 4;
		}
		MesgSend.send("roulette" + " " + roulette);
	}

	public void setBreakIce(int roulette) {
		switch (roulette) {
		case 0:
			breakIce[white] = 0;
			breakIce[blue] = 2;
			break;
		case 1:
			breakIce[white] = 0;
			breakIce[blue] = 1;
			break;
		case 2:
			breakIce[white] = 1;
			breakIce[blue] = 1;
			break;
		case 3:
			breakIce[white] = 3;
			breakIce[blue] = 3;
			break;
		case 4:
			breakIce[white] = 2;
			breakIce[blue] = 0;
			break;
		case 5:
			breakIce[white] = 1;
			breakIce[blue] = 0;
			break;
		default:
			break;
		}
		for (int i = white; i <= blue; i++) {
			if (countIce[i] < breakIce[i]) {
				breakIce[i] = countIce[i];
			}
		}
		Sound.play("turn");
		changeNumIcon();
	}

	public void changeBreakIce(int color) {
		breakIce[color]--;
		countIce[color]--;
		changeNumIcon();
		if (gs.isMyTurn() && breakIce[white] <= 0 && breakIce[blue] <= 0) {
			if (shieldFlag) {
				MesgSend.send("changeTurn");
				return;
			}
			turnFlag = true;
		}
	}

	public void changeHitCount(int jbNum) {
		Sound.play("pick");
		hitCount[jbNum / icesX][jbNum % icesX]++;
	}

	public void changeNumIcon() {
		for (int i = white; i <= blue; i++) {
			numLabels[i].setIcon(numIcons[i][breakIce[i]]);
		}
		if (!startFlag) {
			Timer timer = new Timer(1000, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					gs.setGameStart();
				}
			});
			timer.setRepeats(false);
			timer.start();
		}
	}

	public void breakIce(MouseEvent e) {
		if (!startFlag || !gs.isMyTurn()) return;
		JButton jb = (JButton)e.getComponent();
		Icon icon = jb.getIcon();
		if (icon == brokenIce) {
			return;
		}
		int jbNum = Integer.parseInt(jb.getActionCommand());
		int oppositeNum = (icesX * icesY - 1) - jbNum;
		String iconName = "";
		String soundName = "";
		int diff = 0;
		int color = 0;
		loop: for (int j = white; j <= blue; j++) {
			for (int i = 0; i < 3; i++) {
				if (breakIce[j] > 0) {
					color = j;
					if (icon == hoverIcons[j][i]) {
						if (!shieldFlag) {
							timer = new Timer(1, new PenguinMove(penguin, this, oppositeNum));
							timer.start();
						}
						MesgSend.send("changeHitCount" + " " + jbNum);
						if (ghFlag || hitCount[jbNum / icesX][jbNum % icesX] >= mustHitNum[jbNum / icesX][jbNum % icesX]) {
							jb.setIcon(brokenIce);
							MesgSend.send("changeBreakIce" + " " + j);
							iconName = "broken";
							soundName = "broken";
							im.digOutItem(jbNum);
							break loop;
						}
						diff = mustHitNum[jbNum / icesX][jbNum % icesX] - hitCount[jbNum / icesX][jbNum % icesX];
						if (diff > 0) {
							if (diff < 3) {
								jb.setIcon(hoverIcons[j][2]);
								iconName = "ice2";
							} else if (diff < 5) {
								jb.setIcon(hoverIcons[j][1]);
								iconName = "ice1";
							}
							soundName = "crack";
							break loop;
						}
					}
				}
			}
		}

		if (!iconName.equals("")) {
			MesgSend.send("changeIceIcon" + " " + color + " " + jbNum + " " + iconName + " " + soundName);
		}
	}

	public void changeIceIcon(int color, int jbNum, String iconName) {
		if (gs.isMyTurn()) return;
		Icon icon = null;
		switch (iconName) {
		case "broken":
			icon = brokenIce;
			break;
		case "ice1":
			icon = iceIcons[color][1];
			break;
		case "ice2":
			icon = iceIcons[color][2];
			break;
		default:
			break;
		}
		ices[jbNum / icesX][jbNum % icesX].setIcon(icon);
	}

	public void hoverIceIcon(MouseEvent e) {
		JButton jb = (JButton) e.getComponent();
		Icon icon = jb.getIcon();
		for (int j = white; j <= blue; j++) {
			for (int i = 0; i < 3; i++) {
				if (icon == iceIcons[j][i]) {
					jb.setIcon(hoverIcons[j][i]);
					return;
				} else if (icon == hoverIcons[j][i]) {
					jb.setIcon(iceIcons[j][i]);
					return;
				}
			}
		}
	}

	public void changeHammerIcon(MouseEvent e) {
		Icon icon = ((JButton)e.getComponent()).getIcon();
		if (icon == brokenIce) return;
		for (int j = white; j <= blue; j++) {
			for (int i = 0; i < 3; i++) {
				if (icon == hoverIcons[j][i] && breakIce[j] == 0) return;
			}
		}
		hammer.changeHammerIcon();
	}

	public JButton[][] getIces() {
		return ices;
	}

	public int getIcesX() {
		return icesX;
	}

	public int getIcesY() {
		return icesY;
	}

	public Icon getBrokenIceIcon() {
		return brokenIce;
	}

	public boolean getMoveFlag() {
		return moveFlag;
	}

	public void setMoveFlag(boolean moveFlag) {
		if (this.moveFlag || !moveFlag) {
			timer.stop();
		}
		this.moveFlag = moveFlag;
	}

	public void setGhFlag(boolean ghFlag) {
		if (!ghFlag) {
			hammer.changeHammer();
		}
		this.ghFlag = ghFlag;
	}

	public void setShieldFlag(boolean shieldFlag) {
		this.shieldFlag = shieldFlag;
	}

	public void setStartFlag(boolean startFlag) {
		this.startFlag = startFlag;
	}

	public void mouseReleased(MouseEvent e) {
		changeHammerIcon(e);
	}

	public void mousePressed(MouseEvent e) {
		changeHammerIcon(e);
	}

	public void mouseExited(MouseEvent e) {
		hoverIceIcon(e);
	}

	public void mouseEntered(MouseEvent e) {
		hoverIceIcon(e);
	}

	public void mouseClicked(MouseEvent e) {
		breakIce(e);
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		SwingUtilities.convertPointToScreen(p, e.getComponent());
		SwingUtilities.convertPointFromScreen(p, e.getComponent().getParent());
		hammer.setHammerLocation(p);
	}

	public static class PenguinMove implements ActionListener {
		private Penguin penguin;
		private Ices ices;
		private double x0, x1, y0, y1, x, y, sendX, sendY, speed;
		public PenguinMove(Penguin penguin, Ices ices, int jbNum) {
			this.penguin = penguin;
			this.ices = ices;
			x0 = penguin.getPenguinX();
			y0 = penguin.getPenguinY();
			x1 = ices.ices[jbNum / ices.getIcesX()][jbNum % ices.getIcesX()].getX();
			y1 = ices.ices[jbNum / ices.getIcesX()][jbNum % ices.getIcesX()].getY();
			x = x0 < x1 ? x0 + 1 : x0 - 1;
			y = y0 < y1 ? y0 + 1 : y0 - 1;
			sendX = 0;
			sendY = 0;
			speed = 0.3;
			ices.setMoveFlag(true);
			if (ices.ghFlag) {
				MesgSend.send("changePenguinIcon" + " " + 3);
			} else {
				MesgSend.send("changePenguinIcon" + " " + 1);
			}
		}

		public void actionPerformed(ActionEvent e) {
			if (speed > 0) {
				sendX = (int) x;
				if (Math.abs(x1 - x0) < 25) {
					sendY = y;
					if (y0 < y1) {
						y += speed;
					} else if (y0 >= y1) {
						y -= speed;
					}
				} else if (x0 < x1) {
					sendY = Calculation.lerp(x0, y0, x1, y1, x);
					x += speed;
				} else {
					sendY = Calculation.lerp(x1, y1, x0, y0, x);
					x -= speed;
				}
				speed = speed < 0 ? 0 : speed - 0.002;
				MesgSend.send("move" + " " + (int)sendX + " " + (int)sendY);
				penguin.penguinFall();
			} else if (ices.turnFlag) {
				MesgSend.send("changeTurn");
				ices.turnFlag = false;
			} else {
				ices.setMoveFlag(false);
				if (ices.ghFlag) {
					MesgSend.send("changePenguinIcon" + " " + 2);
				} else if (!ices.shieldFlag) {
					MesgSend.send("changePenguinIcon" + " " + 0);
				}
			}
		}
	}
}
