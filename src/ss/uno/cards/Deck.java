package ss.uno.cards;

import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private ArrayList<AbstractCard> deckCards =  new ArrayList<>();

    /**
     * Creates a deck with all the cards
     * @ensures it creates a deck with 76 normal cards, 24 action cards, 8 wild cards, with a total of 108
     */
    public Deck(){
        Card card;
        deckCards.add(0, new Card(AbstractCard.Colour.RED, AbstractCard.Symbol.ZERO));
        deckCards.add(1, new Card(AbstractCard.Colour.YELLOW, AbstractCard.Symbol.ZERO));
        deckCards.add(2, new Card(AbstractCard.Colour.BLUE, AbstractCard.Symbol.ZERO));
        deckCards.add(3, new Card(AbstractCard.Colour.GREEN, AbstractCard.Symbol.ZERO));
        List<AbstractCard.Symbol> enums = new ArrayList<>();
        enums.add(AbstractCard.Symbol.REVERSE);
        enums.add(AbstractCard.Symbol.PLUSTWO);
        enums.add(AbstractCard.Symbol.SKIPTURN);
        enums.add(AbstractCard.Symbol.ONE);
        enums.add(AbstractCard.Symbol.TWO);
        enums.add(AbstractCard.Symbol.THREE);
        enums.add(AbstractCard.Symbol.FOUR);
        enums.add(AbstractCard.Symbol.FIVE);
        enums.add(AbstractCard.Symbol.SIX);
        enums.add(AbstractCard.Symbol.SEVEN);
        enums.add(AbstractCard.Symbol.EIGHT);
        enums.add(AbstractCard.Symbol.NINE);
        int count = 3;
        for(int i = 0; i <4;i++){
            deckCards.add(count+1, new Card(AbstractCard.Colour.BLACK, AbstractCard.Symbol.CHANGECOLOR));
            deckCards.add(count+2, new Card(AbstractCard.Colour.BLACK, AbstractCard.Symbol.PLUSFOUR));
            count+=2;
        }

        for(AbstractCard.Symbol symbol: enums){
            deckCards.add(count+1, new Card(AbstractCard.Colour.RED, symbol));
            deckCards.add(count+2, new Card(AbstractCard.Colour.RED, symbol));
            deckCards.add(count+3, new Card(AbstractCard.Colour.BLUE, symbol));
            deckCards.add(count+4, new Card(AbstractCard.Colour.BLUE, symbol));
            deckCards.add(count+5, new Card(AbstractCard.Colour.YELLOW, symbol));
            deckCards.add(count+6, new Card(AbstractCard.Colour.YELLOW, symbol));
            deckCards.add(count+7, new Card(AbstractCard.Colour.GREEN, symbol));
            deckCards.add(count+8, new Card(AbstractCard.Colour.GREEN, symbol));
           count=+8;
        }
        /* for (int i = 1; i <= 2; i++) {
            deckCards.add(count+1, new Card(AbstractCard.Colour.RED, AbstractCard.Symbol.ONE));
            deckCards.add(count+2, new Card(AbstractCard.Colour.BLUE, AbstractCard.Symbol.ONE));
            deckCards.add(count+3, new Card(AbstractCard.Colour.YELLOW, AbstractCard.Symbol.TWO));
            deckCards.add(count+4, new Card(AbstractCard.Colour.BLUE, i));
            deckCards.add(count+5, new Card(AbstractCard.Colour.YELLOW, i));
            deckCards.add(count+6, new Card(AbstractCard.Colour.YELLOW, i));
            deckCards.add(count+7, new Card(AbstractCard.Colour.GREEN, i));
            deckCards.add(count+8, new Card(AbstractCard.Colour.GREEN, i));
            count+=8;
        }*/
        Collections.shuffle(deckCards);
    }

    /**
     * Creates a deck without the cards that are in any of the player's hands and that have been played last
     * @param cards the cards in the player's hands and the last played card
     */
    public Deck(ArrayList<AbstractCard> cards){
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
