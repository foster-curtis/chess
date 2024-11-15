package client;

import model.AuthData;
import model.GameData;
import model.JoinRequest;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.serverfacade.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static UserData user;
    private static UserData registeredUser;
    private static AuthData validAuth;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade(port);
        user = new UserData("bobbette", "password", "em@il.com");
        registeredUser = new UserData("bobby", "Password", "em@il.com");
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setup() {
        serverFacade.clear();
        validAuth = serverFacade.register(registeredUser);
    }


    @Test
    public void sampleTest() {
        Assertions.assertTrue(true);
    }

    @Test
    public void testRegister() {
        Assertions.assertInstanceOf(AuthData.class, serverFacade.register(user));
    }

    @Test
    public void testRegisterFail() {
        serverFacade.register(user);
        Assertions.assertThrows(Exception.class, () -> serverFacade.register(user));
    }

    @Test
    public void testLogin() {
        serverFacade.register(user);
        Assertions.assertInstanceOf(AuthData.class, serverFacade.login(user));
    }

    @Test
    public void testLoginFail() {
        serverFacade.register(user);
        UserData badUser = new UserData("bobbette", "098765", "em@il.com");
        Assertions.assertThrows(Exception.class, () -> serverFacade.register(badUser));
    }

    @Test
    public void testLogout() {
        serverFacade.register(user);
        AuthData auth = serverFacade.login(user);
        Assertions.assertDoesNotThrow(() -> serverFacade.logout(auth));
    }

    @Test
    public void testLogoutFail() {
        serverFacade.register(user);
        serverFacade.login(user);
        AuthData badAuth = new AuthData("1o2i3ihfkjhoqodjfhk1jh423hkjshuu2232", "qwerty");
        Assertions.assertThrows(Exception.class, () -> serverFacade.logout(badAuth));
    }

    @Test
    public void testCreateGame() {
        GameData game = new GameData(0, null, null, "name", null);
        Assertions.assertInstanceOf(Integer.class, serverFacade.createGame(game, validAuth));
    }

    @Test
    public void testCreatGameFail() {
        GameData game = new GameData(0, null, null, null, null);
        Assertions.assertThrows(Exception.class, () -> serverFacade.createGame(game, validAuth));
    }

    @Test
    public void testListGames() {
        GameData game = new GameData(0, null, null, "name", null);
        serverFacade.createGame(game, validAuth);
        Assertions.assertInstanceOf(GameData[].class, serverFacade.listGames(validAuth));
    }

    @Test
    public void testListGamesFail() {
        GameData game = new GameData(0, null, null, "name", null);
        serverFacade.createGame(game, validAuth);
        Assertions.assertThrows(Exception.class, () -> serverFacade.listGames(new AuthData("12039938403", "102jajj")));
    }

    @Test
    public void testJoinGame() {
        GameData game = new GameData(0, null, null, "name", null);
        int gameID = serverFacade.createGame(game, validAuth);
        var req = new JoinRequest("WHITE", gameID);
        Assertions.assertDoesNotThrow(() -> serverFacade.joinGame(req, validAuth));
        Assertions.assertEquals(serverFacade.listGames(validAuth)[0].whiteUsername(), validAuth.username());
    }

    @Test
    public void testJoinGameFail() {
        GameData game = new GameData(0, null, null, "name", null);
        int gameID = serverFacade.createGame(game, validAuth);
        var req = new JoinRequest("WHITE", gameID);
        AuthData badAuth = new AuthData("1o2i3ihfkjhoqodjfhk1jh423hkjshuu2232", "qwerty");
        Assertions.assertThrows(Exception.class, () -> serverFacade.joinGame(req, badAuth));
    }
}
