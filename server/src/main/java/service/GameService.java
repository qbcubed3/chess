package service;

import dataAccess.*;
import dataAccess.SQLDataAccess.SQLAuthDAO;
import dataAccess.SQLDataAccess.SQLGameDAO;
import model.GameDataModel;

import java.util.ArrayList;

public class GameService {
    public static ArrayList<GameDataModel> listGames(String auth) throws UnauthorizedException {
        if (!SQLAuthDAO.checkAuth(auth)) {
            throw new UnauthorizedException("auth Token doesn't exist");
        }
        return SQLGameDAO.getGames();
    }

    public static int createGame(String auth, String gameName) throws UnauthorizedException, NullParameterException{
        if (!SQLAuthDAO.checkAuth(auth)){
            throw new UnauthorizedException("auth Token doesn't exist");
        }
        else if (gameName.isEmpty()){
            throw new NullParameterException("game name can't be blank");
        }

        return SQLGameDAO.createGame(gameName);
    }

    public static void joinGame(String auth, int gameId, String playerColor)throws UnauthorizedException, NullParameterException, UsernameTakenException{
        if (!SQLAuthDAO.checkAuth(auth)){
            throw new UnauthorizedException("auth Token doesn't exist");
        }
        String username = SQLAuthDAO.getUsername(auth);
        SQLGameDAO.joinGame(gameId, playerColor, username);
    }
}
