import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public abstract class RoleWithTaskBase {
    public ByteArrayOutputStream bs;
    public Map<String, String> qs;

    public static final String qs_setAnzahlGroups = "setAnzGroups";
    public static final String qs_setAnzahlTeams = "setAnzTeams";
    public static final String qs_refGroupID = "refGroupID";
    public static final String qs_setAnzahlSpielfelder = "setAnzFields";
    public static final String qs_setPrefillScores = "setPrefillScores";
    public static final String qs_matchID = "matchid";
    public static final String qs_editmatch = "editmatch";
    public static final String qs_needRueckspiele = "needrueckspiel";
    public static final String qs_loadArchiv = "loadarchiv";
    public static final String qs_selGroup = "selgroup";
    public static final String qs_selTeam = "selteam";

    public String pathPrefix = "";

    RoleWithTaskBase(ByteArrayOutputStream s, Map<String, String> q){
        bs = s;
        qs = q;
    }

    public abstract void handleRequest() throws IOException ;

    public String teamNameForURL(int groupID, int teamID){
        return "" + groupID + Character.toLowerCase(AppSettings.getTeamLetter(teamID));
    }
}
