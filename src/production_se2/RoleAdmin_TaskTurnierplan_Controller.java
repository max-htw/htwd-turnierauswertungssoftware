import java.util.ArrayList;
import java.util.Map;

public class RoleAdmin_TaskTurnierplan_Controller 
    extends RoleWithTaskBase_Controller<RoleAdmin_TaskTurnierplan_Renderer>{

    RoleAdmin_TaskTurnierplan_Controller(RoleAdmin_TaskTurnierplan_Renderer renderer,
                                    Map<String,String> params, DBInterfaceBase dbBackend){

        super(renderer, params,StringsRole.Admin, StringsRole.TeamTasks.Turnierplan,dbBackend);
    }


    @Override
    public void applyActions() {
        RoleAdmin_TaskTurnierplan_Data d = _renderer.daten;
        ArrayList<DBInterfaceBase.FeldSchedule> tp = _dbInterface.getTurnierPlan();
        int maxRows = 0;
        for (int i = 0; i< tp.size(); i++){
            maxRows = tp.get(i).getSpiele().size() > maxRows? tp.get(i).getSpiele().size() : maxRows;
        }
        for(int i = 0; i < maxRows; i++){
            
            ArrayList<RoleWithTaskBase_Renderer.HyperLink> fieldLinks = new ArrayList<>();
            String timeText = _dbInterface.getTimeSlotsStrings().get(i);
            for(int j = 0; j < tp.size(); j++){
                if(tp.get(j).getSpiele().size() > i && tp.get(j).getSpiele().get(i) != null && tp.get(j).getSpiele().get(i).groupid>=0){
                    DBInterfaceBase.SpielStats st = tp.get(j).getSpiele().get(i);
                    DBInterfaceBase.TurnierMatch tm = _dbInterface.getMatch(st.groupid, st.team1, st.team2);
                    RoleWithTaskBase_Renderer.ActionForRoleAndTask a = 
                        new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Matchdetails, -1, -1);
                    a.parameters.put(StringsActions.refGroupID,"" + st.groupid);
                    a.parameters.put(StringsActions.refTeam1Nr,"" + st.team1);
                    a.parameters.put(StringsActions.refTeam2Nr,"" + st.team2);
                    RoleWithTaskBase_Renderer.HyperLink hl = 
                        new RoleWithTaskBase_Renderer.HyperLink("Gr." + (st.groupid+1) + ": " + 
                            (char)('A' + st.team1) + " vs. " + (char)('A' + st.team2) + " | " + 
                            (char)('1' + (st.isHinspiel? tm.getHinspielRichterGroupID() : tm.getRueckspielRichterGroupID())) +
                            (char)('A' + (st.isHinspiel? tm.getHinspielRichterTeamID() : tm.getRueckspielRichterTeamID())), a, false);
                    fieldLinks.add(hl);
                }
                else{
                    fieldLinks.add(null);
                }
            }
            RoleAdmin_TaskTurnierplan_Renderer.PlanItem p = 
            new RoleAdmin_TaskTurnierplan_Renderer.PlanItem(timeText, fieldLinks);
            d.planItems.add(p);
        }
        
    }

    @Override
    public void applyTestData() {
        RoleAdmin_TaskTurnierplan_Data d = _renderer.daten;

        for(int i = 0; i<12; i++){
            ArrayList<RoleWithTaskBase_Renderer.HyperLink> fieldLinks = new ArrayList<>();
            for(int j = 0; j < 3; j++){
                RoleWithTaskBase_Renderer.HyperLink hl = null;
                if((j == 0) || (j == 1 && i < 8) || (j == 2 && i < 4)){
                    RoleWithTaskBase_Renderer.ActionForRoleAndTask a = 
                        new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Matchdetails, -1, -1);
                    a.parameters.put(StringsActions.matchID , "" + 333);
                    hl = new RoleWithTaskBase_Renderer.HyperLink("1A vs. 1B | 1C", a, true);
                }
                fieldLinks.add(hl);
            }
            RoleAdmin_TaskTurnierplan_Renderer.PlanItem p = new RoleAdmin_TaskTurnierplan_Renderer.PlanItem("00:00", fieldLinks);
            d.planItems.add(p);
        }

    }
    
}
