package service;
import service.ClearService;
import dataAccess.*;

public class ClearService {
    public static void clearDatabase(){
        SQLAuthDAO.clearAuths();
        SQLGameDAO.clearGame();
        SQLUserDAO.clearUsers();
    }
}
