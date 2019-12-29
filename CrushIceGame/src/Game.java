import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;


public class Game {
	PrintWriter out;

	public Game() {
		String myName = "No name";
		String addr = "localhost";
		Socket socket = null;
		try {
			socket = new Socket(addr, 10000);
		} catch (UnknownHostException e) {
			System.err.println("ホストのIPアドレスが判定できません.：" + e);
		} catch (IOException e) {
			System.err.println("エラーが発生しました.：" + e);
		}

		MesgRecvThread mrt = new MesgRecvThread(socket, myName);
		mrt.start();

		GameScreen gs = new GameScreen();
		gs.setVisible(true);
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
				System.err.println("エラーが発生しました：" + e);
			}
		}
	}

	public static void main(String[] args) {
		new Game();
	}
}
