package ss.uno;

import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Board {
    private Deck deckCards;
    public static Card lastCard;

    public Board(Deck deckCards){
        this.deckCards = deckCards;
    }
    /**
     * Returns the last played card
     * @return last card that was played
     */
    public Card getLastCard() {
        return lastCard;
    }

    /**
     * Changes the last played card to the current card
     * @param lastCard - the card that will be set as the last card
     */
    public void setLastCard(Card lastCard) {
        this.lastCard = lastCard;

    }

    /**
     * Returns whether the deck it's finished or not
     * @return true if the deck is finished, and false otherwhise
     */
    public boolean deckFinished(){
        return deckCards.isEmpty();
    }

    public Deck getDeckCards() {
        return deckCards;
    }

    public void setDeckCards(Deck deckCards) {
        this.deckCards = deckCards;
    }

    /**
     * This function recievs the colour from the user, and changes the last card to the one given as input from the user
     * @return the colour the player chose
     */
    public AbstractCard.Colour pickColor(){
        Scanner scanner = new Scanner(System.in);
        List<AbstractCard.Colour> colours = new ArrayList<>();
        colours.add(AbstractCard.Colour.BLUE);
        colours.add(AbstractCard.Colour.RED);
        colours.add(AbstractCard.Colour.GREEN);
        colours.add(AbstractCard.Colour.YELLOW);
        while(true) {
            System.out.println("Please pick a color out of RED, YELLOW, GREEN, BLUE: ");
            String input = scanner.nextLine();
            for (AbstractCard.Colour color : colours) {
                if ( color.toString().equals(input.toUpperCase()) ) {
                    lastCard.setColour(AbstractCard.Colour.valueOf(input.toUpperCase()));
                    return AbstractCard.Colour.valueOf(input.toUpperCase());
                }
            }
            System.out.println("Wrong input! Please try again");
        }
    }
}
