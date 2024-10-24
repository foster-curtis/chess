package service;

import dataaccess.DataAccessException;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;
import spark.utils.Assert;

import java.util.Collection;

import static org.junit.jupiter.api.Assertions.fail;

public class TestService {
    private ChessService service;
    private UserData startingUser;

    @BeforeEach
    public void setup() {
        var auth = new MemoryAuthDAO();
        var game = new MemoryGameDAO();
        var user = new MemoryUserDAO();
        this.service = new ChessService(game, user, auth);
        this.startingUser = new UserData("bob", "12345", "ih@veanemail");
    }

    @Test
    public void registerUserSuccess() {
        try {
            var auth = service.register(startingUser);
            Assertions.assertInstanceOf(AuthData.class, auth);
        } catch (DataAccessException exception) {
            fail("DataAccessException");
        }
    }

    @Test
    public void registerUserFail() {
        try {
            service.register(startingUser);
            service.register(startingUser);
            fail("User was registered twice");
        } catch (DataAccessException exception) {
            Assertions.assertInstanceOf(DataAccessException.class, exception);
        }
    }

    @Test
    public void loginUserSuccess() {
        try {
            service.register(startingUser);
            var auth2 = service.login(startingUser);
            Assertions.assertInstanceOf(AuthData.class, auth2);
        } catch (DataAccessException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void loginUserFailWrongPassword() {
        try {
            service.register(startingUser);
            UserData wrongPass = new UserData("bob", "1234567", "ih@veanemail");
            service.login(wrongPass);
            fail("Logged user in with wrong password");
        } catch (DataAccessException exception) {
            Assertions.assertInstanceOf(DataAccessException.class, exception);
            Assertions.assertEquals(exception.StatusCode(), 401);
        }
    }

    @Test
    public void loginUserFailWrongUsername() {
        try {
            service.register(startingUser);
            UserData wrongUser = new UserData("bobby", "12345", "ih@veanemail");
            service.login(wrongUser);
            fail("Logged user in with wrong password");
        } catch (DataAccessException exception) {
            Assertions.assertInstanceOf(DataAccessException.class, exception);
            Assertions.assertEquals(exception.StatusCode(), 401);
        }
    }

    @Test
    public void logoutUserFail() {
        try {
            service.register(startingUser);
            service.login(startingUser);
            service.logout(new AuthData("45", "bob"));
            fail("did not throw exception with an invalid authToken");
        } catch (DataAccessException exception) {
            Assertions.assertInstanceOf(DataAccessException.class, exception);
            Assertions.assertEquals(exception.StatusCode(), 401);
        }
    }

    @Test
    public void logoutUserSuccess() {
        try {
            service.register(startingUser);
            var auth = service.login(startingUser);
            service.logout(auth);
            Assertions.assertNull(service.getAuth(auth));
            fail("did not throw exception for getAuth");
        } catch (DataAccessException exception) {
            Assertions.assertInstanceOf(DataAccessException.class, exception);
            Assertions.assertEquals(exception.StatusCode(), 401);
        }
    }

    @Test
    public void createGameSuccess() {
        try {
            service.register(startingUser);
            var auth = service.login(startingUser);
            GameData game = new GameData(0, null, null, "I will win", null);
            var gameID = service.createGame(game, auth);
            Assertions.assertNotNull(service.getGame(gameID));
        } catch (DataAccessException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void createGameFailBadAuth() {
        try {
            service.register(startingUser);
            service.login(startingUser);
            GameData game = new GameData(0, null, null, "I will win", null);
            service.createGame(game, new AuthData("45", "bob"));
            fail("should have thrown exception for bad Auth");
        } catch (DataAccessException exception) {
            Assertions.assertInstanceOf(DataAccessException.class, exception);
            Assertions.assertEquals(exception.StatusCode(), 401);
        }
    }

    @Test
    public void createGameFailNoName() {
        try {
            service.register(startingUser);
            var auth = service.login(startingUser);
            GameData game = new GameData(0, null, null, null, null);
            service.createGame(game, auth);
            fail("should have thrown exception for bad request");
        } catch (DataAccessException exception) {
            Assertions.assertInstanceOf(DataAccessException.class, exception);
            Assertions.assertEquals(exception.StatusCode(), 400);
        }
    }

    @Test
    public void joinCreatedGameSuccess() {
        try {
            service.register(startingUser);
            var auth = service.login(startingUser);
            GameData game = new GameData(0, null, null, "Test", null);
            int gameID = service.createGame(game, auth);
            service.joinGame(new JoinRequest("WHITE", gameID), auth);
            GameData updatedGame = service.getGame(gameID);
            Assertions.assertEquals(updatedGame.whiteUsername(), startingUser.username());
        } catch (DataAccessException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void joinCreatedGameNoColor() {
        try {
            service.register(startingUser);
            var auth = service.login(startingUser);
            GameData game = new GameData(0, null, null, "Test", null);
            int gameID = service.createGame(game, auth);
            service.joinGame(new JoinRequest(null, gameID), auth);
            fail("should have thrown an exception for no color");
        } catch (DataAccessException exception) {
            Assertions.assertInstanceOf(DataAccessException.class, exception);
            Assertions.assertEquals(exception.StatusCode(), 400);
        }
    }

    @Test
    public void joinCreatedGameStealColor() {
        try {
            service.register(startingUser);
            var auth = service.login(startingUser);
            GameData game = new GameData(0, null, "Joe", "Test", null);
            int gameID = service.createGame(game, auth);
            service.joinGame(new JoinRequest("BLACK", gameID), auth);
            fail("should have thrown an exception for already taken");
        } catch (DataAccessException exception) {
            Assertions.assertInstanceOf(DataAccessException.class, exception);
            Assertions.assertEquals(exception.StatusCode(), 403);
        }
    }

    @Test
    public void listMultipleGamesSuccess() {
        try {
            service.register(startingUser);
            var auth = service.login(startingUser);
            GameData game1 = new GameData(0, null, null, "I will win", null);
            var gameID = service.createGame(game1, auth);
            GameData game2 = new GameData(0, null, null, "This ain't  a joke", null);
            var game2ID = service.createGame(game2, auth);
            Assertions.assertNotNull(service.getGame(gameID));
            Assertions.assertNotNull(service.getGame(game2ID));

            Collection<GameData> games = service.listGames(auth);
            Assertions.assertEquals(2, games.size());
        } catch (DataAccessException exception) {
            fail(exception.getMessage());
        }
    }

    @Test
    public void listMultipleGamesBadAuthFail() {
        try {
            service.register(startingUser);
            var auth = service.login(startingUser);
            GameData game1 = new GameData(0, null, null, "I will win", null);
            var gameID = service.createGame(game1, auth);
            GameData game2 = new GameData(0, null, null, "This ain't  a joke", null);
            var game2ID = service.createGame(game2, auth);
            Assertions.assertNotNull(service.getGame(gameID));
            Assertions.assertNotNull(service.getGame(game2ID));

            Collection<GameData> games = service.listGames(new AuthData("23234", "yello"));
            fail("should have thrown unauthorized exception");
        } catch (DataAccessException exception) {
            Assertions.assertInstanceOf(DataAccessException.class, exception);
            Assertions.assertEquals(exception.StatusCode(), 401);
        }
    }

    @Test
    public void clear() {
        try {
            var auth = service.register(startingUser);
            Assertions.assertInstanceOf(AuthData.class, auth);
            service.clear();
            Assertions.assertNull(service.getUser(startingUser));
        } catch (DataAccessException exception) {
            fail("Failed to register user");
        }
    }
}
