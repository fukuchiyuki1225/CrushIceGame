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
	private ImageLoader il;
	private final int start = 0, help = 1, setting = 2, again = 3, toTitle = 4, nomal = 0, hover = 1;
	private ImageIcon[][] UI;
	private ImageIcon[] turnIcons, wlIcons;
	private JButton[] buttons;
	private JLabel helpLabel, turnLabel, wlLabel;
	private JButton helpClose;
	private int myTurn;
	private Penguin penguin;
	private ItemManager im;
	private Ices ices;

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
		il = ImageLoader.getInstance();
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(il.load("img/cursor.png")).getImage(), new Point(), ""));
		loadImage();
		setTitleScreen();
	}

	public static GameScreen getInstance() {
		return gs;
	}

	public void loadImage() {
		UI = new ImageIcon[][] {
			{
				new ImageIcon(il.load("img/start.png")),
				new ImageIcon(il.load("img/help.png")),
				new ImageIcon(il.load("img/setting.png")),
				new ImageIcon(il.load("img/again.png")),
				new ImageIcon(il.load("img/to_title.png"))
			},
			{
				new ImageIcon(il.load("img/start_2.png")),
				new ImageIcon(il.load("img/help_2.png")),
				new ImageIcon(il.load("img/setting_2.png")),
				new ImageIcon(il.load("img/again_2.png")),
				new ImageIcon(il.load("img/to_title_2.png"))
			}
		};
		turnIcons = new ImageIcon[] {
				new ImageIcon(il.load("img/your_turn.png")),
				new ImageIcon(il.load("img/my_turn.png"))
		};
		wlIcons = new ImageIcon[] {
				new ImageIcon(il.load("img/win.png")),
				new ImageIcon(il.load("img/lose.png"))
		};
	}

	// タイトル画面
	public void setTitleScreen() {
		currentScreen = "title";

		removeGame();
		if (gameOver != null) {
			gameOver.setVisible(false);
		}

		if (title == null) {
			title = new JLayeredPane();
			title.addMouseMotionListener(this);
			c.add(title);
			addComponent(new JLabel(new ImageIcon(il.load("img/title.png"))), 0, 0, 0, 1200, 900);
			for (int i = start; i <= setting; i++) {
				buttons[i] = new JButton(UI[nomal][i]);
				setButton(buttons[i], this, this);
				addComponent(buttons[i], 100, 100, 450 + i * 100, 458, 93);
			}
			helpLabel = new JLabel(new ImageIcon(il.load("img/help_dialog.png")));
			addComponent(helpLabel, 200, 145, 125, 900, 654);
			helpClose = new JButton(new ImageIcon (il.load("img/help_close.png")));
			setButton(helpClose, this, this);
			addComponent(helpClose, 210, 900, 120, 117, 100);
			setHelp(false);
		}

		addComponent(hammer.getHammerLabel(), 1500, 0, 0, 200, 170);
		title.setVisible(true);
		cleanButtons();
		hammer.cleanHammerIcon();
	}

	// あそびかたの表示・非表示切り替え
	public void setHelp(boolean visible) {
		for (int i = start; i <= setting; i++) {
			buttons[i].setVisible(!visible);
		}
		helpLabel.setVisible(visible);
		helpClose.setVisible(visible);
	}

	// ゲーム画面
	public void setGameScreen(int num) {
		currentScreen = "game";

		if (title != null) title.setVisible(false);
		if (gameOver != null) gameOver.setVisible(false);

		if (num % 2 == 0) {
			myTurn = 0;
		} else {
			myTurn = 1;
		}

		game = new JLayeredPane();
		game.addMouseMotionListener(this);
		c.add(game);
		addComponent(new JLabel(new ImageIcon(il.load("img/sea.png"))), 0, 0, 0, 1200, 900);
		addComponent(new JLabel(new ImageIcon(il.load("img/logo.png"))), 900, 760, 50, 400, 315);
		penguin = new Penguin();
		im = new ItemManager();
		ices = new Ices();
		turnLabel = new JLabel(turnIcons[getMyTurn()]);
		addComponent(turnLabel, 800, 875, 350, 250, 120);
		addComponent(hammer.getHammerLabel(), 1500, 0, 0, 200, 170);
		game.setVisible(true);
		hammer.cleanHammerIcon();
	}

	// ゲームオーバー画面
	public void setGameOverScreen() {
		currentScreen = "gameOver";
		penguin.getPenguinLabel().setVisible(false);
		hammer.changeHammer();

		if (gameOver == null) {
			gameOver = new JLayeredPane();
			gameOver.addMouseMotionListener(this);
			c.add(gameOver);
			for (int i = again; i <= toTitle; i++) {
				buttons[i] = new JButton(UI[nomal][i]);
				setButton(buttons[i], this, this);
				addComponent(buttons[i], 1200, 350, 400 + (i - again) * 100, 458, 93);
				wlLabel = new JLabel();
				addComponent(wlLabel, 1000, 0, 0, 1200, 900);
			}
		}

		wlLabel.setIcon(wlIcons[getMyTurn()]);
		addComponent(hammer.getHammerLabel(), 1500, 0, 0, 200, 170);
		gameOver.setVisible(true);
		removeGame();
		cleanButtons();
		hammer.cleanHammerIcon();
	}

	// ゲーム画面を取り除くメソッド
	public void removeGame() {
		if (game == null) return;
		game.setVisible(false);
		c.remove(game);
		game = null;
	}

	// コンポーネントの追加、レイヤー・位置・サイズ指定をまとめて行うメソッド
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

	// ボタンのマウスイベントの追加、背景・枠線の非表示をまとめて行うメソッド
	public void setButton(JButton jb, MouseListener m1, MouseMotionListener m2) {
		jb.addMouseListener(m1);
		jb.addMouseMotionListener(m2);
		jb.setBorderPainted(false);
		jb.setContentAreaFilled(false);
	}

	// ホバー用の画像への切り替えを行うメソッド
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

	public void clickedButton(MouseEvent e) {
		JButton jb = (JButton)e.getComponent();
		Icon icon = jb.getIcon();
			if (icon == UI[hover][start]) {
				ms.send("join");
			} else if (icon == UI[hover][help]) {
				setHelp(true);
			} else if (icon == helpClose.getIcon()) {
				setHelp(false);
			} else if (icon == UI[hover][again]) {
				ms.send("join");
			} else if (icon == UI[hover][toTitle]) {
				ms.send("toTitle");
			}
	}

	// 画面遷移後にボタンがホバー状態のままにならないよう、画像を元に戻すメソッド
	public void cleanButtons() {
		for (int i = start; i <= toTitle; i++) {
			if (buttons[i] == null) break;
			if (buttons[i].getIcon() == UI[hover][i]) {
				buttons[i].setIcon(UI[nomal][i]);
			}
		}
	}

	public String getCurrentScreen() {
		return currentScreen;
	}

	public Penguin getPenguin() {
		return penguin;
	}

	public ItemManager getItemManager() {
		return im;
	}

	public Ices getIces() {
		return ices;
	}

	public int getMyTurn() {
		return myTurn;
	}

	public void setMyTurn() {
		myTurn = 1 - myTurn;
		turnLabel.setIcon(turnIcons[getMyTurn()]);
		ices.spinTheRoulette();
		ices.setGhFlag(false);
		ices.setShieldFlag(false);
		penguin.changePenguinIcon(0);
		hammer.cleanHammerIcon();
		im.setItemInvisible();
		im.setItemButtons();
	}

	public boolean isMyTurn() {
		if (myTurn == 1) return true;
		return false;
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
		clickedButton(e);
	}

	public void mousePressed(MouseEvent e) {
		hammer.changeHammerIcon();
	}

	public void mouseReleased(MouseEvent e) {
		hammer.changeHammerIcon();
	}

	public void mouseEntered(MouseEvent e) {
		hoverUIIcon(e);
	}

	public void mouseExited(MouseEvent e) {
		hoverUIIcon(e);
	}
}

