package ss.uno.server;

import ss.uno.Board;
import ss.uno.Protocol;
import ss.uno.UnoGame;
import ss.uno.cards.AbstractCard;
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
        boolean waiting = true;
        while(waiting) {
            for(ClientHandler handler : Server.get_handlers()){
                try {
                    handler.sendProtocol(Protocol.WAIT+Protocol.DELIMITER+gameName+Protocol.DELIMITER+maxPlayers+Protocol.DELIMITER+players.size());
                    TimeUnit.SECONDS.sleep(10);
                } catch (IOException e) {
                    System.out.println("Couldn't send wait command");
                } catch (InterruptedException e) {
                    System.out.println("Interrupted wait");
                }
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
        unoGame.run();
        while(!unoGame.isGameOver()) {
            for (ClientHandler handler : Server.get_handlers()) {
                try {
                    handler.sendProtocol(Protocol.NEWROUND);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            while (!unoGame.isRoundOver()) {
                for (ClientHandler handler : Server.get_handlers()) {
                    try {
                        handler.sendProtocol(Protocol.CURRENTPLAYER+Protocol.DELIMITER+unoGame.getPlayersTurn().getName());
                        handler.sendProtocol(Protocol.UPDATEFIELD+Protocol.DELIMITER+unoGame.getBoard().getLastCard().getColour()+Protocol.DELIMITER+unoGame.getBoard().getLastCard().getSymbol());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                ArrayList<AbstractCard> playerHand = new ArrayList<>();
                for(AbstractCard card : unoGame.getPlayersTurn().getHand()){
                    playerHand.add(card);
                }
                for (ClientHandler handler : Server.get_handlers()) {
                    if(handler.get_players().get(0) == unoGame.getPlayersTurn()){
                        try {
                            if(handler.get_players().get(0).existsValidMove(unoGame.getBoard())){
                                handler.sendProtocol(Protocol.MOVE + formatMoveList(playerHand));
                                unoGame.getPlayersTurn().determineMove(unoGame.getBoard());
                            }
                        } catch (IOException e) {
                            System.out.println("Couldn't send move list");
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
    public boolean waiting(){
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
    public String formatMoveList(List<AbstractCard> cards) {
        String protocolMsg = "";
        protocolMsg += cards.get(0).getColour().toString()+Protocol.DELIMITERINITEMS+cards.get(0).getSymbol().toString();
        for (int i = 1; i < cards.size(); i++) {
            protocolMsg += Protocol.DELIMITERINITEMS+cards.get(i).getColour().toString()+Protocol.DELIMITERINITEMS+cards.get(i).getSymbol().toString();
        }
        return protocolMsg;
    }
}
