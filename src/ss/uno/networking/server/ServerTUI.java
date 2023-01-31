package ss.uno.networking.server;

import java.io.IOException;

public class ServerTUI {

    public static void main(String[] args) throws IOException {
        Server server = new Server();
        server.run();
    }
}
