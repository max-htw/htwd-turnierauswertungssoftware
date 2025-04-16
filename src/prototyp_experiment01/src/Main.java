import java.util.Timer;
import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class Main {

    public static void main(String[] args) throws IOException {
        archivateTimerTask timerTask = new archivateTimerTask();
        //running timer task as daemon thread
        Timer timer = new Timer(true);
        timer.scheduleAtFixedRate(timerTask, 0, 1*1000); // 1*1000 = 1 Sekunde

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