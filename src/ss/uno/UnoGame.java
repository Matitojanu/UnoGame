package ss.uno;

import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;
import ss.uno.player.AbstractPlayer;
import ss.uno.player.HumanPlayer;

import static ss.uno.cards.AbstractCard.Symbol.PLUSFOUR;
import static ss.uno.cards.AbstractCard.Symbol.REVERSE;
import static ss.uno.cards.AbstractCard.Symbol.PLUSTWO;
import static ss.uno.cards.AbstractCard.Symbol.SKIPTURN;
import static ss.uno.cards.AbstractCard.Symbol.CHANGECOLOR;

public class UnoGame implements AbstractCard.Ability {
    private static Board board;
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
    public AbstractPlayer getWinner(){
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
        Deck  deck = board.getDeckCards();
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

    public void abilityFunction(){
        Deck deck = board.getDeckCards();
        Card card = (Card) deck.getCard(board.getLastCard());
        switch (card.getSymbol()){
            case  PLUSTWO-> {
                for(int i = 0; i<2; i++){
                    drawCard(card);
                }
            }
            case PLUSFOUR -> {
                for(int i = 0; i<4; i++){
                    drawCard(card);
                }
                board.pickColor();
            }
            case REVERSE -> {
                if (playersTurn == player1){
                    playersTurn = player1;
                } else {
                    playersTurn = player2;
                }
            }
            case SKIPTURN -> {
                if (playersTurn == player1){
                    playersTurn = player1;
                } else {
                    playersTurn = player2;
                }
            }
            case CHANGECOLOR -> {
                board.pickColor();
            }
        }
    }

    public Board getBoard() {
        return board;
    }

    public void setBoard(Board board) {
        this.board = board;
    }

    public AbstractPlayer getPlayer1() {
        return player1;
    }

    public void setPlayer1(AbstractPlayer player1) {
        this.player1 = player1;
    }

    public AbstractPlayer getPlayer2() {
        return player2;
    }

    public void setPlayer2(AbstractPlayer player2) {
        this.player2 = player2;
    }

}
