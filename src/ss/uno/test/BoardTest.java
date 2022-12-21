package ss.uno.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.uno.Board;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ss.uno.Board.lastCard;
import static ss.uno.cards.AbstractCard.Colour.RED;

public class BoardTest {
    @BeforeEach
    public void setUp(){
        Board board = new Board();
    }

    @Test
    public void testPickColour(){
        String input = "RED";
        assertTrue(lastCard.getColour() == RED);
    }
}
