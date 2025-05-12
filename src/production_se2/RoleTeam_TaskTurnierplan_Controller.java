import java.util.ArrayList;
import java.util.Map;

public class RoleTeam_TaskTurnierplan_Controller
  extends RoleWithTaskBase_Controller<RoleTeam_TaskTurnierplan_Renderer>{

    RoleTeam_TaskTurnierplan_Controller(RoleTeam_TaskTurnierplan_Renderer renderer,
                                        Map<String,String> params) {
        super(renderer, params,StringsRole.Team, StringsRole.TeamTasks.Turnierplan);
    }

    @Override
    public void applyActions() {
        applyTestData();
    }

    @Override
    public void applyTestData(){
        RoleTeam_TaskTurnierplan_Data d = _renderer.daten;
        d.gruppenName = "Gruppe 0";
        d.teamName = "Team 1";
        d.navLinks = new ArrayList<>();
        d.planItems = new ArrayList<>();
        d.navLinks.add(new RoleWithTaskBase_Renderer.HyperLink("Home",
          new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Team, StringsRole.TeamTasks.Overview, 1, 2),
          false));
        d.navLinks.add(new RoleWithTaskBase_Renderer.HyperLink("Hallo-Link",
          new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Hallo, -1, -1), true));
        d.navLinks.add(new RoleWithTaskBase_Renderer.HyperLink("Aktueller Stand",
          new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Team, StringsRole.TeamTasks.Stand, 1, 2),
          false));

        for(int i = 0; i<15; i++){
            RoleWithTaskBase_Renderer.ActionForRoleAndTask action =
                 new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Team, StringsRole.TeamTasks.Matchdetails, 2, 1);
            action.parameters.put(StringsActions.matchid, "999");
            d.planItems.add(
                    new RoleTeam_TaskTurnierplan_Renderer.PlanItem("00:00",i, "Team " + (i%5),"Team " + (i%5+5),
                            "Team " + (i%4), (i%5)==0?action:null, "20/" + (i%20)));
        }

    }
}
