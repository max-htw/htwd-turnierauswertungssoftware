import java.util.ArrayList;

public class RoleTeam_TaskTurnierplan_Data extends  RoleWithTaskBase_Data{
    public String teamName;
    public String gruppenName;
    public ArrayList<RoleWithTaskBase_Renderer.HyperLink> navLinks = new ArrayList<>();
    public ArrayList<RoleTeam_TaskTurnierplan_Renderer.PlanItem> planItems = new ArrayList<>();

    @Override
    public StringBuilder htmlOfDerrivedClass(RoleWithTaskBase_Renderer.ActionStringGenerator actionStringGenerator){
        StringBuilder r = new StringBuilder();

        r.append("<p>NavLinks:<br>\n");

        for(RoleWithTaskBase_Renderer.HyperLink l: navLinks){
            r.append("<a href=\"" + actionStringGenerator.generateActionString(l.linkAction) + "\">" + l.linkText + "</a><br>");
        }
        r.append("</p>\n");
        r.append("<p>Turnierplan Items:</p>\n");
        r.append("<table>\n");
        for(RoleTeam_TaskTurnierplan_Renderer.PlanItem p: planItems){
            r.append("<tr><td>" + p.uhrZeit + "</td><td>" + p.feldNr + "</td><td>" +
                    p.team1Name + "</td><td>" + p.team2Name + "</td><td>" + p.shiriName +
                    "</td><td>");
            if(!(p.linkAction == null)){
                r.append("<a href=\"" + actionStringGenerator.generateActionString(p.linkAction) + "\">" + p.linkText + "</a>");
            }
            else{
                r.append(p.linkText);
            }
            r.append("</td></tr>\n");
        }
        r.append("</table>");

        return r;
    }
}
