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
	private ImageIcon[][] numIcon, iceIcon, hoverIcon;
	private ImageIcon brokenIce;
	private Hammer hammer;
	private Penguin penguin;
	private GameScreen gs;
	private MesgSend ms;
	private JButton ices[][];
	private int[][] hitCount, mustHitNum;
	private int[] countIce, breakIce;
	private Random random;
	private boolean moveFlag, turnFlag;
	private final int icesX = 9, icesY = 7, white = 0, blue = 1;
	private JLabel[] numLabel;
	private Timer timer;
	private ItemManager im;

	public Ices() {
		loadIceIcon();
		gs = GameScreen.getInstance();
		hammer = Hammer.getInstance();
		penguin = gs.getPenguin();
		ices = new JButton[icesY][icesX];
		hitCount = new int[icesY][icesX];
		mustHitNum = new int[icesY][icesX];
		countIce = new int[2];
		breakIce = new int[2];
		random = new Random();
		moveFlag = false;
		turnFlag = false;
		im = gs.getItemManager();
		ms = MesgSend.getInstance();

		if (gs.isMyTurn()) {
			for (int j = 0; j < icesY; j++) {
				for (int i = 0; i < icesX; i++) {
					int rand = random.nextInt(2);
					int mustHitNum = random.nextInt(5) + 1;
					ms.send("initialize" + " " + j + " " + i + " " + rand + " " + mustHitNum);
				}
			}
		}

		gs.addComponent(new JLabel(new ImageIcon(ImageLoader.loadImage("img/white.png"))), 850, 900, 440, 100, 100);
		gs.addComponent(new JLabel(new ImageIcon(ImageLoader.loadImage("img/blue.png"))), 850, 900, 526, 100, 100);

		numLabel = new JLabel[] {
				new JLabel(numIcon[white][0]),
				new JLabel(numIcon[blue][0])
		};
		gs.addComponent(numLabel[white], 800, 975, 483, 100, 100);
		gs.addComponent(numLabel[blue], 800, 975, 569, 100, 100);

		spinTheRoulette();
	}

	private void loadIceIcon() {
		iceIcon = new ImageIcon[][] {
			{
				new ImageIcon(ImageLoader.loadImage("img/white_ice.png")),
				new ImageIcon(ImageLoader.loadImage("img/white_ice_2.png")),
				new ImageIcon(ImageLoader.loadImage("img/white_ice_3.png"))
			},
			{
				new ImageIcon(ImageLoader.loadImage("img/blue_ice.png")),
				new ImageIcon(ImageLoader.loadImage("img/blue_ice_2.png")),
				new ImageIcon(ImageLoader.loadImage("img/blue_ice_3.png"))
			}
		};
		numIcon = new ImageIcon[][] {
			{
				new ImageIcon(ImageLoader.loadImage("img/white_0.png")),
				new ImageIcon(ImageLoader.loadImage("img/white_1.png")),
				new ImageIcon(ImageLoader.loadImage("img/white_2.png")),
				new ImageIcon(ImageLoader.loadImage("img/white_3.png"))
			},
			{
				new ImageIcon(ImageLoader.loadImage("img/blue_0.png")),
				new ImageIcon(ImageLoader.loadImage("img/blue_1.png")),
				new ImageIcon(ImageLoader.loadImage("img/blue_2.png")),
				new ImageIcon(ImageLoader.loadImage("img/blue_3.png"))
			}
		};
		hoverIcon = new ImageIcon[][] {
			{
				new ImageIcon(ImageLoader.loadImage("img/white_hover.png")),
				new ImageIcon(ImageLoader.loadImage("img/white_hover_2.png")),
				new ImageIcon(ImageLoader.loadImage("img/white_hover_3.png"))
			},
			{
				new ImageIcon(ImageLoader.loadImage("img/blue_hover.png")),
				new ImageIcon(ImageLoader.loadImage("img/blue_hover_2.png")),
				new ImageIcon(ImageLoader.loadImage("img/blue_hover_3.png"))
			}
		};
		brokenIce = new ImageIcon(ImageLoader.loadImage("img/broken_ice.png"));
	}

	public void initializeIce(int j, int i, int rand, int mustHitCount) {
		ices[j][i] = new JButton(iceIcon[rand][0]);
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
		if (!gs.isMyTurn()) return;
		int roulette = 0;
		if (countIce[white] > 0 && countIce[blue] > 0) {
			roulette = random.nextInt(6);
		} else if (countIce[white] == 0) {
			roulette = random.nextInt(2);
		} else if (countIce[blue] == 0) {
			roulette = random.nextInt(2) + 4;
		}
		ms.send("roulette" + " " + roulette);
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
		changeNumIcon();
	}

	public void changeBreakIce(int color) {
		breakIce[color]--;
		countIce[color]--;
		changeNumIcon();
		if (gs.isMyTurn() && breakIce[white] <= 0 && breakIce[blue] <= 0) {
			turnFlag = true;
		}
	}

	public void changeHitCount(int jbNum) {
		hitCount[jbNum / icesX][jbNum % icesX]++;
	}

	public void changeNumIcon() {
		for (int i = white; i <= blue; i++) {
			numLabel[i].setIcon(numIcon[i][breakIce[i]]);
		}
	}

	public void breakIce(MouseEvent e) {
		if (!gs.isMyTurn()) return;
		JButton jb = (JButton)e.getComponent();
		Icon icon = jb.getIcon();
		if (icon == brokenIce) {
			return;
		}
		int jbNum = Integer.parseInt(jb.getActionCommand());
		int diff = 0;
		String iconName = "";
		int color = 0;
		System.out.println("clicked: " + jbNum);
		loop: for (int j = white; j <= blue; j++) {
			for (int i = 0; i < 3; i++) {
				if (breakIce[j] > 0) {
					if (icon == hoverIcon[j][i]) {
						timer = new Timer(1, new PenguinMove(penguin, this, Integer.parseInt(jb.getActionCommand())));
						timer.start();
						ms.send("changeHitCount" + " " + jbNum);
						if (hitCount[jbNum / icesX][jbNum % icesX] >= mustHitNum[jbNum / icesX][jbNum % icesX]) {
							jb.setIcon(brokenIce);
							ms.send("changeNumIcon" + " " + j);
							color = j;
							iconName = "broken";
							im.digOutItem(jbNum);
							break loop;
						}
						diff = mustHitNum[jbNum / icesX][jbNum % icesX] - hitCount[jbNum / icesX][jbNum % icesX];
						if (diff > 0) {
							if (diff < 3) {
								jb.setIcon(hoverIcon[j][2]);
								color = j;
								iconName = "ice2";
							} else if (diff < 5) {
								jb.setIcon(hoverIcon[j][1]);
								color = j;
								iconName = "ice1";
							}
							break loop;
						}
					}
				}
			}
		}

		if (!iconName.equals("")) {
			ms.send("changeIceIcon" + " " + color + " " + jbNum + " " + iconName);
		}
	}

	public void changeIceIcon(int color, int jbNum, String iconName) {
		if (gs.isMyTurn()) return;
		Icon chIcon = null;
		switch (iconName) {
		case "broken":
			chIcon = brokenIce;
			break;
		case "ice1":
			chIcon = iceIcon[color][1];
			break;
		case "ice2":
			chIcon = iceIcon[color][2];
			break;
		default:
			break;
		}
		ices[jbNum / icesX][jbNum % icesX].setIcon(chIcon);
	}

	//  復活させるかもしれない 氷がホバー状態になってしまっているのを戻すメソッド
	/*public void cleanIceIcon() {
		if (gs.isMyTurn()) return;
		for (int j = 0; j < icesY; j++) {
			for (int i = 0; i < icesX; i++) {
				for (int l = 0; l < 3; l++) {
					for (int k = white; k <= blue; k++) {
						if (ices[j][i].getIcon() == hoverIcon[k][l]) {
							ices[j][i].setIcon(iceIcon[k][l]);
						}
					}
				}
			}
		}
	}*/

	public void hoverIceIcon(MouseEvent e) {
		// if (!gs.isMyTurn()) return;
		JButton jb = (JButton) e.getComponent();
		Icon icon = jb.getIcon();
		for (int j = white; j <= blue; j++) {
			for (int i = 0; i < 3; i++) {
				if (icon == iceIcon[j][i]) {
					jb.setIcon(hoverIcon[j][i]);
					return;
				} else if (icon == hoverIcon[j][i]) {
					jb.setIcon(iceIcon[j][i]);
					return;
				}
			}
		}
	}

	public JButton[][] getIces() {
		return ices;
	}

	public ItemManager getItemManager() {
		return im;
	}

	public int getIcesX() {
		return icesX;
	}

	public Icon getBrokenIceIcon() {
		return brokenIce;
	}

	public void setMoveFlag(boolean moveFlag) {
		if (this.moveFlag || !moveFlag) {
			timer.stop();
		}
		this.moveFlag = moveFlag;
	}

	public void mouseReleased(MouseEvent e) {
		hammer.changeHammerIcon(gs);
	}

	public void mousePressed(MouseEvent e) {
		hammer.changeHammerIcon(gs);
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
			ices.ms.send("changePenguinIcon" + " " + 1);
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
				ices.ms.send("move" + " " + (int)sendX + " " + (int)sendY);
				// ↓デバッグ用　のちのち消す
				// ices.ms.send("move" + " " + (int)x0 + " " + (int)y0);
				penguin.penguinFall(ices, ices.brokenIce);
			} else if (ices.turnFlag) {
				ices.ms.send("changeTurn");
				ices.turnFlag = false;
			} else {
				ices.setMoveFlag(false);
				ices.ms.send("changePenguinIcon" + " " + 0);
			}
		}
	}
}
