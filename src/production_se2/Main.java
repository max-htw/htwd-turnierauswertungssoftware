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
            if(args.length > 0){
              try{
                p = Integer.parseInt(args[0]);
              }
              catch(Exception e){};
            }
            HttpServer server = HttpServer.create(
                    new InetSocketAddress(p), 1024);
            //server.createContext("/", new ContextHandlers.CombHandler());
            server.createContext("/", new WebserverInternal_ContextHandler());
            server.createContext("/output.css", new WebserverInternal_ContextHandler.CssHttpHandler());
            server.start();
            System.out.println("\nSE1 Turnierauswertungssoftware, Production Build.\n" +
                    "HTTP-Server gestartet unter: http://localhost:" + p + "\n" +
                    "Beenden mit 'CTRL + C'\n");
        }
        else if(webserverType == AppSettings.SupportedWebServerTypes.cgi) {
            //ToDo:
            // - Socket starten, um auf die Verbindungen von CGI-Prozessen zu warten
            // - Threads implementieren
        }
        else if(webserverType == AppSettings.SupportedWebServerTypes.mock){
          //die Code hier dient nur zur Demonstration des Aufrufs des Mock-Webservers.
          //die Verwendung des Mock-Webservers findet haupsaechlich in Unit-Tests statt.
          WebserverMock w = new WebserverMock(new DBInterface_InMemory());
          w.requestRole = StringsRole.Admin;
          w.requestTask = StringsRole.AdminTasks.Einstellungen;
          w.requestGroupID = -1;
          w.requestTeamNr = -1;
          w.requestParams.clear();
          w.requestParams.put(StringsActions.setAnzGroups, "" + 3);

          RoleWithTaskBase_Data d = w.serveUserRequest();
          if(d instanceof RoleAdmin_TaskEinstellungen_Data){
            System.out.println(((RoleAdmin_TaskEinstellungen_Data) d).anzGruppen);
          }

        }
        else if(webserverType == AppSettings.SupportedWebServerTypes.swing){
          //ToDo: Swing-Oberflaeche entwickeln
          //  - der Swing-Webserver ist dafuer gedacht um zu pruefen, ob die Architektur
          //    der Anwendung es zulaesst, eine andere View-Technologie, als HTML zu verwenden.
          new WebserverSwing_Handler("Turnierauswertungssoftware");
        }
    }
}
