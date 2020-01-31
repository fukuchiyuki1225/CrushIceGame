import java.io.PrintWriter;
import java.net.Socket;

public class MesgSend {
	private static MesgSend ms;
	private PrintWriter out;

	private MesgSend(Socket socket) {
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (Exception e) {
			GameScreen.getInstance().setConnectLabel(2, true);
		}
	}

	public static MesgSend getInstance(Socket socket) {
		ms = new MesgSend(socket);
		return ms;
	}

	public static MesgSend getInstance() {
		return ms;
	}

	public void send(String mesg) {
		out.println(mesg);
	}
}
