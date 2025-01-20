import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class RoleTeam_TaskOverview extends RoleWithTaskBase{

    private int _groupID;
    private int _teamID;

    RoleTeam_TaskOverview(ByteArrayOutputStream s, Map<String, String> q, int groupID, int teamID) {
        super(s,q);
        _groupID = groupID;
        _teamID = teamID;
    }

    @Override
    public void handleRequest() throws IOException {
        try {
            if (qs.containsKey(qs_editmatch)) {
                MyHelpers.Match m = DataBaseQueries.getMatchByHash(Integer.valueOf(qs.get(qs_editmatch)));
                if (qs.containsKey(AppSettings.post_score_team1_hinspiel)) {
                    int s = Integer.valueOf(qs.get(AppSettings.post_score_team1_hinspiel));
                    m.set_firstTeamHinspielPunkte(s);
                }
                if (qs.containsKey(AppSettings.post_score_team2_hinspiel)) {
                    int s = Integer.valueOf(qs.get(AppSettings.post_score_team2_hinspiel));
                    m.set_secondTeamHinspielPunkte(s);
                }
                if (qs.containsKey(AppSettings.post_score_team1_rueckspiel) && AppSettings.needRueckspiele()) {
                    int s = Integer.valueOf(qs.get(AppSettings.post_score_team1_rueckspiel));
                    m.set_firstTeamRueckspielPunkte(s);
                }
                if (qs.containsKey(AppSettings.post_score_team2_rueckspiel) && AppSettings.needRueckspiele()) {
                    int s = Integer.valueOf(qs.get(AppSettings.post_score_team2_rueckspiel));
                    m.set_secondTeamRueckspielPunkte(s);
                }
                DataBaseQueries.currentTurnierChanged = true;
            }
        }
        catch (Exception e) {
            int dummy = 1;
        }

        bs.write(("<!DOCTYPE html>\n<html>\n<head>\n").getBytes());
        bs.write(("<style>\n").getBytes());
        bs.write(("td,th{text-align:center;vertical-align:middle;padding:5px;}\n").getBytes());
        bs.write(("th{border:1px solid #dddddd;border-collapse:collapse;background-color:#dddddd;}\n").getBytes());
        bs.write((MyHelpers.dropDownCSS()).getBytes());
        bs.write((MyHelpers.btnLinkCss()).getBytes());
        bs.write(("</style>\n").getBytes());
        bs.write(("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">").getBytes());
        bs.write(("</head>\n<body>\n").getBytes());
        bs.write((MyHelpers.dropDownForTeam(true, pathPrefix, _groupID, _teamID)).getBytes());
        bs.write(("<h1><span class=\"" + StringsCSS.htwdOrange + "\">Team</span> " + _groupID + AppSettings.getTeamLetter(_teamID) + "</h1>\n").getBytes());

        bs.write(("<table>\n<tr><td colspan=\"5\">Aktuelles Spiel:</td></tr>\n").getBytes());
        bs.write(("<tr><th>Uhrzeit</th><th>Spielfeld</th><th>Team 1</th><th>Team 2</th><th>Shiri</th></tr>\n").getBytes());
        //fuer jeden Timeslot, Reihe nach, wird auf jedem Feld ein Match gesucht, wo das Team entweder spielt oder pfeift
        //und der Spielstand ist  -1 zu -1
        int idx = -1; //timeslot
        boolean endeErreicht = false;
        while (!endeErreicht) {
            idx += 1;
            endeErreicht = true;
            for (int i = 0; i < AppSettings.get_anzSpielfelder(); i++) {
                MyHelpers.FeldSpiele f = DataBaseQueries.getTurnierplan().get(i);
                if(f.getAnzahlSpiele() > (idx + 1) )
                    endeErreicht = false; //mindestens bei einem Spielfeld wurden noch nicht alle Timeslots abgefragt
                Integer h = f.getSpielHashByIdx(idx);
                if(h > 0) {
                    MyHelpers.SpielStats stats = DataBaseQueries.getSpielStatsByHash(h);
                    if(stats.team2Punkte < 0 && stats.team1Punkte < 0 &&
                            ( (stats.groupid == _groupID && _teamID == stats.team1) ||
                              (stats.groupid == _groupID && _teamID == stats.team2) ||
                              (stats.richter.x == _groupID && _teamID == stats.richter.y) )){
                        // ein Spiel gefunden, wo das Team entweder spielt, oder pfeift
                        bs.write(("<tr>").getBytes());
                        bs.write(("<td>" + AppSettings.getTimeSlotStr(idx) + "</td>\n").getBytes());
                        bs.write(("<td>" + f.feldNr + "</td>\n").getBytes());
                        bs.write(("<td>" + stats.groupid + AppSettings.getTeamLetter(stats.team1) + "</td>\n").getBytes());
                        bs.write(("<td>" + stats.groupid + AppSettings.getTeamLetter(stats.team2) + "</td>\n").getBytes());
                        bs.write(("<td>" + stats.richter.x + AppSettings.getTeamLetter(stats.richter.y) + "</td>\n").getBytes());
                        bs.write(("</tr>").getBytes());
                        if(stats.richter.x == _groupID && _teamID == stats.richter.y) {
                            //das Team pfeift bei dem Spiel und darf die Punktzahlen eintragen
                            bs.write(("<tr><td colspan=\"5\" style=\"text-align:right;\">" + "<a class=\"" + StringsCSS.w3btn + "\" href=\"" + pathPrefix +
                                    "/" + teamNameForURL(_groupID, _teamID) + "/" + StringsRole.TeamTasks.Matchdetails.name().toLowerCase() +
                                    "/?" + qs_matchID + "=" + h + "\">&#x270E;</a></td></tr>\n").getBytes());
                        }
                        endeErreicht = true;
                        break;
                    }
                }
            }
        }
        bs.write(("</table>\n").getBytes());
        bs.write(("<br><br><br>\n").getBytes());
        bs.write((htmlNavigationLinks()).getBytes());
        bs.write(("</body></html>").getBytes());
    }

    public String htmlNavigationLinks(){
        return "<a class=\"" + StringsCSS.w3btn + "\" href=\"" + pathPrefix + "/" + teamNameForURL(_groupID, _teamID) + "/" +
                StringsRole.TeamTasks.Turnierplan.name().toLowerCase() + "\">Turnierplan</a>\n" +
                "<a class=\"" + StringsCSS.w3btn + "\" href=\"" + pathPrefix + "/" + teamNameForURL(_groupID, _teamID) + "/" +
                StringsRole.TeamTasks.Stand.name().toLowerCase() + "\">Aktueller Stand</a><br><br>\n";
    }
}
