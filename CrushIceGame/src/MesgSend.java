import java.io.PrintWriter;
import java.net.Socket;

public class MesgSend {
	private static MesgSend ms;
	private static PrintWriter out;

	private MesgSend(Socket socket) {
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {
			GameScreen.getInstance().setConnectLabel(GameScreen.getInstance().SERVERERROR, true);
		}
	}

	public static MesgSend getInstance(Socket socket) {
		if (ms == null) ms = new MesgSend(socket);
		return ms;
	}

	public static void send(String mesg) {
		out.println(mesg);
	}
}
