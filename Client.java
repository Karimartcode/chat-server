import java.net.*;
import java.io.*;

public class Client {
    private Socket socket;
    private PrintWriter out;
    private BufferedReader in;
    private BufferedReader console;

    public Client(String host, int port) throws IOException {
        socket = new Socket(host, port);
        out = new PrintWriter(socket.getOutputStream(), true);
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        console = new BufferedReader(new InputStreamReader(System.in));
    }

    public void start() {
        new Thread(() -> {
            try {
                String msg;
                while ((msg = in.readLine()) != null)
                    System.out.println(msg);
            } catch (IOException e) {}
        }).start();

        try {
            String input;
            while ((input = console.readLine()) != null) {
                out.println(input);
                if (input.equals("/quit")) break;
            }
        } catch (IOException e) {}
        try { socket.close(); } catch (IOException e) {}
    }

    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "localhost";
        int port = args.length > 1 ? Integer.parseInt(args[1]) : 5000;
        new Client(host, port).start();
    }
}
