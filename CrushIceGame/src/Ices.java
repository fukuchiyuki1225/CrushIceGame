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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class Ices extends JFrame implements MouseListener, MouseMotionListener {
	private ImageIcon whiteIce, whiteIce2, whiteIce3, whiteHover, whiteHover2, whiteHover3, blueIce, blueIce2, blueIce3, blueHover, blueHover2, blueHover3, brokenIce;
	private ImageIcon white0, white1, white2, white3, blue0, blue1, blue2, blue3;
	private ImageIcon[][] numIcon, iceIcon, hoverIcon;
	private JButton ices[][];
	private final int icesX = 9, icesY = 7, white = 0, blue = 1;
	private JLabel whiteLabel, whiteLabel2, blueLabel, blueLabel2;
	private JLabel[] numLabel;
	private int[][] hitCount, mustHitNum;
	private int countWhite, countBlue, roulette, breakWhite, breakBlue;
	private int[] countIce, breakIce;
	private Hammer hammer;
	private Penguin penguin;
	private Random random;
	private boolean moveFlag;
	private Timer timer;

	public Ices(Hammer hammer, Penguin penguin, GameScreen gs) {
		loadIceIcon();
		ices = new JButton[icesY][icesX];
		hitCount = new int[icesY][icesX];
		mustHitNum = new int[icesY][icesX];
		countIce = new int[2];
		breakIce = new int[2];
		this.hammer = hammer;
		this.penguin = penguin;

		random = new Random();
		for (int j = 0; j < 7; j++) {
			for (int i = 0; i < icesX; i++) {
				/*if (random.nextInt(2) == 0) {
					// ices[j][i] = new JButton(whiteIce);
					ices[j][i] = new JButton(iceIcon[white][0]);
					countWhite++;
				} else {
					// ices[j][i] = new JButton(blueIce);
					ices[j][i] = new JButton(iceIcon[blue][0]);
					countBlue++;
				}*/
				int rand = random.nextInt(2);
				ices[j][i] = new JButton(iceIcon[rand][0]);
				countIce[rand]++;
				if (i % 2 == 0) {
					gs.addComponent(ices[j][i], 100, i * 75 + 50, j * 86 + 43 + 100, 100, 100);
				} else {
					gs.addComponent(ices[j][i], 100, i * 75 + 50, j * 86 + 100, 100, 100);
				}
				ices[j][i].setBorderPainted(false);
				ices[j][i].addMouseListener(this);
				ices[j][i].addMouseMotionListener(this);
				ices[j][i].setContentAreaFilled(false);
				ices[j][i].setActionCommand(Integer.toString(i + j * icesX));
				mustHitNum[j][i] = random.nextInt(5) + 1;
				hitCount[j][i] = 0;
			}
		}

		whiteLabel = new JLabel(white0);
		gs.addComponent(whiteLabel, 800, 925, 358, 100, 100);

		whiteLabel2 = new JLabel(new ImageIcon(ImageLoader.readImage("img/white.png")));
		gs.addComponent(whiteLabel2, 850, 850, 315, 100, 100);

		blueLabel = new JLabel(blue0);
		gs.addComponent(blueLabel, 800, 925, 444, 100, 100);

		blueLabel2 = new JLabel(new ImageIcon(ImageLoader.readImage("img/blue.png")));
		gs.addComponent(blueLabel2, 850, 850, 401, 100, 100);

		numLabel = new JLabel[] {
				new JLabel(numIcon[white][0]),
				new JLabel(numIcon[blue][0])
		};

		gs.addComponent(numLabel[0], 800, 925, 358, 100, 100);
		gs.addComponent(numLabel[1], 800, 925, 444, 100, 100);

		spinTheRoulette();
		moveFlag = false;
	}

	private void loadIceIcon() {
		/*whiteIce = new ImageIcon(ImageLoader.readImage("img/white_ice.png"));
		whiteIce2 = new ImageIcon(ImageLoader.readImage("img/white_ice_2.png"));
		whiteIce3 = new ImageIcon(ImageLoader.readImage("img/white_ice_3.png"));
		whiteHover = new ImageIcon(ImageLoader.readImage("img/white_hover.png"));
		whiteHover2 = new ImageIcon(ImageLoader.readImage("img/white_hover_2.png"));
		whiteHover3 = new ImageIcon(ImageLoader.readImage("img/white_hover_3.png"));
		blueIce = new ImageIcon(ImageLoader.readImage("img/blue_ice.png"));
		blueIce2 = new ImageIcon(ImageLoader.readImage("img/blue_ice_2.png"));
		blueIce3 = new ImageIcon(ImageLoader.readImage("img/blue_ice_3.png"));
		blueHover = new ImageIcon(ImageLoader.readImage("img/blue_hover.png"));
		blueHover2 = new ImageIcon(ImageLoader.readImage("img/blue_hover_2.png"));
		blueHover3 = new ImageIcon(ImageLoader.readImage("img/blue_hover_3.png"));
		brokenIce = new ImageIcon(ImageLoader.readImage("img/broken_ice.png"));
		white0 = new ImageIcon(ImageLoader.readImage("img/white_0.png"));
		white1 = new ImageIcon(ImageLoader.readImage("img/white_1.png"));
		white2 = new ImageIcon(ImageLoader.readImage("img/white_2.png"));
		white3 = new ImageIcon(ImageLoader.readImage("img/white_3.png"));
		blue0 = new ImageIcon(ImageLoader.readImage("img/blue_0.png"));
		blue1 = new ImageIcon(ImageLoader.readImage("img/blue_1.png"));
		blue2 = new ImageIcon(ImageLoader.readImage("img/blue_2.png"));
		blue3 = new ImageIcon(ImageLoader.readImage("img/blue_3.png"));*/

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
	}

	public void spinTheRoulette() {
		roulette = random.nextInt(6);
		switch (roulette) {
		case 0:
			/*breakWhite = 0;
			breakBlue = 2;*/
			breakIce[white] = 0;
			breakIce[blue] = 2;
			break;
		case 1:
			/*breakWhite = 2;
			breakBlue = 0;*/
			breakIce[white] = 2;
			breakIce[blue] = 0;
			break;
		case 2:
			/*breakWhite = 1;
			breakBlue = 1;*/
			breakIce[white] = 1;
			breakIce[blue] = 1;
		case 3:
			/*breakWhite = 1;
			breakBlue = 0;*/
			breakIce[white] = 1;
			breakIce[blue] = 0;
			break;
		case 4:
			/*breakWhite = 0;
			breakBlue = 1;*/
			breakIce[white] = 0;
			breakIce[blue] = 1;
			break;
		case 5:
			/*breakWhite = 3;
			breakBlue = 3;*/
			breakIce[white] = 3;
			breakIce[blue] = 3;
			break;
		default:
			break;
		}
		/*if (countWhite < breakWhite) {
			breakWhite = countWhite;
		}
		if (countBlue < breakBlue) {
			breakBlue = countBlue;
		}*/
		setNumIcon();
	}

	public void setNumIcon() {
		for (int i = 0; i <= blue; i++) {
			numLabel[i].setIcon(numIcon[i][breakIce[i]]);
			System.out.println("breakIce" + breakIce[i]);
		}
		/*if (color == 0) {
			switch(count) {
			case 0:
				whiteLabel.setIcon(white0);
				break;
			case 1:
				whiteLabel.setIcon(white1);
				break;
			case 2:
				whiteLabel.setIcon(white2);
				break;
			case 3:
				whiteLabel.setIcon(white3);
				break;
			default:
				break;
			}
		} else {
			switch(count) {
			case 0:
				blueLabel.setIcon(blue0);
				break;
			case 1:
				blueLabel.setIcon(blue1);
				break;
			case 2:
				blueLabel.setIcon(blue2);
				break;
			case 3:
				blueLabel.setIcon(blue3);
				break;
			default:
				break;
			}
		}*/
	}

	public void breakIce(JButton jb) {
		Icon icon = jb.getIcon();
		if (icon == brokenIce) {
			return;
		}
		int jbNum = Integer.parseInt(jb.getActionCommand());
		int diff = 0;

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
							jb.setIcon(brokenIce);
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

		for (int i = 0; i <= blue; i++) {
			if (countIce[i] < breakIce[i]) {
				breakIce[i] = countIce[i];
			}
			if (breakIce[i] < 0) {
				breakIce[i] = 0;
			}
			System.out.println(breakIce[i]);
		}

		/*if (((icon == whiteHover || icon == whiteHover2 || icon == whiteHover3) && breakWhite > 0) || ((icon == blueHover || icon == blueHover2 || icon == blueHover3) && breakBlue > 0)) {
			if (!moveFlag) {
				timer = new Timer(1, new PenguinMove(penguin, this, Integer.parseInt(jb.getActionCommand())));
				timer.start();
			}
			hitCount[jbNum / icesX][jbNum % icesX]++;
			if (hitCount[jbNum / icesX][jbNum % icesX] >= mustHitNum[jbNum / icesX][jbNum % icesX]) {
				if (icon == whiteHover || icon == whiteHover2 || icon == whiteHover3) {
					breakWhite--;
					countWhite--;
				} else if (icon == blueHover || icon == blueHover2 || icon == blueHover3) {
					breakBlue--;
					countBlue--;
				}
				jb.setIcon(brokenIce);
			}
			diff = mustHitNum[jbNum / icesX][jbNum % icesX] - hitCount[jbNum / icesX][jbNum % icesX];
			if (diff > 0) {
				if (diff < 3) {
					if (icon == whiteHover || icon == whiteHover2) {
						jb.setIcon(whiteHover3);
					} else if (icon == blueHover || icon == blueHover2) {
						jb.setIcon(blueHover3);
					}
				} else if (diff < 5) {
					if (icon == whiteHover) {
						jb.setIcon(whiteHover2);
					} else if (icon == blueHover) {
						jb.setIcon(blueHover2);
					}
				}
			}*/
		// }

		setNumIcon();

		if (breakIce[white] <= 0 && breakIce[blue] <= 0) {
			spinTheRoulette();
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
		JButton jb = (JButton) e.getComponent();
		Icon icon = jb.getIcon();
		// Icon chIcon = icon;
		for (int j = 0; j <= blue; j++) {
			for (int i = 0; i < 3; i++) {
				if (icon == hoverIcon[j][i]) {
					jb.setIcon(iceIcon[j][i]);
					return;
				}
			}
		}
		/*if (icon == whiteHover) {
			chIcon = whiteIce;
		} else if (icon == whiteHover2) {
			chIcon = whiteIce2;
		} else if (icon == whiteHover3) {
			chIcon = whiteIce3;
		} else if (icon == blueHover) {
			chIcon = blueIce;
		} else if (icon == blueHover2) {
			chIcon = blueIce2;
		} else if (icon == blueHover3) {
			chIcon = blueIce3;
		}
		jb.setIcon(chIcon);*/
	}

	public void mouseEntered(MouseEvent e) {
		JButton jb = (JButton) e.getComponent();
		Icon icon = jb.getIcon();
		for (int j = 0; j <= blue; j++) {
			for (int i = 0; i < 3; i++) {
				if (icon == iceIcon[j][i]) {
					jb.setIcon(hoverIcon[j][i]);
					return;
				}
			}
		}
		/*Icon chIcon = icon;
		if (icon == whiteIce) {
			chIcon = whiteHover;
		} else if (icon == whiteIce2) {
			chIcon = whiteHover2;
		} else if (icon == whiteIce3) {
			chIcon = whiteHover3;
		} else if (icon == blueIce) {
			chIcon = blueHover;
		} else if (icon == blueIce2) {
			chIcon = blueHover2;
		} else if (icon == blueIce3) {
			chIcon = blueHover3;
		}
		jb.setIcon(chIcon);*/
	}

	public void mouseClicked(MouseEvent e) {
		JButton jb = (JButton) e.getComponent();
		System.out.println(jb.getActionCommand());
		breakIce(jb);
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
			ices.setMoveFlag(true);
			x = x0 < x1 ? x0 + 1 : x0 - 1;
			y = y0 < y1 ? y0 + 1 : y0 - 1;
			speed = 0.2;
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
				setSpeed(speed - 0.002);
				penguin.penguinFall(ices, ices.brokenIce);
			} else {
				ices.setMoveFlag(false);
			}
		}

		public void setSpeed(double speed) {
			this.speed = speed;
			if (this.speed < 0) {
				this.speed = 0;
			}
		}
	}

}
