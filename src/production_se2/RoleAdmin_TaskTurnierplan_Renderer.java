import java.util.ArrayList;

public class RoleAdmin_TaskTurnierplan_Renderer extends RoleWithTaskBase_Renderer_Admin<RoleAdmin_TaskTurnierplan_Data>{

    // Alte PlanItem-Klasse
    /*public static class PlanItem {
        String uhrZeit;
        int feldNr;
        String team1Name;
        String team2Name;
        String shiriName;
        Object linkAction; // Use the correct type here
        String ergebnis;

        public PlanItem(String uhrZeit, int feldNr, String team1Name, String team2Name, String shiriName, Object linkAction, String ergebnis) {
            this.uhrZeit = uhrZeit;
            this.feldNr = feldNr;
            this.team1Name = team1Name;
            this.team2Name = team2Name;
            this.shiriName = shiriName;
            this.linkAction = linkAction;
            this.ergebnis = ergebnis;
        }
    }*/


    @Override
    public StringBuilder renderHtmlResponse() {
      StringBuilder r = new StringBuilder();

      r.append("<div class='p-6 space-y-6'>");
      // Testdaten mit 2 Spielfeldern
      /*this.daten.planItems.add(new RoleTeam_TaskTurnierplan_Renderer.PlanItem("10:00", 1, "Team A", "Team B", "Schiri X", null, ""));
      this.daten.planItems.add(new RoleTeam_TaskTurnierplan_Renderer.PlanItem("10:00", 2, "Team C", "Team D", "Schiri Y", null, ""));
      this.daten.planItems.add(new RoleTeam_TaskTurnierplan_Renderer.PlanItem("10:30", 1, "Team E", "Team F", "Schiri Z", null, ""));
      this.daten.planItems.add(new RoleTeam_TaskTurnierplan_Renderer.PlanItem("10:30", 2, "Team G", "Team H", "Schiri W", null, ""));*/


      // Oben: Überschriften + Dropdowns
      r.append("<div class='flex flex-col md:flex-row justify-start items-center md:gap-12'>");

      // Feld-Auswahl
      r.append("<div>");
      r.append("<h2 class='text-2xl font-semibold'>Auswahl des Feldes:</h2>");
      r.append("<form method='get' action='#'>");
      r.append("<select onchange='this.form.submit()' ");
      r.append("class='px-4 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500' ");
      r.append("name='feld'>");
      r.append("<option value='alle'>Alle Felder</option>");
      r.append("<option value='feld1'>Feld 1</option>");
      r.append("<option value='feld2'>Feld 2</option>");
      r.append("</select>");
      r.append("</form>");
      r.append("</div>");

      // Team-Auswahl
      r.append("<div>");
      r.append("<h2 class='text-2xl font-semibold'>Auswahl des Teams:</h2>");
      r.append("<form method='get' action='#'>");
      r.append("<select onchange='this.form.submit()' ");
      r.append("class='px-4 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500' ");
      r.append("name='team'>");
      r.append("<option value='alle'>Alle Teams</option>");
      r.append("<option value='team1'>Team 1</option>");
      r.append("<option value='team2'>Team 2</option>");
      // Optional: Dynamisch aus Daten füllen, z.B. für(String t : daten.teamNames) { ... }
      r.append("</select>");
      r.append("</form>");
      r.append("</div>");

      r.append("</div>"); // Ende Flex-Container

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
      for (RoleAdmin_TaskTurnierplan_Renderer.TimeSlotPlanItem item : this.daten.planItems) {
        String zeitText = item.timeSlotString;
        for (int i = 0; i < item.fieldLinks.size(); i++) {
            r.append("<tr class='hover:bg-gray-50'>");

            r.append("<td class='px-4 py-2 border'>").append(zeitText).append("</td>");
            r.append("<td class='px-4 py-2 border'>").append(i + 1).append("</td>");
            r.append("<td class='px-4 py-2 border'>").append(item.teamANames.get(i)).append("</td>");
            r.append("<td class='px-4 py-2 border'>").append(item.teamBNames.get(i)).append("</td>");
            r.append("<td class='px-4 py-2 border'>").append(item.schiriNames.get(i)).append("</td>");

            String lText = "";
            if (item.ergebnisLinks.get(i) != null) {
                lText = item.ergebnisLinks.get(i).linkText;
            }

            r.append("<td class='px-4 py-2 border'>");
            r.append("<form method='post' action='?action=setScore' style='display:inline'>");
            r.append("<input type='hidden' name='slot' value='").append(item.timeSlotNr).append("'/>");
            r.append("<input type='hidden' name='feld' value='").append(i).append("'/>");
            r.append("<input name='score' value='").append(lText).append("' ");
            r.append("onblur='this.form.submit()' ");
            r.append("onkeydown='if(event.key===\"Enter\"){this.form.submit();}' ");
            r.append("class='text-center w-24 border rounded px-2 py-1'/>");
            r.append("</form>");
            r.append("</td>");

            r.append("</tr>");
            zeitText = "";
        }
      }
      r.append("</tbody>");

      r.append("</table>");
      r.append("</div>");

      r.append("</div>");

      return r;
    }

    @Override
    public RoleAdmin_TaskTurnierplan_Data getEmptyDaten() {
        return new RoleAdmin_TaskTurnierplan_Data();
    }

    public  static  class TimeSlotPlanItem{
        public int timeSlotNr;
        public String timeSlotString;

        public ArrayList<HyperLink> fieldLinks = new ArrayList<>();
        public ArrayList<String> teamANames = new ArrayList<>();
        public ArrayList<String> teamBNames = new ArrayList<>();
        public ArrayList<String> schiriNames = new ArrayList<>();
        public ArrayList<HyperLink> ergebnisLinks = new ArrayList<>();

        TimeSlotPlanItem(int timeSlot,
                         String timeSlotText,
                         ArrayList<HyperLink> fieldLinks,
                         ArrayList<String> teamANames,
                         ArrayList<String> teamBNames,
                         ArrayList<String> schiriNames,
                         ArrayList<HyperLink> ergebnisLinks){
            this.timeSlotNr = timeSlot;
            this.timeSlotString = timeSlotText;
            this.fieldLinks = fieldLinks;
            this.teamANames = teamANames;
            this.teamBNames = teamBNames;
            this.schiriNames = schiriNames;
            this.ergebnisLinks = ergebnisLinks;
        }
    }

}
