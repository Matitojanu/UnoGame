package ss.uno.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;

import java.util.ArrayList;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeckTest {
    private Deck deck;
    private ArrayList<AbstractCard> deckCards;

    @BeforeEach
    public void setUp(){
        deck = new Deck();
        deckCards =  new ArrayList<>();
        deckCards.addAll(deck.getDeckCards());
    }

    @Test
    public void testCreateDeck(){
        assertTrue(deckCards.size() == 108);
    }

    @Test
    public void testGetCard(){
        deck.getCard();
        ArrayList<AbstractCard> deckCards2 = new ArrayList<>();
        deckCards2.addAll(deck.getDeckCards());
        assertTrue(deckCards2.size() == 107);
    }

    @Test
    public void testShuffle(){
        AbstractCard exampleCard = deckCards.get(0);
        deck = new Deck();
        assertFalse( deck.getCard() == exampleCard);
    }
}
