package ss.uno.cards;

import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;

import java.util.ArrayList;
import java.util.Collections;

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
            deckCards.add(count+1, new Card(AbstractCard.Colour.BLACK, AbstractCard.Symbol.CHANGECOLOR));
            deckCards.add(count+2, new Card(AbstractCard.Colour.BLACK, AbstractCard.Symbol.PLUSFOUR));
            count+=2;
        }

        for(int i=0; i<2; i++){
            deckCards.add(count+1, new Card(AbstractCard.Colour.RED, AbstractCard.Symbol.PLUSTWO));
            deckCards.add(count+2, new Card(AbstractCard.Colour.RED, AbstractCard.Symbol.REVERSE));
            deckCards.add(count+3, new Card(AbstractCard.Colour.RED, AbstractCard.Symbol.SKIPTURN));

            deckCards.add(count+1, new Card(AbstractCard.Colour.YELLOW, AbstractCard.Symbol.PLUSTWO));
            deckCards.add(count+2, new Card(AbstractCard.Colour.YELLOW, AbstractCard.Symbol.REVERSE));
            deckCards.add(count+3, new Card(AbstractCard.Colour.YELLOW, AbstractCard.Symbol.SKIPTURN));

            deckCards.add(count+1, new Card(AbstractCard.Colour.GREEN, AbstractCard.Symbol.PLUSTWO));
            deckCards.add(count+2, new Card(AbstractCard.Colour.GREEN, AbstractCard.Symbol.REVERSE));
            deckCards.add(count+3, new Card(AbstractCard.Colour.GREEN, AbstractCard.Symbol.SKIPTURN));

            deckCards.add(count+1, new Card(AbstractCard.Colour.BLUE, AbstractCard.Symbol.PLUSTWO));
            deckCards.add(count+2, new Card(AbstractCard.Colour.BLUE, AbstractCard.Symbol.REVERSE));
            deckCards.add(count+3, new Card(AbstractCard.Colour.BLUE, AbstractCard.Symbol.SKIPTURN));
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
        Collections.shuffle(deckCards);
    }

    /**
     * Creates a deck without the cards that are in any of the player's hands and that have been played last
     * @param cards the cards in the player's hands and the last played card
     */
    public Deck(ArrayList<AbstractCard> cards){
        this();
        for (int i = 0; i < cards.size(); i++) {
            if(deckCards.contains(cards.get(i))){
                deckCards.remove(cards.get(i));
            }
        }
        Collections.shuffle(deckCards);
    }

    /**
     * Returns the deck of cards
     * @return the cards that are in the deck;
     */
    public ArrayList<AbstractCard> getDeckCards() {
        return deckCards;
    }

    public AbstractCard getCard(){
        Card card= (Card) deckCards.get(0);
        deckCards.remove(0);
        return card;

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
