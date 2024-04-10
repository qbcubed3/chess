package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{
    int gameID;
    ChessGame.TeamColor playerColor;

    public JoinPlayerCommand(String auth, int id, ChessGame.TeamColor color){
        super(auth);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameID = id;
        this.playerColor = color;
    }

    public int getId(){
        return gameID;
    }

    public ChessGame.TeamColor getColor(){
        return playerColor;
    }
}
