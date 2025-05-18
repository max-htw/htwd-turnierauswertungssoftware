import java.util.ArrayList;

public abstract class DBInterfaceBase {

  //Datenabfragen

  abstract void reset();

  abstract ArrayList <String> turnierKonf_getGroupNames(); // index = groupID -> groupName
  abstract int turnierKonf_getAnzGruppen();

  abstract ArrayList<String> turnierKonf_getTeamNamesByGroupID(int groupID); // <teamNr, teamName>
  abstract int turnierKonf_getAnzTeamsByGroupID(int GroupID);

  abstract boolean turnierKonf_getNeedRueckspiele();
  abstract int turnierKonf_getAnzSpielfelder();

  abstract ArrayList<TurnierMatch> getMatchesByGroupID(int groupID);
  abstract TurnierMatch getMatch(int groupID, int team1, int team2);

  abstract SpielStats getSpielStats(int groupID, int team1, int team2, boolean isHinspiel);

  abstract ArrayList<String> getTurnierArchiveNames();
  abstract void loadTurnierFromArchive(int pos);

  abstract ArrayList<SpielStats> getFeldSchedule(int feldNr);
  abstract SpielStats getSpielStatsByFeldUndTimeSlot(int feldNr, int idx);

  abstract ArrayList<FeldSchedule> getTurnierPlan();

  abstract int turnierKonf_getAnzTimeSlots();
  abstract ArrayList<String> getTimeSlotsStrings();

  //Datenmanipulationen

  abstract boolean turnierKonf_setAnzGruppen(int anz);
  abstract boolean turnierKonf_setAnzTeamsByGroupID(int groupID, int anzTeams);
  abstract boolean turnierKonf_setAnzSpielfelder(int anz);
  abstract boolean turnierKonf_setNeedRueckspiele(boolean needRueckspiele);

  abstract boolean saveCurrentTurnierToArchive(String turnierName);


  //Classen

  public static class TurnierMatch{

    public TurnierMatch(int group, int team1, int team2){
      groupID = group;
      if(team2 < team1){
        team2Nr = team1;
        team1Nr = team2;
      }
      else {
        team1Nr = team1;
        team2Nr = team2;
      }
    }

    int groupID;

    int team1Nr;
    String team1Name;
    int team1PunkteHinspiel;
    int team1PunkteRueckspiel;

    int team2Nr;
    String team2Name;
    int team2PunkteHinspiel;
    int team2PunkteRueckspiel;

    int hinspielRichterGroupID;
    int hinspielRichterTeamID;
    int hinspielFeldNr;
    int hinspielTimeSlot;

    int rueckspielRichterGroupID;
    int rueckspielRichterTeamID;
    int rueckspielFeldNr;
    int rueckspielTimeSlot;

    @Override
    public int hashCode(){
      return groupID*100000 + team1Nr*1000 + team2Nr;
    }
  }

  public static class SpielStats{
    public boolean isHinspiel = true;
    public int groupid = -1;
    public int team1 = -1;
    public int team2 = -1;
    public int team1Punkte = 0;
    public int team2Punkte = 0;
    public int feldID = -1;
    public int richterGroupID = -1;
    public int richterTeamID = -1;
  }

  public static class TurnierKonfiguration{
    public int anzGruppen;
    public ArrayList<Integer> anzTeams;
    public boolean needRueckspiele;
  }

  public  static  class FeldSchedule  {
    public  final int feldNr;

    public ArrayList<SpielStats> spiele = new ArrayList<>();

    FeldSchedule(int fNr){
      feldNr = fNr;
    }
  }


}
