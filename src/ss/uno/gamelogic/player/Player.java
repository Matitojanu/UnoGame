package ss.uno.gamelogic.player;

import ss.uno.gamelogic.Board;

/**
 * This interface is the base of the player, which holds a method for
 * determining the move of the player
 */
public interface Player {
    /**
     * Returns a valid move
     */
    public int determineMove(Board board);
}
