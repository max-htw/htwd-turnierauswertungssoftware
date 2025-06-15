import java.util.ArrayList;
import java.util.Map;

public class RoleTeam_TaskTurnierplan_Controller
  extends RoleWithTaskBase_Controller<RoleTeam_TaskTurnierplan_Renderer>{

    RoleTeam_TaskTurnierplan_Controller(int groupID, int teamNr, RoleTeam_TaskTurnierplan_Renderer renderer,
                                        Map<String,String> params, DBInterfaceBase dbBackend) {

        super(renderer, params,StringsRole.Team, StringsRole.TeamTasks.Turnierplan,dbBackend);
        setGroupID(groupID);
        setTeamNr(teamNr);
    }

    @Override
    public void applyActions() {
      RoleTeam_TaskTurnierplan_Data d = _renderer.daten;

      ArrayList<DBInterfaceBase.TurnierMatch> matches = _dbInterface.getMatchesByGroupID(this.getGroupID());
      String groupName = _dbInterface.turnierKonf_getGroupNames().get(this.getGroupID());
      ArrayList<String> teamNames = _dbInterface.turnierKonf_getTeamNamesByGroupID(this.getGroupID());
      String teamName = teamNames.get(this.getTeamNr());

      d.gruppenName = groupName;
      d.teamName = teamName;

      for(DBInterfaceBase.TurnierMatch m: matches){

        RoleWithTaskBase_Renderer.ActionForRoleAndTask actionHinspiel = null;
        if(m.getTeam1PunkteHinspiel() > 0 || m.getTeam2PunkteHinspiel() > 0) {
          actionHinspiel = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
            StringsRole.Team, StringsRole.TeamTasks.Matchdetails, this.getGroupID(), this.getTeamNr());
          actionHinspiel.parameters.put(StringsActions.refGroupID, "" + m.getGroupID());
          actionHinspiel.parameters.put(StringsActions.refTeam1Nr, "" + m.getTeam1Nr());
          actionHinspiel.parameters.put(StringsActions.refTeam2Nr, "" + m.getTeam2Nr());
          actionHinspiel.parameters.put(StringsActions.refHinspiel, "true");
        }

        String punkteTeam1 = m.getTeam1PunkteHinspiel() > 0 ? Integer.toString(m.getTeam1PunkteHinspiel()) : "--";
        String punkteTeam2 = m.getTeam2PunkteHinspiel() > 0 ? Integer.toString(m.getTeam2PunkteHinspiel()) : "--";

        d.planItems.add(
          new RoleTeam_TaskTurnierplan_Renderer.PlanItem
            (_dbInterface.getTimeSlotString(m.getHinspielTimeSlot()),
              m.getHinspielFeldNr(),
              teamNames.get(m.getTeam1Nr()),
              teamNames.get(m.getTeam2Nr()),
              teamNames.get(m.getHinspielRichterTeamID()),
              actionHinspiel,
              punkteTeam1 + "/" + punkteTeam2));

        RoleWithTaskBase_Renderer.ActionForRoleAndTask actionRueckspiel = null;
        if(m.getTeam1PunkteHinspiel() > 0 || m.getTeam2PunkteHinspiel() > 0) {
          actionRueckspiel = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
            StringsRole.Team, StringsRole.TeamTasks.Matchdetails, this.getGroupID(), this.getTeamNr());
          actionRueckspiel.parameters.put(StringsActions.refGroupID, "" + m.getGroupID());
          actionRueckspiel.parameters.put(StringsActions.refTeam1Nr, "" + m.getTeam1Nr());
          actionRueckspiel.parameters.put(StringsActions.refTeam2Nr, "" + m.getTeam2Nr());
          actionRueckspiel.parameters.put(StringsActions.refHinspiel, "false");
        }

        punkteTeam1 = m.getTeam1PunkteRueckspiel() > 0 ? Integer.toString(m.getTeam1PunkteRueckspiel()) : "--";
        punkteTeam2 = m.getTeam2PunkteRueckspiel() > 0 ? Integer.toString(m.getTeam2PunkteRueckspiel()) : "--";

        d.planItems.add(
          new RoleTeam_TaskTurnierplan_Renderer.PlanItem
            (_dbInterface.getTimeSlotString(m.getRueckspielTimeSlot()),
              m.getRueckspielFeldNr(),
              teamNames.get(m.getTeam1Nr()),
              teamNames.get(m.getTeam2Nr()),
              teamNames.get(m.getRueckspielRichterTeamID()),
              actionRueckspiel,
              punkteTeam1 + "/" + punkteTeam2));
      }
    }

    @Override
    public void applyTestData(){
        RoleTeam_TaskTurnierplan_Data d = _renderer.daten;
        d.gruppenName = "Gruppe 0";
        d.teamName = "Team 1";
        d.planItems = new ArrayList<>();

        for(int i = 0; i<15; i++){
            RoleWithTaskBase_Renderer.ActionForRoleAndTask action =
                 new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Team, StringsRole.TeamTasks.Matchdetails, 2, 1);
          action.parameters.put(StringsActions.refGroupID, "" + 1);
          action.parameters.put(StringsActions.refTeam1Nr, "" + (i%5));
          action.parameters.put(StringsActions.refTeam2Nr, "" + (i%5+5));
          action.parameters.put(StringsActions.refHinspiel, "false");
            d.planItems.add(
                    new RoleTeam_TaskTurnierplan_Renderer.PlanItem("00:00",(i%3)+1, "Team " + (i%5),"Team " + (i%5+5),
                            "Team " + (i%4), (i%5)==0?action:null, "20/" + (i%20)));
        }

    }
}
