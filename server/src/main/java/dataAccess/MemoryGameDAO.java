package dataAccess;
import java.util.HashMap;
import chess.ChessGame;

public class MemoryGameDAO implements GameDAO{
    static HashMap<Integer, String> whiteUsernames = new HashMap<>();
    static HashMap<Integer, String> blackUsernames = new HashMap<>();
    static HashMap<Integer, String> gameNames = new HashMap<>();
    static HashMap<Integer, ChessGame> games = new HashMap<>();

    public static void clearGame(){
        whiteUsernames.clear();
        blackUsernames.clear();
        gameNames.clear();
        games.clear();
    }

    public static int length(){
        return whiteUsernames.size() + blackUsernames.size() + gameNames.size() + games.size();
    }
}

