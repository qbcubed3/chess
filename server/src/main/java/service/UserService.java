package service;

import dataAccess.*;
import model.AuthDataModel;
import model.UserDataModel;

public class UserService {
    public static String registerUser(UserDataModel data) throws UsernameTakenException, NullParameterException{
        if (data.email()==null || data.username()==null || data.password()==null){
            throw new NullParameterException("Error: bad request");
        }
        if (data.email().isEmpty() || data.username().isEmpty() || data.password().isEmpty()){
            throw new NullParameterException("Error: bad request");
        }
        MemoryUserDAO.registerUser(data);
        return MemoryAuthDAO.addAuth(data.username());
    }

    public static String loginUser(String username, String password) throws UnauthorizedException, NullParameterException {
        String user = "";
        String auth = "";
        if (!username.isEmpty() && !password.isEmpty()){
            user = MemoryUserDAO.getAuth(username, password);
            auth = MemoryAuthDAO.addAuth(user);
        }
        else{
            throw new NullParameterException("username is null");
        }
        return auth;
    }

    public static String logoutUser(String auth) throws UnauthorizedException{
        return MemoryAuthDAO.findUsername(auth);
    }
}
