import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.FileWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;


public class ContextHandlers {
    private static  String qs_setAnzahlGroups = "setAnzGroups";
    private static  String qs_setAnzahlTeams = "setAnzTeams";
    private static  String qs_refGroupID = "refGroupID";
    private static  String qs_setAnzahlSpielfelder = "setAnzFields";
    private static  String qs_setRole = "setRole";
    private static  String qs_setPrefillScores = "setPrefillScores";
    private static  String qs_groupID = "groupid";
    private static  String qs_matchID = "matchid";
    private static  String qs_editmatch = "editmatch";

    public static Map<String, String> queryToMap(String query) {
        if(query == null) {
            return null;
        }
        Map<String, String> result = new HashMap<>();
        for (String param : query.split("&")) {
            String[] entry = param.split("=");
            if (entry.length > 1) {
                result.put(entry[0], entry[1]);
            }else{
                result.put(entry[0], "");
            }
        }
        return result;
    }

    static class RootHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> qsParams = ContextHandlers.queryToMap(exchange.getRequestURI().getQuery());
            try {
                if (qsParams.containsKey(qs_editmatch)) {
                   MyHelpers.Match m = DataBaseQueries.getMatchByHash(Integer.valueOf(qsParams.get(qs_editmatch)));
                   if(qsParams.containsKey(AppSettings.post_score_team1_hinspiel)){
                       int s = Integer.valueOf(qsParams.get(AppSettings.post_score_team1_hinspiel));
                       m.set_firstTeamHinspielPunkte(s);
                   }
                    if(qsParams.containsKey(AppSettings.post_score_team2_hinspiel)){
                        int s = Integer.valueOf(qsParams.get(AppSettings.post_score_team2_hinspiel));
                        m.set_secondTeamHinspielPunkte(s);
                    }
                    if(qsParams.containsKey(AppSettings.post_score_team1_rueckspiel)){
                        int s = Integer.valueOf(qsParams.get(AppSettings.post_score_team1_rueckspiel));
                        m.set_firstTeamRueckspielPunkte(s);
                    }
                    if(qsParams.containsKey(AppSettings.post_score_team2_rueckspiel)){
                        int s = Integer.valueOf(qsParams.get(AppSettings.post_score_team2_rueckspiel));
                        m.set_secondTeamRueckspielPunkte(s);
                    }
                }
                else if (qsParams.containsKey(qs_setAnzahlTeams)) {
                    if(qsParams.containsKey(qs_refGroupID)) {
                        AppSettings.set_anzTeams(
                                Integer.valueOf(qsParams.get(qs_setAnzahlTeams)),
                                Integer.valueOf(qsParams.get(qs_refGroupID))
                        );
                    }
                } else if (qsParams.containsKey(qs_setAnzahlSpielfelder)) {
                    AppSettings.set_anzSpielfelder(Integer.valueOf(qsParams.get(qs_setAnzahlSpielfelder)));
                } else if (qsParams.containsKey(qs_setRole)) {
                    AppSettings.setRole(Integer.valueOf(qsParams.get(qs_setRole)));
                } else if (qsParams.containsKey(qs_setPrefillScores)) {
                    AppSettings.setNeedPrefillScores(Boolean.valueOf(qsParams.get(qs_setPrefillScores)));

                }
            }
            catch (Exception e) {
                int dummy = 1;
            }


            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            bs.write(("<!DOCTYPE html>\n<html>\n<head>\n").getBytes());
            bs.write(("<style>\n").getBytes());
            bs.write(("td{text-align:center;vertical-align:middle;min-width:9vmin;height:9vmin;font-size:4vmin;font-weight:bold;}\n").getBytes());
            bs.write(("td.hinSpiel{border-top:0.2vmin solid grey; border-right:0.2vmin solid grey;}\n").getBytes());
            bs.write(("td.rueckSpiel{border-bottom:0.2vmin solid grey; border-left:0.2vmin solid grey;}\n").getBytes());
            bs.write(("td.stats{border-bottom:0.2vmin solid grey;font-size:3vmin;font-weight:normal;}").getBytes());
            bs.write(("td.trenner{min-width:4vmin;}").getBytes());
            bs.write(("td a:link, td a:visited, td a:hover, td a:active{text-decoration:none;\n").getBytes());
            bs.write(("</style>\n").getBytes());
            bs.write(("</head>\n<body>\n").getBytes());
            //bs.write(("<script>document.write('width: ' + document.documentElement.clientWidth + '<br>')</script>\n").getBytes());
            bs.write((MyHelpers.htmlNavigationLinks()).getBytes());

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
                                bs.write(("<td class=\"rueckSpiel\">").getBytes());
                                String obenStr = rs.y >= 0 ? String.valueOf(rs.y) : "--";
                                String untenStr = rs.x >= 0 ? String.valueOf(rs.x) : "--";
                                if ((rs.x >= 0 && rs.y >= 0) || (AppSettings.getRole() >= 0)) {
                                    int hashID = (groupID*100 + Math.max(i,j)*10 + Math.min(i,j));
                                    bs.write(("<a href=\"/match/?" + qs_matchID + "=" + hashID + "\">").getBytes());
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
                            bs.write(("<td class=\"hinSpiel\">").getBytes());
                            String obenStr = ts.x >= 0 ? String.valueOf(ts.x) : "--";
                            String untenStr = ts.y >= 0 ? String.valueOf(ts.y) : "--";
                            if ((ts.x >= 0 && ts.y >= 0) || (AppSettings.getRole() >= 0)) {
                                int hashID = (groupID*100 + Math.min(i,j)*10 + Math.max(i,j));
                                bs.write(("<a href=\"/match/?" + qs_matchID + "=" + hashID + "\">").getBytes());
                                bs.write(("<sup style=\"color:" + AppSettings.getTeamColor(i) + ";\">" +
                                        obenStr + "</sup>&frasl;<sub  style=\"color:" +
                                        AppSettings.getTeamColor(j) + ";\">" + untenStr + "</sub>").getBytes());
                                bs.write(("</a>").getBytes());
                            }
                            bs.write(("</td>\n").getBytes());
                        }
                    }
                    bs.write(("<td class=\"trenner\"></td><td class=\"stats\">" + a.score.x + "<br>(" + a.score.y + ")</td>\n").getBytes());
                    cntJ = 0;
                    bs.write(("</tr>\n").getBytes());
                }
                bs.write(("</table>\n").getBytes());
        }
            bs.write(("</body></html>").getBytes());

            exchange.sendResponseHeaders(200, bs.size());
            exchange.getResponseHeaders().set("Custom-Header-1", "A~HA");
            OutputStream os = exchange.getResponseBody();
            bs.writeTo(os);
            os.flush();
            os.close();
            exchange.close();
        }
    }

    static class EinstellungenHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            Map<String, String> qsParams = ContextHandlers.queryToMap(exchange.getRequestURI().getQuery());
            try {
                if (qsParams.containsKey(qs_setAnzahlGroups)) {
                    AppSettings.set_anzGroups(Integer.valueOf(qsParams.get(qs_setAnzahlGroups)));
                }
                else if (qsParams.containsKey(qs_setAnzahlTeams)) {
                    if(qsParams.containsKey(qs_refGroupID)) {
                        AppSettings.set_anzTeams(
                                Integer.valueOf(qsParams.get(qs_setAnzahlTeams)),
                                Integer.valueOf(qsParams.get(qs_refGroupID))
                                );
                    }
                } else if (qsParams.containsKey(qs_setAnzahlSpielfelder)) {
                    AppSettings.set_anzSpielfelder(Integer.valueOf(qsParams.get(qs_setAnzahlSpielfelder)));
                } else if (qsParams.containsKey(qs_setRole)) {
                    AppSettings.setRole(Integer.valueOf(qsParams.get(qs_setRole)));
                } else if (qsParams.containsKey(qs_setPrefillScores)) {
                    AppSettings.setNeedPrefillScores(Boolean.valueOf(qsParams.get(qs_setPrefillScores)));

                }
            }
            catch (Exception e) {
                int dummy = 1;
            }

            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            bs.write(("<!DOCTYPE html>\n<html>\n<head>\n").getBytes());
            bs.write(("<style>\n").getBytes());
            bs.write(("td{text-align:center;vertical-align:middle;min-width:9vmin;height:9vmin;font-size:4vmin;font-weight:bold;}\n").getBytes());
            bs.write(("td.hinSpiel{border-top:0.2vmin solid grey; border-right:0.2vmin solid grey;}\n").getBytes());
            bs.write(("td.stats{border-bottom:0.2vmin solid grey;font-size:3vmin;font-weight:normal;}").getBytes());
            bs.write(("td.trenner{min-width:4vmin;}").getBytes());
            bs.write(("td a:link, td a:visited, td a:hover, td a:active{text-decoration:none;}\n").getBytes());
            bs.write(("div.section{border:0.2vmin solid grey;padding:5px;}\n").getBytes());
            bs.write(("</style>\n").getBytes());
            bs.write(("</head>\n<body>\n").getBytes());
            //bs.write(("<script>document.write('width: ' + document.documentElement.clientWidth + '<br>')</script>\n").getBytes());
            bs.write((MyHelpers.htmlNavigationLinks()).getBytes());

            bs.write(("<div style=\"border: 0.2vmin solid #0c0;padding: 1rem;width: 40vmin;font-size:3vmin;\">\n").getBytes());
            bs.write(("<span style=\"text-decoration:underline;\">Aktuelles Turnier</span><br><br>\n").getBytes());

            bs.write(("<div class=\"section\">").getBytes()); // start Abschnitt Gruppen
            bs.write(("Anzahl Gruppen: " + AppSettings.get_anzGroups()+"<br>\n").getBytes());
            String trn = " (aendern zu ";
            for (int i=1; i<=AppSettings.get_maxAnzGroups(); i++){
                if(i != AppSettings.get_anzGroups()){
                    bs.write((trn + "<a href=\"./einstellungen?" + qs_setAnzahlGroups + "=" + i + "\">" + i + "</a>").getBytes());
                    trn = " | ";
                }
            }
            bs.write((")").getBytes());

            for(int groupID = 1; groupID <= AppSettings.get_anzGroups(); groupID++) {
                bs.write(("<br><br>\nAnzahl Teams Gruppe " + groupID + ": " + AppSettings.get_anzTeams(groupID) + "<br>\n").getBytes());
                trn = " (aendern zu ";
                for (int i = 3; i <= AppSettings.get_maxAnzTeams(); i++) {
                    if (i != AppSettings.get_anzTeams(groupID)) {
                        bs.write((trn + "<a href=\"./einstellungen?" +
                                qs_setAnzahlTeams + "=" + i + "&" +
                                qs_refGroupID + "=" + groupID +
                                "\">" + i + "</a>").getBytes());
                        trn = " | ";
                    }
                }
                bs.write((")").getBytes());
            }

            bs.write(("</div>\n").getBytes()); //ende Gruppen Abschnitt

            bs.write(("<br><br>\nAnzahl Spielfelder: " + AppSettings.get_anzSpielfelder()+"<br>\n").getBytes());
            trn = " (aendern zu ";
            for (int i=1; i<=AppSettings.get_maxAnzSpielfelder(); i++){
                if(i != AppSettings.get_anzSpielfelder()){
                    bs.write((trn + "<a href=\"./einstellungen?" + qs_setAnzahlSpielfelder + "=" + i + "\">" + i + "</a>").getBytes());
                    trn = " | ";
                }
            }

            bs.write((")<br><br>\nBenutzerrolle: " + AppSettings.getRoleStr(AppSettings.getRole()) + "<br>\n").getBytes());
            trn = "(aendern zu ";
            int iVals[] = {-1, 0, 3};
            for (int i: iVals){
                if(i != AppSettings.getRole()){
                    bs.write((trn + "<a href=\"./einstellungen?" + qs_setRole + "=" + i + "\">" + AppSettings.getRoleStr(i) + "</a>").getBytes());
                    trn = " | ";
                }
            }

            bs.write(("<br><br>\nVorausfuellen mit zufaelligen Punktzahlen: " + AppSettings.getNeedPrefillScores() + "<br>\n").getBytes());
            trn = "(aendern zu ";
            bs.write((trn + "<a href=\"./einstellungen?" + qs_setPrefillScores + "=" + !(AppSettings.getNeedPrefillScores()) + "\">"
                    + !(AppSettings.getNeedPrefillScores())+ "</a>").getBytes());

            bs.write((")<br>\n").getBytes());

            bs.write(("</div>\n").getBytes());

            bs.write(("<br><br>\ngespeicherte Turniere:<br>\n").getBytes());

            bs.write(("</body></html>").getBytes());

            exchange.sendResponseHeaders(200, bs.size());
            exchange.getResponseHeaders().set("Custom-Header-1", "A~HA");
            OutputStream os = exchange.getResponseBody();
            bs.writeTo(os);
            os.flush();
            os.close();
            exchange.close();

        }

    }

    static class TPlanHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
                Map<String,String> qsParams = ContextHandlers.queryToMap(exchange.getRequestURI().getQuery());
                try{
                    //if(qsParams.containsKey(qs_irgendwas)){
                    //}
                }
                catch (Exception e){}

                ByteArrayOutputStream bs = new ByteArrayOutputStream();
                bs.write(("<!DOCTYPE html>\n<html>\n<head>\n").getBytes());
                bs.write(("<style>\n").getBytes());
                bs.write(("td{text-align:center;vertical-align:middle;min-width:30vmin;height:5vmin;font-size:3vmin;}\n").getBytes());
                bs.write(("</style>\n").getBytes());
                bs.write(("</head>\n<body>\n").getBytes());
                bs.write((MyHelpers.htmlNavigationLinks()).getBytes());
                bs.write(("<br>\n").getBytes());
                bs.write(("<a href=\"/turnier.pdf\" target=\"blanck\">PDF Herunterladen</a><br>\n").getBytes());
                bs.write(("<a href=\"/turnier.csv\" target=\"blanck\">Excel Herunterladen</a><br><br>\n").getBytes());
                try {
                    bs.write(("<table>\n<tr>\n").getBytes());
                    bs.write(("<th></th>\n").getBytes()); //Zeit-Spalte
                    for(int i=0; i<AppSettings.get_anzSpielfelder(); i++){
                        bs.write(("<th>Feld " + (i+1) + "</th>\n").getBytes());
                    }
                    bs.write(("</tr>\n").getBytes());

                    int idx=0;
                    boolean endeErreicht = false;
                    while(!endeErreicht) {
                        endeErreicht = true;
                        bs.write(("<tr>\n").getBytes());
                        bs.write(("<td>" + AppSettings.getTimeSlotStr(idx) + "</td>\n").getBytes());//Zeit-Spalte
                        for (int i = 0; i < AppSettings.get_anzSpielfelder(); i++) {

                            bs.write(("<td>").getBytes());
                            MyHelpers.FeldSpiele_new f = DataBaseQueries.getTurnierplan_new().get(i);
                            if (f.getAnzahlSpiele() > idx) {
                                endeErreicht = false;
                                Integer h = f.getSpielHashByIdx(idx);
                                if(h > 0) {
                                    bs.write(("<a href=\"/match/?" + qs_matchID + "=" + h + "\">").getBytes());
                                    MyHelpers.SpielStats stats = DataBaseQueries.getSpielStatsByHash(h);
                                    bs.write(("" + stats.groupid + AppSettings.getTeamLetter(stats.team1) +
                                            " vs. " + stats.groupid + AppSettings.getTeamLetter(stats.team2) +
                                            " | " + stats.richter.x +
                                            AppSettings.getTeamLetter(stats.richter.y)).getBytes());
                                    bs.write(("</a>").getBytes());
                                }
                            }
                            bs.write(("</td>\n").getBytes());
                        }
                        bs.write(("</tr>\n").getBytes());
                        idx += 1;
                    }
                    bs.write(("</table>\n").getBytes());
                } catch (Exception e) {
                    bs.write(("<br><span style=\"color:red\">Funktion getTurnierplan ist fehlgeschlagen</span><br>").getBytes());
                }

            bs.write(("</body></html>\n").getBytes());
            exchange.sendResponseHeaders(200, bs.size());
            exchange.getResponseHeaders().set("Custom-Header-1", "A~HA");
            OutputStream os = exchange.getResponseBody();
            bs.writeTo(os);
            os.flush();
            os.close();
            exchange.close();
        }
    }

    static class MatchDetailsHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            int mID = -1;
            Map<String, String> qsParams = ContextHandlers.queryToMap(exchange.getRequestURI().getQuery());
            try {
                if(qsParams.containsKey(qs_matchID)){
                        mID = Integer.valueOf(qsParams.get(qs_matchID));
                }
            } catch (Exception e) {
                int dummy = 5;
            }

            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            bs.write(("<!DOCTYPE html>\n<html>\n<head>\n").getBytes());
            bs.write(("<style>\n").getBytes());
            bs.write(("div.section{border:0.2vmin solid grey;}\n").getBytes());
            bs.write(("</style>\n").getBytes());
            bs.write(("</head>\n<body>\n").getBytes());
            bs.write((MyHelpers.htmlNavigationLinks()).getBytes());
            if(mID > 0){
                boolean editHinspiel = (mID % 100)/10 < (mID % 10);
                boolean editRueckspiel = !editHinspiel;
                MyHelpers.Match savedM = DataBaseQueries.getMatchByHash(mID);
                if(savedM != null){
                    bs.write(("Match: " + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_firstTeam()) +
                            " vs. " + + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_secondTeam())).getBytes());
                    bs.write(("<div class=\"section\">\n").getBytes()); //start Div-section-hinspiel
                    bs.write(("Hinspiel:<br>\n").getBytes());
                    bs.write(("<br>\nRichter: " + savedM.getRichterHinspiel().x +
                            AppSettings.getTeamLetter(savedM.getRichterHinspiel().y) + "<br>\n").getBytes());
                    bs.write(("Spielfeld: " + (savedM.get_feldNrHinspiel()+1) + "<br>\n").getBytes());
                    bs.write(("Timeslot: " + AppSettings.getTimeSlotStr(savedM.get_timeslotHinspiel()) + "<br>\n").getBytes());
                    //Organisatoren und Richter-Team duerfen Spielstand aendern. Die Gaeste koennen nur lesen:
                    if(editHinspiel && (AppSettings.getRole() == 0 || AppSettings.getRole() == savedM.getRichterHinspiel().y)){
                        bs.write(("<form action=\"/\">").getBytes());
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
                    bs.write(("</div>\n").getBytes());//end Div-section-Rueckspiel
                    bs.write(("<br>\n").getBytes());


                    bs.write(("<div class=\"section\">\n").getBytes()); //start Div-section-Rueckspiel
                    bs.write(("Rueckspiel:<br>\n").getBytes());
                    bs.write(("<br>\nRichter: " + savedM.getRichterRueckspiel().x +
                            AppSettings.getTeamLetter(savedM.getRichterRueckspiel().y) + "<br>\n").getBytes());
                    bs.write(("Spielfeld: " + (savedM.get_feldNrRueckspiel()+1) + "<br>\n").getBytes());
                    bs.write(("Timeslot: " + AppSettings.getTimeSlotStr(savedM.get_timeslotRueckspiel()) + "<br>\n").getBytes());
                    //Organisatoren und Richter-Team duerfen Spielstand aendern. Die Gaeste koennen nur lesen:
                    if(editRueckspiel && (AppSettings.getRole() == 0 || AppSettings.getRole() == savedM.getRichterRueckspiel().y)){
                        bs.write(("<form action=\"/\">").getBytes());
                        bs.write(("\n<input type=\"hidden\" name=\"" + qs_editmatch + "\" value=\"" + savedM.hashCode() + "\">").getBytes());
                        bs.write(("<br>\nPunkte Team " + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_firstTeam()) +
                                ": <input type=\"text\" name=\"" + AppSettings.post_score_team1_rueckspiel +
                                "\"").getBytes());
                        if (savedM.get_firstTeamRueckspielPunkte() >= 0)
                            bs.write((" value=\"" + savedM.get_firstTeamRueckspielPunkte()  +"\"").getBytes());
                        bs.write(("><br>\n").getBytes());

                        bs.write(("Punkte Team " + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_secondTeam()) +
                                ": <input type=\"text\" name=\"" + AppSettings.post_score_team2_rueckspiel + "\"").getBytes());
                        if (savedM.get_secondTeamRueckspielPunkte() >= 0)
                            bs.write((" value=\"" + savedM.get_secondTeamRueckspielPunkte()  +"\"").getBytes());
                        bs.write(("><br>\n").getBytes());
                        bs.write(("<button type=\"submit\">speichern</button><br>\n").getBytes());
                        bs.write(("</form>\n").getBytes());
                    }
                    else{
                        String punkteStr = "";
                        if(savedM.get_firstTeamRueckspielPunkte()>=0)
                            punkteStr += savedM.get_firstTeamRueckspielPunkte();

                        bs.write(("<br>\nPunkte Team " + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_firstTeam()) +
                                ": " + punkteStr +"<br>\n").getBytes());

                        punkteStr = "";
                        if(savedM.get_secondTeamRueckspielPunkte()>=0)
                            punkteStr += savedM.get_secondTeamRueckspielPunkte();

                        bs.write(("Punkte Team " + savedM.groupID() + AppSettings.getTeamLetter(savedM.get_secondTeam()) +
                                ": " + punkteStr + "<br>\n").getBytes());
                    }

                    bs.write(("</div>\n").getBytes());//end Div-section-rueckspiel
                }
            }
            bs.write(("</body></html>").getBytes());

            exchange.sendResponseHeaders(200, bs.size());
            exchange.getResponseHeaders().set("Custom-Header-1", "A~HA");
            OutputStream os = exchange.getResponseBody();
            bs.writeTo(os);
            os.flush();
            os.close();
            exchange.close();
        }
    }

    static class TurnierPdfHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {
            /**
             * Create font object with times roman font & give it name as F1.
             */
            PDFObject fontName = new PDFObject("Font");
            fontName.addKey("Subtype", "/Type1");
            fontName.addKey("BaseFont", "/Times-Roman");
            PDFObject font = new PDFObject("Font", new PDFObject("F1", fontName));

            /**
             * Create text object with above font with size 18 & coordinate position (10,10)
             * with text "Hello World"
             */

            String inhaltStr = "Turnierplan (erstellt am " + LocalDateTime.now() +
                    "); Anzahl Felder: " + AppSettings.get_anzSpielfelder() +
                    "; Anzahl Gruppen: " + AppSettings.get_anzGroups();


            PDFObject text = new PDFObject(4, 0);
            text.addKey("Length", Integer.toString(inhaltStr.getBytes().length));
            text.addTextStream("F1", 10, 10, 300, inhaltStr);


            /**
             * Create page object with above font & text
             */
            PDFObject page = new PDFObject(3, 0, "Page");
            page.addObjectKey("Resources", font);
            page.addObjectReferenceKey("Contents", text);

            /**
             * Create pages object with above page.
             */
            PDFObject pages = new PDFObject(2, 0, "Pages");
            pages.addKey("Count", "1");
            pages.addKey("MediaBox", "[0 0 600 400]");
            pages.addObjectReferenceArrayKey("Kids", page);

            page.addObjectReferenceKey("Parent", pages);

            /**
             * Create root object wrapping pages object.
             */
            PDFObject root = new PDFObject(1, 0, "Catalog");
            root.addObjectReferenceKey("Pages", pages);

            /**
             * Create PDF with abvoe root & all of the objects
             */
            PDF pdf = new PDF(root, pages, page, text);

            /**
             * Write PDF to a file.
             */
            //FileWriter fileWriter = new FileWriter("generatedPDF.pdf");
            //fileWriter.write(pdf.build());
            //fileWriter.close();

            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            bs.write((pdf.build()).getBytes());

            exchange.sendResponseHeaders(200, bs.size());
            exchange.getResponseHeaders().set("Content-Type", "application/pdf");
            OutputStream os = exchange.getResponseBody();
            bs.writeTo(os);
            os.flush();
            os.close();
            exchange.close();
        }
    }

    static class TurnierCsvHandler implements HttpHandler {
        public void handle(HttpExchange exchange) throws IOException {

            ByteArrayOutputStream bs = new ByteArrayOutputStream();

            bs.write(("Zeit").getBytes());
            for(int i=0; i<AppSettings.get_anzSpielfelder(); i++){
                bs.write((";Feld " + (i+1) ).getBytes());
            }
            bs.write(("\n").getBytes());

            int idx=0;
            boolean endeErreicht = false;
            while(!endeErreicht) {
                endeErreicht = true;

                bs.write((AppSettings.getTimeSlotStr(idx)).getBytes());//Zeit-Spalte
                for (int i = 0; i < AppSettings.get_anzSpielfelder(); i++) {

                    bs.write((";").getBytes());
                    try{

                        MyHelpers.FeldSpiele_new f = DataBaseQueries.getTurnierplan_new().get(i);
                        if (f.getAnzahlSpiele() > idx) {
                            endeErreicht = false;
                            Integer h = f.getSpielHashByIdx(idx);
                            if(h > 0) {
                                MyHelpers.SpielStats stats = DataBaseQueries.getSpielStatsByHash(h);
                                bs.write(("" + stats.groupid + AppSettings.getTeamLetter(stats.team1) +
                                        " vs. " + stats.groupid + AppSettings.getTeamLetter(stats.team2) +
                                        " | " + stats.richter.x +
                                        AppSettings.getTeamLetter(stats.richter.y)).getBytes());
                            }
                        }
                    }
                    catch(Exception e){}
                }
                bs.write(("\n").getBytes());
                idx += 1;
            }







            exchange.sendResponseHeaders(200, bs.size());
            exchange.getResponseHeaders().set("Content-Type", "text/csv");
            OutputStream os = exchange.getResponseBody();
            bs.writeTo(os);
            os.flush();
            os.close();
            exchange.close();
        }
    }
}
