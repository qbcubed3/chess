package service;

import dataAccess.MemoryAuthDAO;
import dataAccess.MemoryUserDAO;
import dataAccess.NullParameterException;
import dataAccess.UsernameTakenException;
import model.UserDataModel;

public class UserService {
    public static String registerUser(UserDataModel data) throws UsernameTakenException, NullParameterException{
        if (data.email().isEmpty() || data.username().isEmpty() || data.password().isEmpty()){
            throw new NullParameterException("Error: bad request");
        }
        MemoryUserDAO.registerUser(data);
        return MemoryAuthDAO.addAuth(data.username());
    }
}
