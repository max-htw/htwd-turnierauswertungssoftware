import java.util.ArrayList;
import java.util.Map;

public class RoleAdmin_TaskErgebnisse_Controller extends RoleWithTaskBase_Controller<RoleAdmin_TaskErgebnisse_Renderer>{
    RoleAdmin_TaskErgebnisse_Controller(RoleAdmin_TaskErgebnisse_Renderer renderer, Map<String, String> params, DBInterfaceBase dbBackend) {
        super(renderer, params, StringsRole.Admin, StringsRole.AdminTasks.Ergebnisse, dbBackend);
    }

    @Override
    public void applyActions() {
        RoleAdmin_TaskErgebnisse_Data d = _renderer.daten;
        d.htmlFormAction = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Ergebnisse, -1, -1);

        int selectedGroup = 0;
        if(existsParam(StringsActions.selGroupID)){
            Integer p = validateIntParam(StringsActions.selGroupID);
            if(p != null){
                selectedGroup = p;
            }
        }    

        //Gruppen-Links
        ArrayList<String> names = _dbInterface.turnierKonf_getGroupNames();
        for(int i=0; i<names.size(); i++){
            RoleWithTaskBase_Renderer.ActionForRoleAndTask a = 
            new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Ergebnisse, -1, -1);
            a.parameters.put(StringsActions.selGroupID, "" + i);
            RoleWithTaskBase_Renderer.HyperLink l = new RoleWithTaskBase_Renderer.HyperLink(names.get(i), a, i==selectedGroup ? true : false);
            d.groupSelectionLinks.add(l);
        }

        for(DBInterfaceBase.AuswertungsEintrag a : _dbInterface.calculateAuswertung(selectedGroup)){
            d.ergebnisse.add(
                new RoleAdmin_TaskErgebnisse_Renderer.ErgebnisItem(selectedGroup, a.teamId, 
                        _dbInterface.turnierKonf_getTeamNamesByGroupID(selectedGroup).get(a.teamId), a.anzGewonnen, a.anzGespielt, a.pktDifferenz));
        }
    }

    @Override
    public void applyTestData() {
        RoleAdmin_TaskErgebnisse_Data d = _renderer.daten;

        d.htmlFormAction = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
            StringsRole.Admin, StringsRole.AdminTasks.Ergebnisse, -1, -1);
        d.selectBoxName = StringsActions.selGroupID;

        //Gruppen-Links
        for(int i=0; i<3; i++){
            String gName = "Group " + (char)('A' + i);
            RoleWithTaskBase_Renderer.ActionForRoleAndTask a = 
            new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Ergebnisse, -1, -1);
            a.parameters.put(StringsActions.selGroupID, "" + i);
            RoleWithTaskBase_Renderer.HyperLink l = new RoleWithTaskBase_Renderer.HyperLink(gName, a, i==1 ? true : false);
            d.groupSelectionLinks.add(l);
        }

        d.ergebnisse.add(new RoleAdmin_TaskErgebnisse_Renderer.ErgebnisItem(1, 2, "Alte Hasen", 5, 6, 37));
        d.ergebnisse.add(new RoleAdmin_TaskErgebnisse_Renderer.ErgebnisItem(1, 0, "Blitz Kicker", 4, 6, +15));
        d.ergebnisse.add(new RoleAdmin_TaskErgebnisse_Renderer.ErgebnisItem(1, 4, "Die Tornados", 3, 6, +2));
        d.ergebnisse.add(new RoleAdmin_TaskErgebnisse_Renderer.ErgebnisItem(1, 1, "FC Chaos", 3, 6, -11));
        d.ergebnisse.add(new RoleAdmin_TaskErgebnisse_Renderer.ErgebnisItem(1, 3, "Youngstars", 3, 6, -43));
    }
}
