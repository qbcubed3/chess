package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import dataAccess.NullParameterException;
import dataAccess.UnauthorizedException;
import dataAccess.UsernameTakenException;
import model.AuthDataModel;
import model.GameDataModel;
import model.UserDataModel;
import service.GameService;
import service.UserService;
import spark.Request;
import spark.Response;
import spark.Route;

import java.util.ArrayList;

public class GameServiceHandler {

    public static String callListService(Request request, Response response){
        Gson gson = new Gson();
        String auth = gson.fromJson(request.headers("authorization"), String.class);
        try{
            ArrayList<GameDataModel> games = GameService.listGames(auth);
            return gson.toJson(games);
        }
        catch (UnauthorizedException e) {
            response.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
    }

    public static String callCreateGameService(Request request, Response response){
        Gson gson = new Gson();
        String name = request.body();
        JsonElement jsonElement = JsonParser.parseString(name);
        String auth = gson.fromJson(request.headers("authorization"), String.class);
        String gameName = jsonElement.getAsJsonObject().get("gameName").getAsString();
        try{
            int gameId = GameService.createGame(auth, gameName);
            response.status(200);
            return "{ \"gameID\":" + gameId +  "}";
        }
        catch (NullParameterException e){
            response.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        }
        catch (UnauthorizedException e){
            response.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
        }
    }

    public static String callJoinGameService(Request request, Response response){
        Gson gson = new Gson();
        String body = request.body();
        String auth = gson.fromJson(request.headers("authorization"), String.class);
        JsonElement jsonElement = JsonParser.parseString(body);
        int gameId = jsonElement.getAsJsonObject().get("gameID").getAsInt();
        String playerColor = jsonElement.getAsJsonObject().get("playerColor").getAsString();
        try{
            GameService.joinGame(auth, gameId, playerColor);
            return "{}";
        }
        catch (UnauthorizedException e){
            response.status(401);
            return "{ \"message\": \"Error: unauthorized\" }";
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
