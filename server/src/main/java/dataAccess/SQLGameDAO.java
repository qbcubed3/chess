package dataAccess;

import chess.ChessBoard;
import chess.ChessGame;
import com.google.gson.Gson;
import model.GameDataModel;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SQLGameDAO {
    private static final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS games (`whiteUsername` varchar(100), `blackUsername` varchar(100), `gameName` varchar(100), `gameId` INT AUTO_INCREMENT PRIMARY KEY, `game` varchar(500))
            """
    };
    public SQLGameDAO() throws Exception{
       configureDatabase();
    }

    private static void configureDatabase() throws Exception {
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
        try{
            configureDatabase();
        }
        catch(Exception e){System.out.println(e.getMessage());}
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

    public static void updateGame(ChessGame game, int gameId){
        try{
            configureDatabase();;
        }
        catch (Exception e){System.out.println(e.getMessage());}
        try{
            var statement = "UPDATE games SET game = ? WHERE gameID = ?";
            var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement(statement);
            Gson gson = new Gson();
            String jsonGame = gson.toJson(game);
            preparedStatement.setString(1, jsonGame);
            preparedStatement.setInt(2, gameId);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public static ArrayList<GameDataModel> getGames(){
        try{
            configureDatabase();
        }
        catch(Exception e){System.out.println(e.getMessage());}
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
    public static GameDataModel getGame(int id){
        try{
            configureDatabase();
        }
        catch(Exception e){System.out.println(e.getMessage());}
        GameDataModel model = new GameDataModel(0, null, null, null, null);
        var statement = "SELECT * FROM games WHERE gameID = ?";
        Gson gson = new Gson();
        try{
            var conn = DatabaseManager.getConnection();
            var prepStatement = conn.prepareStatement(statement);
            prepStatement.setInt(1, id);
            ResultSet result = prepStatement.executeQuery();
            if (result.next()){
                var whiteUsername = result.getString(1);
                var blackUsername = result.getString(2);
                var gameName = result.getString(3);
                var gameID = result.getInt(4);
                var gameJson = result.getString(5);
                model = new GameDataModel(gameID, whiteUsername, blackUsername, gameName, gson.fromJson(gameJson, ChessGame.class));
            }
            else {
                throw new DataAccessException("Couldn't find the game ID");
            }
        }
        catch (SQLException | DataAccessException e){
            System.out.println(e.getMessage());
        }
        return model;
    }

    public static int createGame(String gameName){
        try{
            configureDatabase();
        }
        catch(Exception e){System.out.println(e.getMessage());}
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES(?,?,?,?)";
        ChessGame game = new ChessGame();
        ChessBoard board = new ChessBoard();
        board.resetBoard();
        game.setBoard(board);
        Gson gson = new Gson();
        int id = 0;
        var jsonGame = gson.toJson(game);
        System.out.println("json: " + jsonGame);
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
        try{
            configureDatabase();
        }
        catch(Exception e){System.out.println(e.getMessage());}
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

    public static void joinGame(int gameId, String playerColor, String username) throws UsernameTakenException, NullParameterException {
        try{
            configureDatabase();
        }
        catch(Exception e){System.out.println(e.getMessage());}
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

    public static void leaveGame(int id, String color) throws UsernameTakenException, NullParameterException {
        try{
            configureDatabase();
        }
        catch(Exception e){System.out.println(e.getMessage());}
        var gamesCheck = "SELECT * FROM games WHERE gameID = ?";
        String statement = "";
        if (color.equals("WHITE")){
            statement = "UPDATE games SET whiteUsername = ? WHERE gameID = ?";
        }
        else if (color.equals("BLACK")){
            statement = "UPDATE games SET blackUsername = ? WHERE gameID = ?";
        }
        try{
            var conn = DatabaseManager.getConnection();
            var prepped = conn.prepareStatement(statement);
            prepped.setInt(1, id);
        }
        catch (SQLException | DataAccessException e){
            System.out.println(e.getMessage());
        }
    }
}
