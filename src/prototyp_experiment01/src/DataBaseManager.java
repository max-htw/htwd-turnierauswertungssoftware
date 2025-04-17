import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DataBaseManager {

    public static Connection connect() {
        // Relativer Pfad zur Datenbank
        String dbPath = "src/prototyp_experiment01/src/database.db";

        try {
            // Verbindung aufbauen
            String url = "jdbc:sqlite:" + dbPath;
            return DriverManager.getConnection(url);

        } catch (SQLException e) {
            System.out.println("Verbindungsfehler: " + e.getMessage());
            return null;
        }
    }

    //Beispielumsetzung zum Auslesen der Tabelle "Matches"
    public static void getMatchResult(int matchId) {
        String sql = "SELECT _firstTeamHinspielPunkte, _secondTeamHinspielPunkte, _firstTeamRueckspielPunkte, _secondTeamRueckspielPunkte FROM Matches WHERE id = ?";

        // Öffnen der Verbindung und vorbereiten des SQL-Statements
        try (Connection conn = connect();
            PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // Beispiel: Setze den Parameter für die ID (matchId) auf 1
            pstmt.setInt(1, matchId);

            // Ausführen der Abfrage
            try (ResultSet rs = pstmt.executeQuery()) {
                // Verarbeitung des ResultSets
                if (rs.next()) {
                    System.out.println("Hinspiel Punkte Team 1: " + rs.getInt("_firstTeamHinspielPunkte"));
                    System.out.println("Hinspiel Punkte Team 2: " + rs.getInt("_secondTeamHinspielPunkte"));
                    System.out.println("Rückspiel Punkte Team 1: " + rs.getInt("_firstTeamRueckspielPunkte"));
                    System.out.println("Rückspiel Punkte Team 2: " + rs.getInt("_secondTeamRueckspielPunkte"));
                } else {
                    System.out.println("Kein Match mit dieser ID gefunden.");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

}