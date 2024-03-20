package handlers;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.stream.MalformedJsonException;
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
            StringBuilder finalJson = new StringBuilder(gson.toJson(games));
            finalJson.insert(0, "{ \"games\": ");
            finalJson.append("}");
            return finalJson.toString();
        }
        catch (UnauthorizedException e) {
            response.status(401);
            System.out.println("this one");
            return "{ \"message\": \"Error: unauthorized\" }";
        }
    }

    public static String callCreateGameService(Request request, Response response){
        Gson gson = new Gson();
        String name = request.body();
        JsonElement jsonElement = JsonParser.parseString(name);
        String auth = gson.fromJson(request.headers("authorization"), String.class);
        String gameName;
        try{
            gameName = name;
        }
        catch (Exception e){
            response.status(400);
            return "{ \"message\": \"Error: bad request\" }";
        }
        try{
            int gameId = GameService.createGame(auth, gameName);
            response.status(200);
            var thing = "{ \"gameID\":" + gameId +  "}";
            System.out.println(thing.getClass());
            return thing;
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
        String auth = "";
        try{
            auth = gson.fromJson(request.headers("authorization"), String.class);
        }
        catch (Exception e){
            response.status(401);
            return "{ \"message\": \"Error: bad request\" }";
        }
        JsonElement jsonElement = JsonParser.parseString(body);
        int gameId = jsonElement.getAsJsonObject().get("gameID").getAsInt();
        String playerColor = "";
        if (jsonElement.getAsJsonObject().get("playerColor") != null){
            playerColor = jsonElement.getAsJsonObject().get("playerColor").getAsString();
        }
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
