package ss.uno.server;

import ss.uno.Protocol;
import ss.uno.UnoGame;
import ss.uno.cards.AbstractCard;
import ss.uno.player.AbstractPlayer;

import java.io.IOException;
import java.util.ArrayList;
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

    public void start(){
        new Thread(this).start();
    }
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
                for(AbstractCard card : players.get(players.indexOf(unoGame.getPlayersTurn())).getHand()){

                }
            }
        }
    }

    public boolean waiting(){
        if(players.size() == maxPlayers){
            return false;
        }
        return true;
    }
    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public int getMaxPlayers() {
        return maxPlayers;
    }

    public void setMaxPlayers(int maxPlayers) {
        this.maxPlayers = maxPlayers;
    }

    public String getGamemode() {
        return gamemode;
    }

    public void setGamemode(String gamemode) {
        this.gamemode = gamemode;
    }

    public int getNumberOfPlayers() {
        return numberOfPlayers;
    }

    public void setNumberOfPlayers(int numberOfPlayers) {
        this.numberOfPlayers = numberOfPlayers;
    }

    public ArrayList<AbstractPlayer> getPlayers() {
        return players;
    }

    public UnoGame getUnoGame() {
        return unoGame;
    }

    public void addPlayer(AbstractPlayer player){
        players.add(player);
    }
}
