package ss.uno.cards;

import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;

import java.util.ArrayList;

public class Deck {
    ArrayList<AbstractCard> deckCards;

    /**
     * Creates a deck withh all the cards
     */
    public Deck(){

    }

    /**
     * Creates a deck without the cards that are in any of the player's hands or that have been played last
     * @param cards the cards in the player's hands and the last played card
     */
    public Deck(ArrayList<AbstractCard> cards){

    }

    /**
     * Returns the deck of cards
     * @return the cards that are in the deck;
     */
    public ArrayList<AbstractCard> getDeckCards() {
        return deckCards;
    }

    /**
     * Removes a card from the deck
     * @param card the card that should be removed
     */
    public void removeCard(Card card){

    }

    public static void main(String[] args) {

    }

}
