import java.awt.Component;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class GameScreen extends JFrame implements MouseListener, MouseMotionListener {
	private Container c;
	private JLayeredPane j;
	private Cursor cursor;
	private MesgSend ms;
	private Hammer hammer;
	private Penguin penguin;
	private Ices ices;
	private int myTurn;

	public GameScreen(int num, Socket socket) {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("クラッシュアイスゲーム");
		setSize(1200, 935);
		setLocationRelativeTo(null);
		setResizable(false);

		c = getContentPane();
		j = new JLayeredPane();
		c.add(j);
		j.addMouseListener(this);
		j.addMouseMotionListener(this);
		j.setLayout(null);

		addComponent(new JLabel(new ImageIcon(ImageLoader.readImage("img/sea.png"))), 0, 0, 0, 1200, 900);
		addComponent(new JLabel(new ImageIcon(ImageLoader.readImage("img/logo.png"))), 900, 760, 50, 400, 315);

		cursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon(ImageLoader.readImage("img/cursor.png")).getImage(), new Point(), "");
		setCursor(cursor);

		if (num % 2 == 0) {
			myTurn = 0;
		} else {
			myTurn = 1;
		}
		ms = new MesgSend(socket);
		hammer = new Hammer(this);
		penguin = new Penguin(this);
		ices = new Ices(hammer, penguin, this);
	}

	public void addComponent(Component comp, int layer, int x, int y, int width, int height) {
		j.setLayer(comp, layer);
		j.add(comp);
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

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		hammer.setHammerLocation(e.getPoint());
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
	}

	public void mouseExited(MouseEvent e) {
	}
}
