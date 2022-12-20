package ss.uno.player;

import ss.uno.cards.Card;
import ss.uno.cards.Deck;

public abstract class AbstractPlayer implements Player {
    private Deck hand;

    @Override
    public abstract Card determineMove();

    public abstract Deck getHand();

    public void setHand(Deck hand) {
        this.hand = hand;
    }
}


