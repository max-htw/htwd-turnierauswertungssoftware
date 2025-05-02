import java.io.IOException;
import java.net.InetSocketAddress;
import com.sun.net.httpserver.HttpServer;

public class Main {

    public static void main(String[] args) throws IOException {
        AppSettings.SupportedWebServerTypes webserverType = DevSettings.getWebServerType();
        if(webserverType == null)
            webserverType = AppSettings.getWebServerType();

        if(webserverType == AppSettings.SupportedWebServerTypes.internal) {
            int p = 8450;
            HttpServer server = HttpServer.create(
                    new InetSocketAddress(p), 1024);
            //server.createContext("/", new ContextHandlers.CombHandler());
            server.createContext("/", new WebserverInternal_ContextHandler());
            server.createContext("/output.css", new WebserverInternal_ContextHandler.CssHttpHandler());
            server.start();
            System.out.println("\nSE1 Turnierauswertungssoftware, experimenteller Prototyp 01.\n" +
                    "HTTP-Server gestartet unter: http://localhost:" + p + "\n" +
                    "Beenden mit 'CTRL + C'\n");
        }
        else if(webserverType == AppSettings.SupportedWebServerTypes.cgi) {
            //ToDo:
            // - Socket starten, um auf die Verbindungen von CGI-Prozessen zu warten
            // - Threads implementieren
        }
        else if(webserverType == AppSettings.SupportedWebServerTypes.mock){

        }
    }
}
