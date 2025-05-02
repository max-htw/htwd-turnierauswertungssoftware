import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class RoleTeam_TaskTurnierplan_Renderer extends RoleWithTaskBase_Renderer<RoleTeam_TaskTurnierplan_Data>{

    @Override
    public RoleTeam_TaskTurnierplan_Data getEmptyDaten() {
        return new RoleTeam_TaskTurnierplan_Data();
    }

    public  static  class PlanItem{
        String uhrZeit;
        int feldNr;
        String team1Name;
        String team2Name;
        String shiriName;
        String linkUrl;
        String linkText;
        PlanItem(String zeit, int feld, String team1, String team2, String shiri, String url, String text){
            uhrZeit = zeit;
            feldNr = feld;
            team1Name = team1;
            team2Name = team2;
            shiriName = shiri;
            linkUrl = url;
            linkText = text;
        }
    }

}
