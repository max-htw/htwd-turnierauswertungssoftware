import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class ContextHandlers {
    private static  String qs_setAnzahlTeams = "setAnzTeams";
    private static  String qs_setAnzahlSpielfelder = "setAnzFields";
    private static  String qs_setRole = "setRole";
    private static  String qs_matchID = "matchid";

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
            Map<String,String> qsParams = ContextHandlers.queryToMap(exchange.getRequestURI().getQuery());
            try{
                if(qsParams.containsKey(qs_setAnzahlTeams)){
                    AppSettings.set_anzTeams(Integer.valueOf(qsParams.get(qs_setAnzahlTeams)));
                }
                else if(qsParams.containsKey(qs_setAnzahlSpielfelder)){
                    AppSettings.set_anzSpielfelder(Integer.valueOf(qsParams.get(qs_setAnzahlSpielfelder)));
                }
                else if(qsParams.containsKey(qs_setRole)){
                    AppSettings.setRole(Integer.valueOf(qsParams.get(qs_setRole)));
                }
            }
            catch (Exception e){}

            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            bs.write(("<!DOCTYPE html>\n<html>\n<head>\n").getBytes());
            bs.write(("<style>\n").getBytes());
            bs.write(("td{text-align:center;vertical-align:middle;min-width:9vmin;height:9vmin;font-size:4vmin;font-weight:bold;}\n").getBytes());
            bs.write(("td.hinSpiel{border-top:0.2vmin solid grey; border-right:0.2vmin solid grey;}\n").getBytes());
            bs.write(("td.stats{border-bottom:0.2vmin solid grey;font-size:3vmin;font-weight:normal;}").getBytes());
            bs.write(("td.trenner{min-width:4vmin;}").getBytes());
            bs.write(("td a:link, td a:visited, td a:hover, td a:active{text-decoration:none;\n").getBytes());
            bs.write(("</style>\n").getBytes());
            bs.write(("</head>\n<body>\n").getBytes());
            //bs.write(("<script>document.write('width: ' + document.documentElement.clientWidth + '<br>')</script>\n").getBytes());
            bs.write((MyHelpers.htmlNavigationLinks()).getBytes());

            bs.write(("<div style=\"border: 0.2vmin solid #0c0;padding: 1rem;width: 40vmin;font-size:3vmin;\">\n").getBytes());
            bs.write(("<span style=\"text-decoration:underline;\">Current settings</span><br><br>\n").getBytes());
            bs.write(("Anzahl Teams: " + AppSettings.get_anzTeams()+"<br>\n").getBytes());
            String trn = " (aendern zu ";
            for (int i=3; i<=AppSettings.get_maxAnzTeams(); i++){
                if(i != AppSettings.get_anzTeams()){
                    bs.write((trn + "<a href=\"./?" + qs_setAnzahlTeams + "=" + i + "\">" + i + "</a>").getBytes());
                    trn = " | ";
                }
            }

            bs.write((")<br><br>\nAnzahl Spielfelder: " + AppSettings.get_anzSpielfelder()+"<br>\n").getBytes());
            trn = " (aendern zu ";
            for (int i=1; i<=AppSettings.get_maxAnzSpielfelder(); i++){
                if(i != AppSettings.get_anzSpielfelder()){
                    bs.write((trn + "<a href=\"./?" + qs_setAnzahlSpielfelder + "=" + i + "\">" + i + "</a>").getBytes());
                    trn = " | ";
                }
            }

            bs.write((")<br><br>\nBenutzerrole: " + AppSettings.getRoleStr(AppSettings.getRole()) + "<br>\n").getBytes());
            trn = "(aendern zu ";
            int iVals[] = {-1, 0, 3};
            for (int i: iVals){
                if(i != AppSettings.getRole()){
                    bs.write((trn + "<a href=\"./?" + qs_setRole + "=" + i + "\">" + AppSettings.getRoleStr(i) + "</a>").getBytes());
                    trn = " | ";
                }
            }
            bs.write((")<br>\n").getBytes());

            bs.write(("</div>\n").getBytes());
            bs.write(("<br><br><span style=\"font-size:2vw\">Aktueller Spielstand:</span><br>\n").getBytes());
            bs.write(("<table>\n").getBytes());

            ArrayList<MyHelpers.AuswertungsEintrag> auswertung = DataBaseQueries.calculateAuswertung();
            int cntI = 0, cntJ = 0;
            for(MyHelpers.AuswertungsEintrag a: auswertung){
                int i = a.teamId;
                cntI += 1;
                bs.write(("<tr>\n").getBytes());
                for(MyHelpers.AuswertungsEintrag b: auswertung){
                    int j = b.teamId;
                    cntJ += 1;
                    MyHelpers.IntPair ts = DataBaseQueries.getSingleHinspielScore(new MyHelpers.IntPair(i,j));
                    if(cntJ<cntI) bs.write(("<td></td>\n").getBytes());
                    else if(cntJ==cntI){
                        bs.write(("<td style=\"color:white;background-color:"
                                + AppSettings.getTeamColor(i) + ";\">" +
                                AppSettings.getTeamLetter(i) +
                                "</td>\n").getBytes());
                    }
                    else{
                        bs.write(("<td class=\"hinSpiel\">").getBytes());
                        String obenStr = ts.x>=0?String.valueOf(ts.x):"--";
                        String untenStr= ts.y>=0?String.valueOf(ts.y):"--";
                        if((ts.x>=0 && ts.y>=0) || (AppSettings.getRole() >= 0)) {
                            int hashID = i<j?(i*10+j):(j*10+i);
                            bs.write(("<a href=\"/match/?" + qs_matchID + "=" + hashID + "\">" ).getBytes());
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

                try {
                    bs.write(("<table>\n<tr>\n").getBytes());
                    for(int i=0; i<AppSettings.get_anzSpielfelder(); i++){
                        bs.write(("<th>Feld " + (i+1) + "</th>\n").getBytes());
                    }
                    bs.write(("</tr>\n").getBytes());

                    int idx=0;
                    boolean endeErreicht = false;
                    while(!endeErreicht) {
                        endeErreicht = true;
                        bs.write(("<tr>\n").getBytes());
                        for (int i = 0; i < AppSettings.get_anzSpielfelder(); i++) {

                            bs.write(("<td>").getBytes());
                            MyHelpers.FeldSpiele f = DataBaseQueries.getTurnierplan().get(i);
                            if (f.getAnzahlSpiele() > idx) {
                                endeErreicht = false;
                                MyHelpers.IntPair match = f.getMatchByIdx(idx);
                                int r = f.getRichterByIdx(idx);
                                bs.write((AppSettings.getTeamLetter(match.x) +
                                        " vs. " + AppSettings.getTeamLetter(match.y) +
                                        ", Richter " + AppSettings.getTeamLetter(r)).getBytes());
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
            }

            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            bs.write(("<!DOCTYPE html>\n<html>\n<head>\n").getBytes());
            bs.write(("<style>\n").getBytes());
            //bs.write(("td{text-align:center;vertical-align:middle;min-width:30vmin;height:5vmin;font-size:3vmin;}\n").getBytes());
            bs.write(("</style>\n").getBytes());
            bs.write(("</head>\n<body>\n").getBytes());
            bs.write((MyHelpers.htmlNavigationLinks()).getBytes());
            if(mID > 0){
                MyHelpers.IntPair m = MyHelpers.IntPair.hashDecode(mID);
                MyHelpers.IntPair savedM = null;
                int savedR = -1;
                int savedF = -1;
                for(int i = 0; i<AppSettings.get_anzSpielfelder();i++){
                    try {
                        for (int idx = 0; idx < DataBaseQueries.getTurnierplan().get(i).getAnzahlSpiele(); idx++) {
                            MyHelpers.IntPair tmpM = DataBaseQueries.getTurnierplan().get(i).getMatchByIdx(idx);
                            if(tmpM != null && tmpM.x == m.x && tmpM.y == m.y){
                                savedM = tmpM;
                                savedR = DataBaseQueries.getTurnierplan().get(i).getRichterByIdx(idx);
                                savedF = i;
                                break;
                            }
                        }
                    } catch (Exception e) {
                        int tmp = 5;
                    }
                    if(savedM != null){
                        break;
                    }
                }
                if(savedM != null){
                    bs.write(("Match: " + AppSettings.getTeamLetter(savedM.x) + " vs. " +
                            AppSettings.getTeamLetter(savedM.y)).getBytes());
                    bs.write(("<br>\nRichter: " + AppSettings.getTeamLetter(savedR) + "<br>\n").getBytes());
                    bs.write(("Spielfeld: " + (savedF+1) + "<br>\n").getBytes());
                    MyHelpers.IntPair score = DataBaseQueries.getSingleHinspielScore(savedM);
                    //Organisatoren und Richter-Team duerfen Spielstand aendern. Die Gaeste koennen nur lesen:
                    if(AppSettings.getRole() == 0 || AppSettings.getRole() == savedR){
                        bs.write(("<br>\nPunkte Team " + AppSettings.getTeamLetter(savedM.x) +
                                ": <input type=\"text\" name=\"score_team_" + savedM.x + "\"").getBytes());
                        if (score.x >= 0)
                            bs.write((" value=\"" + score.x  +"\"").getBytes());
                        bs.write(("><br>\n").getBytes());

                        bs.write(("Punkte Team " + AppSettings.getTeamLetter(savedM.y) +
                                ": <input type=\"text\" name=\"score_team_" + savedM.y + "\"").getBytes());
                        if (score.y >= 0)
                            bs.write((" value=\"" + score.y  +"\"").getBytes());
                        bs.write(("><br>\n").getBytes());
                        bs.write(("<button type=\"button\" disabled>speichern</button><br>\n").getBytes());
                    }
                    else{
                        bs.write(("<br>\nPunkte Team " + AppSettings.getTeamLetter(savedM.x) +
                                ": " + score.x +"<br>\n").getBytes());

                        bs.write(("Punkte Team " + AppSettings.getTeamLetter(savedM.y) +
                                ": " + score.y + "<br>\n").getBytes());
                    }
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
}
