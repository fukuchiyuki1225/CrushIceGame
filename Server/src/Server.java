import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

class ClientProcThread extends Thread {
	private BufferedReader myIn;
	private PrintWriter myOut;
	private int number;

	public ClientProcThread(BufferedReader in, PrintWriter out, int n) {
		myIn = in;
		myOut = out;
		number = n;
	}

	public void run() {
		try {
			myOut.println(number);

			while (true) {
				String str = myIn.readLine();
				if (str != null) {
					String inputTokens[] = str.split(" ");
					String cmd = inputTokens[0];
					switch (cmd) {
					case "join":
						Server.setMemNum();
						break;
					case "fall":
						Server.resetMemNum();
						break;
					case "cancel":
						Server.resetMemNum();
						Server.SetFlag(Integer.parseInt(inputTokens[1]), false);
						break;
					case "close":
						Server.SetFlag(Integer.parseInt(inputTokens[1]), false);
						break;
					case "gameOver":
						Server.setAllFlag();
						break;
					default:
						break;
					}
					Server.SendAll(str);
				}
			}
		} catch (Exception e) {
			if (!Server.getFlag(number)) return;
			if (Server.getIsGame()) {
				Server.SendAll("disconnect");
				Server.setAllFlag();
			}
			Server.resetMemNum();
			Server.SetFlag(number, false);
		}
	}
}

class Server {
	private static int maxConnection = 100;
	private static Socket[] incoming;
	private static boolean[] flag;
	private static InputStreamReader[] isr;
	private static BufferedReader[] in;
	private static PrintWriter[] out;
	private static ClientProcThread[] myClientProcThread;
	private static int member, memNum = 0;
	private static boolean isGame = false;

	public static void SendAll(String str) {
		int turn = new Random().nextInt(2);
		boolean sendFlag = false;
		String cmd = str.split(" ")[0];
		for (int i = 1; i <= member; i++) {
			if (flag[i]) {
				if (cmd.equals("start")) {
					if (!sendFlag) {
						str = cmd + " " + i + " " + turn;
						sendFlag = true;
					} else {
						turn = 1 - turn;
						str = cmd + " " + i + " " + turn;
					}
				}
				out[i].println(str);
				out[i].flush();
			}
		}
	}

	public static void SetFlag(int n, boolean value) {
		flag[n] = value;
	}

	public static boolean getFlag(int n) {
		return flag[n];
	}

	public static void setAllFlag() {
		for (int i = 1; i <= member; i++) {
			flag[i] = false;
		}
	}

	public static void setMemNum() {
		if (memNum >= 2) {
			Server.SendAll("noVacancy");
			return;
		}
		memNum++;
		if (memNum == 2) {
			isGame = true;
			Server.SendAll("start");
			System.out.println("start");
		}
		System.out.println("now mem  " + memNum);
	}

	public static void resetMemNum() {
		if (isGame) {
			memNum = 0;
			isGame = false;
		} else {
			memNum--;
			if (memNum < 0) memNum = 0;
		}
		System.out.println("now mem  " + memNum);
	}

	public static boolean getIsGame() {
		return isGame;
	}

	public static void main(String[] args) {
		incoming = new Socket[maxConnection];
		flag = new boolean[maxConnection];
		isr = new InputStreamReader[maxConnection];
		in = new BufferedReader[maxConnection];
		out = new PrintWriter[maxConnection];
		myClientProcThread = new ClientProcThread[maxConnection];
		int n = 1;
		member = 0;

		try {
			ServerSocket serverSocket = new ServerSocket(10000);
			while (true) {
				incoming[n] = serverSocket.accept();
				flag[n] = true;
				isr[n] = new InputStreamReader(incoming[n].getInputStream());
				in[n] = new BufferedReader(isr[n]);
				out[n] = new PrintWriter(incoming[n].getOutputStream(), true);
				myClientProcThread[n] = new ClientProcThread(in[n], out[n], n);
				myClientProcThread[n].start();
				member = n;
				n++;
			}
		} catch (Exception e) {
			System.out.println("�\�P�b�g�쐬���ɃG���[���������܂����F" + e);
		}
	}
}
