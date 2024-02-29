package dataAccess;
import model.UserDataModel;

import java.util.HashMap;
public class MemoryUserDAO implements UserDAO{
    static HashMap<String, String> passwords = new HashMap<>();
    static HashMap<String, String> usernames = new HashMap<>();

    public static void clearUsers(){
        passwords.clear();
        usernames.clear();
    }

    public static void registerUser(UserDataModel data) throws UsernameTakenException{
        if (!usernames.isEmpty() && usernames.containsValue(data.username())){
            throw new UsernameTakenException("Error: already taken");
        }
        else{
            passwords.put(data.email(), data.password());
            usernames.put(data.email(), data.username());
        }
    }

    public static int length(){
        return passwords.size() + usernames.size();
    }
}
