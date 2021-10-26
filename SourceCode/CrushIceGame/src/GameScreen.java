import java.awt.Component;
import java.awt.Container;
import java.awt.Font;
import java.awt.Point;
import java.awt.Toolkit;
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
import javax.swing.JLayeredPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

public class GameScreen extends JFrame implements MouseListener, MouseMotionListener {
	private static GameScreen gs = new GameScreen();
	private Container c;
	private JLayeredPane title, game, gameOver;
	private String currentScreen, myIp;
	private Hammer hammer;
	private GhostHammer ghostHammer;
	private ImageIcon[][] UI, msgIcons;
	private ImageIcon[] turnIcons, wlIcons, connectIcons;
	private JButton[] buttons;
	private JLabel helpLabel, turnLabel, wlLabel, msgLabel, connectLabel, inputLabel;
	private JButton helpClose, bgm, se, cancelButton, decideButton;
	private boolean bgmFlag, seFlag, waitFlag;
	private int myTurn, myNum;
	public final int START = 0, HELP = 1, AGAIN = 2, TOTITLE = 3, BGMOFF= 4, BGMON = 5, SEOFF= 6, SEON = 7, CANCEL = 8, DECIDE = 9 , WAIT = 0, DISCONNECT = 1, SERVERERROR = 2, NOVACANCY = 3, NOMAL = 0, HOVER = 1;
	private Penguin penguin;
	private ItemManager im;
	private Ices ices;
	private JTextField ip;

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
		bgmFlag = true;
		seFlag = true;
		waitFlag = false;
		setCursor(Toolkit.getDefaultToolkit().createCustomCursor(Image.getImageIcon("cursor").getImage(), new Point(), ""));
		loadImage();
		setTitleScreen();
	}

	public static GameScreen getInstance() {
		return gs;
	}

	public void loadImage() {
		UI = new ImageIcon[][] {
			{
				Image.getImageIcon("start"),
				Image.getImageIcon("help"),
				Image.getImageIcon("again"),
				Image.getImageIcon("to_title"),
				Image.getImageIcon("bgm_off"),
				Image.getImageIcon("bgm_on"),
				Image.getImageIcon("se_off"),
				Image.getImageIcon("se_on"),
				Image.getImageIcon("cancel"),
				Image.getImageIcon("decide")
			},
			{
				Image.getImageIcon("start_2"),
				Image.getImageIcon("help_2"),
				Image.getImageIcon("again_2"),
				Image.getImageIcon("to_title_2"),
				Image.getImageIcon("bgm_off_2"),
				Image.getImageIcon("bgm_on_2"),
				Image.getImageIcon("se_off_2"),
				Image.getImageIcon("se_on_2"),
				Image.getImageIcon("cancel_2"),
				Image.getImageIcon("decide_2")
			}
		};
		msgIcons = new ImageIcon[][] {
			{
				Image.getImageIcon("win_msg"),
				Image.getImageIcon("win_msg_2")
			},
			{
				Image.getImageIcon("lose_msg"),
				Image.getImageIcon("lose_msg_2")
			}
		};
		turnIcons = new ImageIcon[] {
				Image.getImageIcon("your_turn"),
				Image.getImageIcon("my_turn")
		};
		wlIcons = new ImageIcon[] {
				Image.getImageIcon("win"),
				Image.getImageIcon("lose")
		};
		connectIcons = new ImageIcon[] {
				Image.getImageIcon("wait"),
				Image.getImageIcon("disconnect"),
				Image.getImageIcon("server_error"),
				Image.getImageIcon("no_vacancy")
		};
	}

	// タイトル画面
	public void setTitleScreen() {
		currentScreen = "title";
		Sound.loop("bgm");

		removeGame();
		if (gameOver != null) {
			gameOver.setVisible(false);
		}

		if (title == null) {
			title = new JLayeredPane();
			title.addMouseMotionListener(this);
			c.add(title);
			addComponent(new JLabel(Image.getImageIcon("title")), 0, 0, 0, 1200, 900);
			buttons = new JButton[TOTITLE + 1];
			for (int i = START; i <= HELP; i++) {
				buttons[i] = new JButton(UI[NOMAL][i]);
				setButton(buttons[i], this, this);
				addComponent(buttons[i], 100, 100, 450 + i * 100, 458, 93);
			}
			bgm = new JButton(UI[NOMAL][BGMON]);
			setButton(bgm, this, this);
			addComponent(bgm, 100, 100, 650, 213, 93);
			se = new JButton(UI[NOMAL][SEON]);
			setButton(se, this, this);
			addComponent(se, 100, 345, 650, 213, 93);
			helpLabel = new JLabel(Image.getImageIcon("help_dialog"));
			addComponent(helpLabel, 200, 145, 125, 900, 654);
			helpClose = new JButton(Image.getImageIcon("help_close"));
			setButton(helpClose, this, this);
			addComponent(helpClose, 210, 900, 120, 117, 100);
			inputLabel = new JLabel(Image.getImageIcon("input_ip"));
			addComponent(inputLabel, 120, 100, 490, 458, 213);
			decideButton = new JButton(UI[NOMAL][DECIDE]);
			setButton(decideButton, this, this);
			addComponent(decideButton, 140, 250, 625, 150, 56);
			ip = new JTextField("", 20);
			ip.setHorizontalAlignment(JTextField.CENTER);
			ip.setFont(new Font(Font.DIALOG_INPUT, Font.BOLD, 20));
			addComponent(ip, 130, 180, 570, 300, 30);
			connectLabel = new JLabel();
			addComponent(connectLabel, 1000, 350, 378, 500, 145);
			cancelButton = new JButton(UI[NOMAL][CANCEL]);
			setButton(cancelButton, this, this);
			addComponent(cancelButton, 1050, 715, 390, 121, 40);
		}
		setHelp(false);
		setInputIP(false);
		setConnectLabel(WAIT, false);

		addComponent(hammer.getHammerLabel(), 1500, 0, 0, 200, 170);
		title.setVisible(true);
		cleanButtons();
		hammer.cleanHammerIcon();
	}

	// あそびかたの表示・非表示切り替え
	public void setHelp(boolean visible) {
		for (int i = START; i <= HELP; i++) {
			buttons[i].setVisible(!visible);
		}
		bgm.setVisible(!visible);
		se.setVisible(!visible);
		helpLabel.setVisible(visible);
		helpClose.setVisible(visible);
	}

	public void setInputIP(boolean visible) {
		for (int i = START; i <= HELP; i++) {
			buttons[i].setVisible(!visible);
		}
		bgm.setVisible(!visible);
		se.setVisible(!visible);
		inputLabel.setVisible(visible);
		ip.setVisible(visible);
		decideButton.setVisible(visible);
	}

	// ゲーム画面
	public void setGameScreen(int myNum, int myTurn) {
		if (this.myNum != myNum) return;
		this.myTurn = myTurn;

		currentScreen = "game";
		Sound.loop("bgm");

		if (title != null) title.setVisible(false);
		if (gameOver != null) gameOver.setVisible(false);

		game = new JLayeredPane();
		game.addMouseMotionListener(this);
		c.add(game);
		addComponent(new JLabel(Image.getImageIcon("sea")), 0, 0, 0, 1200, 900);
		addComponent(new JLabel(Image.getImageIcon("logo")), 900, 760, 25, 400, 315);
		addComponent(new JLabel(Image.getImageIcon("item_ice")), 450, 805, 640, 359, 212);
		penguin = new Penguin();
		im = new ItemManager();
		ices = new Ices();
		ghostHammer = GhostHammer.getInstance();
		addComponent(ghostHammer.getHammerLabel(), 800, 0, 0, 200, 170);
		turnLabel = new JLabel(turnIcons[getMyTurn()]);
		addComponent(turnLabel, 800, 875, 325, 250, 120);
		addComponent(hammer.getHammerLabel(), 1500, 0, 0, 200, 170);
		game.setVisible(true);
		hammer.cleanHammerIcon();
	}

	public void setGameStart() {
		JLabel gameStart = new JLabel(Image.getImageIcon("game_start"));
		addComponent(gameStart, 2000, 70, 400, 650, 121);
		Sound.play("start");
		Timer timer = new Timer(2000, new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				gameStart.setVisible(false);
				ices.setStartFlag(true);
			}
		});
		timer.setRepeats(false);
		timer.start();
	}

	// ゲームオーバー画面
	public void setGameOverScreen() {
		currentScreen = "gameOver";
		penguin.getPenguinLabel().setVisible(false);
		Sound.stop();
		Sound.play("fall");

		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		hammer.changeHammer();

		if (gameOver == null) {
			gameOver = new JLayeredPane();
			gameOver.addMouseMotionListener(this);
			c.add(gameOver);
			for (int i = AGAIN; i <= TOTITLE; i++) {
				buttons[i] = new JButton(UI[NOMAL][i]);
				setButton(buttons[i], this, this);
				addComponent(buttons[i], 1200, 350, 400 + (i - AGAIN) * 100, 458, 93);
				wlLabel = new JLabel();
				addComponent(wlLabel, 1000, 0, 0, 1200, 900);
				msgLabel = new JLabel();
				addComponent(msgLabel, 1100, 350, 625, 500, 200);
			}
		}

		wlLabel.setIcon(wlIcons[getMyTurn()]);
		msgLabel.setIcon(msgIcons[getMyTurn()][new Random().nextInt(2)]);
		addComponent(hammer.getHammerLabel(), 1500, 0, 0, 200, 170);
		gameOver.setVisible(true);

		if (!isMyTurn()) {
			Sound.play("win");
		} else {
			Sound.play("lose");
		}

		removeGame();
		cleanButtons();
		hammer.cleanHammerIcon();

		MesgSend.send("gameOver");
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
		for (int j = NOMAL; j <= HOVER; j++) {
			for (int i = START; i <= DECIDE; i++) {
				if (icon == UI[j][i]) {
					if (j == NOMAL) {
						jb.setIcon(UI[HOVER][i]);
						return;
					} else if (j == HOVER) {
						jb.setIcon(UI[NOMAL][i]);
						return;
					}
				}
			}
		}
	}

	public void setConnectLabel(int state, boolean visible) {
		if (state == 3) {
			if (currentScreen.equals("game")) return;
			MesgSend.send("close" + " " + myNum);
		}

		connectLabel.setIcon(connectIcons[state]);
		addComponent(connectLabel, 1000, 350, 378, 500, 145);
		connectLabel.setVisible(visible);

		if (state == WAIT) {
			waitFlag = visible;
			buttons[START].setVisible(!visible);
			buttons[HELP].setVisible(!visible);
			bgm.setVisible(!visible);
			se.setVisible(!visible);
			cancelButton.setVisible(visible);
			return;
		} else {
			cancelButton.setVisible(false);
			Timer timer = new Timer(5000, new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					setTitleScreen();;
				}
			});
			timer.setRepeats(false);
			timer.start();
		}
	}

	public void clickedButton(MouseEvent e) {
		JButton jb = (JButton)e.getComponent();
		Icon icon = jb.getIcon();
		if (icon == UI[HOVER][START]) {
			setInputIP(true);
		} else if (icon == UI[HOVER][DECIDE]) {
			setInputIP(false);
			setConnectLabel(WAIT, true);
			myIp = ip.getText();
			new Game(myIp);
		} else if (icon == cancelButton.getIcon()) {
			MesgSend.send("cancel" + " " + getMyNum());
			setConnectLabel(WAIT, false);
		} else if (icon == UI[HOVER][HELP]) {
			setHelp(true);
		} else if (icon == helpClose.getIcon()) {
			setHelp(false);
		} else if (icon == UI[HOVER][BGMON]) {
			setBgmFlag(false);
			jb.setIcon(UI[HOVER][BGMOFF]);
		} else if (icon == UI[HOVER][BGMOFF]) {
			setBgmFlag(true);
			jb.setIcon(UI[HOVER][BGMON]);
		} else if (icon == UI[HOVER][SEON]) {
			seFlag = false;
			jb.setIcon(UI[HOVER][SEOFF]);
		} else if (icon == UI[HOVER][SEOFF]) {
			seFlag = true;
			jb.setIcon(UI[HOVER][SEON]);
		} else if (icon == UI[HOVER][AGAIN]) {
			setTitleScreen();
			setConnectLabel(WAIT, true);
			new Game(myIp);
		} else if (icon == UI[HOVER][TOTITLE]) {
			setTitleScreen();
		}
		Sound.play("button");
	}

	// 画面遷移後にボタンがホバー状態のままにならないよう、画像を元に戻すメソッド
	public void cleanButtons() {
		for (int i = START; i <= TOTITLE; i++) {
			if (buttons[i] == null) break;
			if (buttons[i].getIcon() == UI[HOVER][i]) {
				buttons[i].setIcon(UI[NOMAL][i]);
			}
		}
	}

	public void initMyTurn(int myNum, int myTurn) {
		if (this.myNum == myNum) return;
		this.myTurn = myTurn;
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

	public boolean getBgmFlag() {
		return bgmFlag;
	}

	public boolean getSeFlag() {
		return seFlag;
	}

	public boolean getWaitFlag() {
		return waitFlag;
	}

	public int getMyTurn() {
		return myTurn;
	}

	public int getMyNum() {
		return myNum;
	}

	public void setBgmFlag(boolean bgmFlag) {
		this.bgmFlag = bgmFlag;
		if (!this.bgmFlag) {
			Sound.stop("bgm");
		} else {
			Sound.loop("bgm");
		}
	}

	public void setMyNum(int myNum) {
		this.myNum = myNum;
	}

	public void setMyTurn() {
		ices.setGhFlag(false);
		ices.setShieldFlag(false);
		penguin.changePenguinIcon(0);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		myTurn = 1 - myTurn;
		turnLabel.setIcon(turnIcons[getMyTurn()]);
		ices.spinTheRoulette();
		hammer.cleanHammerIcon();
		ghostHammer.cleanHammerIcon();
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

