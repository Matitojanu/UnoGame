package ss.uno.player;

import ss.uno.Board;
import ss.uno.UnoGame;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;

public class AI extends AbstractPlayer {
    /**
     * Returns the move that the AI will do
     * @return the card the AI will play
     */
    @Override
    public AbstractCard determineMove(Board board) {
        while (true){
            for (int i = 0; i < getHand().size(); i++) {
                if ( getHand().get(i).getColour() == board.getLastCard().getColour() ) {
                    return getHand().get(i);
                } else if ( ((Card) getHand().get(i)).getNumber() == board.getLastCard().getNumber() ) {
                    return getHand().get(i);
                } else if ( ((Card) getHand().get(i)).getSymbol() == board.getLastCard().getSymbol() ) {
                    return getHand().get(i);
                }
            }
        }
    }
}
