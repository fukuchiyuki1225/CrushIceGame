import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MesgSend {
	private static MesgSend ms;
	private PrintWriter out;

	private MesgSend(Socket socket) {
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
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
