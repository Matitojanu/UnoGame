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
    private  Board board;
    private AbstractPlayer player1;
    private AbstractPlayer player2;
    private AbstractPlayer playersTurn;

    public UnoGame(AbstractPlayer player1, AbstractPlayer player2){
        board = new Board(new Deck());
        this.player1 = player1;
        this.player2 = player2;
        for(int i = 0; i < 7; i++){
            playersTurn=player2;
            drawCard();
            playersTurn=player1;
            drawCard();
        }
        board.setLastCard((Card) board.getDeckCards().getCard());
    }

    public void run(){
        while(!isGameOver()){
            if(playersTurn.existsValidMove(board)){
                playCard((Card) playersTurn.getHand().get(playersTurn.determineMove(board)));
                abilityFunction();
            }else{
                drawCard();
            }
            getTurn();
        }
        System.out.println("Player "+getWinner().toString()+" won!");
    }
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
        if(player1.getHand().size()==0){
            return true;
        } else if ( player2.getHand().size()==0 ) {
            return true;
        }
        return false;
    }

    /**
     * This function plays the card that is given as parameters
     * @param card that will be played
     */
    public void playCard(Card card){
        Deck deck = getBoard().getDeckCards();
        if(playersTurn == player1){
            board.setLastCard(card);
            player1.getHand().remove(player1.getHand().indexOf(card));
        }else{
            board.setLastCard(card);
            player2.getHand().remove(player2.getHand().indexOf(card));
        }
    }

    /**
     *  This function draws a card from the deck
     * @param card that will be drawn from the deck
     */
    public void drawCard(){
        if( playersTurn == player1 ){
            player1.getHand().add(getBoard().getDeckCards().getCard());
        }else {
            player2.getHand().add(getBoard().getDeckCards().getCard());//i am looking to see if in any functions that use that is the cause
        }
    }

    public void abilityFunction(){
        Deck deck = board.getDeckCards();
        Card card = (Card) board.getLastCard();
        switch (card.getSymbol()){
            case  PLUSTWO-> {
                for(int i = 0; i<2; i++){
                    getTurn();
                    drawCard();
                }
            }
            case PLUSFOUR -> {
                for(int i = 0; i<4; i++){
                    getTurn();
                    drawCard();
                }
                board.pickColor();
            }
            case REVERSE -> {
                /*(if (playersTurn == player1){
                    playersTurn = player1;
                } else {
                    playersTurn = player2;
                }*/
                getTurn();
            }
            case SKIPTURN -> {
                /*if (playersTurn == player1){
                    playersTurn = player1;
                } else {
                    playersTurn = player2;
                }*/
                getTurn();
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
