import chess.*;
import dataAccess.*;
import model.UserDataModel;
import server.Server;


public class Main {
    public static void main(String[] args) throws Exception {
        SQLUserDAO.createTable();
        SQLAuthDAO.createTable();
        SQLGameDAO.createTable();
        SQLUserDAO.registerUser(new UserDataModel("username", "thing", "email@email.com"));
        SQLUserDAO.clearUsers();
    }
}