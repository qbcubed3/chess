package server;

import com.google.gson.Gson;
import model.AuthDataModel;
import model.GameDataModel;
import model.UserDataModel;

import java.io.*;
import java.net.*;
import java.util.ArrayList;

public class ServerFacade {
    private final String serverUrl;
    private String authToken;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public AuthDataModel register(String username, String password, String email) throws Exception {
        var path = "/user";
        var user = new UserDataModel(username, password, email);
        var auth = this.makeRequest("POST", path, user, AuthDataModel.class);
        if (auth != null) {
            this.authToken = auth.authToken();
        } else {
            throw new Exception("invalid");
        }
        return auth;
    }

    public AuthDataModel login(String username, String password) throws Exception{
        var path = "/session";
        var user = new UserDataModel(username, password, null);
        var auth = this.makeRequest("POST", path, user, AuthDataModel.class);
        this.authToken = auth.authToken();
        return auth;
    }

    public AuthDataModel logout() throws Exception{
        var path = "/session";
        var auth = this.makeRequest("DELETE", path, null, AuthDataModel.class);
        if (authToken != null){authToken = null;}
        return auth;
    }
    public ArrayList<GameDataModel> list() throws Exception {
        var path = "/game";
        var games = new ArrayList<GameDataModel>();
        return this.makeRequest("GET", path, null, games.getClass());
    }

    public void create(String name) throws Exception {
        var path = "/game";
        this.makeRequest("POST", path, name, null);
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws Exception {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);
            if (authToken != null){http.setRequestProperty("authorization", authToken);}

            writeBody(request, http);
            http.connect();
            var status = http.getResponseCode();
            if (!(status/100 == 2)){
                throw new Exception("failure: " + status);
            }
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }
}
