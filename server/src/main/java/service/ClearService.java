package service;
import dataAccess.SQLAuthDAO;
import dataAccess.SQLGameDAO;
import dataAccess.SQLUserDAO;

public class ClearService {
    public static void clearDatabase(){
        SQLAuthDAO.clearAuths();
        SQLGameDAO.clearGame();
        SQLUserDAO.clearUsers();
    }
}
