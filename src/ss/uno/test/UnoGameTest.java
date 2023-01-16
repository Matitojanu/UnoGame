package ss.uno.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ss.uno.UnoGame;
import ss.uno.cards.Deck;
import ss.uno.player.AbstractPlayer;
import ss.uno.player.HumanPlayer;
import ss.uno.player.Player;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class UnoGameTest {
    ArrayList <AbstractPlayer> players = new ArrayList<>();
    UnoGame game;
    @BeforeEach
    public void setUp(){
        players.add(0, new HumanPlayer("Amy"));
        players.add(1, new HumanPlayer("Maty"));
        players.add(2, new HumanPlayer("Pavvy"));
        game =  new UnoGame(players);
    }

    @Test
    public void initialDrawCardTest(){
        //for (int i = 0; i < players.size(); i++) {
        assertTrue(players.get(0).getHand().size()==7);
        assertTrue(players.get(1).getHand().size()==7);
        assertTrue(players.get(2).getHand().size()==7);
        //}
    }

    @Test
    public void testDrawCard(){
        game.drawCard();
        assertTrue(players.get(0).getHand().size() == 8);
    }
}
