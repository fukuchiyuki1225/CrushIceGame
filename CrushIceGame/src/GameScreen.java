import java.awt.Component;
import java.awt.Container;
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
	private static GameScreen gs = new GameScreen();
	private Container c;
	private JLayeredPane title, game, gameOver;
	private String currentScreen;
	private Hammer hammer;
	private MesgSend ms;
	private Penguin penguin;
	private Ices ices;
	private ItemManager im;
	private int myTurn;
	private final int start = 0, help = 1, setting = 2, again = 3, toTitle = 4, nomal = 0, hover = 1;
	private JButton[] buttons;
	private JLabel helpLabel, turnLabel;
	private JButton helpClose;
	private ImageIcon[][] UI;
	private ImageIcon[] turnIcon;

	private GameScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("クラッシュアイスゲーム");
		setSize(1200, 935);
		setLocationRelativeTo(null);
		setResizable(false);
		c = getContentPane();
		title = null;
		game = null;
		gameOver = null;
		currentScreen = "";
		hammer = Hammer.getInstance();
		buttons = new JButton[toTitle + 1];
		ms = MesgSend.getInstance();
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(ImageLoader.loadImage("img/cursor.png")).getImage(), new Point(), ""));
		loadImage();
		setTitleScreen();
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

	// タイトル画面
	public void setTitleScreen() {
		currentScreen = "title";
		// 前のシーンを取り除く
		removeScreen(game);
		if (gameOver != null) {
			gameOver.setVisible(false);
		}
		// 初回のみコンポーネントの設置などを行う
		if (title == null) {
			title = new JLayeredPane();
			title.addMouseMotionListener(this);
			c.add(title);
			addComponent(new JLabel(new ImageIcon(ImageLoader.loadImage("img/title.png"))), 0, 0, 0, 1200, 900);

			for (int i = start; i <= setting; i++) {
				buttons[i] = new JButton(UI[nomal][i]);
				setButton(buttons[i], this, this);
				addComponent(buttons[i], 100, 100, 450 + i * 100, 458, 93);
			}
			// あそびかた
			helpLabel = new JLabel(new ImageIcon(ImageLoader.loadImage("img/help_dialog.png")));
			addComponent(helpLabel, 200, 145, 125, 900, 654);
			helpLabel.setVisible(false);
			helpClose = new JButton(new ImageIcon (ImageLoader.loadImage("img/help_close.png")));
			setButton(helpClose, this, this);
			addComponent(helpClose, 210, 900, 120, 117, 100);
			helpClose.setVisible(false);
		}
		addComponent(hammer.getHammerLabel(), 1500, 0, 0, 200, 170);

		title.setVisible(true);
		cleanButtons();
		hammer.cleanHammerIcon();
	}

	public void setGameScreen(int num) {
		currentScreen = "game";

		title.setVisible(false);
		if (gameOver != null) {
			gameOver.setVisible(false);
		}

		if (num % 2 == 0) {
			myTurn = 0;
		} else {
			myTurn = 1;
		}

		game = new JLayeredPane();
		game.addMouseMotionListener(this);
		c.add(game);
		addComponent(new JLabel(new ImageIcon(ImageLoader.loadImage("img/sea.png"))), 0, 0, 0, 1200, 900);
		addComponent(new JLabel(new ImageIcon(ImageLoader.loadImage("img/logo.png"))), 900, 760, 50, 400, 315);
		penguin = new Penguin();
		im = new ItemManager();
		ices = new Ices();
		turnLabel = new JLabel(turnIcon[getMyTurn()]);
		addComponent(turnLabel, 800, 875, 350, 250, 120);
		addComponent(hammer.getHammerLabel(), 1500, 0, 0, 200, 170);

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

			for (int i = again; i <= toTitle; i++) {
				buttons[i] = new JButton(UI[nomal][i]);
				setButton(buttons[i], this, this);
				addComponent(buttons[i], 1200, 350, 400 + (i - again) * 100, 458, 93);
			}
		}

		if (isMyTurn()) {
			addComponent(new JLabel(new ImageIcon(ImageLoader.loadImage("img/lose.png"))), 1000, 0, 0, 1200, 900);
		} else {
			addComponent(new JLabel(new ImageIcon(ImageLoader.loadImage("img/win.png"))), 1000, 0, 0, 1200, 900);
		}

		addComponent(hammer.getHammerLabel(), 1500, 0, 0, 200, 170);

		gameOver.setVisible(true);
		removeScreen(game);
		cleanButtons();
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

	public ItemManager getItemManager() {
		return im;
	}

	public static GameScreen getInstance() {
		return gs;
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
		hammer.cleanHammerIcon();
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

	public void cleanButtons() {
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
			ms.send("join");
		} else if (icon == UI[hover][again] && currentScreen.equals("gameOver")) {
			ms.send("join");
		} else if (icon == UI[hover][toTitle] && currentScreen.equals("gameOver")) {
			ms.send("toTitle");
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
		hammer.changeHammerIcon(this);
	}

	public void mouseReleased(MouseEvent e) {
		hammer.changeHammerIcon(this);
	}

	public void mouseEntered(MouseEvent e) {
		if (currentScreen.equals("title") || currentScreen.equals("gameOver")) hoverUIIcon(e);
	}

	public void mouseExited(MouseEvent e) {
		if (currentScreen.equals("title") || currentScreen.equals("gameOver")) hoverUIIcon(e);
	}
}
