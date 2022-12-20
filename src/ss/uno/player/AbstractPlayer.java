package ss.uno.player;

import ss.uno.Board;
import ss.uno.cards.AbstractCard;

import java.util.ArrayList;

public abstract class AbstractPlayer implements Player {
    private ArrayList<AbstractCard> hand;

    @Override
    public abstract AbstractCard determineMove(Board board);

    public ArrayList<AbstractCard> getHand(){
        return hand;
    }

    public void setHand(ArrayList<AbstractCard> hand) {
        this.hand = hand;
    }

}


