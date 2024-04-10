package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayerCommand extends UserGameCommand{
    int gameId;
    ChessGame.TeamColor color;

    public JoinPlayerCommand(String auth, int id, ChessGame.TeamColor color){
        super(auth);
        this.commandType = CommandType.JOIN_PLAYER;
        this.gameId = id;
        this.color = color;
    }

    public int getId(){
        return gameId;
    }

    public ChessGame.TeamColor getColor(){
        return color;
    }
}
