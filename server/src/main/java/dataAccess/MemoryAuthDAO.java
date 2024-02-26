package dataAccess;
import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO{
    private static HashSet<String> usernames;
    private static HashSet<String> authTokens;
}
