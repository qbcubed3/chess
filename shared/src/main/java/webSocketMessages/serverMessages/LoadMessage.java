package webSocketMessages.serverMessages;

import chess.ChessGame;

public class LoadMessage extends ServerMessage {
    ChessGame game;


    public LoadMessage(ChessGame game){
        super(ServerMessage.ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    public ChessGame getGame(){
        return game;
    }

}
