public class RoleTeam_TaskTurnierplan_Renderer_Max extends RoleTeam_TaskTurnierplan_Renderer{

  @Override
  public StringBuilder renderResponse() {
    StringBuilder r = new StringBuilder();

    System.out.println(">>> renderResponse wurde aufgerufen!");

    r.append("<h1>Turnierplan Übersicht</h1>\n");
    r.append("<p>Team: ").append(daten.teamName).append("</p>\n");
    r.append("<p>Gruppe: ").append(daten.gruppenName).append("</p>\n");

    // hier die HTML-Tabelle aus den Daten anhängen
    r.append(daten.htmlOfDerrivedClass());

    return r;
}


}
