package webSocketMessages.serverMessages;

import chess.ChessGame;
import com.google.gson.Gson;

public class LoadMessage extends ServerMessage {
    ChessGame game;


    public LoadMessage(ChessGame game){
        super(ServerMessage.ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame(){
        return game;
    }

    public String toString(){
        Gson gson = new Gson();
        return gson.toJson(this);
    }

}
