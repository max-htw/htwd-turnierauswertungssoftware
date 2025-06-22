import java.util.ArrayList;

public abstract class DBInterfaceBase {

  //Datenabfragen

  //die zwei Funktionen werden hauptsaechlich in Tests verwendet.
  abstract void resetKonfiguration();
  abstract void resetMatches();

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

  abstract int turnierKonf_getTurnierStartAsMinutes();
  abstract int turnierKonf_getTimeSlotDuration();
  abstract int turnierKonf_getAnzTimeSlots();

  public String getTimeSlotString(int timeSlotNr) {
    if(timeSlotNr < 0){return "--:--";}

    int minutes = turnierKonf_getTurnierStartAsMinutes();
    minutes += turnierKonf_getTimeSlotDuration() * timeSlotNr;

    int h = minutes / 60;
    int m = minutes % 60;

    String a = String.format("%02d:%02d", h, m);
    return a;
  }


  abstract boolean isTurnierPlanAktuell();
  abstract void fillTurnierPlan(ArrayList<FeldSchedule> turnierPlan);

  abstract public ArrayList<AuswertungsEintrag> calculateAuswertung(int groupID);

  //Datenmanipulationen

  abstract boolean turnierKonf_setAnzGruppen(int anz);
  abstract boolean turnierKonf_setAnzTeamsByGroupID(int groupID, int anzTeams);
  abstract boolean turnierKonf_setAnzSpielfelder(int anz);
  abstract boolean turnierKonf_setNeedRueckspiele(boolean needRueckspiele);
  abstract boolean turnierKonf_setNeedPrefillScores(boolean needPrefillScores);

  abstract void turnierKonf_setTurnierStartAsMinutes(int minutes);
  abstract void turnierKonf_setTimeSlotDuration(int minutes);
  abstract void turnierKonf_setAnzTimeSlots(int anz);

  abstract void match_setPunkteTeam1Hinspiel(int groupID, int team1ID, int team2ID, int team1Punkte);
  abstract void match_setPunkteTeam1Rueckspiel(int groupID, int team1ID, int team2ID, int team1Punkte);

  abstract void match_setPunkteTeam2Hinspiel(int groupID, int team1ID, int team2ID, int team2Punkte);
  abstract void match_setPunkteTeam2Rueckspiel(int groupID, int team1ID, int team2ID, int team2Punkte);

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

    private int groupID = -1;
    public int getGroupID(){ return groupID;}
    public void setGroupID(int id){ groupID = id;}

    private int team1Nr = -1;
    public int getTeam1Nr(){return team1Nr;}
    public void setTeam1Nr(int nr){team1Nr = nr;}

    private String team1Name;
    public String getTeam1Name(){return team1Name;}
    public void setTeam1Name(String name){team1Name = name;}

    private int team1PunkteHinspiel = -1;
    public int getTeam1PunkteHinspiel(){return  team1PunkteHinspiel;}
    public void setTeam1PunkteHinspiel(int punkte){team1PunkteHinspiel = punkte;}

    private int team1PunkteRueckspiel = -1;
    public int getTeam1PunkteRueckspiel(){return team1PunkteRueckspiel;}
    public void setTeam1PunkteRueckspiel(int punkte){team1PunkteRueckspiel = punkte;}

    private int team2Nr = -1;
    public int getTeam2Nr(){return team2Nr;}
    public void setTeam2Nr(int nr){team2Nr = nr;}

    private String team2Name;
    public String getTeam2Name(){return team2Name;}
    public void setTeam2Name(String name){team2Name = name;}

    private int team2PunkteHinspiel = -1;
    public int getTeam2PunkteHinspiel(){return  team2PunkteHinspiel;}
    public void setTeam2PunkteHinspiel(int punkte){team2PunkteHinspiel = punkte;}

    private int team2PunkteRueckspiel = -1;
    public int getTeam2PunkteRueckspiel(){return team2PunkteRueckspiel;}
    public void setTeam2PunkteRueckspiel(int punkte){team2PunkteRueckspiel = punkte;}

    private int hinspielRichterGroupID = -1;
    public int getHinspielRichterGroupID(){return  hinspielRichterGroupID;}
    public void setHinspielRichterGroupID(int id){
      hinspielRichterGroupID = id;
    }

    private int hinspielRichterTeamID = -1;
    public int getHinspielRichterTeamID(){return hinspielRichterTeamID;}
    public void setHinspielRichterTeamID(int id){hinspielRichterTeamID = id;}

    private int hinspielFeldNr = -1;
    public int getHinspielFeldNr(){return hinspielFeldNr;}
    public void setHinspielFeldNr(int nr){hinspielFeldNr = nr;}

    private int hinspielTimeSlot = -1;
    public int getHinspielTimeSlot(){return hinspielTimeSlot;}
    public void setHinspielTimeSlot(int slot){hinspielTimeSlot = slot;}

    private int rueckspielRichterGroupID = -1;
    public int getRueckspielRichterGroupID(){return  rueckspielRichterGroupID;}
    public void setRueckspielRichterGroupID(int id){rueckspielRichterGroupID = id;}

    private int rueckspielRichterTeamID = -1;
    public int getRueckspielRichterTeamID(){return rueckspielRichterTeamID;}
    public void setRueckspielRichterTeamID(int id){rueckspielRichterTeamID = id;}

    private int rueckspielFeldNr = -1;
    public int getRueckspielFeldNr(){return rueckspielFeldNr;}
    public void setRueckspielFeldNr(int nr){rueckspielFeldNr = nr;}

    private int rueckspielTimeSlot = -1;
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

    TurnierKonfiguration(){
      anzTeamsJedeGruppe = new ArrayList<>();
        anzahlSpielfelder = -1;
        needRueckspiele = false;
      }
      public ArrayList<Integer> anzTeamsJedeGruppe;
      public int anzahlSpielfelder;
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

  public  static  class AuswertungsEintrag implements Comparable<AuswertungsEintrag>{
        final int teamId;
        public int anzGewonnen = 0;
        public int anzGespielt = 0;
        public int pktDifferenz = 0;
        //MyHelpers.IntPair score = new MyHelpers.IntPair(0,0);

        AuswertungsEintrag(int id) {
            this.teamId = id;
        }

        public int compareTo(AuswertungsEintrag a){
            if(this.anzGewonnen > a.anzGewonnen) // wenn this mehr Siege hat als a
                return 1;
            else if(a.anzGewonnen > this.anzGewonnen)// wenn this weniger siege hat als a
                return -1;
            else{ //wenn beide gleiche Anzahle der Siege haben
                if(this.pktDifferenz > a.pktDifferenz) //jetzt muessen die gesammte Punkte beruecksichtigt werden
                    return 1;
                else if(a.pktDifferenz > this.pktDifferenz)
                    return -1;
            }
            return  0; // unentschieden, da gleiche anzahl von siegen und gesamtpunkte sind gleich
        }
  }

  protected abstract void reset();

  public abstract void updateMatch(TurnierMatch match);

}
