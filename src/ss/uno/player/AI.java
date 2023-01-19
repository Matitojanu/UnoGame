package ss.uno.player;

import ss.uno.Board;
import ss.uno.UnoGame;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;

public class AI extends AbstractPlayer {
    private String name;

    public AI(String name){
        this.name = name;
    }
    private int validMove;
    @Override
    public boolean existsValidMove(Board board){
        for (int index = 0; index < getHand().size(); index++) {
            if ( getHand().get(index).getColour() == board.getLastCard().getColour() ){
                validMove = index;
                return true;
            /*} else if ( ((Card) getHand().get(index)).getNumber() == board.getLastCard().getNumber() ) {
                validMove = index;
                return true;*/
            } else if ( ((Card) getHand().get(index)).getSymbol() == board.getLastCard().getSymbol() ) {
                validMove = index;
                return true;
            }
        }
        return false;
    }
    /**
     * Returns the move that the AI will do
     * @return the card the AI will play
     */
    @Override
    public int determineMove(Board board) {
        return validMove;
    }

    @Override
    public String getName() {
        return "AI" + name;
    }
}
