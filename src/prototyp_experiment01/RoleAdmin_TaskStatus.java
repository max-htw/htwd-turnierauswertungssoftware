import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

public class RoleAdmin_TaskStatus extends RoleWithTaskBase{
    RoleAdmin_TaskStatus(ByteArrayOutputStream s, Map<String, String> q) {
        super(s, q);
    }

    @Override
    public void handleRequest() throws IOException {
        try {
            if (qs.containsKey(qs_editmatch)) {
                MyHelpers.Match m = DataBaseQueries.getMatchByHash(Integer.valueOf(qs.get(qs_editmatch)));
                if(qs.containsKey(AppSettings.post_score_team1_hinspiel)){
                    int s = Integer.valueOf(qs.get(AppSettings.post_score_team1_hinspiel));
                    m.set_firstTeamHinspielPunkte(s);
                }
                if(qs.containsKey(AppSettings.post_score_team2_hinspiel)){
                    int s = Integer.valueOf(qs.get(AppSettings.post_score_team2_hinspiel));
                    m.set_secondTeamHinspielPunkte(s);
                }
                if(qs.containsKey(AppSettings.post_score_team1_rueckspiel) && AppSettings.needRueckspiele()){
                    int s = Integer.valueOf(qs.get(AppSettings.post_score_team1_rueckspiel));
                    m.set_firstTeamRueckspielPunkte(s);
                }
                if(qs.containsKey(AppSettings.post_score_team2_rueckspiel) && AppSettings.needRueckspiele()){
                    int s = Integer.valueOf(qs.get(AppSettings.post_score_team2_rueckspiel));
                    m.set_secondTeamRueckspielPunkte(s);
                }
            } else if(qs.containsKey(qs_loadArchiv)){
                int tid = Integer.valueOf(qs.get(qs_loadArchiv));
                DataBaseQueries.loadTurnierFromArchive(tid);
            } else if (qs.containsKey(qs_setAnzahlTeams)) {
                if(qs.containsKey(qs_refGroupID)) {
                    AppSettings.set_anzTeams(
                            Integer.valueOf(qs.get(qs_setAnzahlTeams)),
                            Integer.valueOf(qs.get(qs_refGroupID))
                    );
                }
            } else if (qs.containsKey(qs_setAnzahlSpielfelder)) {
                AppSettings.set_anzSpielfelder(Integer.valueOf(qs.get(qs_setAnzahlSpielfelder)));
            } else if (qs.containsKey(qs_setPrefillScores)) {
                AppSettings.setNeedPrefillScores(Boolean.valueOf(qs.get(qs_setPrefillScores)));

            }
        }
        catch (Exception e) {
            int dummy = 1;
        }

        bs.write(("<!DOCTYPE html>\n<html>\n<head>\n").getBytes());
        bs.write(("<style>\n").getBytes());
        bs.write(("td{text-align:center;vertical-align:middle;min-width:9vmin;height:9vmin;font-size:4vmin;font-weight:bold;}\n").getBytes());
        bs.write(("td.hinSpiel{border-top:0.2vmin solid grey; border-right:0.2vmin solid grey;}\n").getBytes());
        bs.write(("td.rueckSpiel{border-bottom:0.2vmin solid grey; border-left:0.2vmin solid grey;}\n").getBytes());
        bs.write(("td.stats{border-bottom:0.2vmin solid grey;font-size:3vmin;font-weight:normal;}").getBytes());
        bs.write(("td.trenner{min-width:4vmin;}").getBytes());
        bs.write(("td a:link, td a:visited, td a:hover, td a:active{text-decoration:none;}\n").getBytes());
        bs.write((MyHelpers.btnLinkCss()).getBytes());
        bs.write(("</style>\n").getBytes());
        bs.write(("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">").getBytes());
        bs.write(("</head>\n<body>\n").getBytes());
        //bs.write(("<script>document.write('width: ' + document.documentElement.clientWidth + '<br>')</script>\n").getBytes());
        bs.write((htmlNavigationLinks()).getBytes());

        for (int groupID = 1; groupID <= AppSettings.get_anzGroups(); groupID++) {
            bs.write(("<br><br><span style=\"font-size:2vw\">Aktueller Spielstand Gruppe " + groupID +":</span><br>\n").getBytes());
            bs.write(("<table>\n").getBytes());


            ArrayList<MyHelpers.AuswertungsEintrag> auswertung = DataBaseQueries.calculateAuswertung_new(groupID);
            int cntI = 0, cntJ = 0;

            for (MyHelpers.AuswertungsEintrag a : auswertung) {
                int i = a.teamId;
                cntI += 1;
                bs.write(("<tr>\n").getBytes());
                for (MyHelpers.AuswertungsEintrag b : auswertung) {
                    int j = b.teamId;
                    cntJ += 1;
                    MyHelpers.Match m = DataBaseQueries.getMatchByHash(groupID*100 + i*10 + j);
                    int hinTeamI = 0;
                    int hinTeamJ = 0;
                    int rueckTeamI = 0;
                    int rueckTeamJ = 0;
                    if(cntJ != cntI) {
                        //wenn sie gliech sind - das ist ein diagonalelement, team mit sich selbst.
                        //dafuer existiert kein Match
                        if (m.get_firstTeam() == i) {
                            hinTeamI = m.get_firstTeamHinspielPunkte();
                            hinTeamJ = m.get_secondTeamHinspielPunkte();
                            rueckTeamI = m.get_firstTeamRueckspielPunkte();
                            rueckTeamJ = m.get_secondTeamRueckspielPunkte();
                        } else {
                            hinTeamJ = m.get_firstTeamHinspielPunkte();
                            hinTeamI = m.get_secondTeamHinspielPunkte();
                            rueckTeamJ = m.get_firstTeamRueckspielPunkte();
                            rueckTeamI = m.get_secondTeamRueckspielPunkte();
                        }
                    }
                    //MyHelpers.IntPair ts = DataBaseQueries.getSingleHinspielScore(new MyHelpers.IntPair(i, j), groupID);
                    MyHelpers.IntPair ts = new MyHelpers.IntPair(hinTeamI, hinTeamJ);
                    //MyHelpers.IntPair rs = DataBaseQueries.getSingleRueckspielScore(new MyHelpers.IntPair(i, j), groupID);
                    MyHelpers.IntPair rs = new MyHelpers.IntPair(rueckTeamI, rueckTeamJ);
                    if (cntJ < cntI){
                        if(AppSettings.needRueckspiele()) {
                            bs.write(("<td class=\"" + StringsCSS.rueckSpiel.name() + "\">").getBytes());
                            String obenStr = rs.y >= 0 ? String.valueOf(rs.y) : "--";
                            String untenStr = rs.x >= 0 ? String.valueOf(rs.x) : "--";
                            boolean canEdit = true;
                            if (rs.x >= 0 && rs.y >= 0 || canEdit) {
                                int hashID = (groupID*100 + Math.max(i,j)*10 + Math.min(i,j));
                                bs.write(("<a href=\"" + pathPrefix + "/" + StringsRole.Admin.name().toLowerCase() +
                                        "/" + StringsRole.AdminTasks.Matchdetails.name().toLowerCase() +
                                        "/?" + qs_matchID + "=" + hashID + "\">").getBytes());
                                bs.write(("<sup style=\"color:" + AppSettings.getTeamColor(j) + ";\">" +
                                        obenStr + "</sup>&frasl;<sub  style=\"color:" +
                                        AppSettings.getTeamColor(i) + ";\">" + untenStr + "</sub>").getBytes());
                                bs.write(("</a>").getBytes());
                            }
                            bs.write(("</td>\n").getBytes());
                        }
                        else
                            bs.write(("<td></td>\n").getBytes());
                    }
                    else if (cntJ == cntI) {
                        bs.write(("<td style=\"color:white;background-color:"
                                + AppSettings.getTeamColor(i) + ";\">" +
                                AppSettings.getTeamLetter(i) +
                                "</td>\n").getBytes());
                    } else {
                        bs.write(("<td class=\"" + StringsCSS.hinSpiel.name() + "\">").getBytes());
                        String obenStr = ts.x >= 0 ? String.valueOf(ts.x) : "--";
                        String untenStr = ts.y >= 0 ? String.valueOf(ts.y) : "--";
                        boolean canEdit = true;
                        if (ts.x >= 0 && ts.y >= 0 || canEdit) {
                            int hashID = (groupID*100 + Math.min(i,j)*10 + Math.max(i,j));
                            bs.write(("<a href=\"" + pathPrefix + "/" + StringsRole.Admin.name().toLowerCase() +
                                    "/" + StringsRole.AdminTasks.Matchdetails.name().toLowerCase() +
                                        "/?" + qs_matchID + "=" + hashID + "\">").getBytes());
                            bs.write(("<sup style=\"color:" + AppSettings.getTeamColor(i) + ";\">" +
                                    obenStr + "</sup>&frasl;<sub  style=\"color:" +
                                    AppSettings.getTeamColor(j) + ";\">" + untenStr + "</sub>").getBytes());
                            bs.write(("</a>").getBytes());
                        }
                        bs.write(("</td>\n").getBytes());
                    }
                }
                bs.write(("<td class=\"" + StringsCSS.trenner.name() +
                        "\"></td><td class=\"" + StringsCSS.stats.name() + "\">" + a.score.x + "<br>(" + a.score.y + ")</td>\n").getBytes());
                cntJ = 0;
                bs.write(("</tr>\n").getBytes());
            }
            bs.write(("</table>\n").getBytes());
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
