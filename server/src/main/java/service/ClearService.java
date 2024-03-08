package service;
import dataAccess.SQLDataAccess.SQLAuthDAO;
import dataAccess.SQLDataAccess.SQLGameDAO;
import dataAccess.SQLDataAccess.SQLUserDAO;

public class ClearService {
    public static void clearDatabase(){
        SQLAuthDAO.clearAuths();
        SQLGameDAO.clearGame();
        SQLUserDAO.clearUsers();
    }
}
