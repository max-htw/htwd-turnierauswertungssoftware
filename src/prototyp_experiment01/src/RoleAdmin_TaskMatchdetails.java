import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class RoleAdmin_TaskMatchdetails extends RoleWithTaskBase{
    RoleAdmin_TaskMatchdetails(ByteArrayOutputStream s, Map<String, String> q) {
        super(s,q);
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
        bs.write(("div.section{border:0.2vmin solid grey;padding:5px;}\n").getBytes());
        bs.write((MyHelpers.btnLinkCss()).getBytes());
        bs.write(("</style>\n").getBytes());
        bs.write(("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">").getBytes());
        bs.write(("</head>\n<body>\n").getBytes());
        bs.write((htmlNavigationLinks()).getBytes());
        if(mID > 0){
            boolean editHinspiel = (mID % 100)/10 < (mID % 10);
            boolean editRueckspiel = !editHinspiel;
            MyHelpers.Match savedM = DataBaseQueries.getMatchByHash(mID);
            if(savedM != null){
                bs.write(("Match: " + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_firstTeam()) +
                        " vs. " + + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_secondTeam())).getBytes());
                bs.write(("<div class=\"" + StringsCSS.section.name() + "\">\n").getBytes()); //start Div-section-hinspiel
                bs.write(("Hinspiel:<br>\n").getBytes());
                bs.write(("<br>\nRichter: " + savedM.getRichterHinspiel().x +
                        AppSettings.getTeamLetter(savedM.getRichterHinspiel().y) + "<br>\n").getBytes());
                bs.write(("Spielfeld: " + (savedM.get_feldNrHinspiel()+1) + "<br>\n").getBytes());
                bs.write(("Timeslot: " + AppSettings.getTimeSlotStr(savedM.get_timeslotHinspiel()) + "<br>\n").getBytes());
                //Organisatoren und Richter-Team duerfen Spielstand aendern. Die Gaeste koennen nur lesen:
                if(editHinspiel){
                    bs.write(("<form action=\"" + pathPrefix + "/" +
                            StringsRole.Admin.name().toLowerCase() + "/" +
                            StringsRole.AdminTasks.Status.name().toLowerCase() + "/\">").getBytes());
                    bs.write(("\n<input type=\"hidden\" name=\"" + qs_editmatch + "\" value=\"" + savedM.hashCode() + "\">").getBytes());
                    bs.write(("<br>\nPunkte Team " + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_firstTeam()) +
                            ": <input type=\"text\" name=\"" + AppSettings.post_score_team1_hinspiel +
                            "\"").getBytes());
                    if (savedM.get_firstTeamHinspielPunkte() >= 0)
                        bs.write((" value=\"" + savedM.get_firstTeamHinspielPunkte()  +"\"").getBytes());
                    bs.write(("><br>\n").getBytes());

                    bs.write(("Punkte Team " + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_secondTeam()) +
                            ": <input type=\"text\" name=\"" + AppSettings.post_score_team2_hinspiel + "\"").getBytes());
                    if (savedM.get_secondTeamHinspielPunkte() >= 0)
                        bs.write((" value=\"" + savedM.get_secondTeamHinspielPunkte()  +"\"").getBytes());
                    bs.write(("><br>\n").getBytes());
                    bs.write(("<button type=\"submit\">speichern</button><br>\n").getBytes());
                    bs.write(("</form>\n").getBytes());
                }
                else{
                    String punkteStr = "";
                    if(savedM.get_firstTeamHinspielPunkte()>=0)
                        punkteStr += savedM.get_firstTeamHinspielPunkte();

                    bs.write(("<br>\nPunkte Team " + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_firstTeam()) +
                            ": " + punkteStr + "<br>\n").getBytes());

                    punkteStr = "";
                    if(savedM.get_secondTeamHinspielPunkte()>=0)
                        punkteStr += savedM.get_secondTeamHinspielPunkte();

                    bs.write(("Punkte Team " + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_secondTeam()) +
                            ": " + punkteStr + "<br>\n").getBytes());
                }
                bs.write(("</div>\n").getBytes());//end Div-section-Hinspiel
                bs.write(("<br>\n").getBytes());

                if(AppSettings.needRueckspiele()) {
                    bs.write(("<div class=\"" + StringsCSS.section.name() + "\">\n").getBytes()); //start Div-section-Rueckspiel
                    bs.write(("Rueckspiel:<br>\n").getBytes());
                    bs.write(("<br>\nRichter: " + savedM.getRichterRueckspiel().x +
                            AppSettings.getTeamLetter(savedM.getRichterRueckspiel().y) + "<br>\n").getBytes());
                    bs.write(("Spielfeld: " + (savedM.get_feldNrRueckspiel() + 1) + "<br>\n").getBytes());
                    bs.write(("Timeslot: " + AppSettings.getTimeSlotStr(savedM.get_timeslotRueckspiel()) + "<br>\n").getBytes());
                    //Organisatoren und Richter-Team duerfen Spielstand aendern. Die Gaeste koennen nur lesen:
                    if (editRueckspiel) {
                        bs.write(("<form action=\"" + pathPrefix + "/" +
                                StringsRole.Admin.name().toLowerCase() + "/" +
                                StringsRole.AdminTasks.Status.name().toLowerCase() + "/\">").getBytes());
                        bs.write(("\n<input type=\"hidden\" name=\"" + qs_editmatch + "\" value=\"" + savedM.hashCode() + "\">").getBytes());
                        bs.write(("<br>\nPunkte Team " + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_firstTeam()) +
                                ": <input type=\"text\" name=\"" + AppSettings.post_score_team1_rueckspiel +
                                "\"").getBytes());
                        if (savedM.get_firstTeamRueckspielPunkte() >= 0)
                            bs.write((" value=\"" + savedM.get_firstTeamRueckspielPunkte() + "\"").getBytes());
                        bs.write(("><br>\n").getBytes());

                        bs.write(("Punkte Team " + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_secondTeam()) +
                                ": <input type=\"text\" name=\"" + AppSettings.post_score_team2_rueckspiel + "\"").getBytes());
                        if (savedM.get_secondTeamRueckspielPunkte() >= 0)
                            bs.write((" value=\"" + savedM.get_secondTeamRueckspielPunkte() + "\"").getBytes());
                        bs.write(("><br>\n").getBytes());
                        bs.write(("<button type=\"submit\">speichern</button><br>\n").getBytes());
                        bs.write(("</form>\n").getBytes());
                    } else {
                        String punkteStr = "";
                        if (savedM.get_firstTeamRueckspielPunkte() >= 0)
                            punkteStr += savedM.get_firstTeamRueckspielPunkte();

                        bs.write(("<br>\nPunkte Team " + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_firstTeam()) +
                                ": " + punkteStr + "<br>\n").getBytes());

                        punkteStr = "";
                        if (savedM.get_secondTeamRueckspielPunkte() >= 0)
                            punkteStr += savedM.get_secondTeamRueckspielPunkte();

                        bs.write(("Punkte Team " + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_secondTeam()) +
                                ": " + punkteStr + "<br>\n").getBytes());
                    }

                    bs.write(("</div>\n").getBytes());//end Div-section-rueckspiel
                }
            }
        }
        bs.write(("</body></html>").getBytes());

    }

    public String htmlNavigationLinks(){
        return "<a class=\"" + StringsCSS.w3btn + "\" href=\"" + pathPrefix + "/" + StringsRole.Admin.name().toLowerCase() + "/" +
                StringsRole.AdminTasks.Status.name().toLowerCase() + "\">Spielstand</a>\n" +
                "<a class=\"" + StringsCSS.w3btn + "\" href=\"" + pathPrefix + "/" + StringsRole.Admin.name().toLowerCase() + "/" +
                StringsRole.AdminTasks.Turnierplan.name().toLowerCase() + "\">Turnierplan</a>\n" +
                "<a class=\"" + StringsCSS.w3btn + "\" href=\"" + pathPrefix + "/" + StringsRole.Admin.name().toLowerCase() + "/" +
                StringsRole.AdminTasks.Einstellungen.name().toLowerCase() + "\">&#x2699;</a><br><br>\n";
    }
}
