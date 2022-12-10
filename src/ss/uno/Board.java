package ss.uno;

import ss.uno.cards.Card;
import ss.uno.cards.Deck;

public class Board {
    private Deck deckCards;
    private Card lastCard;

    public Card getLastCard() {
        return lastCard;
    }

    public void setLastCard(Card lastCard) {
        this.lastCard = lastCard;
    }

    public void deckFinished(){

    }
}
