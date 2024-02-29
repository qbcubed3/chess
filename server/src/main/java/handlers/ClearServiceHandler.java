package handlers;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import spark.Request;
import spark.Response;
import spark.Route;
import service.ClearService;

public class ClearServiceHandler{

    public static Object callClearService(Request request, Response response){
        try {
            ClearService.clearDatabase();
        }
        catch (Exception e){
            response.status(500);
            String message = e.getMessage();
            JsonObject jsonError = new JsonObject();
            jsonError.addProperty("message", "Error: " + message);

            return jsonError.toString();
        }
        response.status(200);

        return "{}";
    }
}
