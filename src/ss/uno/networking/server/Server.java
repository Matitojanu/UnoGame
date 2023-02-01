package ss.uno.networking.server;

import ss.uno.networking.Protocol;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * This class implemenets the interface ServerInterface and is responsible for connecting to the Client,
 * sending all the messages according to the protocol and closing the connection once the Server has disconnected
 */
public class Server implements ServerInterface {
    public final int port = Protocol.PORT;
    private static List<ClientHandler> _handlers; //_ is best practice to diferentiate members of the class from local parameters
    private ServerSocket _serverSocket;
    private Socket _socket;
    private static ArrayList<Lobby> _lobbyList;
    private static ArrayList<String> _playerNames;

    /**
     * This constructor initializes the list of Lobbies, list of ClientHandlers and the names of the players
     * @throws IOException
     */
    public Server() throws IOException {
        this._handlers = new ArrayList<>();
        this._lobbyList = new ArrayList<>();
        this._playerNames = new ArrayList<>();
    }

    /**
     * This method accepts connections from Clients and allocates them to new threads
     */
    public void start(){
        boolean running = true;
        int threadCount = 0;
        while(running){
            try {
                _socket = _serverSocket.accept();
                ClientHandler clientHandler = new ClientHandler(_socket, "thread-"+threadCount);
                _handlers.add(clientHandler);
                threadCount += 1;
                new Thread(clientHandler).start();
            } catch (IOException e){
                System.out.println("Error");
                running = false;
            }
        }
        shutDown();
        new Thread(this).start();
    }

    /**
     * This method disconnects from the clients and shutsDown the Server
     */
    public void shutDown(){
        try{
            _serverSocket.close();
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    /**
     * This method runs when a new Server thread gets created.
     * It creates a new ServerSocket at a specified port
     */
    @Override
    public void run() {
        try {
            _serverSocket = new ServerSocket(port, 10, Protocol.IPADDRESS);
        } catch (IOException e) {
            System.out.println("Could not start server at port " + port);
        }
        System.out.println("Starting server!");
        start();
    }

    /**
     * This method returns a list of ClientHandlers on the Server
     * @return a list of ClientHandlers
     */
    public static List<ClientHandler> get_handlers() {
        return _handlers;
    }

    /**
     * This method returns a list of Lobbies on the Server
     * @return a list of Lobbies on the Server
     */
    public static ArrayList<Lobby> get_lobbyList() {
        return _lobbyList;
    }

    /**
     * This method adds a new Lobby to the Lobby list
     * @param lobby the lobby to be added to the list
     */
    public static void addLobby(Lobby lobby){
        _lobbyList.add(lobby);
    }

    /**
     * This method returns a list of the names of players connected to the Server
     * @return a list of the names of players connected to the Server
     */
    public static ArrayList<String> get_playerNames() {
        return _playerNames;
    }

    /**
     * This method adds a player name to the name list
     * @param name the name to be added to the list
     */
    public static void add_playerName(String name) {
        _playerNames.add(name);
    }
}
