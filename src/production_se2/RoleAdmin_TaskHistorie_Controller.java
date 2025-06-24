import java.util.Map;

public class RoleAdmin_TaskHistorie_Controller extends RoleWithTaskBase_Controller<RoleAdmin_TaskHistorie_Renderer>{
    RoleAdmin_TaskHistorie_Controller(RoleAdmin_TaskHistorie_Renderer renderer, Map<String, String> params, DBInterfaceBase dbBackend) {
        super(renderer, params, StringsRole.Admin, StringsRole.AdminTasks.Historie, dbBackend);
    }

    @Override
    public void applyActions() {
        RoleAdmin_TaskHistorie_Data d = this._renderer.daten;
        
        for(String s: _dbInterface.getTurnierArchiveNames()){
            RoleWithTaskBase_Renderer.ActionForRoleAndTask a =
                new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
            
            a.parameters.put(StringsActions.loadArchiv, s);
            RoleWithTaskBase_Renderer.HyperLink l =
                new RoleWithTaskBase_Renderer.HyperLink(s, a, false);
            d.archiveLinks.add(l);
        }
    }

    @Override
    public void applyTestData() {
        RoleAdmin_TaskHistorie_Data d = _renderer.daten;
        //eigentlich hier gibt es nichts zu fuellen. Aber trotzdem, als Beispiel:
        d.debugMessage = "Die Daten kommen nicht aus der Datenbank, sondern werden manuell erstellt";
    }
}
