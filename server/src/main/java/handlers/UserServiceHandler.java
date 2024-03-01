package handlers;

import com.google.gson.Gson;
import dataAccess.NullParameterException;
import dataAccess.UnauthorizedException;
import dataAccess.UsernameTakenException;
import model.AuthDataModel;
import model.UserDataModel;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserServiceHandler {

    public static Object callRegisterService(Request request, Response response){
        Gson gson = new Gson();
        UserDataModel data = gson.fromJson(request.body(), UserDataModel.class);
        try{
            String auth = UserService.registerUser(data);
            AuthDataModel authData = new AuthDataModel(auth, data.username());
            response.status(200);
            return new Gson().toJson(authData);
        }
        catch (UsernameTakenException e){
            response.status(403);
            return "{ \"message\": \"Error: already taken\" }";
        }
        catch (NullParameterException e){
            response.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        }
    }

    public static Object callLoginService(Request request, Response response){
        Gson gson = new Gson();
        UserDataModel data = gson.fromJson(request.body(), UserDataModel.class);
        try{
            String auth = UserService.loginUser(data.username(), data.password());
            AuthDataModel authData = new AuthDataModel(auth, data.username());
            response.status(200);
            return gson.toJson(authData);
        }
        catch (UnauthorizedException e){
            response.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
        catch (Exception e){
            response.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }

    public static Object callLogoutService(Request request, Response response){
        Gson gson = new Gson();
        String auth = gson.fromJson(request.headers("authorization"), String.class);
        try{
            String username = UserService.logoutUser(auth);
            AuthDataModel authData = new AuthDataModel(auth, username);
            response.status(200);
            return gson.toJson(authData);
        }
        catch (UnauthorizedException e) {
            response.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
        catch (Exception e){
            response.status(500);
            return "{ \"message\": \"Error: description\" }";
        }
    }
}
