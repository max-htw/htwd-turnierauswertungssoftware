import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class Main {

    public static void main(String[] args) throws IOException {
        int p = 8440; //80
        HttpServer server = HttpServer.create(
                new InetSocketAddress(p), 1024);
        server.createContext("/", new ContextHandlers.CombHandler());//new ContextHandlers.RootHandler());
        server.createContext("/turnier.pdf", new ContextHandlers.TurnierPdfHandler());
        server.createContext("/turnier.csv", new ContextHandlers.TurnierCsvHandler());
        server.start();
        System.out.println("\nSE1 Turnierauswertungssoftware, experimenteller Prototyp 01.\n" +
                "HTTP-Server gestartet unter: http://localhost:" + p + "\n" +
                "Beenden mit 'CTRL + C'\n");
    }
}