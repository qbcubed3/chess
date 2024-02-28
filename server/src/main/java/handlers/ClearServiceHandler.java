package handlers;
import com.google.gson.Gson;
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
            handlerException exception = new handlerException();
            exception.setErrorCode("500");
            exception.setErrorMessage(e.getMessage());
            var serializer = new Gson();
            var json = serializer.toJson(exception);
            System.out.println(json);
            return serializer.toJson(exception);
        }
        response.status(200);

        return "{}";
    }
}
