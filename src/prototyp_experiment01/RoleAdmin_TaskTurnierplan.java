import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

public class RoleAdmin_TaskTurnierplan extends RoleWithTaskBase{
    RoleAdmin_TaskTurnierplan(ByteArrayOutputStream s, Map<String, String> q) {
        super(s,q);
    }

    @Override
    public void handleRequest() throws IOException {
        bs.write(("<!DOCTYPE html>\n<html>\n<head>\n").getBytes());
        bs.write(("<style>\n").getBytes());
        bs.write(("td{text-align:center;vertical-align:middle;min-width:30vmin;height:5vmin;font-size:3vmin;}\n").getBytes());
        bs.write(("td." + StringsCSS.gameOver.name() + "{background-color:#E5E4E2;}\n").getBytes());
        bs.write(("td." + StringsCSS.gameFuture.name() + "{}\n").getBytes());
        bs.write((MyHelpers.btnLinkCss()).getBytes());
        bs.write(("</style>\n").getBytes());
        bs.write(("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">").getBytes());
        bs.write(("</head>\n<body>\n").getBytes());
        bs.write((htmlNavigationLinks()).getBytes());
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
                    MyHelpers.FeldSpiele f = DataBaseQueries.getTurnierplan().get(i);
                    if (f.getAnzahlSpiele() > idx) {
                        endeErreicht = false;
                        Integer h = f.getSpielHashByIdx(idx);
                        if(h > 0) {
                            MyHelpers.SpielStats stats = DataBaseQueries.getSpielStatsByHash(h);
                            if(stats.team1Punkte >= 0 && stats.team2Punkte >= 0){
                                bs.write(("<td class=\"" + StringsCSS.gameOver.name() + "\">").getBytes());
                            }
                            else {
                                bs.write(("<td class=\"" + StringsCSS.gameFuture.name() + "\">").getBytes());
                            }

                            bs.write(("<a href=\"" + pathPrefix + "/" + StringsRole.Admin.name().toLowerCase() +
                                    "/" + StringsRole.AdminTasks.Matchdetails.name().toLowerCase() +
                                    "/?" + qs_matchID + "=" + h + "\">").getBytes());
                            bs.write(("" + stats.groupid + AppSettings.getTeamLetter(stats.team1) +
                                    " vs. " + stats.groupid + AppSettings.getTeamLetter(stats.team2) +
                                    " | " + stats.richter.x +
                                    AppSettings.getTeamLetter(stats.richter.y)).getBytes());
                            bs.write(("</a>").getBytes());
                        }
                        else{
                            bs.write(("<td>").getBytes());
                        }
                    }
                    else{
                        bs.write(("<td>").getBytes()); // leere Zellen braucht man unten, bei den Spalten mit weniger Eintraege.
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
