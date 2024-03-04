import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable {
    private Socket socket;
    private Server server;
    private PrintWriter out;
    private BufferedReader in;
    private String username;

    public ClientHandler(Socket socket, Server server) throws IOException {
        this.socket = socket;
        this.server = server;
        this.out = new PrintWriter(socket.getOutputStream(), true);
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
    }

    public String getUsername() { return username; }

    public void sendMessage(String msg) {
        out.println(msg);
    }

    public void run() {
        try {
            out.println("Enter username:");
            username = in.readLine();
            if (username == null) return;
            server.broadcast(username + " joined the chat", this);
            String message;
            while ((message = in.readLine()) != null) {
                if (message.equals("/quit")) break;
                if (message.equals("/list")) {
                    out.println("Online: " + String.join(", ", server.getUsernames()));
                    continue;
                }
                server.broadcast(username + ": " + message, this);
            }
        } catch (IOException e) {
        } finally {
            server.removeClient(this);
            server.broadcast(username + " left the chat", this);
            try { socket.close(); } catch (IOException e) {}
        }
    }
}
