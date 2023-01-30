package ss.uno.server;

import ss.uno.Board;
import ss.uno.Protocol;
import ss.uno.UnoGame;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;
import ss.uno.player.AbstractPlayer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Lobby implements Runnable{
    private String gameName;
    private int maxPlayers;
    private String gamemode;
    private int numberOfPlayers;
    private UnoGame unoGame;
    private ArrayList<AbstractPlayer> players;

    public Lobby(String gameName, int maxPlayers, String gamemode){
        this.gameName = gameName;
        this.maxPlayers = maxPlayers;
        this.gamemode = gamemode;
        this.numberOfPlayers = 0;
        this.players = new ArrayList<>();
    }

    /**
     * Starts a new Thread
     */
    public void start(){
        new Thread(this).start();
    }

    /**
     * Runs this section when a new Thread gets created, creates and runs a new unoGame
     */
    @Override
    public void run() {
        while(isWaiting()) {
            for(int j = 0; j < Server.get_handlers().size(); j++){
                for(int i = 0; i < players.size(); i++){
                    if(Server.get_handlers().get(j).get_player() != null) {
                        if (Server.get_handlers().get(j).get_player().getName().equals(players.get(i).getName())) {
                            try {
                                Server.get_handlers().get(j).sendProtocol(Protocol.WAIT + Protocol.DELIMITER + gameName + Protocol.DELIMITER + maxPlayers + Protocol.DELIMITER + players.size());

                            } catch (IOException e) {
                                System.out.println("Couldn't send wait command");
                            }
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
            try {
                handler.sendProtocol(Protocol.START);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        unoGame = new UnoGame(players);
        runUnoGame(unoGame);
    }

    public void runUnoGame(UnoGame unoGame){
        while(!unoGame.isGameOver()) {
            unoGame.boardSetUp();
            unoGame.drawCardsInitial();
            unoGame.setPlayersTurn(players.get(0));
            for(int j = 0; j < Server.get_handlers().size(); j++) {
                for (int i = 0; i < players.size(); i++) {
                    if (Server.get_handlers().get(j).get_player() != null) {
                        if (Server.get_handlers().get(j).get_player().getName().equals(players.get(i).getName())) {
                            try {
                                Server.get_handlers().get(j).sendProtocol(Protocol.NEWROUND);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    }
                }
            }
            while (!unoGame.isRoundOver()) {
                for(int j = 0; j < Server.get_handlers().size(); j++){
                    for(int i = 0; i < players.size(); i++) {
                        if (Server.get_handlers().get(j).get_player() != null) {
                            if (Server.get_handlers().get(j).get_player().getName().equals(players.get(i).getName())) {
                                try {
                                    Server.get_handlers().get(j).sendProtocol(Protocol.CURRENTPLAYER + Protocol.DELIMITER + unoGame.getPlayersTurn().getName());
                                } catch (IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                        }
                    }
                }
                //ArrayList<AbstractCard> playerHand = new ArrayList<>();
                //for(AbstractCard card : unoGame.getPlayersTurn().getHand()){
                //    playerHand.add(card);
                //}
                for(int j = 0; j < Server.get_handlers().size(); j++){
                    for(int i = 0; i < players.size(); i++) {
                        if (Server.get_handlers().get(j).get_player() != null) {
                            if (Server.get_handlers().get(j).get_player().getName().equals(players.get(i).getName())) {
                                try {
                                    if(Server.get_handlers().get(j).get_player() == unoGame.getPlayersTurn()) {
                                        if (Server.get_handlers().get(j).get_player().existsValidMove(unoGame.getBoard())) {
                                            String moveList = formatMoveList(unoGame.getPlayersTurn().getHand());
                                            Server.get_handlers().get(j).sendProtocol(Protocol.MOVE + moveList);
                                            String msgFromClient = Server.get_handlers().get(j).listenToMessage();;
                                            String[] msgArray = msgFromClient.split("\\" + Protocol.DELIMITER);
                                            if (msgArray[0].equals(Protocol.MOVE)) {
                                                try {
                                                    Server.get_handlers().get(j).handleMessage(msgFromClient);
                                                } catch (IOException e) {
                                                    System.out.println("Couldn't get client choice");
                                                }
                                            }
                                            Server.get_handlers().get(j).sendProtocol(Protocol.UPDATEFIELD + Protocol.DELIMITER + unoGame.getBoard().getLastCard().getColour() + Protocol.DELIMITER + unoGame.getBoard().getLastCard().getSymbol());
                                        } else {
                                            System.out.println("why are we here");
                                        }
                                    }
                                } catch (IOException e) {
                                    System.out.println("Couldn't send move list");
                                }
                            }
                        }
                    }
                }
            }
            ArrayList<Integer> playerPoints = new ArrayList<>();
            ArrayList<AbstractPlayer> pointOwners = new ArrayList<>();
            for(AbstractPlayer player : unoGame.getPlayersPoints().keySet()){
                playerPoints.add(unoGame.getPlayersPoints().get(player));
                pointOwners.add(player);
            }
            String results = "";
            for(int i = 0; i < players.size(); i++){
                results += Protocol.DELIMITER+pointOwners.get(i).toString()+Protocol.DELIMITERINITEMS+playerPoints.get(i).toString();
            }
            for(ClientHandler handler : Server.get_handlers()){
                try {
                    handler.sendProtocol(Protocol.DISPLAYRESULTS+results);
                } catch (IOException e) {
                    System.out.println("Couldn't display results");
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
        protocolMsg += playerHand.get(0).getColour().toString()+Protocol.DELIMITERINITEMS+playerHand.get(0).getSymbol().toString();
        for (int i = 1; i < playerHand.size(); i++) {
            protocolMsg += Protocol.DELIMITER+playerHand.get(i).getColour().toString()+Protocol.DELIMITERINITEMS+playerHand.get(i).getSymbol().toString();
        }
        return protocolMsg;
    }
}
