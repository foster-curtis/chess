package ui.serverfacade;

import com.google.gson.Gson;
import exception.ResponseException;
import model.*;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final int port;

    public ServerFacade(int port) {
        this.port = port;
    }

    public void clear() {
        String path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    public AuthData register(UserData user) {
        String path = "/user";
        return this.makeRequest("POST", path, user, AuthData.class);
    }

    public AuthData login(UserData user) {
        String path = "/session";
        return this.makeRequest("POST", path, user, AuthData.class);
    }

    public void logout(AuthData auth) {
        String path = "/session";
        this.makeRequest("DELETE", path, auth, null);
    }


    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI("http://localhost:" + port + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new ResponseException(ex.getMessage(), 500);
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

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        var message = http.getResponseMessage();
        if (!isSuccessful(status)) {
            throw new ResponseException(status + " failure: " + message, status);
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


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
