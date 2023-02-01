package ss.uno.networking.client;

import ss.uno.gamelogic.UnoGame;
import ss.uno.gamelogic.player.AbstractPlayer;

import java.util.List;

/**
 * This class extends the interface <code>Runnable</code> and holds and specifies the main methods all Clients should have
 */
public interface ClientInterface extends Runnable {
    /**
     * This method tries to connect the client to the server by sending the "HANDSHAKE" protocol
     *
     * @return True if the server sends back to the clienty the same protocol, false if it doesn't send the right protocol
     */
    boolean connect();

    /**
     * This method closes the socket, which means it closes the conection between the server and the client
     */
    void close();

    /**
     * This method will be called when a new thread for this class has started.
     * The method creates an UnoGame object, which constly updates the last card, based on what the server sends,
     * but in rest, it only stores the important information with which the client of that coresponding user needs in order to make moves, drawe or play.
     */
    @Override
    void run();


    /**
     * This method sends the move of the client to the client handler, according to the protocol
     *
     * @param move the move of the client
     */
    void sendMove(int move);

    /**
     * This method sends the name of the player to the server for the first time and waits for the server to send whether it si accepted or denied, according to the protocol
     *
     * @param name of the client
     * @return True if the name isn't already taken and sets the name of the client to the input, False if the name is indeed taken
     */
    boolean sendName(String name);

    /**
     * This method sends to the server a protocol message
     *
     * @param message the protocol
     */
    void sendProtocol(String message);

    /**
     * This function sends the List of features as a String to the server, according to the protocol
     *
     * @param features the list of functionalities the client chose
     */
    void sendFunctionalities(List<String> features);

    /**
     * This method formats the functionalities sent as parameter, according to protocol
     * @param features the list of aditional functionalities
     * @return the string representation of the functionalities, as the protocol wishes for it
     */
    String formatFunctionalities(List<String> features);

    /**
     * This method returns the game which the client stores
     * @return the game the client has joined and is playing
     */
    UnoGame getGame();

    /**
     * This method sets the global variable of the game as the game that is given in parameters
     * @param game the game to which the global variable will be set to
     */
    void setGame(UnoGame game);

    /**
     * This method returns the status of the client, whether it is curently in a game or if it's not
     * @return true, if the client has joined or created a game, false if it has not entered a game
     */
    boolean is_inGame();

    /**
     * This method sets the status of the client, reguarding wheter it is in a game or not to the variable given as parameter
     * @param _inGame the in game status to wisch the local variable of the client will be set to
     */
    void set_inGame(boolean _inGame);

    /**
     * This method sets the variable that hold what type of player the client is, to the one given as parameters
     * @param playerType the type of player that the client will be set to
     */
    void set_playerClient(AbstractPlayer playerType);
}
