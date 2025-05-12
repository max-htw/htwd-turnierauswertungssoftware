public class RoleTeam_TaskTurnierplan_Renderer_Jonas extends RoleTeam_TaskTurnierplan_Renderer {

    @Override
    public StringBuilder renderResponse() {
        // Testdaten mit 2 Spielfeldern
        this.daten.planItems.add(new PlanItem("10:00", 1, "Team A", "Team B", "Schiri X", "", ""));
        this.daten.planItems.add(new PlanItem("10:00", 2, "Team C", "Team D", "Schiri Y", "", ""));
        this.daten.planItems.add(new PlanItem("10:30", 1, "Team E", "Team F", "Schiri Z", "", ""));
        this.daten.planItems.add(new PlanItem("10:30", 2, "Team G", "Team H", "Schiri W", "", ""));

        StringBuilder html = new StringBuilder();

        html.append("<html>");
        html.append("<head><title>Turnierplan</title>");
        html.append("<style>");
        html.append("body { font-family: Arial, sans-serif; margin: 20px; }");
        html.append("table { border-collapse: collapse; width: 100%; margin-top: 20px; }");
        html.append("th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }");
        html.append("th { background-color: #f2f2f2; }");
        html.append(".buttons { margin-top: 20px; }");
        html.append(".buttons button { margin-right: 10px; padding: 8px 16px; border: none; cursor: pointer; }");
        html.append(".button-highlight { background-color: #4CAF50; color: white; }"); // Grüne Hintergrundfarbe
        html.append(".button-highlight:hover { background-color: #45a049; }"); // Hover-Effekt
        html.append("</style>");
        html.append("</head>");
        html.append("<body>");

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
            html.append("<td>").append(item.feldNr).append("</td>");
            html.append("<td>").append(item.team1Name).append("</td>");
            html.append("<td>").append(item.team2Name).append("</td>");
            html.append("<td>").append(item.shiriName).append("</td>");
            html.append("</tr>");
        }
        html.append("</table>");

        html.append("</body></html>");

        return html;
    }
}
