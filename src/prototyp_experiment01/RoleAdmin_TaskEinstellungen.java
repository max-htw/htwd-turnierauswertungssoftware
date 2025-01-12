import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;

public class RoleAdmin_TaskEinstellungen extends RoleWithTaskBase {

    RoleAdmin_TaskEinstellungen(ByteArrayOutputStream s, Map<String, String> q) {
        super(s, q);
    }

    @Override
    public void handleRequest() throws IOException {
        try {
            if (qs.containsKey(qs_setAnzahlGroups)) {
                AppSettings.set_anzGroups(Integer.valueOf(qs.get(qs_setAnzahlGroups)));
            }
            else if (qs.containsKey(qs_setAnzahlTeams)) {
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

            } else if (qs.containsKey(qs_needRueckspiele)){
                AppSettings.set_needRueckspiele(Boolean.valueOf((qs.get(qs_needRueckspiele))));
            }else if(qs.containsKey(AppSettings.post_saveTurnierAsName)){
                String s = String.valueOf(AppSettings.post_saveTurnierAsName);
                DataBaseQueries.saveCurrentTurnierToArchive(qs.get(s));
            }

        }
        catch (Exception e) {
            int dummy = 1;
        }

        bs.write(("<!DOCTYPE html>\n<html>\n<head>\n").getBytes());
        bs.write(("<style>\n").getBytes());
        bs.write(("td{text-align:center;vertical-align:middle;min-width:9vmin;height:9vmin;font-size:4vmin;font-weight:bold;}\n").getBytes());
        bs.write(("td.hinSpiel{border-top:0.2vmin solid grey; border-right:0.2vmin solid grey;}\n").getBytes());
        bs.write(("td.stats{border-bottom:0.2vmin solid grey;font-size:3vmin;font-weight:normal;}").getBytes());
        bs.write(("td.trenner{min-width:4vmin;}").getBytes());
        bs.write(("td a:link, td a:visited, td a:hover, td a:active{text-decoration:none;}\n").getBytes());
        bs.write(("div.section{border:0.2vmin solid grey;padding:5px;}\n").getBytes());
        bs.write((MyHelpers.dropDownCSS()).getBytes());
        bs.write((MyHelpers.btnLinkCss()).getBytes());
        bs.write(("</style>\n").getBytes());
        bs.write(("<meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\">").getBytes());
        bs.write(("</head>\n<body>\n").getBytes());
        //bs.write(("<script>document.write('width: ' + document.documentElement.clientWidth + '<br>')</script>\n").getBytes());
        bs.write((MyHelpers.dropDownForTeam(true, pathPrefix, 0, 0)).getBytes());
        bs.write((htmlNavigationLinks()).getBytes());

        bs.write(("<div style=\"border: 0.2vmin solid #0c0;padding: 1rem;width: 40vmin;font-size:3vmin;\">\n").getBytes());
        bs.write(("<span style=\"text-decoration:underline;\">Aktuelles Turnier</span><br><br>\n").getBytes());

        bs.write(("<div class=\"" + StringsCSS.section.name() + "\">").getBytes()); // start Abschnitt Gruppen
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

        bs.write(("<br>\nmit Rueckspielen: " + AppSettings.needRueckspiele() + "<br>\n").getBytes());
        trn = "(aendern zu ";
        bs.write((trn + "<a href=\"./einstellungen?" + qs_needRueckspiele + "="
                + !(AppSettings.needRueckspiele()) + "\">"
                + !(AppSettings.needRueckspiele())+ "</a>)").getBytes());

        bs.write(("<br><br>\nAnzahl Spielfelder: " + AppSettings.get_anzSpielfelder()+"<br>\n").getBytes());
        trn = " (aendern zu ";
        for (int i=1; i<=AppSettings.get_maxAnzSpielfelder(); i++){
            if(i != AppSettings.get_anzSpielfelder()){
                bs.write((trn + "<a href=\"./einstellungen?" + qs_setAnzahlSpielfelder + "=" + i + "\">" + i + "</a>").getBytes());
                trn = " | ";
            }
        }


        bs.write((")<br><br>\n").getBytes());

        bs.write(("Vorausfuellen mit zufaelligen Punktzahlen: " + AppSettings.getNeedPrefillScores() + "<br>\n").getBytes());
        trn = "(aendern zu ";
        bs.write((trn + "<a href=\"./einstellungen?" + qs_setPrefillScores + "=" + !(AppSettings.getNeedPrefillScores()) + "\">"
                + !(AppSettings.getNeedPrefillScores())+ "</a>").getBytes());

        bs.write((")<br>\n").getBytes());

        bs.write(("</div>\n").getBytes());

        bs.write(("<br>\n<form>\n").getBytes());
        bs.write(("Aktuelles Turnier speichern unter:" +
                "<input type=\"text\" name=\"" + AppSettings.post_saveTurnierAsName + "\" value=\"" +
                LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))
                + "\">").getBytes());

        bs.write(("<button type=\"submit\">speichern</button>\n").getBytes());
        bs.write(("</form>\n").getBytes());
        bs.write(("<br>\nEin gespeichertes Turnier laden:<br>\n").getBytes());
        int idx = 0;
        for(String s: DataBaseQueries.GetTurnierArchivenames()){
            bs.write(("<a href=\"" + pathPrefix + "/" + StringsRole.Admin.name().toLowerCase() +
                    "/" + StringsRole.AdminTasks.Status.name().toLowerCase() +
                    "/?" + qs_loadArchiv + "=" + idx + "\">" + s +"</a><br>\n").getBytes());
            idx++;
        }

        bs.write(("<br><br><span id=\"javaScriptTestLbl\">JavaScript Test: fehlgeschlagen.</span>\n").getBytes());
        bs.write(("<script>\ndocument.getElementById('javaScriptTestLbl').innerHTML = \"JavaScript Test: erfolgreich.\"\n</script>").getBytes());
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
