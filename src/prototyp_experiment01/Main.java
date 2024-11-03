import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class Main {

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(
                new InetSocketAddress(8440), 1024);
        server.createContext("/", new ContextHandlers.RootHandler());
        server.createContext("/turnierplan", new ContextHandlers.TPlanHandler());
        server.createContext("/match", new ContextHandlers.MatchDetailsHandler());
        server.start();
        System.out.println("\nSE1 Turnierauswertungssoftware, experimenteller Prototyp 01.\n" +
                "HTTP-Server gestartet unter: http://localhost:8440\n" +
                "Beenden mit 'CTRL + C'\n");
    }
}