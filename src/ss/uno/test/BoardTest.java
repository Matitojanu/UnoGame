package ss.uno.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.uno.Board;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static ss.uno.cards.AbstractCard.Colour.RED;
import static ss.uno.Board.lastCard;

public class BoardTest {
    private Deck deckCards;
    private Board board;

    @BeforeEach
    public void setUp(){
        deckCards = new Deck();
        board = new Board(deckCards);
    }

    @Test
    public void testDeckIsFinished(){
        for (int i = 0; i < 108; i++) {
            deckCards.getCard();
        }
        assertTrue(board.deckFinished());
    }

    @Test
    public void testLastCard(){
        Card card = (Card) board.getDeckCards().getCard();
        board.setLastCard(card);
        assertTrue(board.getLastCard() == card);
    }
}
