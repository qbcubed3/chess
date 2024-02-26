package handlers;

import service.ClearService;

public class ClearServiceHandler{

    private static void callClearService(){
        ClearService.clearDatabase();
    }
}
