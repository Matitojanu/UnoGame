package ss.uno.server;

import ss.uno.Protocol;
import ss.uno.UnoGame;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.client.Client;
import ss.uno.player.AbstractPlayer;
import ss.uno.player.HumanPlayer;
import ss.uno.player.OnlinePlayer;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static java.lang.Integer.valueOf;

public class ClientHandler implements Runnable {
    private Socket _socket;
    private final BufferedReader _in;
    private final PrintWriter _out;
    private boolean _running;
    private String _name;
    private ArrayList<String> _playerNames;
    private ArrayList<AbstractPlayer> _players;
    private ArrayList<Lobby> _lobbyList;
    private Lobby lobby;

    public ClientHandler(Socket socket, String name) throws IOException {
        this._socket = socket;
        this._out = new PrintWriter(_socket.getOutputStream(), true);
        this._in = new BufferedReader(new InputStreamReader((_socket.getInputStream())));
        this._running = true;
        this._name = name;
        this._playerNames = new ArrayList<>();
        this._players = new ArrayList<>();
        this._lobbyList = new ArrayList<>();
    }

    /**
     * Sends a Hadnshake command, gets the playerName from
     * the Client and sends the Client the list of games allowing them to join
     */
    public void setUp() {
        try {
            String msgFromClient = _in.readLine();
            if (msgFromClient.equals(Protocol.HANDSHAKE + Protocol.DELIMITER + Protocol.HELLO)) {
                try {
                    handleMessage(msgFromClient);
                } catch (IOException e) {
                    System.out.println("Failed to handshake");
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        }

        try {
            //sendProtocol(Protocol.PLAYERNAME);
            String msgFromClient = _in.readLine();
            String[] msgArray = msgFromClient.split("\\" + Protocol.DELIMITER);
            if (msgArray[0].equals(Protocol.PLAYERNAME)) {
                try {
                    handleMessage(msgFromClient);
                } catch (IOException e) {
                    System.out.println("Couldn't get player name");
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        }

        System.out.println("Starting thread for user: " + _name);

        try {
            sendProtocol(Protocol.FUNCTIONALITIES);
            String msgFromClient = _in.readLine();
            if(msgFromClient != null) {
                String[] msgArray = msgFromClient.split("\\" + Protocol.DELIMITER);
                if (msgArray[0].equals(Protocol.FUNCTIONALITYARR)) {
                    formatFunctionalities(Collections.singletonList(msgArray[1]));
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        }

        try {
            sendServerList();
            String msgFromClient = _in.readLine();
            if(msgFromClient != null) {
                String[] msgArray = msgFromClient.split("\\" + Protocol.DELIMITER);
                if (msgArray[0].equals(Protocol.NEWGAME) || msgArray[0].equals(Protocol.JOINGAME)) {
                    try {
                        handleMessage(msgFromClient);
                    } catch (IOException e) {
                        System.out.println("Couldn't get client choice");
                    }
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        }
    }

    /**
     * Runs this section when a new Thread gets created, calls the setUp() function
     */
    @Override
    public void run() {
        setUp();
        try (_in) {
            String line;
            while ((line = _in.readLine()) != null) {
                try (_in) {
                    handleMessage(line);
                } catch (IOException e) {
                    System.out.println("Error");
                }
            }
        } catch (IOException e) {
            System.out.println("Error");
        }
    }

    /**
     * Contains a switch case statement that chooses how to respond to different messages sent by the Client
     * @param message sent by the client
     * @throws IOException
     */
    public void handleMessage(String message) throws IOException {
        String[] messageArr = message.split("\\" + Protocol.DELIMITER);
        switch (messageArr[0]) {
            case Protocol.HANDSHAKE -> {
                sendProtocol(Protocol.HANDSHAKE + Protocol.DELIMITER + Protocol.HELLO);
                break;
            }
            case Protocol.PLAYERNAME -> {
                if (messageArr.length == 2) {
                    checkName(messageArr[1]);
                }
                break;
            }
            case Protocol.FUNCTIONALITIES -> {

            }
            case Protocol.NEWGAME -> {
                String[] itemSplit = messageArr[1].split(Protocol.DELIMITERINITEMS);
                this.lobby = new Lobby(itemSplit[1], Integer.parseInt(itemSplit[2]), itemSplit[3]);
                lobby.start();
                lobby.addPlayer(_players.get(_players.indexOf(_name)));
                _lobbyList.add(lobby);
                sendServerList();
                break;
            }
            case Protocol.JOINGAME -> {
                _lobbyList.get(Integer.parseInt(messageArr[1])-1).getPlayers().add(_players.get(_players.indexOf(_name)));
                break;
            }

            //Gameplay loop

            case Protocol.MOVE -> {
                if(messageArr[1].equals(Protocol.COLOR)){
                    lobby.getUnoGame().getBoard().getLastCard().setColour(AbstractCard.Colour.valueOf(messageArr[2]));
                }else{
                    lobby.getUnoGame().getPlayersTurn().determineMove(lobby.getUnoGame().getBoard());
                }
                break;
            }
            case Protocol.DRAW -> {
                lobby.getUnoGame().drawCard();
                sendProtocol(Protocol.DRAW+Protocol.DELIMITER+lobby.getUnoGame().getPlayersTurn().getHand().get(lobby.getUnoGame().getPlayersTurn().getHand().size()-1).getColour().toString()+Protocol.DELIMITER+lobby.getUnoGame().getPlayersTurn().getHand().get(lobby.getUnoGame().getPlayersTurn().getHand().size()-1).getSymbol().toString());
                break;
            }
            case Protocol.INSTANTDISCARD -> {
                lobby.getUnoGame().playCard((Card) lobby.getUnoGame().getPlayersTurn().getHand().get(lobby.getUnoGame().getPlayersTurn().getHand().size()-1));
                break;
            }
        }
    }

    /**
     * Sends a chat message to the Client (currently not used)
     * @param message
     */
    public void sendMessage(String message) {
        this._out.println(message);
        System.out.println("[" + this._name + "]" + message);
    }

    /**
     * Sends a protocol command to the client
     * @param message sent to the client
     * @throws IOException
     */
    public void sendProtocol(String message) throws IOException {
        _out.println(message);
    }

    /**
     * Checks whether the name is valid and sends the appropriate protocol response
     * @param name to be checked by the method
     * @throws IOException
     */
    public void checkName(String name) throws IOException {
        if (_playerNames.contains(name) || name.contains(" ")) {
            sendProtocol(Protocol.PLAYERNAME + Protocol.DELIMITER + Protocol.DENIED);
            sendProtocol(Protocol.PLAYERNAME);
        } else {
            _playerNames.add(name);
            this._name = name;
            AbstractPlayer player = new HumanPlayer(name);
            _players.add(player);
            sendProtocol(Protocol.PLAYERNAME + Protocol.DELIMITER + Protocol.ACCEPTED);
        }
    }

    /**
     * Sends the list of currently existing lobbies to the Client
     */
    public void sendServerList(){
        if(_lobbyList.size() != 0) {
            String msgForClient = "";
            ArrayList<String> serverList = new ArrayList<>();
            for (int i = 0; i < _lobbyList.size(); i++) {
                msgForClient = _lobbyList.get(i).getGameName() + Protocol.DELIMITERINITEMS + Integer.toString(_lobbyList.get(i).getMaxPlayers()) + Protocol.DELIMITERINITEMS + Integer.toString(_lobbyList.get(i).getNumberOfPlayers()) + Protocol.DELIMITERINITEMS + lobby.getGamemode().toString();
                serverList.add(msgForClient);
            }
            String[] serverListArr = serverList.toArray(new String[0]);
            try {
                sendProtocol(Protocol.SERVERLIST + Protocol.DELIMITER + serverListArr);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Formats the features sent by the Client (currently not used)
     * @param features sent by the Client
     * @return formatted functionalities
     */
    public String formatFunctionalities(List<String> features) {
        String protocolMsg = "";
        protocolMsg = features.get(0);
        for (int i = 1; i < features.size(); i++) {
            if (!protocolMsg.contains(features.get(i))) {
                protocolMsg = Protocol.DELIMITER + features.get(i).toUpperCase();
            }
        }
        return protocolMsg;
    }

    /**
     * Returns currently connected players
     * @return currently connected players
     */
    public ArrayList<AbstractPlayer> get_players() {
        return _players;
    }

    /**
     * Returns the input stream
     * @return the input stream
     */
    public BufferedReader get_in() {
        return _in;
    }

    /**
     * Returns the output stream
     * @return the output stream
     */
    public PrintWriter get_out() {
        return _out;
    }
}
