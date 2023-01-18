package ss.uno.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.uno.UnoGame;
import ss.uno.cards.Deck;
import ss.uno.player.AbstractPlayer;
import ss.uno.player.HumanPlayer;
import ss.uno.player.Player;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnoGameTest {
    private AbstractPlayer player1;
    private AbstractPlayer player2;
    UnoGame game;
    @BeforeEach
    public void setUp(){
        player1 =  new HumanPlayer("Ami");
        player2 =  new HumanPlayer("Matuesz");
        //game =  new UnoGame((AbstractPlayer) player1, (AbstractPlayer) player2);
    }

    @Test
    public void initialDrawCardTest(){
        assertTrue(player1.getHand().size()==7);
        assertTrue(player2.getHand().size()==7);
    }

    @Test
    public void testDrawCard(){
        game.drawCard();
        assertTrue(player1.getHand().size() == 8);
    }
}
