package ss.uno.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.uno.Board;
import ss.uno.cards.Deck;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ss.uno.cards.AbstractCard.Colour.RED;
import static ss.uno.Board.lastCard;

public class BoardTest {
    private Deck deckCards;
    @BeforeEach
    public void setUp(){
        Board board = new Board(deckCards);
    }

    @Test
    public void testPickColour(){
        String input = "RED";
        assertTrue(lastCard.getColour() == RED);
    }
}
