import java.util.ArrayList;

public class RoleTeam_TaskTurnierplan_Data extends  RoleWithTaskBase_Data{
    public String teamName;
    public String gruppenName;
    public ArrayList<RoleWithTaskBase_Renderer.NaviLink> navLinks = new ArrayList<>();
    public ArrayList<RoleTeam_TaskTurnierplan_Renderer.PlanItem> planItems = new ArrayList<>();

    @Override
    public StringBuilder htmlOfDerrivedClass(){
        StringBuilder r = new StringBuilder();

        r.append("<p>NavLinks:<br>\n");

        for(RoleWithTaskBase_Renderer.NaviLink l: navLinks){
            r.append("<a href=\"" + l.linkUrl + "\">" + l.linkText + "</a><br>");
        }
        r.append("</p>\n");
        r.append("<p>Turnierplan Items:</p>\n");
        r.append("<table>\n");
        for(RoleTeam_TaskTurnierplan_Renderer.PlanItem p: planItems){
            r.append("<tr><td>" + p.uhrZeit + "</td><td>" + p.feldNr + "</td><td>" +
                    p.team1Name + "</td><td>" + p.team2Name + "</td><td>" + p.shiriName +
                    "</td><td>");
            if(!p.linkUrl.isEmpty()){
                r.append("<a href=\"" + p.linkUrl + "\">" + p.linkText + "</a>");
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
