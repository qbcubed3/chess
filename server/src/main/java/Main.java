import chess.*;
import dataAccess.*;
import model.UserDataModel;
import server.Server;


public class Main {
    public static void main(String[] args) throws Exception {
        SQLAuthDAO.clearAuths();
        SQLGameDAO.createGame("game");
        String auth = SQLAuthDAO.addAuth("punitiveMedal");
        System.out.println(SQLAuthDAO.checkAuth(auth));
    }
}