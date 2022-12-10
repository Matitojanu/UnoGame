package ss.uno.player;

import ss.uno.cards.Card;
import ss.uno.cards.Deck;

public abstract class AbstractPlayer implements Player {
    Deck hand;


    @Override
    public abstract Card determineMove();
}


