package dataAccess;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
   private static HashMap<String, String> authTokens = new HashMap<>();

    public static void clearAuths(){
        authTokens.clear();
    }
    public static void addAuth(String username, String auth){
        authTokens.put(username, auth);
    }
    public static HashMap<String, String> getData(){
        return authTokens;
    }
}
