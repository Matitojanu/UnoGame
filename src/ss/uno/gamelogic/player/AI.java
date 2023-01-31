package ss.uno.gamelogic.player;

import ss.uno.gamelogic.Board;
import ss.uno.gamelogic.cards.AbstractCard;
import ss.uno.gamelogic.cards.Card;

/**
 * This class extends the abstract class <code>AbstractPlayer</code>.
 * This class is responsible for creating an AI with a name, and always returning a valid move
 */
public class AI extends AbstractPlayer {
    private String name;
    private int validMove;

    /**
     * This method is a constructor. It creates an AI with the name given in teh parameters
     * @param name the name of the ai
     */
    public AI(String name){
        this.name = name;
    }

    /**
     * This method checks whether there are any valid moves in the AIs' hand
     * @param board the board on which the AI will play on
     * @return true if there is a valid move that the AI can play, false otherwhise
     */
    @Override
    public boolean existsValidMove(Board board){
        for (int index = 0; index < getHand().size(); index++) {
            if ( getHand().get(index).getColour() == board.getLastCard().getColour() || getHand().get(index).getColour() == AbstractCard.Colour.WILD ){
                validMove = index;
                return true;
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

    /**
     * This method returns the name of the AI with the String 'AI' before its' name
     * @return the formated name of the AI
     */
    @Override
    public String getName() {
        return "AI" + name;
    }
}
