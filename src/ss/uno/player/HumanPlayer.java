package ss.uno.player;

import ss.uno.Board;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;
import ss.uno.Board;
import ss.uno.UnoGame;

import java.util.Scanner;

public class HumanPlayer extends AbstractPlayer{
    /**
     * Returns a valid move from the player
     * @return card that is wither played or drawn from the deck
     */
    @Override
    public AbstractCard determineMove(Board board) {
        while (true){
            for (int i = 0; i < getHand().size(); i++) {
                System.out.println(i +" - " + getHand().get(i));
            }
            Scanner scanner =  new Scanner(System.in);
            System.out.println("Please input the index of the card you want to play: ");
            int index = scanner.nextInt();
            if ( getHand().get(index).getColour() == board.getLastCard().getColour() ){
                return getHand().get(index);
            } else if ( ((Card) getHand().get(index)).getNumber() == board.getLastCard().getNumber() ) {
                return getHand().get(index);
            } else if ( ((Card) getHand().get(index)).getSymbol() == board.getLastCard().getSymbol() ) {
                return getHand().get(index);
            }
            System.out.println("Wrong input! Please input again");
    }

    }

}
