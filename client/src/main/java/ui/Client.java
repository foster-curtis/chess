package ui;

import model.AuthData;

public interface Client {

    State getState();

    String eval(String input);

    String help();

    AuthData getCurrentUserAuth();
}
