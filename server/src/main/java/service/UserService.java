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
        SQLUserDAO.registerUser(data);
        return SQLAuthDAO.addAuth(data.username());
    }

    public static String loginUser(String username, String password) throws UnauthorizedException, NullParameterException {
        System.out.println("username: " + username + " password: " + password);
        String user = "";
        String auth = "";
        if (!username.isEmpty() && !password.isEmpty()){
            user = SQLUserDAO.getAuth(username, password);
            auth = SQLAuthDAO.addAuth(user);
        }
        else{
            throw new NullParameterException("username is null");
        }
        return auth;
    }

    public static String logoutUser(String auth) throws UnauthorizedException{
        return SQLAuthDAO.removeUsername(auth);
    }
}
