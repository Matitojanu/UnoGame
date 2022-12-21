package ss.uno.player;

import ss.uno.Board;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;
import ss.uno.Board;
import ss.uno.UnoGame;

import java.util.Scanner;

public class HumanPlayer extends AbstractPlayer{
    private String name;

    public HumanPlayer(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean existsValidMove(Board board){
        for (int index = 0; index < getHand().size(); index++) {
            if ( getHand().get(index).getColour() == board.getLastCard().getColour() ){
                return true;
            /*} else if ( ((Card) getHand().get(index)).getNumber() == board.getLastCard().getNumber() ) {
                return true;*/
            } else if ( ((Card) getHand().get(index)).getSymbol() == board.getLastCard().getSymbol() ) {
                return true;
            }
        }
        return false;
    }


    /**
     * Returns a valid move from the player
     * @return card that is wither played or drawn from the deck
     */
    @Override
    public int determineMove(Board board) {
        while (true){
            for (int i = 0; i < getHand().size(); i++) {
                System.out.println(i +" - " + getHand().get(i).toString());
            }
            Scanner scanner =  new Scanner(System.in);
            System.out.println("Please input the index of the card you want to play: ");
            int index = scanner.nextInt();
            if(existsValidMove(board)){
                if(index<getHand().size()){
                    return index;
                }
                System.out.println("Wrong input! Please input again");
            }
            return -1;
        }

    }

}
