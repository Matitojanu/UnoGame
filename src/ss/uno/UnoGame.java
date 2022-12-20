package ss.uno;

import ss.uno.cards.Card;
import ss.uno.player.AbstractPlayer;
import ss.uno.player.HumanPlayer;
import ss.uno.player.Player;

public class UnoGame {
    private Board board;
    private AbstractPlayer player1;
    private AbstractPlayer player2;
    private AbstractPlayer playersTurn = player1;

    /**
     * Returns the player that has to do a move
     * @return the player who's turn is it
     */
    public AbstractPlayer getTurn(){
        if (playersTurn == player1){
            playersTurn = player2;
        } else {
            playersTurn = player1;
        }
        return playersTurn;
    }

    /**
     * Returns the player that has won the game
     * @return the player that has no more cards in their hand
     */
    public Player getWinner(){
        if(player1.getHand().size()==0){
            return player1;
        } else if ( player2.getHand().size()==0 ) {
            return player2;
        }
        return null;
    }

    /**
     * Returns whether the game is finished or not
     * @return true if the game has finished, false otherwhise
     */
    public boolean isGameOver(){
        if(getWinner()!=null){
            return true;
        }
        return false;
    }

    /**
     * This function plays the card that is given as parameters
     * @param card that will be played
     */
    public void playCard(Card card){
        if(playersTurn == player1){
            board.setLastCard(card);
            player1.getHand().remove(card);
        }else{
            board.setLastCard(card);
            player2.getHand().remove(card);
        }
    }

    /**
     *  This function draws a card from the deck
     * @param card that will be drawn from the deck
     */
    public void drawCard(Card card){
        if( getTurn() == player1 ){
            player1.getHand().add(card);
        }else {
            player2.getHand().add(card);
        }
    }
}
