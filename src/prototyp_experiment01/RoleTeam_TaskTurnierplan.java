import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class RoleTeam_TaskTurnierplan extends RoleWithTaskBase{
    private int _groupID;
    private int _teamID;

    RoleTeam_TaskTurnierplan(ByteArrayOutputStream s, Map<String, String> q, int groupID, int teamID) {
        super(s, q);
        _groupID = groupID;
        _teamID = teamID;
    }

    @Override
    public void handleRequest() throws IOException {
        bs.write(("<!DOCTYPE html>\n<html>\n<head>\n").getBytes());
        bs.write(("<style>\n").getBytes());
        bs.write(("td,th{text-align:center;vertical-align:middle;padding:5px;}\n").getBytes());
        bs.write(("th{border:1px solid #dddddd;border-collapse:collapse;background-color:#dddddd;}\n").getBytes());
        bs.write((MyHelpers.btnLinkCss()).getBytes());
        bs.write(("</style>\n").getBytes());
        bs.write(("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">").getBytes());
        bs.write(("</head>\n<body>\n").getBytes());
        bs.write(("<a class=\"" + StringsCSS.w3btn + "\" href=\"" + pathPrefix + "/" + teamNameForURL(_groupID, _teamID) + "/" +
                StringsRole.TeamTasks.Overview.name().toLowerCase() + "\">Home</a><br>\n").getBytes());

        bs.write(("<h1>Turnierplan, <span class=\"" + StringsCSS.htwdOrange + "\">Team</span> " + _groupID + AppSettings.getTeamLetter(_teamID) + "</h1>\n").getBytes());
        bs.write(("<table><tr><th>Uhrzeit</th><th>Feld</th><th>Team 1</th><th>Team 2</th><th>Shiri</th><th>Spielstand</th></tr>\n").getBytes());
        //fuer jeden Timeslot, Reihe nach, wird auf jedem Feld ein Match gesucht, wo das Team entweder spielt oder pfeift
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
                    if(((stats.groupid == _groupID && _teamID == stats.team1) ||
                        (stats.groupid == _groupID && _teamID == stats.team2) ||
                        (stats.richter.x == _groupID && _teamID == stats.richter.y) )){
                        // ein Spiel gefunden, wo das Team entweder spielt, oder pfeift
                        bs.write(("<tr><td>" + AppSettings.getTimeSlotStr(idx) + "</td>\n").getBytes());
                        bs.write(("<td>" + f.feldNr + "</td>\n").getBytes());
                        bs.write(("<td>" + stats.groupid + AppSettings.getTeamLetter(stats.team1) + "</td>\n").getBytes());
                        bs.write(("<td>" + stats.groupid + AppSettings.getTeamLetter(stats.team2) + "</td>\n").getBytes());
                        bs.write(("<td>" + stats.richter.x + AppSettings.getTeamLetter(stats.richter.y) + "</td>\n").getBytes());

                        bs.write(("<td>").getBytes());
                        String team1PunkteStr = "--";
                        if(stats.team1Punkte >= 0)
                            team1PunkteStr = "" + stats.team1Punkte;
                        String team2PunkteStr = "--";
                        if(stats.team2Punkte >= 0)
                            team2PunkteStr = "" + stats.team2Punkte;

                        if(stats.richter.x == _groupID && _teamID == stats.richter.y) {
                            //das Team pfeift bei dem Spiel und darf die Punktzahlen eintragen
                            bs.write(("<a class=\"" + StringsCSS.w3btn + "\" href=\"" + pathPrefix +
                                    "/" + teamNameForURL(_groupID, _teamID) + "/" + StringsRole.TeamTasks.Matchdetails.name().toLowerCase() +
                                    "/?" + qs_matchID + "=" + h + "\">" + team1PunkteStr + " / " + team2PunkteStr + "</a>").getBytes());
                        }
                        else{
                            bs.write((team1PunkteStr + " / " + team2PunkteStr).getBytes());
                        }
                        bs.write(("</td></tr>\n").getBytes());
                    }
                }
            }
        }
        bs.write(("</table>\n").getBytes());
        bs.write(("</body></html>").getBytes());
    }
}
