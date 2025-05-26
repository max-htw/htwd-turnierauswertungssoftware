import java.util.ArrayList;
import java.util.Map;

public class RoleTeam_TaskTurnierplan_Controller
  extends RoleWithTaskBase_Controller<RoleTeam_TaskTurnierplan_Renderer>{

    protected int _groupID;
    protected int _teamNr;

    RoleTeam_TaskTurnierplan_Controller(int groupID, int teamNr, RoleTeam_TaskTurnierplan_Renderer renderer,
                                        Map<String,String> params, DBInterfaceBase dbBackend) {

        super(renderer, params,StringsRole.Team, StringsRole.TeamTasks.Turnierplan,dbBackend);
        _groupID = groupID;
        _teamNr = teamNr;
    }

    @Override
    public void applyActions() {
      RoleTeam_TaskTurnierplan_Data d = _renderer.daten;

      ArrayList<DBInterfaceBase.TurnierMatch> matches = _dbInterface.getMatchesByGroupID(_groupID);
      String groupName = _dbInterface.turnierKonf_getGroupNames().get(_groupID);
      ArrayList<String> teamNames = _dbInterface.turnierKonf_getTeamNamesByGroupID(_groupID);
      String teamName = teamNames.get(_teamNr);
      ArrayList<String> timeSlotsStrings = _dbInterface.getTimeSlotsStrings();

      d.gruppenName = groupName;
      d.teamName = teamName;

      d.navLinksGroup = new ArrayList<>();
      d.navLinksGroup.add(new RoleWithTaskBase_Renderer.HyperLink("Home",
        new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Team, StringsRole.TeamTasks.Overview, _groupID, _teamNr),
        false));
      d.navLinksGroup.add(new RoleWithTaskBase_Renderer.HyperLink("Aktueller Stand",
        new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Team, StringsRole.TeamTasks.Stand, _groupID, _teamNr),
        false));

      for(DBInterfaceBase.TurnierMatch m: matches){

        RoleWithTaskBase_Renderer.ActionForRoleAndTask actionHinspiel = null;
        if(m.team1PunkteHinspiel > 0 || m.team2PunkteHinspiel > 0) {
          actionHinspiel = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
            StringsRole.Team, StringsRole.TeamTasks.Matchdetails, _groupID, _teamNr);
          actionHinspiel.parameters.put(StringsActions.refGroupID, "" + m.groupID);
          actionHinspiel.parameters.put(StringsActions.refTeam1Nr, "" + m.team1Nr);
          actionHinspiel.parameters.put(StringsActions.refTeam2Nr, "" + m.team2Nr);
          actionHinspiel.parameters.put(StringsActions.refHinspiel, "true");
        }

        String punkteTeam1 = m.team1PunkteHinspiel > 0 ? Integer.toString(m.team1PunkteHinspiel) : "--";
        String punkteTeam2 = m.team2PunkteHinspiel > 0 ? Integer.toString(m.team2PunkteHinspiel) : "--";

        d.planItems.add(
          new RoleTeam_TaskTurnierplan_Renderer.PlanItem
            (timeSlotsStrings.get(m.hinspielTimeSlot),
              m.hinspielFeldNr,
              teamNames.get(m.team1Nr),
              teamNames.get(m.team2Nr),
              teamNames.get(m.hinspielRichterTeamID),
              actionHinspiel,
              punkteTeam1 + "/" + punkteTeam2));

        RoleWithTaskBase_Renderer.ActionForRoleAndTask actionRueckspiel = null;
        if(m.team1PunkteHinspiel > 0 || m.team2PunkteHinspiel > 0) {
          actionRueckspiel = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
            StringsRole.Team, StringsRole.TeamTasks.Matchdetails, _groupID, _teamNr);
          actionRueckspiel.parameters.put(StringsActions.refGroupID, "" + m.groupID);
          actionRueckspiel.parameters.put(StringsActions.refTeam1Nr, "" + m.team1Nr);
          actionRueckspiel.parameters.put(StringsActions.refTeam2Nr, "" + m.team2Nr);
          actionRueckspiel.parameters.put(StringsActions.refHinspiel, "false");
        }

        punkteTeam1 = m.team1PunkteRueckspiel > 0 ? Integer.toString(m.team1PunkteRueckspiel) : "--";
        punkteTeam2 = m.team2PunkteRueckspiel > 0 ? Integer.toString(m.team2PunkteRueckspiel) : "--";

        d.planItems.add(
          new RoleTeam_TaskTurnierplan_Renderer.PlanItem
            (timeSlotsStrings.get(m.rueckspielTimeSlot),
              m.rueckspielFeldNr,
              teamNames.get(m.team1Nr),
              teamNames.get(m.team2Nr),
              teamNames.get(m.rueckspielRichterTeamID),
              actionRueckspiel,
              punkteTeam1 + "/" + punkteTeam2));
      }
    }

    @Override
    public void applyTestData(){
        RoleTeam_TaskTurnierplan_Data d = _renderer.daten;
        d.gruppenName = "Gruppe 0";
        d.teamName = "Team 1";
        d.navLinksGroup = new ArrayList<>();
        d.planItems = new ArrayList<>();
        d.navLinksGroup.add(new RoleWithTaskBase_Renderer.HyperLink("Home",
          new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Team, StringsRole.TeamTasks.Overview, 1, 2),
          false));
        d.navLinksGroup.add(new RoleWithTaskBase_Renderer.HyperLink("Hallo-Link",
          new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Hallo, -1, -1), true));
        d.navLinksGroup.add(new RoleWithTaskBase_Renderer.HyperLink("Aktueller Stand",
          new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Team, StringsRole.TeamTasks.Stand, 1, 2),
          false));

        for(int i = 0; i<15; i++){
            RoleWithTaskBase_Renderer.ActionForRoleAndTask action =
                 new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Team, StringsRole.TeamTasks.Matchdetails, 2, 1);
          action.parameters.put(StringsActions.refGroupID, "" + 1);
          action.parameters.put(StringsActions.refTeam1Nr, "" + (i%5));
          action.parameters.put(StringsActions.refTeam2Nr, "" + (i%5+5));
          action.parameters.put(StringsActions.refHinspiel, "false");
            d.planItems.add(
                    new RoleTeam_TaskTurnierplan_Renderer.PlanItem("00:00",i, "Team " + (i%5),"Team " + (i%5+5),
                            "Team " + (i%4), (i%5)==0?action:null, "20/" + (i%20)));
        }

    }
}
