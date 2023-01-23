package ss.uno.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private Socket socket;
    private final BufferedReader in;
    private final PrintWriter out;
    private boolean running;
    private final String name;

    public ClientHandler(Socket s, String name) throws IOException {
        this.socket = s;
        this.in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        this.out = new PrintWriter(socket.getOutputStream());
        this.running = true;
        this.name = name;
    }

    @Override
    public void run() {
        System.out.println("Client started");
        while(running) {
            try {
                String message = in.readLine();
                if ( message == null ) {
                    System.out.println("The message is null!");
                    running = false;
                    continue;
                }
                handleMessage(message);
            } catch (IOException e) {
                System.out.println("Error");
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public void handleMessage(String message){
        out.println(message);
    }


}
