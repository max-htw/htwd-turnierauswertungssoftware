import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DBInterface_SQLite extends DBInterfaceBase{

  public DBInterface_SQLite(){
    //Datenbank initialisieren, wenn noch nicht initialisiert
    String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='{" +
      TurnierMatch.class.getName() + "}'";

    // Öffnen der Verbindung und vorbereiten des SQL-Statements
    try (Connection conn = connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      // Ausführen der Abfrage
      try (ResultSet rs = pstmt.executeQuery()) {
        //TODO: pruefen ob die Tabelle existiert und eventuell neu anlegen
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public static Connection connect() {
    // Relativer Pfad zur Datenbank
    String dbPath = "db/sqlite.db";
    String url = "jdbc:sqlite:" + dbPath;
    Connection c = null;

    try{
      // Verbindung aufbauen
      //Class.forName("org.sqlite.JDBC");
      c = DriverManager.getConnection(url);
      if(c != null){
        //var meta = c.getMetaData();
        //System.out.println("The driver name is " + meta.getDriverName());
        //System.out.println("A new database has been created.");
      }
    } catch (SQLException e) {
      System.out.println("Verbindungsfehler: " + e.getMessage());
      return null;
    }
    /*
    catch (ClassNotFoundException e){
      System.out.println("Verbindungsfehler: " + e.getMessage());
      return null;
    }
    */

    return c;
  }

  private TurnierMatch recordSet_To_TurnierMatch(ResultSet rs) throws SQLException {

    int grID = rs.getInt("group_id");
    int t1 = rs.getInt("first_team");
    int t2 = rs.getInt("second_team");

    TurnierMatch tm = new TurnierMatch(grID,t1, t2);

    tm.setTeam1PunkteHinspiel(rs.getInt("_firstTeamHinspielPunkte"));
    tm.setTeam2PunkteHinspiel(rs.getInt("_secondTeamHinspielPunkte"));
    tm.setTeam1PunkteRueckspiel(rs.getInt("_firstTeamRueckspielPunkte"));
    tm.setTeam2PunkteRueckspiel(rs.getInt("_firstTeamRueckspielPunkte"));
    tm.setHinspielRichterGroupID(grID);
    tm.setRueckspielRichterGroupID(grID);
    tm.setHinspielRichterTeamID(rs.getInt("_richterHinspiel"));
    tm.setRueckspielRichterTeamID(rs.getInt("_richterRueckspiel"));
    tm.setHinspielFeldNr(rs.getInt("_feldNrHinspiel"));
    tm.setRueckspielFeldNr(rs.getInt("_feldNrRueckspiel"));
    tm.setHinspielTimeSlot(rs.getInt("_timeslotHinspiel"));
    tm.setRueckspielTimeSlot(rs.getInt("_timeslotRueckspiel"));

    return tm;
  }

  @Override
  public ArrayList<TurnierMatch> getMatchesByGroupID(int groupID) {

    ArrayList<TurnierMatch> a = new ArrayList<>();

    String sql = "SELECT " +
      "_firstTeamHinspielPunkte, " +
      "_secondTeamHinspielPunkte, " +
      "_firstTeamRueckspielPunkte, " +
      "_secondTeamRueckspielPunkte " +
      "FROM Matches WHERE group_id = ?";

    // Öffnen der Verbindung und vorbereiten des SQL-Statements
    try (Connection conn = connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {

      // Beispiel: Setze den Parameter für die ID (matchId) auf 1
      pstmt.setInt(1, groupID);

      // Ausführen der Abfrage
      try (ResultSet rs = pstmt.executeQuery()) {
        // Verarbeitung des ResultSets
        while (rs.next()) {

          TurnierMatch tm = recordSet_To_TurnierMatch(rs);
          a.add(tm);

        }
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }

    return a;
  }


  @Override
  public ArrayList<String> turnierKonf_getGroupNames() {
    ArrayList<String> a = new ArrayList<>();
    for(int i = 0; i < AppSettings.maxAnzGroups; i++){
      a.add("Gruppe" + i + "(ToDo: getGroupNames)");
    }
    return a;
  }

  @Override
  public int turnierKonf_getAnzGruppen() {
    return AppSettings.minAnzGroups;
  }

  @Override
  public ArrayList<String> turnierKonf_getTeamNamesByGroupID(int groupID) {
    ArrayList<String> a = new ArrayList<>();
    for(int i = 0; i < AppSettings.maxAnzTeams; i++){
      a.add("Team" + i + "(ToDo: getTeamNamesByGroupID)");
    }
    return a;
  }

  @Override
  public int turnierKonf_getAnzTeamsByGroupID(int GroupID) {
    return 0;
  }

  @Override
  public boolean turnierKonf_getNeedRueckspiele() {
    return false;
  }

  @Override
  public int turnierKonf_getAnzSpielfelder() {
    return 0;
  }

  @Override
  public boolean turnierKonf_getNeedPrefillScores() {
    return false;
  }

  @Override
  public TurnierMatch getMatch(int groupID, int team1, int team2) {
    return null;
  }

  @Override
  public SpielStats getSpielStats(int groupID, int team1, int team2, boolean isHinspiel) {
    return null;
  }

  @Override
  public ArrayList<String> getTurnierArchiveNames() {
    return null;
  }

  @Override
  public void loadTurnierFromArchive(int pos) {

  }

  @Override
  public void loadTurnierFromArchive(String turnierName){
    
  }

  @Override
  public ArrayList<SpielStats> getFeldSchedule(int feldNr) {
    return null;
  }

  @Override
  public SpielStats getSpielStatsByFeldUndTimeSlot(int feldNr, int idx) {
    return null;
  }

  @Override
  public ArrayList<FeldSchedule> getTurnierPlan() {
    return null;
  }

  @Override
  public int turnierKonf_getAnzTimeSlots() {
    return 20;
  }

  @Override
  public boolean turnierKonf_setAnzGruppen(int anz) {
    return false;
  }

  @Override
  public boolean turnierKonf_setAnzTeamsByGroupID(int groupID, int anzTeams) {
    return false;
  }

  @Override
  public boolean turnierKonf_setAnzSpielfelder(int anz) {
    return false;
  }

  @Override
  public boolean turnierKonf_setNeedRueckspiele(boolean needRueckspiele) {
    return false;
  }

  @Override
  public boolean turnierKonf_setNeedPrefillScores(boolean needPrefillScores) {
    return false;
  }

  @Override
  public boolean saveCurrentTurnierToArchive(String turnierName) {
    return false;
  }

  @Override
  public ArrayList<DBInterfaceBase.AuswertungsEintrag> calculateAuswertung(int groupID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'calculateAuswertung'");
  }

  @Override
  public boolean isTurnierPlanAktuell() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isTurnierPlanAktuell'");
  }

  @Override
  public void fillTurnierPlan(ArrayList<DBInterfaceBase.FeldSchedule> turnierPlan) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'fillTurnierPlan'");
  }

  @Override
  public int turnierKonf_getTimeSlotDuration() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'turnierKonf_getTimeSlotDuration'");
  }


  @Override
  public void resetKonfiguration() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'resetKonfiguration'");
  }

  @Override
  public void resetMatches() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'resetMatches'");
  }

  @Override
  public void match_setPunkteTeam1Hinspiel(int groupID, int team1id, int team2id, int team1Punkte) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'match_setPunkteTeam1Hinspiel'");
  }

  @Override
  public void match_setPunkteTeam1Rueckspiel(int groupID, int team1id, int team2id, int team1Punkte) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'match_setPunkteTeam1Rueckspiel'");
  }

  @Override
  public void match_setPunkteTeam2Hinspiel(int groupID, int team1id, int team2id, int team2Punkte) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'match_setPunkteTeam2Hinspiel'");
  }

  @Override
  public void match_setPunkteTeam2Rueckspiel(int groupID, int team1id, int team2id, int team2Punkte) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'match_setPunkteTeam2Rueckspiel'");
  }

  @Override
  public int turnierKonf_getTurnierStartAsMinutes() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'turnierKonf_getTurnierStartAsMinutes'");
  }

  @Override
  public void turnierKonf_setTurnierStartAsMinutes(int minutes) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'turnierKonf_setTurnierStartAsMinutes'");
  }

  @Override
  public void turnierKonf_setTimeSlotDuration(int minutes) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'turnierKonf_setTimeSlotDuration'");
  }

  @Override
  public void turnierKonf_setAnzTimeSlots(int minutes) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'turnierKonf_setAnzTimeSlots'");
  }

  @Override
  public void reset() {
      // TODO: Hier die Logik zum Zurücksetzen der SQLite-Daten implementieren
      // Zum Beispiel: Datenbanktabellen leeren oder auf Anfangszustand setzen
  }

  @Override
  public void updateMatch(DBInterfaceBase.TurnierMatch match) {
      Connection conn = connect(); // wir nutzen dein method connect()

      if (conn == null) {
          System.out.println("Keine Verbindung zur Datenbank möglich.");
          return;
      }

      try {
          String sql = "UPDATE turnier_match SET " +
                      "team1PunkteHinspiel = ?, " +
                      "team2PunkteHinspiel = ?, " +
                      "team1PunkteRueckspiel = ?, " +
                      "team2PunkteRueckspiel = ? " +
                      "WHERE groupID = ? AND team1Nr = ? AND team2Nr = ?";

          PreparedStatement pstmt = conn.prepareStatement(sql);
          pstmt.setInt(1, match.getTeam1PunkteHinspiel());
          pstmt.setInt(2, match.getTeam2PunkteHinspiel());
          pstmt.setInt(3, match.getTeam1PunkteRueckspiel());
          pstmt.setInt(4, match.getTeam2PunkteRueckspiel());
          pstmt.setInt(5, match.getGroupID());
          pstmt.setInt(6, match.getTeam1Nr());
          pstmt.setInt(7, match.getTeam2Nr());

          pstmt.executeUpdate();
          pstmt.close();
          conn.close();
      } catch (SQLException e) {
          System.out.println("Fehler beim Update des Matches: " + e.getMessage());
      }
  }

}

