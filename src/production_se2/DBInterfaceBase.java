import java.util.ArrayList;
import java.util.TreeMap;

public abstract class DBInterfaceBase {

  //Datenabfragen

  abstract TreeMap<Integer, String> getGruppen(); // <groupID, groupName>
  abstract TreeMap<Integer, String> getTeamsByGroup(int groupID); // <teamNr, teamName>
  abstract ArrayList<TurnierMatch> getMatchesByGroup(int groupID);

  public static class TurnierMatch{
    int groupID;
    int team1Nr;
    String team1Name;
    int team1PunkteHinspiel;
    int getTeam1PunkteRueckspiel;
    int team2Nr;
    String team2Name;
    int team2PunkteHinspiel;
    int getTeam2PunkteRueckspiel;
  }


}
