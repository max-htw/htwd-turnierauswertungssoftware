import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Random;

public class DBInterface_InMemory extends DBInterfaceBase{

  private static boolean _initialized = false;
  private static TurnierKonfiguration _turnierPlanKonfig = new TurnierKonfiguration();
  private static ArrayList<FeldSchedule> _turnierPlan = new ArrayList<>();
  private static HashMap<Integer, TurnierMatch> _matches = new HashMap<>();
  private static ArrayList<ArrayList<String>> _teamNames = new ArrayList<>();

  private static Random _random;

  public DBInterface_InMemory(){
    if(!_initialized) {
      _initTurnier();
    }
    _initialized = true;
  }

  private void _initTurnier(){
    

      if(_anzTeamsProGruppe.size() < _anzGruppen) {
        _anzTeamsProGruppe = new ArrayList<>();
        for(int i=0; i<_anzGruppen; i++){
          _anzTeamsProGruppe.add(AppSettings.minAnzTeams);
        }
      }

      if(!(_groupNames.size() > 0)){
        _groupNames = new ArrayList<>();
      }
      if(!(_teamNames.size() > 0)){
        _teamNames = new ArrayList<>();
      }
      if(!(_anzTeamsProGruppe.size() > 0)){
        _anzTeamsProGruppe = new ArrayList<>();
      }
      for(int i = 0; i<_anzGruppen; i++){
        if(_groupNames.size() < i+1) {
          _groupNames.add("Gruppe" + i + "(ToDo: _initTurnier())");
        }
        if(_anzTeamsProGruppe.size() < i+1){
          _anzTeamsProGruppe.add(AppSettings.minAnzTeams);
        }
        if(_teamNames.size() < i+1){
          _teamNames.add(new ArrayList<String>());
        }
        if(_teamNames.get(i).size() < _anzTeamsProGruppe.get(i)){
          for(int j = _teamNames.get(i).size(); j < _anzTeamsProGruppe.get(i); j++){
            _teamNames.get(i).add("Team" + (char)('A' + j) + "(ToDo: _initTurnier())");
          }
        }

      }

      _initializeMatches();
    
  }

  private void  _initializeMatches(){
    if(_random==null) _random = new Random();

    _matches.clear();
    for(int g = 0; g < _anzGruppen; g++){
      for(int t1 = 0; t1 < _anzTeamsProGruppe.get(g); t1++){
        for(int t2 = t1+1; t2< _anzTeamsProGruppe.get(g); t2++){
          TurnierMatch m = new TurnierMatch(g, t1, t2);

          //START: Ausfuellen mit zufaelligen Spielpunkten zu Debugging Zwecken:
          if(turnierKonf_getNeedPrefillScores()){
            //meanings: 0 = not played yet, 1 = team 1 winner, 2 = team 2 winner;
            int resHin = _random.nextInt(3);
            if (resHin == 0) {
              m.setTeam1PunkteHinspiel(-1);
              m.setTeam2PunkteHinspiel(-1);
            } else if (resHin == 1) {
              m.setTeam1PunkteHinspiel(25);
              m.setTeam2PunkteHinspiel(_random.nextInt(23));
            } else {
              m.setTeam1PunkteHinspiel(_random.nextInt(23));
              m.setTeam2PunkteHinspiel(25);
            }
            if(turnierKonf_getNeedRueckspiele()) {
              int resRueck = _random.nextInt(3);
              if (resRueck == 0) {
                m.setTeam1PunkteRueckspiel(-1);
                m.setTeam2PunkteRueckspiel(-1);
              } else if (resRueck == 1) {
                m.setTeam1PunkteRueckspiel(25);
                m.setTeam2PunkteRueckspiel(_random.nextInt(23));
              } else {
                m.setTeam1PunkteRueckspiel(_random.nextInt(23));
                m.setTeam2PunkteRueckspiel(25);
              }
            }
          }
          //Ende: Ausfuellen mit zufaelligen Spielpunkten

          _matches.put(m.hashCode(), m);
        }
      }
    }
    //fillTurnierPlan();
  }

  private void fillTurnierPlan(){
    _turnierPlan.clear();
    for(int i = 0; i< turnierKonf_getAnzSpielfelder(); i++){
      _turnierPlan.add(new FeldSchedule(i));
    }
    HashSet<Integer> matchHashesHS = new HashSet<>();
    ArrayList<Integer> matchHashesArr = new ArrayList<>();
    for(int groupID = 0; groupID < turnierKonf_getAnzGruppen(); groupID ++){
      for(int t1ID = 0; t1ID < turnierKonf_getAnzTeamsByGroupID(groupID); t1ID++){
        for(int t2ID = t1ID + 1; t2ID < turnierKonf_getAnzTeamsByGroupID(groupID); t2ID++){
          TurnierMatch m = new TurnierMatch(groupID,t1ID, t2ID);
          int hc = m.hashCode();
          matchHashesHS.add(hc); //hashcode des Hinspiels
          matchHashesArr.add(hc);
          if(turnierKonf_getNeedRueckspiele()){
            int hcr = m.hashCodeRueckspiel();
            matchHashesHS.add(hcr);
            matchHashesArr.add(hcr);
          }
        }
      }
    }

    int timeslotCnt = 0;
    int watchdog = 10000;
    while (matchHashesHS.size() > 0){
      watchdog--;
      if(watchdog == 0) {
        throw new RuntimeException("watchdog in fillTurnierplan");
      }

      int mh = 0;

      //erster integer: groupID, zweiter: teamID
      HashSet<MyHelpers.IntPair> teamsAlreadyInTimeslot = new HashSet<>();

      //Auf jedem Feld fuer den Timeslot ein Match suchen, der gespielt werden kann
      //und auch einen Richter finden
      for(int f = 0; f < turnierKonf_getAnzSpielfelder(); f++){
        boolean needEmptyMatch = true;
        for(int i = matchHashesArr.size() - 1; i >= 0 ; i--){
          int h = matchHashesArr.get(i);
          if(!matchHashesHS.contains(h)){
            matchHashesArr.remove(i);
            continue;
          }
          boolean isHinspiel = TurnierMatch.isHinspielByHash(h);
          int g = groupIdFromHash(h);
          int t1 = team1FromHash(h);
          int t2 = team2FromHash(h);

          // ob ein der beiden match-Teams im selben timeslot shon auf einem vorherigen Feld spielen.
          boolean shonVorhanden =
            teamsAlreadyInTimeslot.contains(new MyHelpers.IntPair(g,t1)) ||
              teamsAlreadyInTimeslot.contains(new MyHelpers.IntPair(g,t2));


          if(shonVorhanden) continue;

          teamsAlreadyInTimeslot.add(new MyHelpers.IntPair(g, t1));
          teamsAlreadyInTimeslot.add(new MyHelpers.IntPair(g, t2));

          needEmptyMatch = false;
          //the match h can be added to the field,
          SpielStats s = new SpielStats();
          s.groupid = g;
          s.team1 = t1;
          s.team2 = t2;
          s.feldID = f;
          s.isHinspiel = isHinspiel;

          _turnierPlan.get(f).addSpiel(s);

          //now the richter has to be found:
          for(int nr = 0; nr < turnierKonf_getAnzTeamsByGroupID(g); nr++){
            if(teamsAlreadyInTimeslot.contains(new MyHelpers.IntPair(g,nr))){
              //das Team kann nicht pfeifen, da es schon an einem anderen Feld beschaeftigt ist
              continue;
            }
            else{
              int gr = groupIdFromHash(h);
              int t1Id = team1FromHash(h);
              int t2Id = team2FromHash(h);
              TurnierMatch m = getMatch(gr,t1Id,t2Id);
              if(isHinspiel) {
                m.setHinspielRichterGroupID(g);
                m.setHinspielRichterTeamID(nr);
                m.setHinspielFeldNr(f);
                m.setHinspielTimeSlot(timeslotCnt);
              }
              else {
                m.setRueckspielRichterGroupID(g);
                m.setRueckspielRichterTeamID(nr);
                m.setRueckspielFeldNr(f);
                m.setRueckspielTimeSlot(timeslotCnt);
              }
              teamsAlreadyInTimeslot.add(new MyHelpers.IntPair(g, nr));

              break;
            }
          }
          matchHashesHS.remove(h);
          break;
        }
        if(needEmptyMatch){
          _turnierPlan.get(f).addSpiel(new SpielStats());
        }
      }
      timeslotCnt += 1;
    }
    int dummy = 5;

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


  private static int _anzGruppen = AppSettings.minAnzGroups;
  @Override
  int turnierKonf_getAnzGruppen() {
    return _anzGruppen;
  }

  private  static ArrayList<String> _groupNames = new ArrayList<>();
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

  private static ArrayList<Integer> _anzTeamsProGruppe = new ArrayList<>();
  @Override
  int turnierKonf_getAnzTeamsByGroupID(int GroupID) {
    if(_anzTeamsProGruppe.size() <= GroupID){
      throw new IllegalArgumentException();
    }
    else {
      return _anzTeamsProGruppe.get(GroupID);
    }
  }

  private static boolean _needRueckspiele = true;
  @Override
  boolean turnierKonf_getNeedRueckspiele() {
    return _needRueckspiele;
  }

  private static int _anzSpielfelder = AppSettings.minAnzSpielfelder;
  @Override
  int turnierKonf_getAnzSpielfelder() {
    return _anzSpielfelder;
  }

  private static boolean _needPrefillScores = true;
  @Override
  boolean turnierKonf_getNeedPrefillScores() {
    return _needPrefillScores;
  }

  @Override
  ArrayList<TurnierMatch> getMatchesByGroupID(int groupID) {
    ArrayList<TurnierMatch> a = new ArrayList<>();
    for(int h : _matches.keySet()){
      if(groupIdFromHash(h) == groupID){
        a.add(_matches.get(h));
      }
    }
    return a;
  }

  @Override
  TurnierMatch getMatch(int groupID, int team1, int team2) {
    TurnierMatch m = new TurnierMatch(groupID, team1, team2);
    return  _matches.get(m.hashCode());
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
    ArrayList<SpielStats> a = null;
    if(feldNr < _turnierPlan.size()){
      a = _turnierPlan.get(feldNr).getSpiele();
    }
    return a;
  }

  @Override
  SpielStats getSpielStatsByFeldUndTimeSlot(int feldNr, int idx) {
    return null;
  }

  @Override
  ArrayList<FeldSchedule> getTurnierPlan() {
    if(_turnierPlan.isEmpty() || !_initialized){
      _initTurnier();
    }
    return _turnierPlan;
  }

  @Override
  int turnierKonf_getAnzTimeSlots() {
    return 1000;
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
        _groupNames.add("Gruppe" + i + "(ToDo: setAnzGruppen())");
        _anzTeamsProGruppe.add(AppSettings.minAnzTeams);
        _teamNames.add(new ArrayList<String>());
        for(int j = 0; j < AppSettings.minAnzTeams; j++){
          _teamNames.get(i).add("Team" + (char)('A' + j) + "(ToDo: setAnzGruppen())");
        }
      }
    _initialized = false;
    return true;
  }

  @Override
  boolean turnierKonf_setAnzTeamsByGroupID(int groupID, int anzTeams) {
    int g = turnierKonf_getAnzGruppen();
    if(groupID < 0 || groupID >= g || anzTeams < AppSettings.minAnzTeams || anzTeams > AppSettings.maxAnzTeams){
      throw new IllegalArgumentException();
    }
    _anzTeamsProGruppe.set(groupID, anzTeams);
    _teamNames.set(groupID, new ArrayList<>());
    for(int j = 0; j < anzTeams; j++){
      _teamNames.get(groupID).add("Team" + (char)('A' + j) + "(ToDo: _setAnzTeamsByGroupID())");
    }
    _initialized = false;
    return true;
  }

  @Override
  boolean turnierKonf_setAnzSpielfelder(int anz) {
    _anzSpielfelder = anz>AppSettings.maxAnzSpielfelder? AppSettings.maxAnzSpielfelder : anz;
    _initialized = false;
    return true;
  }

  @Override
  boolean turnierKonf_setNeedRueckspiele(boolean needRueckspiele) {
    if(needRueckspiele != _needRueckspiele){
      _initialized = false;
    }
    _needRueckspiele = needRueckspiele;
    return true;
  }

  @Override
  boolean turnierKonf_setNeedPrefillScores(boolean needPrefillScores) {
    _needPrefillScores = needPrefillScores;
    return true;
  }

  @Override
  void reset() {
    _initTurnier();
  }

  @Override 
  public ArrayList<AuswertungsEintrag> calculateAuswertung(int groupID){
        ArrayList<AuswertungsEintrag> ausgabe = new ArrayList<>();
        for(int i=0; i<turnierKonf_getAnzTeamsByGroupID(groupID);i++) {

            //newEintrag.score.x - Anzahl der Siege des Teams
            //newEintrag.score.y - Summe der verdienten Punkte in allen Spielen.
            AuswertungsEintrag newEintrag = new AuswertungsEintrag(i);
            for (int j=0; j<turnierKonf_getAnzTeamsByGroupID(groupID);j++) {
                if(j == i) continue;

                TurnierMatch m = this.getMatch(groupID, i, j);
                int teamHinspielPunkte = 0;
                int gegnerHinspielPunkte = 0;
                int teamRueckspielPunkte = 0;
                int gegnerRueckspielPunkte = 0;

                if(i == m.getTeam1Nr()){
                    teamHinspielPunkte = m.getTeam1PunkteHinspiel() >= 0 ? m.getTeam1PunkteHinspiel():-1;
                    gegnerHinspielPunkte = m.getTeam2PunkteHinspiel() >=0 ? m.getTeam2PunkteHinspiel():-1;
                    teamRueckspielPunkte = m.getTeam1PunkteRueckspiel() >=0 ? m.getTeam1PunkteRueckspiel():-1;
                    gegnerRueckspielPunkte = m.getTeam2PunkteRueckspiel() >=0 ? m.getTeam2PunkteRueckspiel():-1;
                }
                else{
                    gegnerHinspielPunkte = m.getTeam1PunkteHinspiel() >= 0 ? m.getTeam1PunkteHinspiel():-1;
                    teamHinspielPunkte = m.getTeam2PunkteHinspiel() >=0 ? m.getTeam2PunkteHinspiel():-1;
                    gegnerRueckspielPunkte = m.getTeam1PunkteRueckspiel() >=0 ? m.getTeam1PunkteRueckspiel():-1;
                    teamRueckspielPunkte = m.getTeam2PunkteRueckspiel() >=0 ? m.getTeam2PunkteRueckspiel():-1;
                }

                if(teamHinspielPunkte >= 0 && gegnerHinspielPunkte >= 0){
                    newEintrag.anzGespielt += 1;
                    if (teamHinspielPunkte > gegnerHinspielPunkte){
                        newEintrag.anzGewonnen+=1;
                    }
                    newEintrag.pktDifferenz += teamHinspielPunkte - gegnerHinspielPunkte;
                }
                if(this.turnierKonf_getNeedRueckspiele()) {
                    if (teamRueckspielPunkte >= 0 && gegnerRueckspielPunkte >= 0) {
                        newEintrag.anzGespielt += 1;
                        if (teamRueckspielPunkte > gegnerRueckspielPunkte){
                            newEintrag.anzGewonnen += 1;
                        }
                        newEintrag.pktDifferenz += teamRueckspielPunkte - gegnerRueckspielPunkte;
                    }
                }
            }
            ausgabe.add(newEintrag);
        }
        ausgabe.sort(Collections.reverseOrder());
        return  ausgabe;
    }

  @Override
  boolean isTurnierPlanAktuell() {
    boolean a = _turnierPlanKonfig.needRueckspiele == turnierKonf_getNeedRueckspiele();
    a = a && _turnierPlanKonfig.anzahlSpielfelder == turnierKonf_getAnzSpielfelder();
    a = a && _turnierPlanKonfig.anzTeamsJedeGruppe.size() == turnierKonf_getAnzGruppen();
    if(a){
      for(int i = 0; i < turnierKonf_getAnzGruppen(); i++){
        a = a && _turnierPlanKonfig.anzTeamsJedeGruppe.get(i) == turnierKonf_getAnzTeamsByGroupID(i);
      }
    }

    return a;
  }

  @Override
  void fillTurnierPlan(ArrayList<DBInterfaceBase.FeldSchedule> turnierPlan) {
    _turnierPlan = turnierPlan;

    _turnierPlanKonfig.anzTeamsJedeGruppe.clear();
    for(int i = 0; i<_anzTeamsProGruppe.size(); i++){
      _turnierPlanKonfig.anzTeamsJedeGruppe.add(_anzTeamsProGruppe.get(i));
    }
    _turnierPlanKonfig.anzahlSpielfelder = turnierKonf_getAnzSpielfelder();
    _turnierPlanKonfig.needRueckspiele = turnierKonf_getNeedRueckspiele();
  }

}
