import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class Game {
	PrintWriter out;
	static GameScreen gs;
	static Sound sound;
	static MesgSend ms;

	public Game(String ip) {
		String myName = "No name";
		String addr = ip;
		if (addr.equals("")) addr = "localhost";
		Socket socket = null;

		try {
			socket = new Socket(addr, 10000);
		} catch (Exception e) {
			gs.setConnectLabel(2, true);
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
				ms = MesgSend.getInstance(socket);
				InputStreamReader sisr = new InputStreamReader(socket.getInputStream());
				BufferedReader br = new BufferedReader(sisr);
				out = new PrintWriter(socket.getOutputStream(), true);
				out.println(myName);
				String myNumberStr = br.readLine();
				int myNumber = Integer.parseInt(myNumberStr);
				gs.setMyNum(myNumber);
				ms.send("join");
				while (true) {
					String inputLine = br.readLine();
					if (inputLine != null) {
						String[] inputTokens = inputLine.split(" ");
						String cmd = inputTokens[0];
						switch (cmd) {
						case "noVacancy":
							gs.setConnectLabel(3, true);
							break;
						case "start":
							gs.setGameScreen(Integer.parseInt(inputTokens[1]), Integer.parseInt(inputTokens[2]));
							break;
						case "ghostMove":
							GhostHammer.getInstance().setHammerLocation(Integer.parseInt(inputTokens[1]), Integer.parseInt(inputTokens[2]), Integer.parseInt(inputTokens[3]));
							break;
						case "ghostClick":
							GhostHammer.getInstance().changeHammerIcon(Integer.parseInt(inputTokens[1]), Integer.parseInt(inputTokens[2]));
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
						case "disconnect":
							gs.setConnectLabel(1, true);
							break;
						default:
							break;
						}
					} else {
						break;
					}
				}
			} catch (Exception e) {
				if (gs.getWaitFlag() || gs.getCurrentScreen().equals("game")) gs.setConnectLabel(2, true);
			}
		}
	}

	public static void main(String[] args) {
		gs = GameScreen.getInstance();
		gs.setVisible(true);
		sound = Sound.getInstance();
		sound.loop("bgm");
		// new Game();
	}
}
