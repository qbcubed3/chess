package dataAccess;
import java.util.ArrayList;
import java.util.HashMap;
import chess.ChessGame;
import model.AuthDataModel;
import model.GameDataModel;

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

    public static ArrayList<GameDataModel> getGames(){
        ArrayList<GameDataModel> gameList = new ArrayList<GameDataModel>();
        for(HashMap.Entry<Integer, ChessGame> entry: games.entrySet()){
            int gameId = entry.getKey();
            ChessGame game = entry.getValue();
            String gameName = gameNames.get(gameId);
            String whiteUsername = whiteUsernames.get(gameId);
            String blackUsername = blackUsernames.get(gameId);
            GameDataModel data = new GameDataModel(gameId, whiteUsername, blackUsername, gameName, game);
            gameList.add(data);
        }
        return gameList;
    }

    public static int createGame(String gameName){
        var gameId = gameNames.size() + 1;
        gameNames.put(gameId, gameName);
        whiteUsernames.put(gameId, "");
        blackUsernames.put(gameId, "");
        games.put(gameId, new ChessGame());
        return gameId;
    }

    public static void joinGame(int gameId, String playerColor, String username)throws UsernameTakenException{
        if (playerColor.equals("BLACK")){
            if (!blackUsernames.get(gameId).isEmpty()){
                throw new UsernameTakenException("black is already taken");
            }
            blackUsernames.replace(gameId, username);
        }
        else if (playerColor.equals("WHITE")){
            if (!whiteUsernames.get(gameId).isEmpty()){
                throw new UsernameTakenException("white is already taken");
            }
            whiteUsernames.replace(gameId, username);
        }
    }
}

