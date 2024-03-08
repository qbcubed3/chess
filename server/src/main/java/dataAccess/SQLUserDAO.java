package dataAccess;

import model.UserDataModel;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.SQLException;
import java.sql.*;

public class SQLUserDAO {

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (`username` VARCHAR(100) UNIQUE, `password` VARCHAR(255), `email` VARCHAR(100) UNIQUE)
            """
    };

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
    public SQLUserDAO() throws Exception{
        configureDatabase();
    }

    public static void clearUsers(){
        var statement = "DELETE FROM users";
        try( var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        }
        catch(SQLException | DataAccessException e){
            System.out.println(e.getMessage());
        }
    }
    public static void registerUser(UserDataModel user) throws UsernameTakenException{
        String username = user.username();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPass = encoder.encode(user.password());
        var statement = "INSERT INTO users (username, password, email) VALUES(?, ?, ?)";
        var checkTaken = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()){
            var preparedCheck = conn.prepareStatement(checkTaken);
            preparedCheck.setString(1, username);
            var result = preparedCheck.executeQuery();
            result.next();
            if (result.getInt(1) > 0){
                throw new UsernameTakenException("username is taken");
            }
            try(var preparedStatement = conn.prepareStatement(statement)){
                preparedStatement.setString(1, username);
                preparedStatement.setString(2, hashedPass);
                preparedStatement.setString(3, user.email());

                preparedStatement.executeUpdate();
            }
            catch(SQLException e){
                System.out.println(e.getMessage());
            }
        }
        catch (SQLException | DataAccessException e) {
            System.out.println(e.getMessage());
        }
    }

    public static int length(){
        int finalLength = 0;
        var statement = "SELECT COUNT(*) AS table_length FROM users";
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

    public static String getAuth(String username, String password) throws UnauthorizedException{
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        var checkStatement = "SELECT username FROM users WHERE username = ?";
        var statement = "SELECT password FROM users WHERE username = ?";
        try(var conn = DatabaseManager.getConnection()) {
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1, username);
            var result = preparedStatement.executeQuery();
            if (result.next()) {
                var getPass = result.getString(1);
                if (encoder.matches(password, getPass)) {
                    return username;
                } else {
                    throw new UnauthorizedException("password/username bad");
                }
            } else {
                throw new UnauthorizedException("bad");
            }
        }
        catch (Exception e){
            throw new UnauthorizedException("bad");
        }
    }

    public static UserDataModel getUser(String username){
        var select = "SELECT username, password, email FROM users WHERE username = ?";
        try (var conn = DatabaseManager.getConnection()){
            var preparedStatement = conn.prepareStatement(select);
            preparedStatement.setString(1, username);
            ResultSet result = preparedStatement.executeQuery();
            result.next();
            String finalUsername = result.getString(1);
            String finalPass = result.getString("password");
            String finalEmail = result.getString(3);
            return new UserDataModel(finalUsername, finalPass, finalEmail);
        }
        catch (SQLException | DataAccessException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
