package handlers;

import com.google.gson.Gson;
import dataAccess.NullParameterException;
import dataAccess.UsernameTakenException;
import model.AuthDataModel;
import model.UserDataModel;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

public class UserServiceHandler {

    public static Object callUserService(Request request, Response response){
        Gson gson = new Gson();
        UserDataModel data = gson.fromJson(request.body(), UserDataModel.class);
        UserService service = new UserService();
        try{
            String auth = service.registerUser(data);
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
}
