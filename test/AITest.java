package ss.uno.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.uno.gamelogic.Board;
import ss.uno.gamelogic.cards.AbstractCard;
import ss.uno.gamelogic.cards.Card;
import ss.uno.gamelogic.cards.Deck;
import ss.uno.gamelogic.player.AI;
import ss.uno.gamelogic.player.AbstractPlayer;

import java.util.ArrayList;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AITest {
    private Deck deck;
    private AbstractPlayer player1;
    private ArrayList<AbstractCard> deckCards;
    private Board board;

    @BeforeEach
    public void setUp(){
        player1 = new AI("Human");
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
