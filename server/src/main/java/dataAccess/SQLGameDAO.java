package dataAccess;

import model.GameDataModel;

import java.sql.SQLException;
import java.util.ArrayList;

public class SQLGameDAO {

    public static void createTable() throws Exception {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "CREATE TABLE IF NOT EXISTS games (" +
                    "whiteUsername VARCHAR(100), " +
                    "blackUsername VARCHAR(100), " +
                    "gameName VARCHAR(100), " +
                    "gameId INT, " +
                    "game VARCHAR(500))";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            throw new Exception(e.getMessage());
        }
    }
    public static ArrayList<GameDataModel> getGames(){
        return new ArrayList<GameDataModel>();
    }
    public static int createGame(String gameName){
        return 1;
    }

    public static int length(){
        return 1;
    }

    public static void joinGame(int gameId, String playerColor, String username){
        return;
    }
}
