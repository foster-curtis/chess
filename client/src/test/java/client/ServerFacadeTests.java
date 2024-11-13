package client;

import model.AuthData;
import model.UserData;
import org.junit.jupiter.api.*;
import server.Server;
import ui.serverfacade.ServerFacade;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade serverFacade;
    private static UserData user;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        serverFacade = new ServerFacade(port);
        user = new UserData("bobbette", "password", "em@il.com");
        System.out.println("Started test HTTP server on " + port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }

    @BeforeEach
    void setup() {
        serverFacade.clear();
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
        ;
    }
}
