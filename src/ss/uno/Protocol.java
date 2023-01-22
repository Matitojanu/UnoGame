package ss.uno;

import ss.uno.server.Server;

public class Protocol {
    public static final String HANDSHAKE = "HANDSHAKE";
    public static final String DELIMITER = "|";
    public static final String SETPLAYERNAME = "SETPLAYERNAME";
    public static final String ACCEPTED = "ACCEPTED";
    public static final String DENIED = "DENIED";

    public static final String JOINSERVER = "JOINSERVER";
    public static final String ERROR = "ERROR";
    public static final String SERVERFULL = "SERVERFULL";
    public static final String NAMETAKEN = "NAMETAKEN";

    public static final String NEWGAME = "NEWGAME";
    public static final String JOINGAME = "JOINGAME";
    public static final String JOINERROR = "ERROR";

    public static final String WAIT = "WAIT";
    public static final String START = "START";
    public static final String NEWROUND = "NEWROUND";

    public static final String CURRENTPLAYER = "CURRENTPLAYER";
    public static final String UPDATEFIELD = "UPDATEFIELD";
    public static final String REQUESTMOVE= "REQUESTMOVE";

    public static final String MOVE = "MOVE";
    public static final String ACKMOVE = "ACKMOVE";

    public static final String DRAW = "DRAW";
    public static final String ACKDRAW = "ACKDRAW";
    public static final String INSTANTDISCARD = "INSTANTDISCARD";
    public static final String ACKDISCARD = "ACKDISCARD";

    public static final String DISPLAYRESULTS = "DISPLAYRESULTS";
    public static final String GAMEOVER = "GAMEOVER";

}
