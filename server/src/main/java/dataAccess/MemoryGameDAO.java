package dataAccess;
import java.util.HashMap;
import chess.ChessGame;

public class MemoryGameDAO implements GameDAO{
    static HashMap<Integer, String> whiteUsernames;
    static HashMap<Integer, String> blackUsernames;
    static HashMap<Integer, String> gameNames;
    static HashMap<Integer, ChessGame> games;
}
