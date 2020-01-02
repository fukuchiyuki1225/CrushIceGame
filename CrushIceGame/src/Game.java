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

		/*GameScreen gs = new GameScreen();
		gs.setVisible(true);*/
	}

	public class MesgRecvThread extends Thread {
		Socket socket;
		String myName;
		Boolean existGs;
		GameScreen gs;

		public MesgRecvThread(Socket socket, String myName) {
			this.socket = socket;
			this.myName = myName;
			existGs = false;
		}

		public void run() {
			try {
				InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(sisr);
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(myName);
				String myNumberStr = br.readLine();
				int myNumber = Integer.parseInt(myNumberStr);
				out.println("join" + " " + myNumber);
				while (true) {
					String inputLine = br.readLine();
					if (inputLine != null) {
						String[] inputTokens = inputLine.split(" ");
						String cmd = inputTokens[0];
						if (cmd.equals("join")) {
							if (!existGs && Integer.parseInt(inputTokens[1]) > 1) {
								gs = new GameScreen(new MyTurn(myNumber), socket);
								gs.setVisible(true);
								existGs = true;
							}
						}
						if (cmd.equals("initialize")) {
							gs.getIces().initializeIce(Integer.parseInt(inputTokens[1]), Integer.parseInt(inputTokens[2]), Integer.parseInt(inputTokens[3]), Integer.parseInt(inputTokens[4]));
						}
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
