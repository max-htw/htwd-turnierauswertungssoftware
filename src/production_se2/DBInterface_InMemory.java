import java.util.ArrayList;
import java.util.HashMap;

public class DBInterface_InMemory extends DBInterfaceBase{

  private static boolean _initialized = false;
  private static ArrayList<FeldSchedule> _turnierPlan = new ArrayList<>();
  private static HashMap<Integer, TurnierMatch> _matches = new HashMap<>();
  private static ArrayList<ArrayList<String>> _teamNames = new ArrayList<>();

  public DBInterface_InMemory(){
    _initTurnier();
  }

  private void _initTurnier(){
    if(!_initialized) {
      _anzGruppen = AppSettings.minAnzGroups;
      _groupNames = new ArrayList<String>();
      _anzTeamsProGruppe = new ArrayList<>();
      _teamNames = new ArrayList<>();
      for(int i = 0; i<_anzGruppen; i++){
        _groupNames.add("Gruppe" + i + "(ToDo: _initTurnier())");
        _anzTeamsProGruppe.add(AppSettings.minAnzTeams);
        _teamNames.add(new ArrayList<String>());
        for(int j = 0; j < AppSettings.minAnzTeams; j++){
          _teamNames.get(i).add("Team" + j + "(ToDo: _initTurnier())");
        }
      }
      _needRueckspiele = true;
      _anzSpielfelder = AppSettings.minAnzSpielfelder;
      _turnierPlan = new ArrayList<>();
      for(int i=0; i<_anzSpielfelder; i++){
        _turnierPlan.add(new FeldSchedule(i));
      }
      _initializeMatches();
    }
    _initialized = true;
  }

  private static void  _initializeMatches(){
    _matches.clear();
    for(int g = 0; g < _anzGruppen; g++){
      for(int t1 = 0; t1 < _anzTeamsProGruppe.get(g); t1++){
        for(int t2 = t1+1; t2< _anzTeamsProGruppe.get(g); t2++){
          TurnierMatch m = new TurnierMatch(g, t1, t2);
          _matches.put(m.hashCode(), m);
        }
      }
    }
  }

  public static int groupIdFromHash(int hashCode){
    return hashCode / 100000;
  }

  public static int team1FromHash(int hashCode){
    return (hashCode % 100000) / 1000;
  }

  public static int team2FromHash(int hashCode){
    return hashCode % 1000;
  }


  private static int _anzGruppen;
  @Override
  int turnierKonf_getAnzGruppen() {
    return _anzGruppen;
  }

  private  static ArrayList<String> _groupNames;
  @Override
  ArrayList<String> turnierKonf_getGroupNames() {
    if(_groupNames == null) {
      _groupNames = new ArrayList<>();
    }
    return _groupNames;
  }

  @Override
  ArrayList<String> turnierKonf_getTeamNamesByGroupID(int groupID) {
    if(_teamNames == null){
      _teamNames = new ArrayList<>();
    }
    else if(_teamNames.size() < groupID - 1){
      return new ArrayList<String>();
    }
    else if(_teamNames.get(groupID) == null){
      _teamNames.set(groupID, new ArrayList<String>());
    }

    return _teamNames.get(groupID);
  }

  private static ArrayList<Integer> _anzTeamsProGruppe;
  @Override
  int turnierKonf_getAnzTeamsByGroupID(int GroupID) {
    return _anzTeamsProGruppe.get(GroupID);
  }

  private static boolean _needRueckspiele;
  @Override
  boolean turnierKonf_getNeedRueckspiele() {
    return _needRueckspiele;
  }

  private static int _anzSpielfelder;
  @Override
  int turnierKonf_getAnzSpielfelder() {
    return _anzSpielfelder;
  }

  @Override
  ArrayList<TurnierMatch> getMatchesByGroupID(int groupID) {
    return new ArrayList<TurnierMatch>();
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
  boolean saveCurrentTurnierToArchive(String turnierName) {
    return false;
  }

  @Override
  ArrayList<String> getTurnierArchiveNames() {
    return new ArrayList<>();
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
  ArrayList<String> getTimeSlotsStrings() {
    ArrayList<String> a = new ArrayList<>();

    for(int i = 0; i < (turnierKonf_getAnzTimeSlots() / 2); i++){
      StringBuilder s = new StringBuilder();
      s.append(6+i);
      s.append(":00 - ");
      s.append(6+i);
      s.append(":30");
      a.add(s.toString());
      s = new StringBuilder();
      s.append(6+i);
      s.append(":30 - ");
      s.append(6+i+1);
      s.append(":00");
      a.add(s.toString());
    }
    return a;
  }
  @Override
  boolean turnierKonf_setAnzGruppen(int anz) {
    if(anz < AppSettings.minAnzGroups || anz > AppSettings.maxAnzGroups){
      throw new IllegalArgumentException();
    }
      _anzGruppen = anz;
      _groupNames = new ArrayList<String>();
      _anzTeamsProGruppe = new ArrayList<>();
      _teamNames = new ArrayList<>();
      for(int i = 0; i<_anzGruppen; i++){
        _groupNames.add("Gruppe" + i + "(ToDo: _initTurnier())");
        _anzTeamsProGruppe.add(AppSettings.minAnzTeams);
        _teamNames.add(new ArrayList<String>());
        for(int j = 0; j < AppSettings.minAnzTeams; j++){
          _teamNames.get(i).add("Team" + j + "(ToDo: _initTurnier())");
        }
      }
    return true;
  }

  @Override
  boolean turnierKonf_setAnzTeamsByGroupID(int groupID, int anzTeams) {
    int g = turnierKonf_getAnzGruppen();
    if(groupID < 0 || groupID >= g || anzTeams < 0 || anzTeams >= AppSettings.maxAnzTeams){
      throw new IllegalArgumentException();
    }
    _anzTeamsProGruppe.set(groupID, anzTeams);
    _teamNames.set(groupID, new ArrayList<>());
    for(int j = 0; j < anzTeams; j++){
      _teamNames.get(groupID).add("Team" + j + "(ToDo: _setAnzTeamsByGroupID())");
    }
    return true;
  }

  @Override
  boolean turnierKonf_setAnzSpielfelder(int anz) {
    return false;
  }

  @Override
  boolean turnierKonf_setNeedRueckspiele(boolean needRueckspiele) {
    _needRueckspiele = needRueckspiele;
    return true;
  }

  @Override
  void reset() {
    _initTurnier();
  }
}
