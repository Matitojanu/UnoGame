package ss.uno.gamelogic.cards;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class is responsible for creating a shuffled deck with all 108 cards, creating a deck with the rest of
 * the cards that are not in the players hands, drawing a card and checking whether the deck is empty
 */
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
            deckCards.add(count+1, new Card(AbstractCard.Colour.WILD, AbstractCard.Symbol.CHANGECOLOR));
            deckCards.add(count+2, new Card(AbstractCard.Colour.WILD, AbstractCard.Symbol.PLUSFOUR));
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
        Collections.shuffle(deckCards);
    }

    /**
     * Creates a deck without the cards that are in any of the player's hands and that have been played last
     * @param cards the cards in the player's hands and the last played card
     * @required a non empty list of cards as parameters
     * @ensures the deck is shuffled after it has been created withoutr the players' hands and last card
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
     * Returns the array of all the cars in the deck
     * @return the array of cards that are in the deck;
     * @ensures the deck of cards will be returned
     */
    public ArrayList<AbstractCard> getDeckCards() {
        return deckCards;
    }

    /**
     * This method returns the first card from the deck and removes it from it
     * @return the first card at the top of the deck
     * @ensures a card from the deck will be drawn and retured, while removed from the deck
     */
    public AbstractCard getCard(){
        Card card = (Card) deckCards.get(0);
        deckCards.remove(0);
        return card;

    }

    /**
     * This method checks whether the deck is empty and returns the apropriate answer
     * @return true if the deck is finished and there are no more cards to draw, and false other whise (if there is at least one card left)
     * @ensures that it returns false if the deck is not empty, and true if it is
     */
    public boolean isEmpty(){
        return deckCards.size()==0;
    }
}
