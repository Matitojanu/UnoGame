package ss.uno.player;

import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;

import java.util.ArrayList;

public abstract class AbstractPlayer implements Player {
    private ArrayList<AbstractCard> hand;

    @Override
    public abstract Card determineMove();

    public ArrayList<AbstractCard> getHand(){
        return hand;
    }

    public void setHand(ArrayList<AbstractCard> hand) {
        this.hand = hand;
    }
}


