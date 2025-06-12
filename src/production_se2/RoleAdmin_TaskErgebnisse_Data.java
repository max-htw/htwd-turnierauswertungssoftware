import java.util.ArrayList;

public class RoleAdmin_TaskErgebnisse_Data extends RoleWithTaskBase_Data {

    public ArrayList<RoleWithTaskBase_Renderer.HyperLink> groupSelectionLinks = new ArrayList<>();
    public RoleWithTaskBase_Renderer.ActionForRoleAndTask htmlFormAction =
        new RoleWithTaskBase_Renderer.ActionForRoleAndTask(null, null, -1, -1);

    public StringsActions selectBoxName = StringsActions.selGroupID;

    public ArrayList<RoleAdmin_TaskErgebnisse_Renderer.ErgebnisItem> ergebnisse = new ArrayList<>();


      @Override
    public StringBuilder htmlOfDerrivedClass(RoleWithTaskBase_Renderer.ActionStringGenerator actionStringGenerator) {
        StringBuilder r = new StringBuilder();

        r.append("<p>Auswahl der Gruppe:<br>");

        for(RoleWithTaskBase_Renderer.HyperLink n: groupSelectionLinks){
        String href_or_style = "href=\"" + actionStringGenerator.generateActionString(n.linkAction) +"\"";
        if(n.isActive)
            href_or_style = "style=\"font-weight:bold;\" ";
        r.append("<a ").append(href_or_style).append(">").append(n.linkText).append("</a> | \n");
        }

        r.append("<table>\n<tr><th></th><th>Team</th><th>Gewonnene Spiele</th><th>Gespielte Spiele</th><th>Punktedifferenz</th></tr>\n");
        for(int i=0; i<ergebnisse.size(); i++){
            RoleAdmin_TaskErgebnisse_Renderer.ErgebnisItem e = ergebnisse.get(i);
            r.append("<tr><td>").append(i+1).
            append(".</td><td>").append(e.teamName).
            append("</td><td>").append(e.gewonneneSpiele).
            append("</td><td>").append(e.gespielteSpiele).
            append("</td><td>").append(e.punkteDifferenz).
            append("</td></tr>\n");
        }
        r.append("</table>\n");

        return r;
    }
}
