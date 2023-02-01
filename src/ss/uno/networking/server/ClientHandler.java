package ss.uno.networking.server;

import ss.uno.networking.Protocol;
import ss.uno.gamelogic.cards.AbstractCard;
import ss.uno.gamelogic.cards.Card;
import ss.uno.gamelogic.player.AbstractPlayer;
import ss.uno.gamelogic.player.HumanPlayer;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

import static java.lang.Integer.valueOf;

/**
 * This class implements Runnable and is responsible for interpreting commands from the Client
 * and sending appropriate respones from the Server according to the Protocol.
 */
public class ClientHandler implements Runnable {
    private Socket _socket;
    private final BufferedReader _in;
    private final PrintWriter _out;
    private boolean _running;
    private String _name;
    private AbstractPlayer _player;
    private Lobby lobby;

    /**
     * This constructor creates a new ClientHandler responsible for interpreting commands from the Client
     * and sending back appropriate respones from the Server without directly linking them.
     * @param socket the ClientHandler connects with
     * @param name of the Client
     * @throws IOException
     */
    public ClientHandler(Socket socket, String name) throws IOException {
        this._socket = socket;
        this._out = new PrintWriter(_socket.getOutputStream(), true);
        this._in = new BufferedReader(new InputStreamReader((_socket.getInputStream())));
        this._running = true;
        this._name = name;
        //this._playerNames = new ArrayList<>();
        //this._players = new ArrayList<>();
    }

    /**
     * Sends a Hadnshake command, gets the playerName from
     * the Client and sends the Client the list of games allowing them to join
     */
    public void setUp() {
        String msgFromClientHandshake = listenToMessage();
        if (msgFromClientHandshake.equals(Protocol.HANDSHAKE + Protocol.DELIMITER + Protocol.HELLO)) {
            try {
                handleMessage(msgFromClientHandshake);
            } catch (IOException e) {
                System.out.println("Failed to handshake");
            }
        }

        //sendProtocol(Protocol.PLAYERNAME);
        String msgFromClientPlayerName = listenToMessage();
        String[] msgArrayPlayerName = msgFromClientPlayerName.split("\\" + Protocol.DELIMITER);
        if (msgArrayPlayerName[0].equals(Protocol.PLAYERNAME)) {
            try {
                handleMessage(msgFromClientPlayerName);
            } catch (IOException e) {
                System.out.println("Couldn't get player name");
            }
        }

        System.out.println("Starting thread for user: " + _name);

        try {
            sendProtocol(Protocol.FUNCTIONALITIES);
            String msgFromClientFunctonalities = listenToMessage();
            if(msgFromClientFunctonalities != null) {
                String[] msgArrayFunctionalities = msgFromClientFunctonalities.split("\\" + Protocol.DELIMITER);
                if (msgArrayFunctionalities[0].equals(Protocol.FUNCTIONALITIES)) {
                    handleMessage(msgFromClientFunctonalities);
                }
            }
        } catch (IOException e) {
            System.out.println("Client disconnected");
        }

        sendServerList();
        String msgFromClientGame = listenToMessage();
        String[] msgArrayGame = msgFromClientGame.split("\\" + Protocol.DELIMITER);
        if (msgArrayGame[0].equals(Protocol.NEWGAME)||msgArrayGame[0].equals(Protocol.JOINGAME)) {
            try {
                handleMessage(msgFromClientGame);
            } catch (IOException e) {
                System.out.println("Couldn't get client choice");
            }
        }
    }

    /**
     * Runs this section when a new Thread gets created, calls the setUp() function
     */
    @Override
    public void run() {
        setUp();
    }

    /**
     * Contains a switch case statement that chooses how to respond to different messages sent by the Client.
     * Handles all commands connected to creating lobbies and the gameplay loop.
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
                ArrayList<String> functionalities = new ArrayList<>();
                for(int i = 0; i < messageArr[1].length(); i++){
                    functionalities.add(messageArr[1]);
                }
            }
            case Protocol.NEWGAME -> {
                this.lobby = new Lobby(messageArr[1], Integer.parseInt(messageArr[2]), messageArr[3]);
                lobby.start();
                lobby.addPlayer(_player);
                Server.addLobby(lobby);
                System.out.println("Starting new lobby: "+lobby.getGameName());
                break;
            }
            case Protocol.JOINGAME -> {
                lobby = Server.get_lobbyList().get(Integer.parseInt(messageArr[1])-1);
                lobby.addPlayer(_player);
                break;
            }

            //Gameplay loop

            case Protocol.MOVE -> {
                AbstractCard lastCard = lobby.getUnoGame().getBoard().getLastCard();
                if(messageArr[1].equals(Protocol.COLOR)){
                    lastCard.setColour(AbstractCard.Colour.valueOf(messageArr[2]));
                }else if(messageArr[1].equals(Protocol.CHALLENGE)){

                }else{
                    Card playedCard = (Card) lobby.getUnoGame().getPlayersTurn().getHand().get(Integer.parseInt(messageArr[1]));
                    lobby.getUnoGame().playCard((playedCard));
                    if(playedCard.getSymbol().equals(AbstractCard.Symbol.CHANGECOLOR) || playedCard.getSymbol().equals(AbstractCard.Symbol.PLUSFOUR)){
                        if(playedCard.getSymbol().equals(AbstractCard.Symbol.PLUSFOUR)){
                            lobby.getUnoGame().changeTurn();
                            for(int i = 0; i<4; i++){
                                lobby.getUnoGame().drawCard();
                            }
                        }
                        sendProtocol(Protocol.MOVE+Protocol.DELIMITER+Protocol.CHOOSECOLOR);
                        String msgFromClient = listenToMessage();
                        handleMessage(msgFromClient);
                        break;
                    }
                    lobby.getUnoGame().abilityFunction();
                }
                break;
            }

            case Protocol.DRAW -> {
                lobby.getUnoGame().drawCard();
                AbstractCard drawnCard = lobby.getUnoGame().getPlayersTurn().getHand().get(lobby.getUnoGame().getPlayersTurn().getHand().size()-1);
                sendProtocol(Protocol.DRAW+Protocol.DELIMITER+drawnCard.getColour().toString()+Protocol.DELIMITER+drawnCard.getSymbol().toString());
                if(canInstantDiscard()){
                    sendProtocol(Protocol.INSTANTDISCARD+Protocol.DELIMITER+drawnCard.getColour().toString()+Protocol.DELIMITER+drawnCard.getSymbol().toString());
                    String msgFromClient = listenToMessage();
                    handleMessage(msgFromClient);
                }
                break;
            }
            case Protocol.INSTANTDISCARD -> {
                if(messageArr.length > 1) {
                    Card discardedCard = (Card) lobby.getUnoGame().getPlayersTurn().getHand().get(lobby.getUnoGame().getPlayersTurn().getHand().size() - 1);
                    lobby.getUnoGame().playCard((discardedCard));
                    if (discardedCard.getSymbol().equals(AbstractCard.Symbol.CHANGECOLOR) || discardedCard.getSymbol().equals(AbstractCard.Symbol.PLUSFOUR)) {
                        if (discardedCard.getSymbol().equals(AbstractCard.Symbol.PLUSFOUR)) {
                            lobby.getUnoGame().changeTurn();
                            for (int i = 0; i < 4; i++) {
                                lobby.getUnoGame().drawCard();
                            }
                        }
                        sendProtocol(Protocol.MOVE + Protocol.DELIMITER + Protocol.CHOOSECOLOR);
                        String msgFromClient = listenToMessage();
                        handleMessage(msgFromClient);
                        break;
                    }
                    lobby.getUnoGame().abilityFunction();
                    break;
                }else{
                    break;
                }
            }
        }
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
        if (Server.get_playerNames().contains(name) || name.contains(" ")) {
            sendProtocol(Protocol.PLAYERNAME + Protocol.DELIMITER + Protocol.DENIED);
            sendProtocol(Protocol.PLAYERNAME);
        } else {
            Server.get_playerNames().add(name);
            this._name = name;
            _player = new HumanPlayer(name);
            sendProtocol(Protocol.PLAYERNAME + Protocol.DELIMITER + Protocol.ACCEPTED);
        }
    }

    /**
     * Sends the list of currently existing lobbies to the Client
     */
    public void sendServerList(){
        if(Server.get_lobbyList().size() != 0) {
            String msgForClient = "";
            ArrayList<String> serverList = new ArrayList<>();
            for (int i = 0; i < Server.get_lobbyList().size(); i++) {
                String gameName = Server.get_lobbyList().get(i).getGameName();
                String maxPlayers = Integer.toString(Server.get_lobbyList().get(i).getMaxPlayers());
                String numberOfPlayers = Integer.toString(Server.get_lobbyList().get(i).getNumberOfPlayers());
                String gamemode = Server.get_lobbyList().get(i).getGamemode().toString();
                msgForClient += Protocol.DELIMITER + gameName + Protocol.DELIMITERINITEMS + maxPlayers + Protocol.DELIMITERINITEMS + numberOfPlayers + Protocol.DELIMITERINITEMS + gamemode;
                serverList.add(msgForClient);
            }

            try {
                sendProtocol(Protocol.SERVERLIST + msgForClient);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Waits for a message from the Client and then returns it
     * @return a message from the Client
     */
    public String listenToMessage() {
        String line;
        while (true) {
            try {
                if ((line = _in.readLine()) != null){
                    return line;
                }
            } catch (IOException e) {

            }
        }
    }

    /**
     * Checks whether the newly drawn card can be discarded immediately
     * @return true if card can be discarded, false if it can't
     */
    public boolean canInstantDiscard(){
        AbstractCard drawnCard = _player.getHand().get(_player.getHand().size()-1);
        AbstractCard lastCard = lobby.getUnoGame().getBoard().getLastCard();
        if(drawnCard.getColour().equals(lastCard.getColour()) || drawnCard.getSymbol().equals(lobby.getUnoGame().getBoard().getLastCard().getSymbol()) || drawnCard.getColour().equals(AbstractCard.Colour.WILD)){
            return true;
        }
        return false;
    }
    /**
     * Returns currently connected players
     * @return currently connected players
     */
    public AbstractPlayer get_player() {
        return _player;
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
