package ss.uno;

import ss.uno.server.Server;

public class Protocol {
    public String joinServerProtocol (String playerName){
        return "JOINSERVER " + playerName;
    }

    public String acceptedJoinServerProtocol (String playerName){
        return "JOINSERVER " + playerName;
    }

    public String fullServerErrorProtocol (String server){
        return "ERROR "; //IDK
    }

    public String nameTakenErrorProtocol (String nameTaken){
        return "ERROR NAMETAKEN"; //IDK
    }

    public String newGameProtocol (int playerAmount, String gameMode, String gameName ){
        return "";
    }

}
