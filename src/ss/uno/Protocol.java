package ss.uno;

import java.net.InetAddress;

/**
 * This class consists of all the protocol messages possible between
 * the client and the server. The protocol also stores the IP and
 * the port on which the client and server run
 */
public class Protocol {
    public static final int PORT = 24042;
    public static final String IPADDRESS = "localhost";

    public static final String HANDSHAKE = "HANDSHAKE";
    public static final String HELLO = "HELLO";
    public static final String DELIMITER = "|";
    public static final String DELIMITERINITEMS = "-";
    public static final String PLAYERNAME = "PLAYERNAME";
    public static final String ACCEPTED = "ACCEPTED";
    public static final String DENIED = "DENIED";

    public static final String ERROR = "ERROR";
    public static final String FUNCTIONALITIES = "FUNCTIONALITIES";
    public static final String[] FUNCTIONALITYUSER = new String[]{"Chat" , "Progressive UNO" , "Jump-IN UNO" , "Seven-O UNO" , "Graphical User Interface"};
    public static final String[] FUNCTIONALITYARR = new String[]{"CHAT" , "PROGRESSIVEUNO" , "JUMPINUNO" , "SEVENOUNO" , "GUI"};
    public static final String SERVERLIST = "SERVERLIST";
    public static final String ORIGINAL = "ORIGINAL";

    public static final String NEWGAME = "NEWGAME";
    public static final String JOINGAME = "JOINGAME";
    public static final String JOINERROR = "JOINERROR";

    public static final String WAIT = "WAIT";
    public static final String START = "START";
    public static final String NEWROUND = "NEWROUND";

    public static final String CURRENTPLAYER = "CURRENTPLAYER";
    public static final String UPDATEFIELD = "UPDATEFIELD";
    public static final String REQUESTMOVE= "REQUESTMOVE";

    public static final String MOVE = "MOVE";
    public static final String CHOOSECOLOR = "CHOOSECOLOR";
    public static final String COLOR = "COLOR";
    public static final String COLORSPOSSIBLE = "YELLOW, RED, BLUE, GREEN";

    public static final String CHALLANGE = "CHALLANGE";
    public static final String TRUE = "TRUE";
    public static final String FALSE = "FALSE";

    public static final String DRAW = "DRAW";
    public static final String INSTANTDISCARD = "INSTANTDISCARD";

    public static final String DISPLAYRESULTS = "DISPLAYRESULTS";
    public static final String GAMEOVER = "GAMEOVER";

}
