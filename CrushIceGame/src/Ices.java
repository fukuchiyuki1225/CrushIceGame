import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.Socket;
import java.util.Random;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Ices extends JFrame implements MouseListener, MouseMotionListener {
	private ImageIcon[][] numIcon, iceIcon, hoverIcon;
	private ImageIcon brokenIce;
	private Hammer hammer;
	private Penguin penguin;
	private MyTurn myTurn;
	private GameScreen gs;
	private JButton ices[][];
	private int[][] hitCount, mustHitNum;
	private int[] countIce, breakIce;
	private Random random;
	private boolean moveFlag;
	private final int icesX = 9, icesY = 7, white = 0, blue = 1;
	private JLabel[] numLabel;
	private Timer timer;
	private MesgSend ms;

	public Ices(Hammer hammer, Penguin penguin, MyTurn myTurn, Socket socket, GameScreen gs) {
		loadIceIcon();
		this.hammer = hammer;
		this.penguin = penguin;
		this.myTurn = myTurn;
		this.gs = gs;
		ices = new JButton[icesY][icesX];
		hitCount = new int[icesY][icesX];
		mustHitNum = new int[icesY][icesX];
		countIce = new int[2];
		breakIce = new int[2];
		random = new Random();
		moveFlag = false;
		ms = new MesgSend(socket);

		if (myTurn.isMyTurn()) {
			for (int j = 0; j < 7; j++) {
				for (int i = 0; i < icesX; i++) {
					int rand = random.nextInt(2);
					ices[j][i] = new JButton(iceIcon[rand][0]);
					countIce[rand]++;
					if (i % 2 == 0) {
						gs.addComponent(ices[j][i], 100, i * 75 + 50, j * 86 + 43 + 120, 100, 100);
					} else {
						gs.addComponent(ices[j][i], 100, i * 75 + 50, j * 86 + 120, 100, 100);
					}
					ices[j][i].setBorderPainted(false);
					ices[j][i].addMouseListener(this);
					ices[j][i].addMouseMotionListener(this);
					ices[j][i].setContentAreaFilled(false);
					ices[j][i].setActionCommand(Integer.toString(i + j * icesX));
					mustHitNum[j][i] = random.nextInt(5) + 1;
					hitCount[j][i] = 0;
					ms.send("initialize" + " " + j + " " + i + " " + rand + " " + mustHitNum[j][i]);
				}
			}
		}

		gs.addComponent(new JLabel(new ImageIcon(ImageLoader.readImage("img/white.png"))), 850, 900, 515, 100, 100);
		gs.addComponent(new JLabel(new ImageIcon(ImageLoader.readImage("img/blue.png"))), 850, 900, 601, 100, 100);

		numLabel = new JLabel[] {
				new JLabel(numIcon[white][0]),
				new JLabel(numIcon[blue][0])
		};
		gs.addComponent(numLabel[white], 800, 975, 558, 100, 100);
		gs.addComponent(numLabel[blue], 800, 975, 644, 100, 100);

		spinTheRoulette();
	}

	private void loadIceIcon() {
		iceIcon = new ImageIcon[][] {
			{
				new ImageIcon(ImageLoader.readImage("img/white_ice.png")),
				new ImageIcon(ImageLoader.readImage("img/white_ice_2.png")),
				new ImageIcon(ImageLoader.readImage("img/white_ice_3.png"))
			},
			{
				new ImageIcon(ImageLoader.readImage("img/blue_ice.png")),
				new ImageIcon(ImageLoader.readImage("img/blue_ice_2.png")),
				new ImageIcon(ImageLoader.readImage("img/blue_ice_3.png"))
			}
		};
		numIcon = new ImageIcon[][] {
			{
				new ImageIcon(ImageLoader.readImage("img/white_0.png")),
				new ImageIcon(ImageLoader.readImage("img/white_1.png")),
				new ImageIcon(ImageLoader.readImage("img/white_2.png")),
				new ImageIcon(ImageLoader.readImage("img/white_3.png"))
			},
			{
				new ImageIcon(ImageLoader.readImage("img/blue_0.png")),
				new ImageIcon(ImageLoader.readImage("img/blue_1.png")),
				new ImageIcon(ImageLoader.readImage("img/blue_2.png")),
				new ImageIcon(ImageLoader.readImage("img/blue_3.png"))
			}
		};
		hoverIcon = new ImageIcon[][] {
			{
				new ImageIcon(ImageLoader.readImage("img/white_hover.png")),
				new ImageIcon(ImageLoader.readImage("img/white_hover_2.png")),
				new ImageIcon(ImageLoader.readImage("img/white_hover_3.png"))
			},
			{
				new ImageIcon(ImageLoader.readImage("img/blue_hover.png")),
				new ImageIcon(ImageLoader.readImage("img/blue_hover_2.png")),
				new ImageIcon(ImageLoader.readImage("img/blue_hover_3.png"))
			}
		};
		brokenIce = new ImageIcon(ImageLoader.readImage("img/broken_ice.png"));
	}

	public void initializeIce(int j, int i, int rand, int mustHitCount) {
		if (myTurn.isMyTurn()) return;
		ices[j][i] = new JButton(iceIcon[rand][0]);
		countIce[rand]++;
		if (i % 2 == 0) {
			gs.addComponent(ices[j][i], 100, i * 75 + 50, j * 86 + 43 + 120, 100, 100);
		} else {
			gs.addComponent(ices[j][i], 100, i * 75 + 50, j * 86 + 120, 100, 100);
		}
		ices[j][i].setBorderPainted(false);
		ices[j][i].addMouseListener(this);
		ices[j][i].addMouseMotionListener(this);
		ices[j][i].setContentAreaFilled(false);
		ices[j][i].setActionCommand(Integer.toString(i + j * icesX));
		mustHitNum[j][i] = mustHitCount;
		hitCount[j][i] = 0;
	}

	public void spinTheRoulette() {
		int roulette = random.nextInt(6);
		switch (roulette) {
		case 0:
			breakIce[white] = 0;
			breakIce[blue] = 2;
			break;
		case 1:
			breakIce[white] = 2;
			breakIce[blue] = 0;
			break;
		case 2:
			breakIce[white] = 1;
			breakIce[blue] = 1;
		case 3:
			breakIce[white] = 1;
			breakIce[blue] = 0;
			break;
		case 4:
			breakIce[white] = 0;
			breakIce[blue] = 1;
			break;
		case 5:
			breakIce[white] = 3;
			breakIce[blue] = 3;
			break;
		default:
			break;
		}
		setNumIcon();
	}

	public void setNumIcon() {
		for (int i = 0; i <= blue; i++) {
			numLabel[i].setIcon(numIcon[i][breakIce[i]]);
		}
	}

	public void breakIce(MouseEvent e) {
		if (!myTurn.isMyTurn()) return;
		JButton jb = (JButton)e.getComponent();
		Icon icon = jb.getIcon();
		if (icon == brokenIce) {
			return;
		}
		int jbNum = Integer.parseInt(jb.getActionCommand());
		int diff = 0;
		System.out.println("clicked: " + jbNum);
		for (int j = 0; j <= blue; j++) {
			for (int i = 0; i < 3; i++) {
				if (breakIce[j] > 0) {
					if (icon == hoverIcon[j][i]) {
						if (!moveFlag) {
							timer = new Timer(1, new PenguinMove(penguin, this, Integer.parseInt(jb.getActionCommand())));
							timer.start();
						}
						hitCount[jbNum / icesX][jbNum % icesX]++;
						if (hitCount[jbNum / icesX][jbNum % icesX] >= mustHitNum[jbNum / icesX][jbNum % icesX]) {
							breakIce[j]--;
							countIce[j]--;
							if (breakIce[j] < 0) {
								breakIce[j] = 0;
							}
							if (countIce[j] < breakIce[j]) {
								breakIce[j] = countIce[j];
							}
							jb.setIcon(brokenIce);
							setNumIcon();
						}
						diff = mustHitNum[jbNum / icesX][jbNum % icesX] - hitCount[jbNum / icesX][jbNum % icesX];
						if (diff > 0) {
							if (diff < 3) {
								jb.setIcon(hoverIcon[j][2]);
							} else if (diff < 5) {
								jb.setIcon(hoverIcon[j][1]);
							}
						}
					}
				}
			}
		}

		if (breakIce[white] <= 0 && breakIce[blue] <= 0) {
			spinTheRoulette();
		}
	}

	public void changeIceIcon(MouseEvent e) {
		if (!myTurn.isMyTurn()) return;
		JButton jb = (JButton) e.getComponent();
		Icon icon = jb.getIcon();
		for (int j = 0; j <= blue; j++) {
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

	public int getIcesX() {
		return icesX;
	}

	public Icon getBrokenIceIcon() {
		return brokenIce;
	}

	public void setMoveFlag(boolean moveFlag) {
		if (!moveFlag) {
			timer.stop();
		}
		this.moveFlag = moveFlag;
	}

	public void mouseReleased(MouseEvent e) {
		hammer.changeHammerIcon();
	}

	public void mousePressed(MouseEvent e) {
		hammer.changeHammerIcon();
	}

	public void mouseExited(MouseEvent e) {
		changeIceIcon(e);
	}

	public void mouseEntered(MouseEvent e) {
		changeIceIcon(e);
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
		private double x0, x1, y0, y1, x, y, speed;
		public PenguinMove(Penguin penguin, Ices ices, int jbNum) {
			this.penguin = penguin;
			this.ices = ices;
			x0 = penguin.getPenguinX();
			y0 = penguin.getPenguinY();
			x1 = ices.ices[jbNum / ices.getIcesX()][jbNum % ices.getIcesX()].getX();
			y1 = ices.ices[jbNum / ices.getIcesX()][jbNum % ices.getIcesX()].getY();
			x = x0 < x1 ? x0 + 1 : x0 - 1;
			y = y0 < y1 ? y0 + 1 : y0 - 1;
			speed = 0.2;
			ices.setMoveFlag(true);
		}

		public void actionPerformed(ActionEvent e) {
			if (speed > 0) {
				if (Math.abs(x1 - x0) < 25) {
					penguin.penguinMove(x, y);
					if (y0 < y1) {
						y += speed;
					} else if (y0 >= y1) {
						y -= speed;
					}
				} else if (x0 < x1) {
					penguin.penguinMove(x, Calculation.lerp(x0, y0, x1, y1, x));
					x += speed;
				} else if (x0 >= x1) {
					penguin.penguinMove(x, Calculation.lerp(x1, y1, x0, y0, x));
					x -= speed;
				}
				speed = speed < 0 ? 0 : speed - 0.002;
				penguin.penguinFall(ices, ices.brokenIce);
			} else {
				ices.setMoveFlag(false);
			}
		}
	}
}
