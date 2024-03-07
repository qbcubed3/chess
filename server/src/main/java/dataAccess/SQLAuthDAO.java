package dataAccess;

import java.sql.SQLException;

public class SQLAuthDAO {

    public static void createTable() throws Exception {
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "CREATE TABLE IF NOT EXISTS auths (" +
                    "username VARCHAR(100), " +
                    "auth VARCHAR(255))";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.executeUpdate();
        }
        catch (SQLException e){
            throw new Exception(e.getMessage());
        }
    }
    public static void clearAuths(){
        return;
    }
    public String addAuth(String username){
        return "";
    }

    public static int length(){
        return 1;
    }

    public static String findUsername(String auth){
        return auth;
    }

    public static String getUsername(String auth){
        return auth;
    }

    public static void printAuths() {
        return;
    }
}
