import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class RoleTeam_TaskMatchdetails extends RoleWithTaskBase{
    private int _groupID;
    private int _teamID;

    RoleTeam_TaskMatchdetails(ByteArrayOutputStream s, Map<String, String> q, int groupID, int teamID) {
        super(s,q);
        _groupID = groupID;
        _teamID = teamID;
    }

    @Override
    public void handleRequest() throws IOException {
        int mID = -1;
        try {
            if(qs.containsKey(qs_matchID)){
                mID = Integer.valueOf(qs.get(qs_matchID));
            }
        } catch (Exception e) {
            int dummy = 5;
        }

        bs.write(("<!DOCTYPE html>\n<html>\n<head>\n").getBytes());
        bs.write(("<style>\n").getBytes());
        bs.write(("td{text-align:center;vertical-align:middle;padding:5px;}\n").getBytes());
        bs.write((MyHelpers.btnLinkCss()).getBytes());
        bs.write(("</style>\n").getBytes());
        bs.write(("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">").getBytes());
        bs.write(("</head>\n<body>\n").getBytes());

        bs.write(("<a class=\"" + StringsCSS.w3btn + "\" href=\"" + pathPrefix + "/" + teamNameForURL(_groupID, _teamID) + "/" +
                StringsRole.TeamTasks.Overview.name().toLowerCase() + "\">Home</a><br><br>\n").getBytes());

        MyHelpers.Match savedM = DataBaseQueries.getMatchByHash(mID);

        if(savedM != null){
            boolean editHinspiel = (mID % 100)/10 < (mID % 10);
            boolean editRueckspiel = !editHinspiel;
            boolean darfBearbeiten = (editHinspiel && savedM.getRichterHinspiel().x == _groupID && savedM.getRichterHinspiel().y == _teamID)
                    || (editRueckspiel && savedM.getRichterRueckspiel().x == _groupID && savedM.getRichterRueckspiel().y == _teamID);

            String team1PunkteStr = "";
            String team2PunkteStr = "";
            String team1TextboxName = "";
            String team2TextboxName = "";
            String uhrzeitStr = "";
            String schiriStr = "";
            String feldStr = "";
            String hinOrRueckspielStr = editHinspiel?"Hinspiel":"Rueckspiel";

            if(editHinspiel){
                if(savedM.get_firstTeamHinspielPunkte() >= 0)
                    team1PunkteStr = "" + savedM.get_firstTeamHinspielPunkte();
                if(savedM.get_secondTeamHinspielPunkte() >= 0)
                    team2PunkteStr = "" + savedM.get_secondTeamHinspielPunkte();
                team1TextboxName = AppSettings.post_score_team1_hinspiel;
                team2TextboxName = AppSettings.post_score_team2_hinspiel;
                uhrzeitStr = AppSettings.getTimeSlotStr(savedM.get_timeslotHinspiel());
                schiriStr = "" + savedM.getRichterHinspiel().x + AppSettings.getTeamLetter(savedM.getRichterHinspiel().y);
                feldStr = "" + (savedM.get_feldNrHinspiel() + 1);
            }
            else{
                if(savedM.get_firstTeamRueckspielPunkte() >= 0)
                    team1PunkteStr = "" + savedM.get_firstTeamRueckspielPunkte();
                if(savedM.get_secondTeamRueckspielPunkte() >= 0)
                    team2PunkteStr = "" + savedM.get_secondTeamRueckspielPunkte();
                team1TextboxName = AppSettings.post_score_team1_rueckspiel;
                team2TextboxName = AppSettings.post_score_team2_rueckspiel;
                uhrzeitStr = AppSettings.getTimeSlotStr(savedM.get_timeslotRueckspiel());
                schiriStr = "" + savedM.getRichterRueckspiel().x + AppSettings.getTeamLetter(savedM.getRichterRueckspiel().y);
                feldStr = "" + (savedM.get_feldNrRueckspiel() + 1);
            }

            if(darfBearbeiten) {
                bs.write(("<form action=\"" + pathPrefix + "/" +
                        teamNameForURL(_groupID, _teamID) + "/" +
                        StringsRole.TeamTasks.Overview.name().toLowerCase() + "/\">").getBytes());
                bs.write(("\n<input type=\"hidden\" name=\"" + qs_editmatch + "\" value=\"" + savedM.hashCode() + "\">").getBytes());
            }
            bs.write(("<table><tr><th><span class=\"" + StringsCSS.htwdOrange + "\">Team</span></th><th>Punkte</th><th></th></tr>\n").getBytes());
            bs.write(("<tr>\n<td>" + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_firstTeam()) +"</td>").getBytes());
            if(!darfBearbeiten){
                bs.write(("<td>" + team1PunkteStr + "</td>\n").getBytes());
            }
            else{
                bs.write(("<td><input type=\"number\" min=\"0\" max=\"99\" name=\"" + team1TextboxName +
                        "\" value=\"" + team1PunkteStr + "\"></td>\n").getBytes());
            }
            bs.write(("<td rowspan=\"2\">").getBytes());
            if(darfBearbeiten){
                bs.write(("<button type=\"submit\">speichern</button><br>\n").getBytes());
            }
            bs.write(("</td></tr>\n").getBytes());

            bs.write(("<tr>\n<td>" + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_secondTeam()) +"</td>").getBytes());
            if(!darfBearbeiten){
                bs.write(("<td>" + team2PunkteStr + "</td>\n").getBytes());
            }
            else{
                bs.write(("<td><input type=\"number\" min=\"0\" max=\"99\" name=\"" + team2TextboxName +
                        "\" value=\"" + team2PunkteStr + "\"></td>\n").getBytes());
            }
            bs.write(("</tr>\n").getBytes());
            bs.write(("</table>\n").getBytes());

            if(darfBearbeiten)
                bs.write(("</form>\n").getBytes());

            bs.write(("<br>").getBytes());
            if(AppSettings.needRueckspiele())
                bs.write((hinOrRueckspielStr +"<br>\n").getBytes());

            bs.write(("Uhrzeit: " + uhrzeitStr + "<br>\n").getBytes());
            bs.write(("Schiedsrichter: " + schiriStr + "<br>\n").getBytes());
            bs.write(("Spielfeld: " + feldStr + "<br>\n").getBytes());
        }
        bs.write(("</body></html>").getBytes());
    }
}
