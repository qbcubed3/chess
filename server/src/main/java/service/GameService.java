package service;

import dataAccess.*;
import model.GameDataModel;

import java.util.ArrayList;

public class GameService {
    public static ArrayList<GameDataModel> listGames(String auth) throws UnauthorizedException {
        if (!MemoryAuthDAO.checkAuth(auth)) {
            throw new UnauthorizedException("auth Token doesn't exist");
        }
        return MemoryGameDAO.getGames();
    }

    public static int createGame(String auth, String gameName) throws UnauthorizedException, NullParameterException{
        if (!MemoryAuthDAO.checkAuth(auth)){
            throw new UnauthorizedException("auth Token doesn't exist");
        }
        else if (gameName.isEmpty()){
            throw new NullParameterException("game name can't be blank");
        }

        return MemoryGameDAO.createGame(gameName);
    }

    public static void joinGame(String auth, int gameId, String playerColor)throws UnauthorizedException, NullParameterException, UsernameTakenException{
        if (!MemoryAuthDAO.checkAuth(auth)){
            throw new UnauthorizedException("auth Token doesn't exist");
        }
        else if (gameId == 0){
            throw new NullParameterException("game id can't be blank");
        }
        String username = MemoryAuthDAO.findUsername(auth);
        MemoryGameDAO.joinGame(gameId, playerColor, username);
    }
}
