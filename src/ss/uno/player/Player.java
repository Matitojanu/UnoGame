package ss.uno.player;

import ss.uno.Board;
import ss.uno.cards.AbstractCard;

public interface Player {
    /**
     * Returns a valid move
     */
    public int determineMove(Board board);
}
