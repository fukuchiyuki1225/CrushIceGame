import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;

public class GameScreen extends JFrame implements MouseListener, MouseMotionListener {
	private Container c;
	static JLayeredPane j;
	private ImageIcon backGround;
	private Cursor cursor;
	private Hammer hammer;
	private Penguin penguin;
	private Ices ices;

	public GameScreen() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("クラッシュアイスゲーム");
		setSize(1200, 900);
		setLocationRelativeTo(null);

		c = getContentPane();
		j = new JLayeredPane();
		c.add(j);
		c.setBackground(new Color(115, 245, 245));
		j.addMouseListener(this);
		j.addMouseMotionListener(this);
		j.setLayout(null);

		backGround = new ImageIcon("img/sea.png");
		JLabel backLabel = new JLabel(backGround);
		j.setLayer(backLabel, 0);
		j.add(backLabel);
		backLabel.setBounds(0, 0, 1200, 900);

		cursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("img/surcor.png").getImage(), new Point(), "");
		setCursor(cursor);

		hammer = new Hammer();
		penguin = new Penguin();
		ices = new Ices(hammer, penguin);
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
