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
  ArrayList<TurnierMatch> getMatchesByGroupID(int groupID) {

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
  ArrayList<String> turnierKonf_getGroupNames() {
    ArrayList<String> a = new ArrayList<>();
    for(int i = 0; i < AppSettings.maxAnzGroups; i++){
      a.add("Gruppe" + i + "(ToDo: getGroupNames)");
    }
    return a;
  }

  @Override
  int turnierKonf_getAnzGruppen() {
    return AppSettings.minAnzGroups;
  }

  @Override
  ArrayList<String> turnierKonf_getTeamNamesByGroupID(int groupID) {
    ArrayList<String> a = new ArrayList<>();
    for(int i = 0; i < AppSettings.maxAnzTeams; i++){
      a.add("Team" + i + "(ToDo: getTeamNamesByGroupID)");
    }
    return a;
  }

  @Override
  int turnierKonf_getAnzTeamsByGroupID(int GroupID) {
    return 0;
  }

  @Override
  boolean turnierKonf_getNeedRueckspiele() {
    return false;
  }

  @Override
  int turnierKonf_getAnzSpielfelder() {
    return 0;
  }

  @Override
  boolean turnierKonf_getNeedPrefillScores() {
    return false;
  }

  @Override
  TurnierMatch getMatch(int groupID, int team1, int team2) {
    return null;
  }

  @Override
  SpielStats getSpielStats(int groupID, int team1, int team2, boolean isHinspiel) {
    return null;
  }

  @Override
  ArrayList<String> getTurnierArchiveNames() {
    return null;
  }

  @Override
  void loadTurnierFromArchive(int pos) {

  }

  @Override
  ArrayList<SpielStats> getFeldSchedule(int feldNr) {
    return null;
  }

  @Override
  SpielStats getSpielStatsByFeldUndTimeSlot(int feldNr, int idx) {
    return null;
  }

  @Override
  ArrayList<FeldSchedule> getTurnierPlan() {
    return null;
  }

  @Override
  int turnierKonf_getAnzTimeSlots() {
    return 20;
  }

  @Override
  boolean turnierKonf_setAnzGruppen(int anz) {
    return false;
  }

  @Override
  boolean turnierKonf_setAnzTeamsByGroupID(int groupID, int anzTeams) {
    return false;
  }

  @Override
  boolean turnierKonf_setAnzSpielfelder(int anz) {
    return false;
  }

  @Override
  boolean turnierKonf_setNeedRueckspiele(boolean needRueckspiele) {
    return false;
  }

  @Override
  boolean turnierKonf_setNeedPrefillScores(boolean needPrefillScores) {
    return false;
  }

  @Override
  boolean saveCurrentTurnierToArchive(String turnierName) {
    return false;
  }

  @Override
  public ArrayList<DBInterfaceBase.AuswertungsEintrag> calculateAuswertung(int groupID) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'calculateAuswertung'");
  }

  @Override
  boolean isTurnierPlanAktuell() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'isTurnierPlanAktuell'");
  }

  @Override
  void fillTurnierPlan(ArrayList<DBInterfaceBase.FeldSchedule> turnierPlan) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'fillTurnierPlan'");
  }

  @Override
  int turnierKonf_getTimeSlotDuration() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'turnierKonf_getTimeSlotDuration'");
  }


  @Override
  void resetKonfiguration() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'resetKonfiguration'");
  }

  @Override
  void resetMatches() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'resetMatches'");
  }

  @Override
  void match_setPunkteTeam1Hinspiel(int groupID, int team1id, int team2id, int team1Punkte) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'match_setPunkteTeam1Hinspiel'");
  }

  @Override
  void match_setPunkteTeam1Rueckspiel(int groupID, int team1id, int team2id, int team1Punkte) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'match_setPunkteTeam1Rueckspiel'");
  }

  @Override
  void match_setPunkteTeam2Hinspiel(int groupID, int team1id, int team2id, int team2Punkte) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'match_setPunkteTeam2Hinspiel'");
  }

  @Override
  void match_setPunkteTeam2Rueckspiel(int groupID, int team1id, int team2id, int team2Punkte) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'match_setPunkteTeam2Rueckspiel'");
  }

  @Override
  int turnierKonf_getTurnierStartAsMinutes() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'turnierKonf_getTurnierStartAsMinutes'");
  }

  @Override
  void turnierKonf_setTurnierStartAsMinutes(int minutes) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'turnierKonf_setTurnierStartAsMinutes'");
  }

  @Override
  void turnierKonf_setTimeSlotDuration(int minutes) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'turnierKonf_setTimeSlotDuration'");
  }

  @Override
  void turnierKonf_setAnzTimeSlots(int minutes) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'turnierKonf_setAnzTimeSlots'");
  }
}
