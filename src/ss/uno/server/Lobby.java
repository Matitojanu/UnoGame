package ss.uno.server;

import ss.uno.Protocol;
import ss.uno.UnoGame;
import ss.uno.player.AbstractPlayer;

import java.io.IOException;
import java.util.ArrayList;

public class Lobby implements Runnable{
    private String gameName;
    private int maxPlayers;
    private String gamemode;
    private int numberOfPlayers;
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
                    handler.sendProtocol(Protocol.WAIT+"\\"+Protocol.DELIMITER+gameName+"\\"+Protocol.DELIMITER+maxPlayers+"\\"+Protocol.DELIMITER+players.size());
                } catch (IOException e) {
                    throw new RuntimeException(e);
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
        UnoGame unoGame = new UnoGame(players);
        unoGame.run();
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

    public void addPlayer(AbstractPlayer player){
        players.add(player);
    }

}
