package ss.uno.networking.server;

import java.io.IOException;

/**
 * This class creates a new Server object and runs it
 */
public class ServerTUI {

    /**
     * creates a new Server object and runs it
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        ServerInterface server = new Server();
        server.run();
    }
}
