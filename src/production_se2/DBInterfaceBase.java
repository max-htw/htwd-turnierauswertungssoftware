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

  abstract boolean turnierKonf_getNeedPrefillScores();

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
  abstract boolean turnierKonf_setNeedPrefillScores(boolean needPrefillScores);

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

    private int groupID;
    public int getGroupID(){ return groupID;}
    public void setGroupID(int id){ groupID = id;}

    private int team1Nr;
    public int getTeam1Nr(){return team1Nr;}
    public void setTeam1Nr(int nr){team1Nr = nr;}

    private String team1Name;
    public String getTeam1Name(){return team1Name;}
    public void setTeam1Name(String name){team1Name = name;}

    private int team1PunkteHinspiel;
    public int getTeam1PunkteHinspiel(){return  team1PunkteHinspiel;}
    public void setTeam1PunkteHinspiel(int punkte){team1PunkteHinspiel = punkte;}

    private int team1PunkteRueckspiel;
    public int getTeam1PunkteRueckspiel(){return team1PunkteRueckspiel;}
    public void setTeam1PunkteRueckspiel(int punkte){team1PunkteRueckspiel = punkte;}

    private int team2Nr;
    public int getTeam2Nr(){return team2Nr;}
    public void setTeam2Nr(int nr){team2Nr = nr;}

    private String team2Name;
    public String getTeam2Name(){return team2Name;}
    public void setTeam2Name(String name){team2Name = name;}

    private int team2PunkteHinspiel;
    public int getTeam2PunkteHinspiel(){return  team2PunkteHinspiel;}
    public void setTeam2PunkteHinspiel(int punkte){team2PunkteHinspiel = punkte;}

    private int team2PunkteRueckspiel;
    public int getTeam2PunkteRueckspiel(){return team2PunkteRueckspiel;}
    public void setTeam2PunkteRueckspiel(int punkte){team2PunkteRueckspiel = punkte;}

    private int hinspielRichterGroupID;
    public int getHinspielRichterGroupID(){return  hinspielRichterGroupID;}
    public void setHinspielRichterGroupID(int id){hinspielRichterGroupID = id;}

    private int hinspielRichterTeamID;
    public int getHinspielRichterTeamID(){return hinspielRichterTeamID;}
    public void setHinspielRichterTeamID(int id){hinspielRichterTeamID = id;}

    private int hinspielFeldNr;
    public int getHinspielFeldNr(){return hinspielFeldNr;}
    public void setHinspielFeldNr(int nr){hinspielFeldNr = nr;}

    private int hinspielTimeSlot;
    public int getHinspielTimeSlot(){return hinspielTimeSlot;}
    public void setHinspielTimeSlot(int slot){hinspielTimeSlot = slot;}

    private int rueckspielRichterGroupID;
    public int getRueckspielRichterGroupID(){return  rueckspielRichterGroupID;}
    public void setRueckspielRichterGroupID(int id){rueckspielRichterGroupID = id;}

    private int rueckspielRichterTeamID;
    public int getRueckspielRichterTeamID(){return rueckspielRichterTeamID;}
    public void setRueckspielRichterTeamID(int id){rueckspielRichterTeamID = id;}

    private int rueckspielFeldNr;
    public int getRueckspielFeldNr(){return rueckspielFeldNr;}
    public void setRueckspielFeldNr(int nr){rueckspielFeldNr = nr;}

    private int rueckspielTimeSlot;
    public int getRueckspielTimeSlot(){return rueckspielTimeSlot;}
    public void setRueckspielTimeSlot(int slot){rueckspielTimeSlot = slot;}

    @Override
    public int hashCode(){
      return groupID*100000 + team1Nr*1000 + team2Nr;
    }

    public int hashCodeRueckspiel(){
      return groupID*100000 + team2Nr*1000 + team1Nr;
    }

    public static boolean isHinspielByHash(int hash){
      //im Hash der Hinspiele ist an ersten Stelle immer der kleinere Team
      //und bei den Rueckspielen: immer der groessere
      boolean ausgabe  = ((hash % 100000)/1000) < (hash%1000);;
      return  ausgabe;
    }
  }

  public static class SpielStats{
    public boolean isHinspiel = true;
    public int groupid = -1;
    public int team1 = -1;
    public int team2 = -1;
    public int feldID = -1;
  }

  public static class TurnierKonfiguration{
    public int anzGruppen;
    public ArrayList<Integer> anzTeams;
    public boolean needRueckspiele;
  }

  public  static  class FeldSchedule  {
    public  final int feldNr;

    private ArrayList<SpielStats> spiele = new ArrayList<>();

    FeldSchedule(int fNr){
      feldNr = fNr;
    }

    public void addSpiel(SpielStats s){
      spiele.add(s);
    }

    public ArrayList<SpielStats> getSpiele(){
      return spiele;
    }
  }


}
