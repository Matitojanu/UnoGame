package ss.uno.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.uno.UnoGame;
import ss.uno.cards.Deck;
import ss.uno.player.AI;
import ss.uno.player.AbstractPlayer;
import ss.uno.player.HumanPlayer;
import ss.uno.player.Player;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnoGameTest {
    private AbstractPlayer player1;
    private AbstractPlayer player2;
    private AbstractPlayer player3;
    private AbstractPlayer player4;
    UnoGame game;

    @BeforeEach
    public void setUp(){
        ArrayList<AbstractPlayer> players =  new ArrayList<>();
        players.add(player1 =  new HumanPlayer("Ami"));
        players.add(player2 =  new HumanPlayer("Matuesz"));
        players.add(player3 =  new AI("-AliceBot"));
        players.add(player4 =  new AI("-SandyBot"));
        game =  new UnoGame(players);
    }

    @Test
    public void initialDrawCardTest(){
        assertTrue(player1.getHand().size()==7);
        assertTrue(player2.getHand().size()==7);
        assertTrue(player3.getHand().size()==7);
        assertTrue(player4.getHand().size()==7);
    }

    @Test
    public void testDrawCard(){
        game.drawCard();
        assertTrue(player1.getHand().size() == 8);
    }
}
