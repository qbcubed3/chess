package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{
    int gameId;
    ChessMove move;

    public MakeMoveCommand(String auth, int id, ChessMove move){
        super(auth);
        this.gameId = id;
        this.move = move;
    }
}
