package ss.uno.player;

import ss.uno.Board;

public class OnlinePlayer extends AbstractPlayer{
    @Override
    public int determineMove(Board board) {
        return 0;
    }

    @Override
    public String getName() {
        return null;
    }
    //this player is basically the same as human player, just that it recives input from the server
}
