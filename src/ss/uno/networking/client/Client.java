package ss.uno.networking.client;

import ss.uno.networking.Protocol;
import ss.uno.gamelogic.UnoGame;
import ss.uno.gamelogic.cards.AbstractCard;
import ss.uno.gamelogic.cards.Card;
import ss.uno.gamelogic.player.AbstractPlayer;
import ss.uno.gamelogic.player.HumanPlayer;

import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.List;

import static ss.uno.networking.client.ClientTUI.*;


/**
 * This class implemenets the interface <code>ClientInterface</code> and is responsible for connecting to the server,
 * sending all the messages according to the protocol and closing the connection once the client has disconected
 */
public class Client implements ClientInterface {
    private String _name;
    private Socket _sock;
    private PrintWriter _out;
    private UnoGame _game;
    private BufferedReader _in;
    private boolean _handshakeComplete;
    private boolean _inGame = false;
    private int _maxPlayers;
    private AbstractPlayer _playerClient;
    private String _winnerName;

    /**
     * This method is a constructor. It creates a client instance, initialising the port, IPAdress and creates a socket,
     * while setting up the BufferedReder and PrintWritter to be linked to the socket
     * @throws IOException when an error occurs while initialising the BufferedReader and PrintWritter
     */
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
    @Override
    public boolean connect(){
        this.sendProtocol(Protocol.HANDSHAKE+Protocol.DELIMITER+Protocol.HELLO);
        try {
            String msgFromServer = _in.readLine();
            if(msgFromServer.equals(Protocol.HANDSHAKE+Protocol.DELIMITER+Protocol.HELLO)){
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
    @Override
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
     * This method will be called when a new thread for this class has started. 
     * The method creates an UnoGame object, which constly updates the last card, based on what the server sends, 
     * but in rest, it only stores the important information with which the client of that coresponding user needs in order to make moves, drawe or play.   
     */
    @Override
    public void run() {
        try  {
            String line;
            List<String> servers = new ArrayList<>();
            while ((line = _in.readLine()) != null){
                String[] words  = line.split( "\\" + Protocol.DELIMITER );
                if(_handshakeComplete){
                    switch (words[0]){
                        case Protocol.SERVERLIST:{
                            for (int i = 1; i < words.length; i++) {
                                    servers.add(words[i]);
                            }
                            printServerListText(servers);
                            break;
                        }
                        case Protocol.ERROR:{
                            if(words[1] == Protocol.JOINERROR) {
                                printJoinErrorText();
                                _inGame = false;
                            }
                            break;
                        }
                        case Protocol.WAIT:{
                            String gameName = words[1];
                            _maxPlayers = Integer.parseInt(words[2]);
                            int ammountPlayers = Integer.parseInt(words[3]);
                            printWaitText(gameName, _maxPlayers, ammountPlayers);
                            _inGame = true;
                            break;
                        }
                        case Protocol.START:{
                            printStartText();
                            _inGame = true;
                            List<AbstractPlayer> playerList = new ArrayList<>();
                            playerList.add(_playerClient);
                            for (int i = 0; i < _maxPlayers - 1; i++) {
                                playerList.add(new HumanPlayer("Player " + i));
                            }
                            _game = new UnoGame(playerList);
                            break;
                        }

                        case Protocol.NEWROUND:{
                            _game.drawCardsInitial();
                            printNewRoundText();
                            break;

                        }
                        case Protocol.CURRENTPLAYER: {
                            printCurrentPlayerText(words[1]);
                            break;
                        }
                        case Protocol.UPDATEFIELD :{
                            String[] arguments = new String[]{words[1], words[2]};
                            Card card = parseCard(arguments);
                            _game.getBoard().setLastCard(card);
                            if(!card.getSymbol().equals(AbstractCard.Symbol.CHANGECOLOR) && !card.getSymbol().equals(AbstractCard.Symbol.PLUSFOUR)){
                                _game.abilityFunction();
                            }
                            printUpdatedFieldText(card);
                            break;
                        }
                        case Protocol.MOVE:{
                            if ( words[1].equals(Protocol.CHOOSECOLOR) ) {
                                AbstractCard.Colour pickedColor = null;
                                if(_playerClient instanceof HumanPlayer) {
                                     pickedColor = choseColorFromUserText();

                                }else {
                                    for (AbstractCard abstractCard : _playerClient.getHand()) {
                                        if ( abstractCard.getColour() != AbstractCard.Colour.WILD ) {
                                            pickedColor = abstractCard.getColour();
                                        }
                                    }
                                    if(pickedColor == null){
                                        pickedColor = AbstractCard.Colour.RED;
                                    }
                                }
                                _game.getBoard().getLastCard().setColour(pickedColor);
                                sendProtocol(Protocol.MOVE+Protocol.DELIMITER+ Protocol.COLOR + Protocol.DELIMITER + pickedColor.toString());
                                break;
                            }
                            if( words[1].equals(Protocol.CHALLENGE) ){
                                if(_playerClient instanceof HumanPlayer) {
                                    boolean response = askUserIfChallangeText();
                                    if ( response ) {
                                        sendProtocol(Protocol.MOVE + Protocol.DELIMITER + Protocol.CHALLENGE + Protocol.DELIMITER + Protocol.TRUE);
                                        break;
                                    }
                                }
                                sendProtocol(Protocol.MOVE + Protocol.DELIMITER + Protocol.CHALLENGE + Protocol.DELIMITER + Protocol.FALSE);
                                break;
                            }

                            List<AbstractCard> hand = new ArrayList<>();
                            for (int i = 1; i < words.length; i++) {
                                hand.add(parseCard(words[i].split(Protocol.DELIMITERINITEMS)));
                            }
                            _playerClient.setHand(hand);
                            if(_playerClient.existsValidMove(_game.getBoard())==false) {
                                if ( _playerClient instanceof HumanPlayer ){
                                    printShowPlayerHandText(hand);
                                    printNoAvailableMovesText();
                                }
                                sendProtocol(Protocol.DRAW);
                                break;
                            }
                            if(_playerClient instanceof HumanPlayer) {

                                int move;
                                while (true) {
                                    printShowPlayerHandText(hand);
                                    move = getMoveFromUserText(hand)-1;
                                    if ( move == hand.size() ) {
                                        sendProtocol(Protocol.DRAW);
                                        break;
                                    }else if( _game.isCardValid((Card) hand.get(move)) ) {
                                        _game.getBoard().setLastCard((Card) hand.get(move));
                                        sendMove(move);
                                        break;
                                    } else {
                                        System.out.println(WRONGINPUT);
                                    }
                                }
                                break;
                            } else {
                                sendMove(_playerClient.determineMove(_game.getBoard()));
                            }
                            break;
                        }
                        case Protocol.DRAW:{
                            String[] arguments = new String[]{words[1], words[2]};
                            Card card = parseCard(arguments);
                            printDrawnCardText(card);
                            Card lastCard = _game.getBoard().getLastCard();
                            break;
                        }
                        case Protocol.INSTANTDISCARD:{
                            String[] cardArguments = new String[]{words[1], words[2]};
                            Card card = parseCard(cardArguments);
                            String response;
                            if(_playerClient instanceof HumanPlayer) {
                                response = askForChoiceToPlayDrawnCardText();
                                if ( response.equalsIgnoreCase("y") ) {
                                    sendProtocol(Protocol.INSTANTDISCARD + Protocol.DELIMITER + card.getColour().toString() + Protocol.DELIMITER + card.getSymbol().toString());
                                    _game.getBoard().setLastCard(card);
                                } else {
                                    sendProtocol(Protocol.INSTANTDISCARD);
                                }

                            }else{
                                //if AI, always play the drawn card
                                sendProtocol(Protocol.INSTANTDISCARD + Protocol.DELIMITER + card.getColour().toString() + Protocol.DELIMITER + card.getSymbol().toString());

                            }
                            break;
                        }
                        case Protocol.DISPLAYRESULTS:{
                            List<String> listResultsString = new ArrayList<>();
                            for (int i = 1; i < words.length; i++) {
                                listResultsString.add(words[i]);
                            }
                            printResultsText(listResultsString);
                            _winnerName = get_winnerName(listResultsString);
                            break;
                        }
                        case Protocol.GAMEOVER:{
                            printWinnerText(_winnerName);
                            synchronized (this){
                                notify();
                            }
                            _inGame=false;
                            break;
                        }
                    }
                }

            }
        } catch (IOException e) {
            this.close();
        }
    }

    /**
     * This method takes as parameters a string array that contains the colour and the symbol of a card, and returns the Card instance of it.
     * @param words - the card arguments, first the colour, then the symbol of the card
     * @return the card object created from the parameters
     */
    private static Card parseCard(String[] words) {
        AbstractCard.Colour cardColour = null;
        AbstractCard.Symbol cardSymbol = null;
        for(AbstractCard.Colour colour : AbstractCard.Colour.values()){
            if(colour.toString().equalsIgnoreCase(words[0])){
                cardColour = colour;
            }
        }
        for(AbstractCard.Symbol symbol : AbstractCard.Symbol.values()){
            if(symbol.toString().equalsIgnoreCase(words[1])){
                cardSymbol = symbol;
            }
        }
        Card card = new Card(cardColour, cardSymbol);
        return card;
    }

    /**
     * This method takes the List as argument and checks for every player if
     * they have the point higher or equal to 30, and returns the name of said player
     * @param playersPointsList the list of players, containing also the points
     * @return the string of the winner with 30 or more points
     */
   public String get_winnerName(List<String> playersPointsList){
        String winner = null;
        for (int i = 0; i < playersPointsList.size(); i++) {
            String[] playersArguments = playersPointsList.get(i).split(Protocol.DELIMITERINITEMS);
            String playerName = playersArguments[0];
            int points = Integer.parseInt(playersArguments[1]);
            if(points>=500){
               winner = playerName;
            }
        }
        return winner;
   }

    /**
     * This method sends the move of the client to the client handler, according to the protocol
     * @param move the move of the client
     */
    @Override
    public void sendMove(int move){
        _out.println(Protocol.MOVE + Protocol.DELIMITER + move);
    }

    /**
     * This method sends the name of the player to the server for the first time and waits for the server to send whether it si accepted or denied, according to the protocol
     * @param name of the client
     * @return True if the name isn't already taken and sets the name of the client to the input, False if the name is indeed taken
     */
    @Override
    public boolean sendName(String name){
        _out.println(Protocol.PLAYERNAME + Protocol.DELIMITER + name);
        try {
            String msgFromServer = _in.readLine();
            if ( msgFromServer.equals(Protocol.PLAYERNAME + Protocol.DELIMITER + Protocol.ACCEPTED) ) {
                this._name = name;
                return true;
            } else if ( msgFromServer.equals(Protocol.PLAYERNAME + Protocol.DELIMITER + Protocol.DENIED) ) {
                return false;
            }
        } catch (SocketTimeoutException e){
            System.out.println("The server has not responded when sending name");
        } catch (IOException e) {
            System.out.println("IO exception when sending name");
        }
        return false;
    }

    /**
     * This method sends to the server a protocol message
     * @param message the protocol
     */
    @Override
    public void sendProtocol(String message){
        _out.println(message);
    }

    /**
     * This function sends the List of features as a String to the server, according to the protocol
     * @param features the list of functionalities the client chose
     */
    @Override
    public void sendFunctionalities(List<String> features) {

        try {
            _out.println(formatFunctionalities(features));
        } catch (Exception e) {
            System.out.println("An exception while sending functionalities");
        }
    }

    /**
     * This method formats the functionalities sent as parameter, according to protocol
     * @param features the list of aditional functionalities
     * @return the string representation of the functionalities, as the protocol wishes for it
     */
    @Override
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

    /**
     * This method returns the status of the client, whether it is curently in a game or if it's not
     * @return true, if the client has joined or created a game, false if it has not entered a game
     */
    @Override
    public boolean is_inGame() {
        return _inGame;
    }

    /**
     * This method sets the status of the client, reguarding wheter it is in a game or not to the variable given as parameter
     * @param _inGame the in game status to wisch the local variable of the client will be set to
     */
    @Override
    public void set_inGame(boolean _inGame) {
        this._inGame = _inGame;
    }

    /**
     * This method sets the variable that hold what type of player the client is, to the one given as parameters
     * @param playerType the type of player that the client will be set to
     */
    @Override
    public void set_playerClient(AbstractPlayer playerType) {
        this._playerClient = playerType;
    }


}
