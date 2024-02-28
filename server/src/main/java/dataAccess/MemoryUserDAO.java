package dataAccess;
import java.util.HashMap;
public class MemoryUserDAO implements UserDAO{
    static HashMap<String, String> passwords;
    static HashMap<String, String> usernames;

    public static void clearUsers(){
        passwords.clear();
        usernames.clear();
    }
}
