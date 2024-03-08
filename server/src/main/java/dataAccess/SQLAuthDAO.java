package dataAccess;

import java.sql.SQLException;
import java.util.Random;

public class SQLAuthDAO {
    public SQLAuthDAO() {
        try{
            DatabaseManager.createDatabase();
            var conn = DatabaseManager.getConnection();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    public static void clearAuths(){
        var statement = "DELETE FROM auths";
        try( var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        }
        catch(SQLException | DataAccessException e){
            System.out.println(e.getMessage());
        }
    }
    public static String addAuth(String username){
        StringBuilder auth = new StringBuilder();
        Random random = new Random();
        String validCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ1234567890";
        for (int i = 0; i < 16; i++){
            int randomIndex = random.nextInt(validCharacters.length());
            auth.append(validCharacters.charAt(randomIndex));
        }
        String authToken = auth.toString();
        var statement = "INSERT INTO auths (username, auth) VALUES(?, ?)";
        try{
            var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, authToken);
            preparedStatement.executeUpdate();
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
        return authToken;
    }

    public static int length(){
        int finalLength = 0;
        var statement = "SELECT COUNT(*) AS table_length FROM auths";
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

    public static boolean checkAuth(String auth) throws UnauthorizedException{
        var statement = "SELECT username FROM auths WHERE auth = ?";
        try{
            var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, auth);
            var result = preparedStatement.executeQuery();
            if (!result.next()){
                throw new UnauthorizedException("bad auth");
            }
        }
        catch (UnauthorizedException e){
            throw new UnauthorizedException("bad auth");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return true;
    }

    public static String removeUsername(String auth) throws UnauthorizedException{
        var statement = "DELETE FROM auths WHERE auth = ?";
        var checkStatement = "SELECT username FROM auths WHERE auth = ?";
        try{
            var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement(statement);
            var preparedCheck = conn.prepareStatement(checkStatement);
            preparedStatement.setString(1, auth);
            preparedCheck.setString(1, auth);
            var result = preparedCheck.executeQuery();
            preparedStatement.executeUpdate();
            if (!result.next()){
                throw new UnauthorizedException("bad auth");
            }
        }
        catch (UnauthorizedException e){
            throw new UnauthorizedException("bad auth");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return auth;
    }

    public static String getUsername(String auth) throws UnauthorizedException {
        String username = "";
        var statement = "SELECT username FROM auths WHERE auth = ?";
        try{
            var conn = DatabaseManager.getConnection();
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, auth);
            var result = preparedStatement.executeQuery();
            if (!result.next()){
                throw new UnauthorizedException("bad auth");
            }
            username = result.getString(1);
        }
        catch (UnauthorizedException e){
            throw new UnauthorizedException("bad auth");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

        return username;
    }
}
