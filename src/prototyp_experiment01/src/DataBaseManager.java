import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.File;

public class DataBaseManager {

    public static Connection connect() {
        // Relativer Pfad zur Datenbank
        String dbPath = "src/prototyp_experiment01/db/spieldaten.db";

        try {
            // Falls Ordner noch nicht existiert, anlegen
            File dbDir = new File("src/prototyp_experiment01/db");
            if (!dbDir.exists()) {
                dbDir.mkdirs();
            }

            // Verbindung aufbauen
            String url = "jdbc:sqlite:" + dbPath;
            return DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.out.println("Verbindungsfehler: " + e.getMessage());
            return null;
        }
    }
}