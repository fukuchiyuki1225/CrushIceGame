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
	}

	public class MesgRecvThread extends Thread {
		Socket socket;
		String myName;
		MesgSend ms;
		GameScreen gs;
		Sound sound;

		public MesgRecvThread(Socket socket, String myName) {
			this.socket = socket;
			this.myName = myName;
			ms = MesgSend.getInstance(socket);
		}

		public void run() {
			try {
				InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(sisr);
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(myName);
				String myNumberStr = br.readLine();
				int myNumber = Integer.parseInt(myNumberStr);
				gs = GameScreen.getInstance();
				gs.setVisible(true);
				sound = Sound.getInstance();
				sound.loop("bgm");
				while (true) {
					String inputLine = br.readLine();
					if (inputLine != null) {
						String[] inputTokens = inputLine.split(" ");
						String cmd = inputTokens[0];
						switch (cmd) {
						case "join":
							gs.setGameScreen(myNumber);
							break;
						case "initIces":
							gs.getIces().initIces(Integer.parseInt(inputTokens[1]), Integer.parseInt(inputTokens[2]), Integer.parseInt(inputTokens[3]), Integer.parseInt(inputTokens[4]));
							break;
						case "initItems":
							gs.getItemManager().initItems(inputTokens[1], Integer.parseInt(inputTokens[2]));
							break;
						case "roulette":
							gs.getIces().setBreakIce(Integer.parseInt(inputTokens[1]));
							break;
						case "changeHitCount":
							gs.getIces().changeHitCount(Integer.parseInt(inputTokens[1]));
							break;
						case "changeBreakIce":
							gs.getIces().changeBreakIce(Integer.parseInt(inputTokens[1]));
							break;
						case "changeIceIcon":
							gs.getIces().changeIceIcon(Integer.parseInt(inputTokens[1]), Integer.parseInt(inputTokens[2]), inputTokens[3]);
							sound.play(inputTokens[4]);
							break;
						case "getItem":
							gs.getItemManager().getItems().get(inputTokens[1]).getItem();
							sound.play("item");
							break;
						case "useItem":
							sound.play("item");
							break;
						case "move":
							gs.getPenguin().penguinMove(Integer.parseInt(inputTokens[1]), Integer.parseInt(inputTokens[2]));
							break;
						case "fall":
							gs.setGameOverScreen();
							break;
						case "changePenguinIcon":
							gs.getPenguin().changePenguinIcon(Integer.parseInt(inputTokens[1]));
							break;
						case "changeTurn":
							gs.setMyTurn();
							break;
						case "toTitle":
							gs.setTitleScreen();
							break;
						default:
							break;
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
