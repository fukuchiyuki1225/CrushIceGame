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
	private JLayeredPane j, title, game;
	private Cursor cursor;
	private MesgSend ms;
	private Hammer hammer;
	private Penguin penguin;
	private Ices ices;
	private int myTurn;
	private final int start = 0, help = 1, setting = 2, nomal = 0, hover = 1;
	private JLabel titleBack, gameBack;
	private JButton startBt, helpBt, settingBt;
	private ImageIcon[][] UI;
	private String currentScreen;

	public GameScreen(int num, MesgSend ms) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("クラッシュアイスゲーム");
		setSize(1200, 935);
		setLocationRelativeTo(null);
		setResizable(false);

		c = getContentPane();
		j = new JLayeredPane();
		currentScreen = "game";
		c.add(j);
		j.addMouseListener(this);
		j.addMouseMotionListener(this);
		j.setLayout(null);

		addComponent(new JLabel(new ImageIcon(ImageLoader.loadImage("img/sea.png"))), 0, 0, 0, 1200, 900);
		addComponent(new JLabel(new ImageIcon(ImageLoader.loadImage("img/logo.png"))), 900, 760, 50, 400, 315);

		cursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(ImageLoader.loadImage("img/cursor.png")).getImage(), new Point(), "");
		setCursor(cursor);

		if (num % 2 == 0) {
			myTurn = 0;
		} else {
			myTurn = 1;
		}

		/*loadUI();
		setTitleScreen();*/
		this.ms = ms;
		hammer = new Hammer(this);
		penguin = new Penguin(this);
		ices = new Ices(hammer, penguin, this);
	}

	public void loadUI() {
		UI = new ImageIcon[][] {
			{
				new ImageIcon(ImageLoader.loadImage("img/start.png")),
				new ImageIcon(ImageLoader.loadImage("img/help.png")),
				new ImageIcon(ImageLoader.loadImage("img/setting.png"))
			},
			{
				new ImageIcon(ImageLoader.loadImage("img/start_2.png")),
				new ImageIcon(ImageLoader.loadImage("img/help_2.png")),
				new ImageIcon(ImageLoader.loadImage("img/setting_2.png"))
			}
		};
	}

	public void setTitleScreen() {
		title = new JLayeredPane();
		c.add(title);
		title.addMouseMotionListener(this);
		title.setLayout(null);

		titleBack = new JLabel(new ImageIcon(ImageLoader.loadImage("img/title.png")));
		title.setLayer(titleBack, 0);
		title.add(titleBack);
		titleBack.setBounds(0, 0, 1200, 900);

		startBt = new JButton(UI[nomal][start]);
		startBt.setBorderPainted(false);
		startBt.setContentAreaFilled(false);
		startBt.addMouseListener(this);
		startBt.addMouseMotionListener(this);
		addComponent(startBt, 100, 100, 450, 458, 93);

		helpBt = new JButton(UI[nomal][help]);
		helpBt.setBorderPainted(false);
		helpBt.setContentAreaFilled(false);
		helpBt.addMouseListener(this);
		helpBt.addMouseMotionListener(this);
		addComponent(helpBt, 100, 100, 550, 458, 93);

		settingBt = new JButton(UI[nomal][setting]);
		settingBt.setBorderPainted(false);
		settingBt.setContentAreaFilled(false);
		settingBt.addMouseListener(this);
		settingBt.addMouseMotionListener(this);
		addComponent(settingBt, 100, 100, 650, 458, 93);
	}

	public void addComponent(Component comp, int layer, int x, int y, int width, int height) {
		switch (currentScreen) {
		case "title":
			title.setLayer(comp, layer);
			title.add(comp);
			break;
		case "game":
			j.setLayer(comp, layer);
			j.add(comp);
			break;
		default:
			break;
		}
		comp.setBounds(x, y, width, height);
	}

	public Ices getIces() {
		return ices;
	}

	public Penguin getPenguin() {
		return penguin;
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
	}

	public void send(String mesg) {
		ms.send(mesg);
	}

	public void hoverUIIcon(MouseEvent e) {
		JButton jb = (JButton)e.getComponent();
		Icon icon = jb.getIcon();
		for (int j = nomal; j <= hover; j++) {
			for (int i = start; i <= setting; i++) {
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

	public void mouseDragged(MouseEvent e) {

	}

	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		SwingUtilities.convertPointToScreen(p, e.getComponent());
		SwingUtilities.convertPointFromScreen(p, e.getComponent().getParent());
		hammer.setHammerLocation(p);
	}

	public void mouseClicked(MouseEvent e) {

	}

	public void mousePressed(MouseEvent e) {
		hammer.changeHammerIcon();
	}

	public void mouseReleased(MouseEvent e) {
		hammer.changeHammerIcon();
	}

	public void mouseEntered(MouseEvent e) {
		if (currentScreen.equals("title")) hoverUIIcon(e);
	}

	public void mouseExited(MouseEvent e) {
		if (currentScreen.equals("title")) hoverUIIcon(e);
	}
}
