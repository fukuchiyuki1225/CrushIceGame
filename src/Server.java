import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

class ClientProcThread extends Thread {
	// private Socket incoming;
	// private InputStreamReader myIsr;
	private BufferedReader myIn;
	private PrintWriter myOut;
	private int number;
	private String name;

	public ClientProcThread(BufferedReader in, PrintWriter out, int n) {
		// incoming = i;
		// myIsr = ist;
		myIn = in;
		myOut = out;
		number = n;
	}

	public void run() {
		try {
			myOut.println("You are a client No." +number);
			name = myIn.readLine();

			while (true) {
				String str = myIn.readLine();
				System.out.println("Received from client No." + number + "(" + name + "), Messages: " + str);
				if (str != null) {
					if (str.toUpperCase().equals("BYE")) {
						myOut.println("Good bye!");
						break;
					}
					Server.SendAll(str, name);
				}
			}
		} catch (Exception e) {
			System.out.println("Disconeect from client No." + number + "(" + name + ")");
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
	private static int member;

	public static void SendAll(String str, String name) {
		for (int i = 1; i <= member; i++) {
			if (flag[i]) {
				out[i].println(str);
				out[i].flush();
				System.out.println("Send Messages to Client No." + i);
			}
		}
	}

	public static void SetFlag(int n, boolean value) {
		flag[n] = value;
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
			System.out.println("The server has launched!");
			ServerSocket serverSocket = new ServerSocket(10000);
			while (true) {
				incoming[n] = serverSocket.accept();
				flag[n] = true;
				System.out.println("Accept client No." + n);
				isr[n] = new InputStreamReader(incoming[n].getInputStream());
				in[n] = new BufferedReader(isr[n]);
				out[n] = new PrintWriter(incoming[n].getOutputStream(), true);
				myClientProcThread[n] = new ClientProcThread(in[n], out[n], n);
				myClientProcThread[n].start();
				member = n;
				n++;
			}
		} catch (Exception e) {
			System.out.println("ソケット作成時にエラーが発生しました：" + e);
		}
	}
}
