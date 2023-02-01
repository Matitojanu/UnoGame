package ss.uno.networking.server;

import java.util.List;

public interface ServerInterface extends Runnable{

    /**
     * This method accepts connections from Clients and allocates them to new threads
     */
    void start();

    /**
     * This method disconnects from the clients and shutsDown the Server
     */
    void shutDown();

    /**
     * This method runs when a new Server thread gets created.
     * It creates a new ServerSocket at a specified port
     */
    void run();




}
