import java.awt.Color;
import java.awt.Container;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLayeredPane;
import javax.swing.JOptionPane;

public class Game extends JFrame implements MouseListener, MouseMotionListener {
	private Cursor cursor;
	private Hammer hammer;
	private Penguin penguin;
	private Ices ices;
	static JLayeredPane j;
	private Container c;
	PrintWriter out;

	public Game() {
		String myName = JOptionPane.showInputDialog(null, "���O����͂��Ă�������.", "���O�̓���", JOptionPane.QUESTION_MESSAGE);
		if (myName.equals("")) {
			myName = "No name";
		}

		String addr = JOptionPane.showInputDialog(null, "�T�[�o�[��IP�A�h���X����͂��Ă�������.", "�T�[�o�[��IP�A�h���X", JOptionPane.QUESTION_MESSAGE);
		if (addr.equals("")) {
			addr = "localhost";
		}

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setTitle("�N���b�V���A�C�X�Q�[��");
		setSize(1200, 900);
		setLocationRelativeTo(null);

		c = getContentPane();
		j = new JLayeredPane();
		c.add(j);
		c.setBackground(new Color(115, 245, 245));
		j.addMouseListener(this);
		j.addMouseMotionListener(this);
		j.setLayout(null);

		hammer = new Hammer();
		ices = new Ices(hammer);
		penguin = new Penguin(ices);

		cursor = Toolkit.getDefaultToolkit().createCustomCursor(new ImageIcon("img/surcor.png").getImage(), new Point(), "");
		setCursor(cursor);

		Socket socket = null;
		try {
			socket = new Socket(addr, 10000);
		} catch (UnknownHostException e) {
			System.err.println("�z�X�g��IP�A�h���X������ł��܂���.�F" + e);
		} catch (IOException e) {
			System.err.println("�G���[���������܂���.�F" + e);
		}

		MesgRecvThread mrt = new MesgRecvThread(socket, myName);
		mrt.start();
	}

	public class MesgRecvThread extends Thread {
		Socket socket;
		String myName;

		public MesgRecvThread(Socket socket, String myName) {
			this.socket = socket;
			this.myName = myName;
		}

		public void run() {
			try {
				InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(sisr);
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(myName);
				// String myNumStr = br.readLine();
				// int myNumber = Integer.parseInt(myNumStr);
				while (true) {
					String inputLine = br.readLine();
					if (inputLine != null) {

					} else {
						break;
					}
				}
			} catch (IOException e) {
				System.err.println("�G���[���������܂����F" + e);
			}
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.setVisible(true);
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

	public void mouseDragged(MouseEvent e) {
		// TODO �����������ꂽ���\�b�h�E�X�^�u

	}

	public void mouseMoved(MouseEvent e) {
		Point p = e.getPoint();
		hammer.setHammerLocation(p);
	}
}
