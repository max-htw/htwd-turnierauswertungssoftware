public class RoleTeam_TaskTurnierplan_Renderer extends RoleWithTaskBase_Renderer_Team<RoleTeam_TaskTurnierplan_Data>{

    @Override
    public RoleTeam_TaskTurnierplan_Data getEmptyDaten() {
        return new RoleTeam_TaskTurnierplan_Data();
    }

    RoleTeam_TaskTurnierplan_Renderer(){
        StringBuilder s = new StringBuilder();

        s.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        s.append("table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
        s.append("th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }");
        s.append("th { background-color: #f2f2f2; }");
        s.append(".buttons { margin-top: 20px; }");
        s.append(".buttons button { margin-right: 10px; padding: 8px 16px; border: none; cursor: pointer; }");
        s.append(".button-highlight { background-color: #4CAF50; color: white; }"); // Grüne Hintergrundfarbe
        s.append(".button-highlight:hover { background-color: #45a049; }"); // Hover-Effekt

        styleSections.add(s.toString());
    }

        @Override
    public StringBuilder renderHtmlResponse() {
        // Testdaten mit 2 Spielfeldern
      /*
        this.daten.planItems.add(new PlanItem("10:00", 1, "Team A", "Team B", "Schiri X", "", ""));
        this.daten.planItems.add(new PlanItem("10:00", 2, "Team C", "Team D", "Schiri Y", "", ""));
        this.daten.planItems.add(new PlanItem("10:30", 1, "Team E", "Team F", "Schiri Z", "", ""));
        this.daten.planItems.add(new PlanItem("10:30", 2, "Team G", "Team H", "Schiri W", "", ""));
      */
        StringBuilder html = new StringBuilder();



        html.append("<h1>Turnierplan Übersicht</h1>");

        // Buttons zur späteren Filterung mit farblichem Hervorheben
        html.append("<div class='buttons'>");
        html.append("<button class='button-highlight' onclick=\"alert('Nur Feld 1 anzeigen')\">Feld 1</button>");
        html.append("<button class='button-highlight' onclick=\"alert('Nur Feld 2 anzeigen')\">Feld 2</button>");
        html.append("<button class='button-highlight' onclick=\"alert('Alle Spiele anzeigen')\">Alle Felder</button>");
        html.append("</div>");

        // Tabelle mit 2 Feldern
        html.append("<table>");
        html.append("<tr><th>Zeit</th><th>Feld</th><th>Team A</th><th>Team B</th><th>Schiedsrichter</th></tr>");
        for (PlanItem item : this.daten.planItems) {
            html.append("<tr>");
            html.append("<td>").append(item.uhrZeit).append("</td>");
            html.append("<td>").append(item.feldNr + 1).append("</td>");
            html.append("<td>").append(item.team1Name).append("</td>");
            html.append("<td>").append(item.team2Name).append("</td>");
            html.append("<td>").append(item.shiriName).append("</td>");
            html.append("</tr>");
        }
        html.append("</table>");

        html.append("</body></html>");

        return html;
    }


    public  static  class PlanItem{
        String uhrZeit;
        int feldNr;
        String team1Name;
        String team2Name;
        String shiriName;
        ActionForRoleAndTask linkAction;
        String linkText;
        PlanItem(String zeit, int feld, String team1, String team2, String shiri, ActionForRoleAndTask linkAction, String text){
            uhrZeit = zeit;
            feldNr = feld;
            team1Name = team1;
            team2Name = team2;
            shiriName = shiri;
            this.linkAction = linkAction;
            linkText = text;
        }
    }

}
