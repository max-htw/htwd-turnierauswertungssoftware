import java.util.ArrayList;

public class RoleAdmin_TaskTurnierplan_Data extends RoleWithTaskBase_Data{
    
    public ArrayList<RoleAdmin_TaskTurnierplan_Renderer.PlanItem> planItems = new ArrayList<>();

    @Override
    public StringBuilder htmlOfDerrivedClass(RoleWithTaskBase_Renderer.ActionStringGenerator actionStringGenerator){
        StringBuilder r = new StringBuilder();

        r.append("<p>Turnierplan Items:</p>\n");
        r.append("<table style=\"border-spacing:1em;\">\n");
        if(planItems.size() > 0){
            r.append("<tr><th>Uhrzeit</th>");
            for(int i = 0; i<planItems.get(0).fieldLinks.size(); i++){
                r.append("<th>Feld ").append(i+1).append("</th>");
            }
            r.append("</tr>\n");
        }
        for(RoleAdmin_TaskTurnierplan_Renderer.PlanItem p: planItems){
            r.append("<tr><td>").append(p.uhrZeit).append("</td>");
            for(RoleWithTaskBase_Renderer.HyperLink hl: p.fieldLinks){
                if(hl != null){
                    r.append("<td><a href=\"").
                    append(actionStringGenerator.generateActionString(hl.linkAction)).append("\">").
                    append(hl.linkText).append("</a></td>");
                }
                else{
                    r.append("<td></td>");
                }
            }
            r.append("</tr>\n");
        }
        r.append("</table>");

        return r;
    }


}
