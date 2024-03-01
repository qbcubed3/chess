package dataAccess;
import java.util.HashMap;
import java.util.Random;

public class MemoryAuthDAO implements AuthDAO{
   private static final HashMap<String, String> usernames = new HashMap<>();

    public static void clearAuths(){
        usernames.clear();
    }
    public static String addAuth(String username){
        StringBuilder auth = new StringBuilder();
        Random random = new Random();
        String validCharacters = "abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ1234567890";
        for (int i = 0; i < 16; i++){
            int randomIndex = random.nextInt(validCharacters.length());
            auth.append(validCharacters.charAt(randomIndex));
        }
        usernames.put(auth.toString(), username);
        return auth.toString();
    }
    public static HashMap<String, String> getData(){
        return usernames;
    }

    public static int length(){
        return usernames.size();
    }

    public static boolean checkAuth(String auth){
        return usernames.containsKey(auth);
    }

    public static String findUsername(String auth) throws UnauthorizedException{
        if (checkAuth(auth)){
            String authToken = usernames.get(auth);
            usernames.remove(auth);
            return authToken;
        }
        else{
            throw new UnauthorizedException("authToken doesn't exist");
        }
    }
    public static String getUsername(String auth) throws UnauthorizedException{
        if (checkAuth(auth)){
            return usernames.get(auth);
        }
        else{
            throw new UnauthorizedException("authToken doesn't exist");
        }
    }

    public static void printAuths(){
        System.out.println(usernames);
    }
}
