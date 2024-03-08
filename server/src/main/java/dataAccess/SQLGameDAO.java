package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import com.mysql.cj.xdevapi.JsonString;
import model.GameDataModel;

import javax.xml.crypto.Data;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLGameDAO {
    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (`whiteUsername` varchar(100), `blackUsername` varchar(100), `gameName` varchar(100), `gameId` INT AUTO_INCREMENT PRIMARY KEY, `game` varchar(500))
            """
    };
    public SQLGameDAO() throws Exception{
       configureDatabase();
    }

    private void configureDatabase() throws Exception {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException ex) {
            throw new Exception(ex.getMessage());
        }
    }
    public static void clearGame(){
        var statement = "DELETE from games";
        try{
            var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static ArrayList<GameDataModel> getGames(){
        ArrayList<GameDataModel> gameList = new ArrayList<GameDataModel>();
        var statement = "SELECT * FROM games";
        Gson gson = new Gson();
        try{
            var conn = DatabaseManager.getConnection();
            var prepStatement = conn.prepareStatement(statement);
            ResultSet result = prepStatement.executeQuery();
            while(result.next()){
                var whiteUsername = result.getString(1);
                var blackUsername = result.getString(2);
                var gameName = result.getString(3);
                var gameID = result.getInt(4);
                var gameJson = result.getString(5);
                var model = new GameDataModel(gameID, whiteUsername, blackUsername, gameName, gson.fromJson(gameJson, ChessGame.class));
                gameList.add(model);
            }
        }
        catch (SQLException | DataAccessException e){
            System.out.println(e.getMessage());
        }
        return gameList;
    }
    public static int createGame(String gameName){
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES(?,?,?,?)";
        ChessGame game = new ChessGame();
        Gson gson = new Gson();
        int id = 0;
        var jsonGame = gson.toJson(game);
        try{
            var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, null);
            preparedStatement.setString(2, null);
            preparedStatement.setString(3, gameName);
            preparedStatement.setString(4, jsonGame);
            int newId = preparedStatement.executeUpdate();
            if (newId > 0){
                ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
                if (generatedKeys.next()){
                    id = generatedKeys.getInt(1);
                }
            }
        }
        catch (SQLException | DataAccessException e){
            System.out.println(e.getMessage());
        }
        return id;
    }

    public static int length(){
        int finalLength = 0;
        var statement = "SELECT COUNT(*) AS table_length FROM games";
        try (var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement(statement);
            var result = preparedStatement.executeQuery();
            if (result.next()){
                finalLength = result.getInt(1);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return finalLength;
    }

    public static void joinGame(int gameId, String playerColor, String username) throws UsernameTakenException, NullParameterException{
        var statement = "";
        var checkStatement = "SELECT whiteUsername, blackUsername FROM games WHERE gameID = ?";
        var gameCheck = "SELECT * FROM games WHERE gameID = ?";
        if (playerColor.equals("WHITE")){
            statement = "UPDATE games SET whiteUsername = ? WHERE gameID = ?";
        }
        else if (playerColor.equals("BLACK")){
            statement = "UPDATE games SET blackUsername = ? WHERE gameID = ?";
        }
        try{
            var conn = DatabaseManager.getConnection();
            conn.setAutoCommit(false);
            var preparedGame = conn.prepareStatement(gameCheck);
            preparedGame.setInt(1, gameId);
            var gameid = preparedGame.executeQuery();
            if (!gameid.next()){
                throw new NullParameterException("bad id");
            }
            var preparedCheck = conn.prepareStatement(checkStatement);
            preparedCheck.setInt(1, gameId);
            ResultSet result = preparedCheck.executeQuery();
            if (result.next()){
                var whiteUsername = result.getString(1);
                var blackUsername = result.getString(2);
                if ((whiteUsername != null && playerColor.equals("WHITE")) || (blackUsername != null && playerColor.equals("BLACK"))){
                    throw new UsernameTakenException("spot taken");
                }
            }
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, username);
            preparedStatement.setInt(2, gameId);
            preparedStatement.executeUpdate();
            conn.commit();
        }
        catch (SQLException | DataAccessException e){
            System.out.println(e.getMessage());
        }
    }
}
