import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;

public class MesgSend {
	PrintWriter out;

	public MesgSend(Socket socket) {
		try {
			out = new PrintWriter(socket.getOutputStream(), true);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void send(String mesg) {
		out.println(mesg);
	}
}
