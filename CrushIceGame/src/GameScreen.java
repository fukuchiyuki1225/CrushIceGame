import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.SwingUtilities;

public class GameScreen extends JFrame implements MouseListener, MouseMotionListener {
	private Container c;
	private JLayeredPane title, game, gameOver;
	private Cursor cursor;
	private MesgSend ms;
	private Hammer hammer;
	private Penguin penguin;
	private Ices ices;
	private int myTurn;
	private final int start = 0, help = 1, setting = 2, again = 3, toTitle = 4, nomal = 0, hover = 1;
	private JButton[] buttons;
	private JLabel helpLabel, turnLabel;
	private JButton helpClose;
	private ImageIcon[][] UI;
	private ImageIcon[] turnIcon;
	private String currentScreen;

	public GameScreen(int num, MesgSend ms) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("クラッシュアイスゲーム");
		setSize(1200, 935);
		setLocationRelativeTo(null);
		setResizable(false);

		c = getContentPane();
		currentScreen = "title";

		title = null;
		game = null;
		gameOver = null;

		hammer = new Hammer(this);
		buttons = new JButton[toTitle + 1];

		cursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(ImageLoader.loadImage("img/cursor.png")).getImage(), new Point(), "");
		setCursor(cursor);

		if (num % 2 == 0) {
			myTurn = 0;
		} else {
			myTurn = 1;
		}

		loadImage();
		setTitleScreen();
		this.ms = ms;
	}

	public void loadImage() {
		UI = new ImageIcon[][] {
			{
				new ImageIcon(ImageLoader.loadImage("img/start.png")),
				new ImageIcon(ImageLoader.loadImage("img/help.png")),
				new ImageIcon(ImageLoader.loadImage("img/setting.png")),
				new ImageIcon(ImageLoader.loadImage("img/again.png")),
				new ImageIcon(ImageLoader.loadImage("img/to_title.png"))
			},
			{
				new ImageIcon(ImageLoader.loadImage("img/start_2.png")),
				new ImageIcon(ImageLoader.loadImage("img/help_2.png")),
				new ImageIcon(ImageLoader.loadImage("img/setting_2.png")),
				new ImageIcon(ImageLoader.loadImage("img/again_2.png")),
				new ImageIcon(ImageLoader.loadImage("img/to_title_2.png"))
			}
		};
		turnIcon = new ImageIcon[] {
				new ImageIcon(ImageLoader.loadImage("img/your_turn.png")),
				new ImageIcon(ImageLoader.loadImage("img/my_turn.png"))
		};
	}

	public void setTitleScreen() {
		currentScreen = "title";

		removeScreen(game);
		if (gameOver != null) {
			gameOver.setVisible(false);
		}

		if (title == null) {
			title = new JLayeredPane();
			title.addMouseMotionListener(this);
			c.add(title);

			addComponent(new JLabel(new ImageIcon(ImageLoader.loadImage("img/title.png"))), 0, 0, 0, 1200, 900);

			buttons[start] = new JButton(UI[nomal][start]);
			setButton(buttons[start], this, this);
			addComponent(buttons[start], 100, 100, 450, 458, 93);

			buttons[help] = new JButton(UI[nomal][help]);
			setButton(buttons[help], this, this);
			addComponent(buttons[help], 100, 100, 550, 458, 93);

			buttons[setting] = new JButton(UI[nomal][setting]);
			setButton(buttons[setting], this, this);
			addComponent(buttons[setting], 100, 100, 650, 458, 93);

			helpLabel = new JLabel(new ImageIcon(ImageLoader.loadImage("img/help_dialog.png")));
			addComponent(helpLabel, 200, 145, 125, 900, 654);
			helpLabel.setVisible(false);
			helpClose = new JButton(new ImageIcon (ImageLoader.loadImage("img/help_close.png")));
			setButton(helpClose, this, this);
			addComponent(helpClose, 210, 900, 120, 117, 100);
			helpClose.setVisible(false);
		}
		addComponent(hammer.getHammer(), 1500, 0, 0, 200, 170);

		title.setVisible(true);
		cleanButton();
		hammer.cleanHammerIcon();
	}

	public void setGameScreen() {
		currentScreen = "game";

		title.setVisible(false);
		if (gameOver != null) {
			gameOver.setVisible(false);
		}

		game = new JLayeredPane();
		game.addMouseMotionListener(this);
		c.add(game);
		addComponent(new JLabel(new ImageIcon(ImageLoader.loadImage("img/sea.png"))), 0, 0, 0, 1200, 900);
		addComponent(new JLabel(new ImageIcon(ImageLoader.loadImage("img/logo.png"))), 900, 760, 50, 400, 315);
		penguin = new Penguin(this);
		ices = new Ices(hammer, penguin, this);
		turnLabel = new JLabel(turnIcon[getMyTurn()]);
		addComponent(turnLabel, 800, 875, 350, 250, 120);
		addComponent(hammer.getHammer(), 1500, 0, 0, 200, 170);

		game.setVisible(true);
		hammer.cleanHammerIcon();
	}

	public void setGameOverScreen() {
		currentScreen = "gameOver";

		penguin.getPenguin().setVisible(false);

		if (gameOver == null) {
			gameOver = new JLayeredPane();
			gameOver.addMouseMotionListener(this);
			c.add(gameOver);

			buttons[again] = new JButton(UI[nomal][again]);
			setButton(buttons[again], this, this);
			addComponent(buttons[again], 1200, 350, 400, 458, 93);

			buttons[toTitle] = new JButton(UI[nomal][toTitle]);
			setButton(buttons[toTitle], this, this);
			addComponent(buttons[toTitle], 1200, 350, 500, 458, 93);
		}

		if (isMyTurn()) {
			addComponent(new JLabel(new ImageIcon(ImageLoader.loadImage("img/lose.png"))), 1000, 0, 0, 1200, 900);
		} else {
			addComponent(new JLabel(new ImageIcon(ImageLoader.loadImage("img/win.png"))), 1000, 0, 0, 1200, 900);
		}

		addComponent(hammer.getHammer(), 1500, 0, 0, 200, 170);

		gameOver.setVisible(true);
		removeScreen(game);
		cleanButton();
		hammer.cleanHammerIcon();
	}

	public void removeScreen(JLayeredPane j) {
		if (j == null) return;
		j.setVisible(false);
		c.remove(j);
		j = null;
	}

	public void addComponent(Component comp, int layer, int x, int y, int width, int height) {
		JLayeredPane j = null;
		switch (currentScreen) {
		case "title":
			j = title;
			break;
		case "game":
			j = game;
			break;
		case "gameOver":
			j = gameOver;
		default:
			break;
		}
		j.setLayer(comp, layer);
		j.add(comp);
		comp.setBounds(x, y, width, height);
	}

	public void setButton(JButton jb, MouseListener m1, MouseMotionListener m2) {
		jb.addMouseListener(m1);
		jb.addMouseMotionListener(m2);
		jb.setBorderPainted(false);
		jb.setContentAreaFilled(false);
	}

	public Ices getIces() {
		return ices;
	}

	public Penguin getPenguin() {
		return penguin;
	}

	public Hammer getHammer() {
		return hammer;
	}

	public String getCurrentScreen() {
		return currentScreen;
	}

	public boolean isMyTurn() {
		if (myTurn == 1) {
			return true;
		}
		return false;
	}

	public int getMyTurn() {
		return myTurn;
	}

	public void setMyTurn() {
		myTurn = 1 - myTurn;
		turnLabel.setIcon(turnIcon[getMyTurn()]);
		ices.spinTheRoulette();
		ices.cleanIceIcon();
		hammer.cleanHammerIcon();
	}

	public void send(String mesg) {
		ms.send(mesg);
	}

	public void hoverUIIcon(MouseEvent e) {
		JButton jb = (JButton)e.getComponent();
		Icon icon = jb.getIcon();
		for (int j = nomal; j <= hover; j++) {
			for (int i = start; i <= toTitle; i++) {
				if (icon == UI[j][i]) {
					if (j == nomal) {
						jb.setIcon(UI[hover][i]);
						return;
					} else if (j == hover) {
						jb.setIcon(UI[nomal][i]);
						return;
					}
				}
			}
		}
	}

	public void cleanButton() {
		for (int i = start; i <= toTitle; i++) {
			if (buttons[i] == null) break;
			if (buttons[i].getIcon() == UI[hover][i]) {
				buttons[i].setIcon(UI[nomal][i]);
			}
		}
	}

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		SwingUtilities.convertPointToScreen(p, e.getComponent());
		SwingUtilities.convertPointFromScreen(p, e.getComponent().getParent());
		hammer.setHammerLocation(p);
	}

	public void mouseClicked(MouseEvent e) {
		JButton jb = (JButton)e.getComponent();
		Icon icon = jb.getIcon();
		if (icon == UI[hover][start] && currentScreen.equals("title")) {
			send("join");
		} else if (icon == UI[hover][again] && currentScreen.equals("gameOver")) {
			send("join");
		} else if (icon == UI[hover][toTitle] && currentScreen.equals("gameOver")) {
			send("toTitle");
		} else if (icon == UI[hover][help] && currentScreen.equals("title")) {
			helpLabel.setVisible(true);
			helpClose.setVisible(true);
			buttons[start].setVisible(false);
			buttons[help].setVisible(false);
			buttons[setting].setVisible(false);
		} else if (icon == helpClose.getIcon() && currentScreen.equals("title")) {
			helpLabel.setVisible(false);
			helpClose.setVisible(false);
			buttons[start].setVisible(true);
			buttons[help].setVisible(true);
			buttons[setting].setVisible(true);
		}
	}

	public void mousePressed(MouseEvent e) {
		hammer.changeHammerIcon();
	}

	public void mouseReleased(MouseEvent e) {
		hammer.changeHammerIcon();
	}

	public void mouseEntered(MouseEvent e) {
		if (currentScreen.equals("title") || currentScreen.equals("gameOver")) hoverUIIcon(e);
	}

	public void mouseExited(MouseEvent e) {
		if (currentScreen.equals("title") || currentScreen.equals("gameOver")) hoverUIIcon(e);
	}
}
