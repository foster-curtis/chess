package service;

import chess.ChessGame;
import chess.InvalidMoveException;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import dataaccess.UserDAO;
import model.*;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import java.util.Objects;

public class WebSocketService extends Service {

    public WebSocketService(GameDAO gameDAO, UserDAO userDAO, AuthDAO authDAO) {
        super(gameDAO, userDAO, authDAO);
    }

    public GameUsernamePackage connect(UserGameCommand cmd) throws DataAccessException {
        AuthData authData = authenticate(new AuthData(cmd.getAuthToken(), null));
        return new GameUsernamePackage(gameAccess.getGame(cmd.getGameID()), authData.username());
    }

    public String leave(UserGameCommand cmd, Boolean gameExpired) throws DataAccessException {
        AuthData authData = authenticate(new AuthData(cmd.getAuthToken(), null));

        if (gameExpired) {
            gameAccess.deleteGame(cmd.getGameID());
        } else {
            GameData game = gameAccess.getGame(cmd.getGameID());

            if (Objects.equals(authData.username(), game.whiteUsername())) {
                var newGame = new GameData(game.gameID(), null, game.blackUsername(), game.gameName(), game.game());
                gameAccess.updateGame(newGame);
            } else if (Objects.equals(authData.username(), game.blackUsername())) {
                var newGame = new GameData(game.gameID(), game.whiteUsername(), null, game.gameName(), game.game());
                gameAccess.updateGame(newGame);
            }
        }
        return authData.username();
    }

    public String resign(UserGameCommand cmd) throws DataAccessException {
        var auth = authenticate(new AuthData(cmd.getAuthToken(), null));
        var gameData = gameAccess.getGame(cmd.getGameID());
        if (!Objects.equals(auth.username(), gameData.whiteUsername()) && !Objects.equals(auth.username(), gameData.blackUsername())) {
            throw new DataAccessException("You are an observer. You cannot resign.");
        }
        return auth.username();
    }

    public MakeMoveResponse makeMove(MakeMoveCommand cmd) throws DataAccessException, InvalidMoveException {
        var authData = authenticate(new AuthData(cmd.getAuthToken(), null));
        var gameData = gameAccess.getGame(cmd.getGameID());
        var game = gameData.game();
        var move = cmd.getMove();
        var color = game.getTeamTurn();

        if (!Objects.equals(authData.username(), gameData.whiteUsername()) && !Objects.equals(authData.username(), gameData.blackUsername())) {
            throw new InvalidMoveException("You are an observer. You cannot make a move.");
        }
        var pieceColor = game.getBoard().getPiece(move.getStartPosition()).getTeamColor();
        if (pieceColor == ChessGame.TeamColor.WHITE && !Objects.equals(authData.username(), gameData.whiteUsername())) {
            throw new InvalidMoveException("That is not your piece!");
        } else if (pieceColor == ChessGame.TeamColor.BLACK && !Objects.equals(authData.username(), gameData.blackUsername())) {
            throw new InvalidMoveException("That is not your piece!");
        }

        // Can't hurt to check valid moves one more time...
        var validMoves = game.validMoves(move.getStartPosition());
        if (validMoves == null) {
            throw new InvalidMoveException("Invalid Move");
        }

        game.makeMove(move);

        boolean inCheck = false, inCheckmate = false, inStalemate = false;
        if (game.isInCheckmate(color)) {
            inCheckmate = true;
        }
        if (game.isInCheck(color)) {
            inCheck = true;
        }
        if (game.isInStalemate(color)) {
            inStalemate = true;
        }

        gameAccess.updateGame(gameData);

        return new MakeMoveResponse(authData.username(), gameData, inCheck, inCheckmate, inStalemate);
    }
}
