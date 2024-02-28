package service;
import service.ClearService;
import dataAccess.*;

public class ClearService {
    public static void clearDatabase(){
        MemoryAuthDAO.clearAuths();
        MemoryGameDAO.clearGame();
        MemoryUserDAO.clearUsers();
    }
}
