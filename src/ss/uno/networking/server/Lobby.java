package ss.uno.networking.server;

import ss.uno.networking.Protocol;
import ss.uno.gamelogic.UnoGame;
import ss.uno.gamelogic.cards.AbstractCard;
import ss.uno.gamelogic.player.AbstractPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * This class implements Runnable and is responsible for creating and managing an UnoGame. It waits for it to fill up
 * with players and then begins an online game with online players or with AI.
 */
public class Lobby implements Runnable{
    private String gameName;
    private int maxPlayers;
    private String gamemode;
    private int numberOfPlayers;
    private UnoGame unoGame;
    private ArrayList<AbstractPlayer> players;

    /**
     * Creates a new Lobby with set name, maximum number of players and chosen gamemode
     * @param gameName - name of the game
     * @param maxPlayers - maximum number of players
     * @param gamemode - the gamemode
     */
    public Lobby(String gameName, int maxPlayers, String gamemode){
        this.gameName = gameName;
        this.maxPlayers = maxPlayers;
        this.gamemode = gamemode;
        this.numberOfPlayers = 1;
        this.players = new ArrayList<>();
    }

    /**
     * Starts a new Thread
     */
    public void start(){
        new Thread(this).start();
    }

    /**
     * Runs this section when a new Thread gets created, waits for the lobby to fill up with players
     * and creates a new UnoGame
     */
    @Override
    public void run() {
        while(isWaiting()) {
            for(ClientHandler handler : Server.get_handlers()){
                for(AbstractPlayer player : players){
                    if(isPlayerInLobby(handler, player)){
                        try {
                            handler.sendProtocol(Protocol.WAIT + Protocol.DELIMITER + gameName + Protocol.DELIMITER + maxPlayers + Protocol.DELIMITER + players.size());
                        } catch (IOException e) {
                            System.out.println("Couldn't send wait command");
                        }

                    }
                }
            }
                try {
                    TimeUnit.SECONDS.sleep(3);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        for(ClientHandler handler : Server.get_handlers()){
            for(AbstractPlayer player : players) {
                try {
                    handler.sendProtocol(Protocol.START);
                } catch (IOException e) {
                    System.out.println("Couldn't send start message");;
                }
            }
        }
        unoGame = new UnoGame(players);
        runUnoGame(unoGame);
    }

    /**
     * Sets up the UnoGame and runs it following all game logic
     * @param unoGame - UnoGame created by the lobby
     */
    public void runUnoGame(UnoGame unoGame){
        for (AbstractPlayer player : players) {
            unoGame.getPlayersPoints().put(player, 0);
        }
        while(!unoGame.isGameOver()) {
            unoGame.boardSetUp();
            unoGame.drawCardsInitial();
            unoGame.setPlayersTurn(players.get(0));
            for(ClientHandler handler : Server.get_handlers()){
                for(AbstractPlayer player : players){
                    if(isPlayerInLobby(handler, player)){
                        try {
                            handler.sendProtocol(Protocol.NEWROUND);
                        } catch (IOException e) {
                            System.out.println("Couldn't send new round message");;
                        }
                    }
                }
            }
            while (!unoGame.isRoundOver()) {
                AbstractCard lastCard = unoGame.getBoard().getLastCard();
                AbstractPlayer playersTurn = unoGame.getPlayersTurn();
                for(ClientHandler handler : Server.get_handlers()){
                    for(AbstractPlayer player : players){
                        if(isPlayerInLobby(handler, player)){
                            try {
                                handler.sendProtocol(Protocol.CURRENTPLAYER + Protocol.DELIMITER + playersTurn.getName());
                                handler.sendProtocol(Protocol.UPDATEFIELD + Protocol.DELIMITER + lastCard.getColour() + Protocol.DELIMITER + lastCard.getSymbol());
                            } catch (IOException e) {
                                System.out.println("Couldn't send update to Client");
                            }
                        }
                    }
                }

                label1:
                for(ClientHandler handler : Server.get_handlers()){
                    for(AbstractPlayer player : players){
                        if(isPlayerInLobby(handler, player)){
                            try {
                                if(handler.get_player() == unoGame.getPlayersTurn()) {
                                    String moveList = formatMoveList(unoGame.getPlayersTurn().getHand());
                                    handler.sendProtocol(Protocol.MOVE + moveList);
                                    String msgFromClient = handler.listenToMessage();
                                    String[] msgArray = msgFromClient.split("\\" + Protocol.DELIMITER);
                                    if (msgArray[0].equals(Protocol.MOVE)) {
                                        try {
                                            handler.handleMessage(msgFromClient);
                                        } catch (IOException e) {
                                            System.out.println("Couldn't get client choice");
                                        }
                                        break label1;
                                    }else if(msgArray[0].equals(Protocol.DRAW)) {
                                        handler.handleMessage(msgFromClient);
                                        break label1;
                                    }
                                }
                            } catch (IOException e) {
                                System.out.println("Couldn't send move list");
                            }
                        }
                    }
                }
                unoGame.changeTurn();
            }
            unoGame.distributePoints();
            String results = "";
            for(AbstractPlayer player : unoGame.getPlayersPoints().keySet()){
                results += Protocol.DELIMITER + player.getName() + Protocol.DELIMITERINITEMS + unoGame.getPlayersPoints().get(player);
            }
            for(ClientHandler handler : Server.get_handlers()){
                for(AbstractPlayer player : players) {
                    if(isPlayerInLobby(handler, player)) {
                        try {
                            handler.sendProtocol(Protocol.DISPLAYRESULTS + results);
                        } catch (IOException e) {
                            System.out.println("Couldn't display results");
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns true when lobby isn't full and false when it gets filled
     * @return true when lobby isn't full and false when it gets filled
     */
    public boolean isWaiting(){
        if(players.size() == maxPlayers){
            return false;
        }
        return true;
    }

    /**
     * Checks whether the play is in lobby
     * @param handler - ClientHandler to get player from
     * @param player - Player from the lobby to compare the player from ClientHandler
     * @return true if player from the ClientHandler is in the Lobby, false if he is not
     */
    public boolean isPlayerInLobby(ClientHandler handler, AbstractPlayer player){
        if(handler.get_player() != null) {
            if (handler.get_player().getName().equals(player.getName())) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the name of the lobby
     * @return the name of the lobby
     */
    public String getGameName() {
        return gameName;
    }

    /**
     * Returns the maximum number of players
     * @return the maximum number of players
     */
    public int getMaxPlayers() {
        return maxPlayers;
    }

    /**
     * Returns the gamemode
     * @return the gamemode
     */
    public String getGamemode() {
        return gamemode;
    }

    /**
     * Returns the current number of players
     * @return the current number of players
     */
    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    /**
     * Returns a list of players in the lobby
     * @return a list of players in the lobby
     */
    public ArrayList<AbstractPlayer> getPlayers() {
        return players;
    }

    /**
     * Returns the unoGame
     * @return the unoGame
     */
    public UnoGame getUnoGame() {
        return unoGame;
    }

    /**
     * Adds a player to the list of players
     * @param player that joined the lobby
     */
    public void addPlayer(AbstractPlayer player){
        players.add(player);
    }

    /**
     * Formats the move list into a list
     * @param cards in the current player's hand
     * @return a formatted move list
     */
    public String formatMoveList(List<AbstractCard> playerHand) {
        String protocolMsg = "";
        protocolMsg += Protocol.DELIMITER+playerHand.get(0).getColour().toString()+Protocol.DELIMITERINITEMS+playerHand.get(0).getSymbol().toString();
        for (int i = 1; i < playerHand.size(); i++) {
            protocolMsg += Protocol.DELIMITER+playerHand.get(i).getColour().toString()+Protocol.DELIMITERINITEMS+playerHand.get(i).getSymbol().toString();
        }
        return protocolMsg;
    }
}
