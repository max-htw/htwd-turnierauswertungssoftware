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
    d.anzGruppen = 2;
    d.anzahlGruppen_Links.add(new RoleWithTaskBase_Renderer.NaviLink("1","/admin/einstellungen", false));
    d.anzahlGruppen_Links.add(new RoleWithTaskBase_Renderer.NaviLink("2","", true));
    d.anzahlGruppen_Links.add(new RoleWithTaskBase_Renderer.NaviLink("3","/admin/einstellungen", false));
    d.anzahlGruppen_Links.add(new RoleWithTaskBase_Renderer.NaviLink("4","/admin/einstellungen", false));

    for(int i = 0; i < d.anzGruppen; i++) {
      d.anzTeams_proGruppe.put(i+1, i+3);

      ArrayList<RoleWithTaskBase_Renderer.NaviLink> l = new ArrayList<>();
      for (int j = 3; j <= DataBaseQueries.get_maxAnzTeams(); j++) {
        l.add(new RoleWithTaskBase_Renderer.NaviLink(Integer.toString(j), "/admin/einstellungen", j == i + 3));
      }
      d.anzTeams_Dictionary.put(i+1,l);
    }

    d.mitRueckspielen = false;
    d.mit_RueckspielenLink = new RoleWithTaskBase_Renderer.NaviLink("true", "/admin/einstellungen", false);

    d.vorausfuellenData = false;
    d.vorausfuellenData_aendernLink = new RoleWithTaskBase_Renderer.NaviLink("true", "/admin/einstellungen", false);
  }
}
