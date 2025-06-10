public class RoleUnbekannt_TaskUnbekannt_Renderer extends RoleWithTaskBase_Renderer<RoleWithTaskBase_Data>{

    @Override
    public RoleWithTaskBase_Data getEmptyDaten() {
        RoleWithTaskBase_Data d = new RoleWithTaskBase_Data();
        d.htmlTitel = "Undefinierter Zustand";
        return d;
    }

    @Override
    public StringBuilder renderHtmlResponse(){
        StringBuilder r = new StringBuilder();
        
        r.append("Seitentitel: ").append(daten.htmlTitel).append("<br>\n");
        r.append("Fehler: <span style=\"color:red\">").append(daten.fehlermeldung).append("</span><br>\n");
        r.append("Debug-Meldung: ").append(daten.debugMessage).append("<br>\n");


        ActionForRoleAndTask action;

        r.append("<br><h1>Erledigte Views</h1>\n-----<br>\n");

        action = new ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Einstellungen , -1, -1);
        r.append("<a href=\"").append(getHref(action)).append("\">Admin - Einstellungen</a><br>\n");
        action = new ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Ergebnisse , -1, -1);
        r.append("<a href=\"").append(getHref(action)).append("\">Admin - Ergebnisse</a><br>\n");
        action = new ActionForRoleAndTask(StringsRole.Team, StringsRole.TeamTasks.Turnierplan , 0, 0);
        r.append("<a href=\"").append(getHref(action)).append("\">Team - Turnierplan</a><br>\n");


        r.append("<br><h1>Nicht erledigte Views</h1>\n-----<br>\n");

        action = new ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Status , -1, -1);
        r.append("<a href=\"").append(getHref(action)).append("\">Admin - Status</a><br>\n");
        action = new ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Turnierplan , -1, -1);
        r.append("<a href=\"").append(getHref(action)).append("\">Admin - Turnierplan</a><br>\n");
        action = new ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Matchdetails , -1, -1);
        r.append("<a href=\"").append(getHref(action)).append("\">Admin - Matchdetails</a><br>\n");

        
        return r;
    }
}
