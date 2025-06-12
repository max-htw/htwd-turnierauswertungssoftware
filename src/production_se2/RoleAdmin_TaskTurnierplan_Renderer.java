import java.util.ArrayList;

public class RoleAdmin_TaskTurnierplan_Renderer extends RoleWithTaskBase_Renderer_Admin<RoleAdmin_TaskTurnierplan_Data>{

    @Override
    public RoleAdmin_TaskTurnierplan_Data getEmptyDaten() {
        return new RoleAdmin_TaskTurnierplan_Data();
    } 

    public  static  class PlanItem{
        String uhrZeit;
        ArrayList<HyperLink> fieldLinks = new ArrayList<>();
        PlanItem(String zeit, ArrayList<HyperLink> links){
            this.uhrZeit = zeit;
            this.fieldLinks = links;
        }
    }

    @Override
    public StringBuilder renderHtmlResponse() {
      StringBuilder r = new StringBuilder();

      r.append("<div class='p-6 space-y-6'>");
      // Testdaten mit 2 Spielfeldern
      this.daten.planItems.add(new RoleTeam_TaskTurnierplan_Renderer.PlanItem("10:00", 1, "Team A", "Team B", "Schiri X", null, ""));
      this.daten.planItems.add(new RoleTeam_TaskTurnierplan_Renderer.PlanItem("10:00", 2, "Team C", "Team D", "Schiri Y", null, ""));
      this.daten.planItems.add(new RoleTeam_TaskTurnierplan_Renderer.PlanItem("10:30", 1, "Team E", "Team F", "Schiri Z", null, ""));
      this.daten.planItems.add(new RoleTeam_TaskTurnierplan_Renderer.PlanItem("10:30", 2, "Team G", "Team H", "Schiri W", null, ""));
    

      // Oben: Überschrift + Dropdown
      r.append("<div class='flex flex-col md:flex-row justify-start items-center gap-4'>");
      r.append("<h2 class='text-2xl font-semibold'>Auswahl des Feldes:</h2>");

      r.append("<form method='get' action='#'>"); // Dummy-URL für Beispiel

      r.append("<select onchange='this.form.submit()' ");
      r.append("class='px-4 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500' ");
      r.append("name='feld'>"); // Beispiel-Name

      r.append("<option value='alle'>Alle Felder</option>");
      r.append("<option value='feld1'>Feld 1</option>");
      r.append("<option value='feld2'>Feld 2</option>");

      r.append("</select>");
      r.append("</form>");
      r.append("</div>");

      // Tabelle
      r.append("<div class='overflow-x-auto'>");
      r.append("<table class='w-full text-center border border-gray-200 shadow-sm mt-4'>");

      // Tabellenkopf
      r.append("<thead class='bg-primary'>");
      r.append("<tr>");
      r.append("<th class='px-4 py-2 border'>Zeit</th>");
      r.append("<th class='px-4 py-2 border'>Feld</th>");
      r.append("<th class='px-4 py-2 border'>Team A</th>");
      r.append("<th class='px-4 py-2 border'>Team B</th>");
      r.append("<th class='px-4 py-2 border'>Schiedsrichter</th>");
      r.append("<th class='px-4 py-2 border'>Ergebnis</th>");
      r.append("</tr>");
      r.append("</thead>");

      // Tabellenkörper
      r.append("<tbody>");
      for (RoleTeam_TaskTurnierplan_Renderer.PlanItem item : this.daten.planItems) {
          r.append("<tr class='hover:bg-gray-50'>");
          r.append("<td class='px-4 py-2 border'>").append(item.uhrZeit).append("</td>");
          r.append("<td class='px-4 py-2 border'>").append(item.feldNr).append("</td>");
          r.append("<td class='px-4 py-2 border'>").append(item.team1Name).append("</td>");
          r.append("<td class='px-4 py-2 border'>").append(item.team2Name).append("</td>");
          r.append("<td class='px-4 py-2 border'>").append(item.shiriName).append("</td>");
          r.append("<td class='px-4 py-2 border'>").append(item.linkAction).append("</td>");
          r.append("</tr>");
      }
      r.append("</tbody>");

      r.append("</table>");
      r.append("</div>");

      r.append("</div>");

      return r;
    }

}
