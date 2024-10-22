import chess.*;
import dataaccess.MemoryAuthDAO;
import dataaccess.MemoryGameDAO;
import dataaccess.MemoryUserDAO;
import server.Server;
import service.ChessService;

public class Main {
    public static void main(String[] args) {
        var auth = new MemoryAuthDAO();
        var game = new MemoryGameDAO();
        var user = new MemoryUserDAO();
        ChessService service = new ChessService(game, user, auth);
        Server s = new Server(service);
        int port = s.run(8080);
        System.out.println("♕ 240 Chess Server: " + port);
    }
}