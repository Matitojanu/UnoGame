package ss.uno.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.uno.Board;
import ss.uno.UnoGame;
import ss.uno.cards.AbstractCard;
import ss.uno.cards.Card;
import ss.uno.cards.Deck;
import ss.uno.player.AbstractPlayer;
import ss.uno.player.HumanPlayer;

import java.util.ArrayList;

import static org.junit.Assert.assertTrue;

public class HumanPlayerTest {
    private Deck deck;
    private AbstractPlayer player1;
    private ArrayList<AbstractCard> deckCards;
    private Board board;

    @BeforeEach
    public void setUp(){
        player1 = new HumanPlayer("Human");
        deck = new Deck();
        deckCards = new ArrayList<>();
        board = new Board(deck);
    }

    @Test
    public void testExistValidMove(){
        board.setLastCard(new Card(AbstractCard.Colour.BLUE, AbstractCard.Symbol.ZERO));
        deckCards.add(new Card(AbstractCard.Colour.BLUE, AbstractCard.Symbol.ZERO));
        player1.setHand(deckCards);
        assertTrue(player1.existsValidMove(board));
    }
}
