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
	private ImageIcon whiteIce, blueIce, whiteHover, blueHover, brokenIce;
	private JButton ices[][];
	private JLabel whiteLabel, whiteLabel2, blueLabel, blueLabel2;
	private int[][] hitCount, mustHitNum;
	private int countWhite, countBlue, roulette, breakWhite, breakBlue;
	private Hammer hammer;
	private Penguin penguin;
	private Random random;
	private boolean moveFlag;
	private Timer timer;

	public Ices(Hammer hammer, Penguin penguin) {
		whiteIce = new ImageIcon("img/white_ice.png");
		blueIce = new ImageIcon("img/blue_ice.png");
		whiteHover = new ImageIcon("img/white_hover.png");
		blueHover = new ImageIcon("img/blue_hover.png");
		brokenIce = new ImageIcon("img/broken_ice.png");
		ices = new JButton[7][9];
		hitCount = new int[7][9];
		mustHitNum = new int[7][9];
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
				mustHitNum[j][i] = random.nextInt(5) + 1;
				hitCount[j][i] = 0;
			}
		}

		whiteLabel = new JLabel(whiteIce);
		whiteLabel.setText(Integer.toString(breakWhite));
		whiteLabel.setHorizontalTextPosition(JLabel.CENTER);
		Game.j.setLayer(whiteLabel, 800);
		Game.j.add(whiteLabel);
		whiteLabel.setBounds(925, 358, 100, 100);

		whiteLabel2 = new JLabel(whiteIce);
		whiteLabel2.setText("白");
		whiteLabel2.setHorizontalTextPosition(JLabel.CENTER);
		Game.j.setLayer(whiteLabel2, 800);
		Game.j.add(whiteLabel2);
		whiteLabel2.setBounds(850, 315, 100, 100);

		blueLabel = new JLabel(blueIce);
		blueLabel.setText(Integer.toString(breakBlue));
		blueLabel.setHorizontalTextPosition(JLabel.CENTER);
		Game.j.setLayer(blueLabel, 800);
		Game.j.add(blueLabel);
		blueLabel.setBounds(925, 444, 100, 100);

		blueLabel2 = new JLabel(blueIce);
		blueLabel2.setText("青");
		blueLabel2.setHorizontalTextPosition(JLabel.CENTER);
		Game.j.setLayer(blueLabel2, 800);
		Game.j.add(blueLabel2);
		blueLabel2.setBounds(850, 401, 100, 100);

		spinTheRoulette();
		moveFlag = false;
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
		if (countWhite < breakWhite) {
			breakWhite = countWhite;
		}
		if (countBlue < breakBlue) {
			breakBlue = countBlue;
		}
		whiteLabel.setText(Integer.toString(breakWhite));
		blueLabel.setText(Integer.toString(breakBlue));
	}

	public void breakIce(JButton jb) {
		Icon icon = jb.getIcon();
		int jbNum = Integer.parseInt(jb.getActionCommand());
		if (!moveFlag && jb.getIcon() != brokenIce) {
			timer = new Timer(1, new PenguinMove(penguin, this, Integer.parseInt(jb.getActionCommand())));
			timer.start();
		}
		hitCount[jbNum / 9][jbNum % 9]++;
		if (hitCount[jbNum / 9][jbNum % 9] >= mustHitNum[jbNum / 9][jbNum % 9]) {
			if (icon == whiteHover && breakWhite > 0) {
				breakWhite--;
				countWhite--;
			} else if (icon == blueHover && breakBlue > 0) {
				breakBlue--;
				countBlue--;
			}
			jb.setIcon(brokenIce);
		}
		if (breakWhite <= 0 && breakBlue <= 0) {
			spinTheRoulette();
		}
		whiteLabel.setText(Integer.toString(breakWhite));
		blueLabel.setText(Integer.toString(breakBlue));
	}

	public JButton[][] getIces() {
		return ices;
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
		// TODO 自動生成されたメソッド・スタブ

	}

	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		SwingUtilities.convertPointToScreen(p, e.getComponent());
		SwingUtilities.convertPointFromScreen(p, e.getComponent().getParent());
		hammer.setHammerLocation(p);
	}

	public static class PenguinMove implements ActionListener {
		private Penguin penguin;
		private JLabel penguinLabel;
		private Ices ices;
		private double x0, x1, y0, y1, x, y, speed;
		private int distance;
		public PenguinMove(Penguin penguin, Ices ices, int jbNum) {
			this.penguin = penguin;
			penguinLabel = penguin.getPenguin();
			this.ices = ices;
			x0 = penguinLabel.getX();
			y0 = penguinLabel.getY();
			x1 = ices.ices[jbNum / 9][jbNum % 9].getX();
			y1 = ices.ices[jbNum / 9][jbNum % 9].getY();
			ices.setMoveFlag(true);
			x = x0 < x1 ? x0 + 1 : x0 - 1;
			y = y0 < y1 ? y0 + 1 : y0 - 1;
			distance = penguin.calcDistance(x0, y0, x1, y1);
			/*if (distance > 800) {
				speed = 1.0;
			} else if (distance > 450) {
				speed = 0.5;
			} else if (distance > 225) {
				speed = 0.4;
			} else {
				speed = 0.2;
			}*/
			speed = 0.2;
			System.out.println(distance);
		}

		public void actionPerformed(ActionEvent e) {
			if (speed > 0) {
				if (Math.abs(x1 - x0) < 25) {
					if (y0 < y1) {
						penguinLabel.setLocation((int)Math.ceil(x), (int)Math.ceil(y));
						y += speed;
						setSpeed(speed - 0.002);
					} else if (y0 > y1) {
						penguinLabel.setLocation((int)Math.ceil(x), (int)Math.ceil(y));
						y -= speed;
						setSpeed(speed - 0.002);
					}
				} else if (x0 < x1) {
					penguinLabel.setLocation((int)Math.ceil(x), (int)Math.ceil(penguin.lerp(x0, y0, x1, y1, x)));
					x += speed;
					setSpeed(speed - 0.002);
				} else if (x0 > x1) {
					penguinLabel.setLocation((int)Math.ceil(x), (int)Math.ceil(penguin.lerp(x1, y1, x0, y0, x)));
					x -= speed;
					setSpeed(speed - 0.002);
				}
				penguin.penguinFall(ices);
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
