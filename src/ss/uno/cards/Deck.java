package ss.uno.cards;

import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.special.*;

import java.util.ArrayList;

public class Deck {
    private ArrayList<AbstractCard> deckCards =  new ArrayList<>();

    /**
     * Creates a deck with all the cards
     * @ensures it creates a deck with 76 normal cards, 24 action cards, 8 wild cards, with a total of 108
     */
    public Deck(){
        Card card;
        deckCards.add(0, new Card(AbstractCard.Colour.RED, 0));
        deckCards.add(1, new Card(AbstractCard.Colour.YELLOW, 0));
        deckCards.add(2, new Card(AbstractCard.Colour.BLUE, 0));
        deckCards.add(3, new Card(AbstractCard.Colour.GREEN, 0));

        int count = 3;
        for(int i = 0; i <4;i++){
            deckCards.add(count+1, new ChangeColourCard(AbstractCard.Colour.BLACK));
            deckCards.add(count+1, new PlusFourCard(AbstractCard.Colour.BLACK));
            count+=2;
        }

        for(int i=0; i<2; i++){
            deckCards.add(count+1, new PlusTwoCard(AbstractCard.Colour.RED));
            deckCards.add(count+2, new ReverseCard(AbstractCard.Colour.RED));
            deckCards.add(count+3, new SkipTurnCard(AbstractCard.Colour.RED));

            deckCards.add(count+4, new PlusTwoCard(AbstractCard.Colour.YELLOW));
            deckCards.add(count+5, new ReverseCard(AbstractCard.Colour.YELLOW));
            deckCards.add(count+6, new SkipTurnCard(AbstractCard.Colour.YELLOW));

            deckCards.add(count+7, new PlusTwoCard(AbstractCard.Colour.GREEN));
            deckCards.add(count+8, new ReverseCard(AbstractCard.Colour.GREEN));
            deckCards.add(count+9, new SkipTurnCard(AbstractCard.Colour.GREEN));

            deckCards.add(count+10, new PlusTwoCard(AbstractCard.Colour.BLUE));
            deckCards.add(count+11, new ReverseCard(AbstractCard.Colour.BLUE));
            deckCards.add(count+12, new SkipTurnCard(AbstractCard.Colour.BLUE));
            count+=12;
        }

        for (int i = 1; i <= 9; i++) {
            deckCards.add(count+1, new Card(AbstractCard.Colour.RED, i));
            deckCards.add(count+2, new Card(AbstractCard.Colour.RED, i));
            deckCards.add(count+3, new Card(AbstractCard.Colour.BLUE, i));
            deckCards.add(count+4, new Card(AbstractCard.Colour.BLUE, i));
            deckCards.add(count+5, new Card(AbstractCard.Colour.YELLOW, i));
            deckCards.add(count+6, new Card(AbstractCard.Colour.YELLOW, i));
            deckCards.add(count+7, new Card(AbstractCard.Colour.GREEN, i));
            deckCards.add(count+8, new Card(AbstractCard.Colour.GREEN, i));
            count+=8;


        }
    }

    /**
     * Creates a deck without the cards that are in any of the player's hands and that have been played last
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
        deckCards.remove(card);
    }

    public boolean isEmpty(){
        return deckCards.size()==0;
    }

}
