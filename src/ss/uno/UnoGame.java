package ss.uno;

import ss.uno.cards.Card;
import ss.uno.player.Player;

public class UnoGame {
    private Board board;
    private Player player1;
    private Player player2;
    private Player playersTurn;

    /**
     * Returns the player that has to do a move
     * @return the player who's turn is it
     */
    public Player getTurn(){
        return null;
    }

    /**
     * Returns the player that has won the game
     * @return the player that has no more cards in their hand
     */
    public Player getWinner(){
        return null;
    }

    /**
     * Returns whether the game is finished or not
     * @return true if the game has finished, false otherwhise
     */
    public boolean isGameOver(){
        return false;
    }

    /**
     * This function plays the card that is given as parameters
     * @param card that will be played
     */
    public void playCard(Card card){

    }

    /**
     *  This function draws a card from the deck
     * @param card that will be drawn from the deck
     */
    public void drawCard(Card card){

    }
}
