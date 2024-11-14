package ui;

import model.AuthData;

import java.util.HashMap;

public interface Client {

    State getState();

    String eval(String input);

    String help();

    AuthData getCurrentUserAuth();
}
