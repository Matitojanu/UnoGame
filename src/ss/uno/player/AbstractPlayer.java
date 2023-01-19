package ss.uno.player;

import ss.uno.Board;
import ss.uno.cards.AbstractCard;

import java.util.ArrayList;

public abstract class AbstractPlayer implements Player {

    private ArrayList<AbstractCard> hand = new ArrayList<AbstractCard>();

    public boolean existsValidMove(Board board) {
        return false;
    }

    @Override
    public abstract int determineMove(Board board);

    public abstract String getName();

    public ArrayList<AbstractCard> getHand(){
        return hand;
    }

    public void setHand(ArrayList<AbstractCard> hand) {
        this.hand = hand;
    }

}


