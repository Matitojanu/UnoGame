package ss.uno.client;

import ss.uno.Protocol;
import ss.uno.UnoGame;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.player.AbstractPlayer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static ss.uno.client.ClientTUI.*;

public class Client implements Runnable {
    private String _name;
    private Socket _sock;
    private PrintWriter _out;
    private UnoGame _game;
    private BufferedReader _in;
    private boolean _handshakeComplete;
    private boolean _inGame = false;


    public Client() throws IOException {
        this._sock = new Socket(Protocol.IPADDRESS, Protocol.PORT);
        _sock.setSoTimeout(180000);
        _in = new BufferedReader(new InputStreamReader(_sock.getInputStream()));
        _out = new PrintWriter(_sock.getOutputStream(), true);
        _handshakeComplete = false;
    }

    /**
     * This method tries to connect the client to the server by sending the "HANDSHAKE" protocol
     * @return True if the server sends back to the clienty the same protocol, false if it doesn't send the right protocol
     */
    public boolean connect(){
        this.sendProtocol(Protocol.HANDSHAKE);
        try {
            String msgFromServer = _in.readLine();
            if(msgFromServer.equals(Protocol.HANDSHAKE+Protocol.DELIMITER+Protocol.HELLO)){ //this can wait for the server infinetly so we need to find solution about it
                new Thread(this).start();
                _handshakeComplete=true;
                return true;
            }
        } catch (IOException e){
            System.out.println("IO exception while handshake");
        }
        return false;
    }

    /**
     * This method closes the socket, which means it closes the conection between the server and the client
     */
    public void close(){
        try {
            _sock.close();
        } catch (SocketException e){
            System.out.println("Socket was closed");
        } catch (IOException e) {
            System.out.println("Unknown IO exception!");
        }
    }

    /**
     * This method will be called when a new thread for this class has started
     */
    @Override
    public void run() {
        try  {
            String line;
            String[] servers;
            while ((line = _in.readLine()) != null){
                String[] words  = line.split(Protocol.DELIMITER);
                if(_handshakeComplete){
                    switch (words[0]){
                        case Protocol.SERVERLIST:{
                            servers = words[1].split(Protocol.DELIMITER);
                            serverListTUI(servers);
                        }
                        case Protocol.ERROR:{
                            if(words[1] == Protocol.JOINERROR) {
                                joinErrorTUI();
                                _inGame = false;
                            }
                        }
                        case Protocol.WAIT:{
                            _inGame = true;
                        }
                        case Protocol.START:{
                            _inGame = true;
                        }
                        case Protocol.CURRENTPLAYER: {
                            currentPlayerTUI(_game.getPlayersTurn().getName());
                        }
                        case Protocol.UPDATEFIELD :{
                            AbstractCard.Colour cardColour = null;
                            AbstractCard.Symbol cardSymbol = null;
                            for(AbstractCard.Colour colour : AbstractCard.Colour.values()){
                                if(colour.toString().toUpperCase() == words[1].split(Protocol.DELIMITERINITEMS)[0].toUpperCase()){
                                    cardColour = colour;
                                }
                            }
                            for(AbstractCard.Symbol symbol : AbstractCard.Symbol.values()){
                                if(symbol.toString().toUpperCase() == words[1].split(Protocol.DELIMITERINITEMS)[1].toUpperCase()){
                                    cardSymbol = symbol;
                                }
                            }
                            Card card = new Card(cardColour, cardSymbol);
                            if( _game.isCardValid(card) ) {
                                _game.playCard(card);
                                _game.abilityFunction();
                                updatedFieldTUI(card);
                            }
                        }
                        case Protocol.MOVE:{
                            AbstractPlayer player = _game.getPlayersTurn();
                            showPlayerHandTUI(player);
                            ArrayList<AbstractCard> hand = player.getHand();
                            int move = getMoveFromUserTUI();
                            if(player.existsValidMove(_game.getBoard())){
                                if(_game.isCardValid((Card) player.getHand().get(move))){
                                    sendMove(move);
                                }
                            } else {
                                sendProtocol(Protocol.DRAW);
                            }
                        }
                        case Protocol.DRAW:{}



                    }
                }

            }
        } catch (IOException e) {
            this.close();
        }
    }

    /**
     * This method sends the username of the client to its cleint handler
     * @param name the username of the client
     */
    public void sendUserNameForChat (String name){
        _out.println("[ " + name + " ] : ");
    }

    /**
     * This method sends the move of the client to the client handler, according to the protocol
     * @param move the move of the client
     */
    public void sendMove(int move){

        try {
            String msgServer;
            if ( (msgServer = _in.readLine()) != null ){
                _out.println(Protocol.MOVE + Protocol.DELIMITER + move);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * This method sends the name of the player to the server for the first time and waits for the server to send whether it si accepted or denied, according to the protocol
     * @param name of the client
     * @return True if the name isn't already taken and sets the name of the client to the input, False if the name is indeed taken
     */
    public boolean sendName(String name){
        _out.println(Protocol.PLAYERNAME + Protocol.DELIMITER + name);
        _out.flush();
        try {
            String msgFromServer = _in.readLine();
            if(msgFromServer.equals(Protocol.PLAYERNAME + Protocol.DELIMITER + Protocol.ACCEPTED )){
                this._name = name;
                return true;
            } else if ( msgFromServer.equals(Protocol.PLAYERNAME + Protocol.DELIMITER + Protocol.DENIED )){
                return false;
            }
        } catch (SocketTimeoutException e){
            System.out.println("The server has not responded");
        } catch (IOException e) {
            System.out.println("IO exception when sending name");
        }
        return false;
    }

    /**
     * This method sends to the server a protocol message
     * @param message the protocol
     */
    public void sendProtocol(String message){
        _out.println(message);
        _out.flush();
    }

    /**
     * This function sends the List of features as a String to the server, according to the protocol
     * @param features the list of functionalities the client chose
     */
    public void sendFunctionalities(List<String> features) {

        try {
            if ( _in.readLine() == Protocol.FUNCTIONALITIES ) {
                _out.println(formatFunctionalities(features));
                _out.flush();
            }
        } catch (Exception e) {
            System.out.println("An exception while sending functionalities");
        }
    }

    public String formatFunctionalities(List<String> features) {
        String protocolMsg = "";
        protocolMsg = features.get(0);
        for (int i = 1; i < features.size(); i++) {
            if ( !protocolMsg.contains(features.get(i)) ) {
                protocolMsg = Protocol.DELIMITER + features.get(i).toUpperCase();
            }
        }
        return protocolMsg;
    }


    public UnoGame getGame() {
        return _game;
    }

    public void setGame(UnoGame game) {
        this._game = game;
    }

    public boolean is_inGame() {
        return _inGame;
    }
}
