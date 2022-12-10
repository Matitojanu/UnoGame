package ss.uno.player;

import ss.uno.cards.Card;

public interface Player {
    /**
     * Returns a valid move
     */
    public Card determineMove();
}
