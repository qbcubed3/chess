package dataAccess;
import model.UserDataModel;

import java.util.HashMap;
public class MemoryUserDAO implements UserDAO{
    static HashMap<String, String> emails = new HashMap<>();
    static HashMap<String, String> passwords = new HashMap<>();

    public static void clearUsers(){
        emails.clear();
        passwords.clear();
    }

    public static void registerUser(UserDataModel data) throws UsernameTakenException{
        if (!passwords.isEmpty() && passwords.containsKey(data.username())){
            throw new UsernameTakenException("Error: already taken");
        }
        else{
            emails.put(data.username(), data.email());
            passwords.put(data.username(), data.password());
        }
    }

    public static int length(){
        return emails.size() + passwords.size();
    }

    public static String getAuth(String username, String password) throws UnauthorizedException{
        if (passwords.containsKey(username)) {
            if (passwords.get(username).equals(password)) {
                return MemoryAuthDAO.addAuth(username);
            }
        }
        throw new UnauthorizedException("");

    }
}
