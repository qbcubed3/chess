package dataAccess;
import java.util.HashMap;

public class MemoryAuthDAO implements AuthDAO{
   private static HashMap<String, String> authTokens = new HashMap<>();

    public static void clearAuths(){
        authTokens.clear();
    }
    public static String addAuth(String username){
        StringBuilder auth = new StringBuilder();
        for (int i = 0; i < username.length(); i++){
            int ascii = username.charAt(i);
            ascii++;
            auth.append((char)ascii);
        }
        authTokens.put(username, auth.toString());
        return auth.toString();
    }
    public static HashMap<String, String> getData(){
        return authTokens;
    }

    public static int length(){
        return authTokens.size();
    }
}
