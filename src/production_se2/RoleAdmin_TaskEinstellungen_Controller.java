import java.util.ArrayList;
import java.util.Map;

public class RoleAdmin_TaskEinstellungen_Controller extends RoleWithTaskBase_Controller<RoleAdmin_TaskEinstellungen_Renderer>{


  RoleAdmin_TaskEinstellungen_Controller(
    RoleAdmin_TaskEinstellungen_Renderer renderer, Map<String, String> params)
  {
    super(renderer, params, StringsRole.Admin, StringsRole.AdminTasks.Einstellungen);
  }

  @Override
  public void applyActions() {
    applyTestData();
  }

  @Override
  public void applyTestData() {
    RoleAdmin_TaskEinstellungen_Data d = _renderer.daten;

    d.navLinks.add(new RoleWithTaskBase_Renderer.HyperLink("Spielstand",
      new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Status, 1, 2),
      false));
    d.navLinks.add(new RoleWithTaskBase_Renderer.HyperLink("Turnierplan",
      new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Turnierplan, -1, -1), true));
    d.navLinks.add(new RoleWithTaskBase_Renderer.HyperLink("Turnierkonfiguration",
      new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, 1, 2),
      false));

    d.anzGruppen = 2;
    for(int i = 1; i <=4; i++){
      RoleWithTaskBase_Renderer.ActionForRoleAndTask action = null;
      boolean isActive = false;
      if(i != d.anzGruppen){
        action = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1 );
        action.parameters.put(StringsActions.setAnzGroups, "" + i);
        isActive = true;
      }
      d.anzahlGruppen_Links.add(new RoleWithTaskBase_Renderer.HyperLink("" + i, action, isActive ));
    }

    for(int i = 0; i < d.anzGruppen; i++) {
      d.anzTeams_proGruppe.put(i+1, i+3);

      ArrayList<RoleWithTaskBase_Renderer.HyperLink> l = new ArrayList<>();
      for (int j = 3; j <= DataBaseQueries.get_maxAnzTeams(); j++) {
        RoleWithTaskBase_Renderer.ActionForRoleAndTask action = null;
        boolean isActive = false;
        if(j != d.anzTeams_proGruppe.get(i+1)){
          action = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
            StringsRole.Admin,
            StringsRole.AdminTasks.Einstellungen, -1, -1);
          action.parameters.put(StringsActions.setAnzTeams, "" + j);
          action.parameters.put(StringsActions.refGroupID, "" + (i+1));
          isActive = true;
        }
        l.add(new RoleWithTaskBase_Renderer.HyperLink(Integer.toString(j), action, isActive));
      }
      d.anzTeams_Dictionary.put(i+1,l);
    }

    d.mitRueckspielen = false;
    RoleWithTaskBase_Renderer.ActionForRoleAndTask a1 = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
      StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    a1.parameters.put(StringsActions.needrueckspiel, "true");
    d.mit_RueckspielenLink = new RoleWithTaskBase_Renderer.HyperLink("true", a1, true);

    d.anzSpielfelder = 2;
    for(int i = 1; i <= DataBaseQueries.get_maxAnzSpielfelder(); i++){
      if(i != d.anzSpielfelder){
        RoleWithTaskBase_Renderer.ActionForRoleAndTask a2 = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
          StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
        a2.parameters.put(StringsActions.setAnzFields, "" + i);
        d.anzSpielfelder_aendernLinks.add(new RoleWithTaskBase_Renderer.HyperLink("" + i, a2, true));
      }
    }

    d.vorausfuellenData = false;
    RoleWithTaskBase_Renderer.ActionForRoleAndTask a3 = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
      StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    a3.parameters.put(StringsActions.setPrefillScores, "true");
    d.vorausfuellenData_aendernLink = new RoleWithTaskBase_Renderer.HyperLink("true", a3, true);

    d.htmlFormAction = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
      StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    d.textBoxName = StringsActions.saveTurnier;

    for(int i = 0; i<4; i++){
      RoleWithTaskBase_Renderer.ActionForRoleAndTask a = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
        StringsRole.Admin, StringsRole.AdminTasks.Status, -1, -1);
      a.parameters.put(StringsActions.loadArchiv, "" + i);
      d.savedTurniereLinks.add(new RoleWithTaskBase_Renderer.HyperLink("gespeichertes Turnier Nr " + i, a, true));
    }
  }
}
