package ss.uno.server;

import ss.uno.Protocol;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {
    private Socket _socket;
    private final BufferedReader _in;
    private final PrintWriter _out;
    private boolean _running;
    private String _name;
    private ArrayList<String> _playerNames;

    public ClientHandler(Socket socket, String name) throws IOException {
        this._socket = socket;
        this._out = new PrintWriter(_socket.getOutputStream(), true);
        this._in = new BufferedReader(new InputStreamReader((_socket.getInputStream())));
        this._running = true;
        this._name = name;
        this._playerNames = new ArrayList<>();
    }

    @Override
    public void run() {
        try {
            String msgFromClient = _in.readLine();
            if (msgFromClient.equals(Protocol.HANDSHAKE+Protocol.DELIMITER+Protocol.HELLO)) {
                try {
                    handleMessage(msgFromClient);
                    System.out.println("Handshake!");
                } catch (IOException e) {
                    System.out.println("Failed to handshake");
                }
            }
        }catch (IOException e){
            System.out.println("Client disconnected");
        }

        try {
            sendProtocol(Protocol.FUNCTIONALITIES);
            String msgFromClient = _in.readLine();
            if (msgFromClient.contains(Protocol.PLAYERNAME)) {
                try {
                    handleMessage(msgFromClient);
                } catch (IOException e) {
                    System.out.println("Couldn't get player name");
                }
            }
        }catch (IOException e){
            System.out.println("Client disconnected");
        }

        try {
            String msgFromClient = _in.readLine();
            if (msgFromClient.equals(Protocol.HANDSHAKE+Protocol.DELIMITER+Protocol.HELLO)) {
                try {
                    handleMessage(msgFromClient);
                } catch (IOException e) {
                    System.out.println("Failed to handshake");
                }
            }
        }catch (IOException e){
            System.out.println("Client disconnected");
        }

        System.out.println("Starting thread ");
        try (BufferedReader in = new BufferedReader(new InputStreamReader(_socket.getInputStream()))) {
            String line;
            while ((line = _in.readLine()) != null) {
                try (_in) {
                    handleMessage(line);
                } catch (IOException e) {
                    System.out.println("Error");
                }
            }
        }catch (IOException e){
            System.out.println("Error");
        }
    }

    public void handleMessage(String message) throws IOException {
        String[] messageArr = message.split("\\|", 0);
        switch (messageArr[0]){
            case Protocol.HANDSHAKE -> {
                sendProtocol(Protocol.HANDSHAKE+Protocol.DELIMITER+Protocol.HELLO);
            }
            case Protocol.PLAYERNAME -> {
                checkName(messageArr[1]);
            }
        }
    }

    public void sendMessage(String message){
        this._out.println(message);
        System.out.println("["+this._name+"]" + message);
    }

    public void sendProtocol(String message) throws IOException {
        _out.println(message);
    }

    public void checkName(String name) throws IOException {
        if(_playerNames.contains(name)){
            sendProtocol(Protocol.PLAYERNAME+Protocol.DELIMITER+Protocol.DENIED);
        }else {
            _playerNames.add(name);
            System.out.println(_playerNames);
            sendProtocol(Protocol.PLAYERNAME + Protocol.DELIMITER + Protocol.ACCEPTED);
        }
    }
}
