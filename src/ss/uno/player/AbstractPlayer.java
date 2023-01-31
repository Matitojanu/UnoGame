package ss.uno.player;

import ss.uno.Board;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements the interface <code>Player</code> and has additional
 * methods that all players must have such as getting and setting the name of the player,
 * setting and getting the hand and checking for valid moves
 */
public abstract class AbstractPlayer implements Player {

    private List<AbstractCard> hand = new ArrayList<>();

    /**
     * This checks for any valid moves in the players' hand
     * @param board the board of the game, on which the valid move will be played
     * @return true if there are any valid moves, false otherwhise
     */
    public boolean existsValidMove(Board board) {
        return false;
    }

    /**
     * This method returns a valid move, based on the hand the player has
     * @param board the board of the game on which the card will be played
     * @return the index of th card the player wishes to play
     */
    @Override
    public abstract int determineMove(Board board);

    /**
     * This method returns the name of the player
     * @return the name the player has
     */
    public abstract String getName();

    /**
     * This method returns the hand of the player
     *
     * @return the hand of the player
     */
    public List<AbstractCard> getHand(){
        return hand;
    }

    /**
     * This method will set the players' hand to the argument given as parameter
     * @param hand the hand to which the players' hand will be set to
     */
    public void setHand(List<AbstractCard> hand) {
        this.hand = hand;
    }

}


