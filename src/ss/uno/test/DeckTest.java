package ss.uno.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DeckTest {
    @BeforeEach
    public void setUp(){
        Deck deck = new Deck();
    }

    @Test
    public void testCreateDeck(){
        ArrayList<AbstractCard> deckCards =  new ArrayList<>();
        new Deck(deckCards);
        assertTrue(deckCards.size() == 108);
    }

    @Test
    public void testShuffle(){

    }
}
