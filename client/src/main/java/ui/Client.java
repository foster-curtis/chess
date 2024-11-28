package ui;

import chess.ChessGame;
import model.AuthData;

public interface Client {

    State getState();

    String eval(String input);

    String help();

    AuthData getCurrentUserAuth();

    Integer getGameID();

    ChessGame.TeamColor getPlayerColor();
}
