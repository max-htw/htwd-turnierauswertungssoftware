import java.util.Map;

public class RoleAdmin_TaskHome_Controller extends RoleWithTaskBase_Controller<RoleAdmin_TaskHome_Renderer>{
    RoleAdmin_TaskHome_Controller(RoleAdmin_TaskHome_Renderer renderer, Map<String, String> params, DBInterfaceBase dbBackend) {
        super(renderer, params, StringsRole.Admin, StringsRole.AdminTasks.Home, dbBackend);
    }

    @Override
    public void applyActions() {

    }

    @Override
    public void applyTestData() {
        RoleAdmin_TaskHome_Data d = _renderer.daten;
        //eigentlich hier gibt es nichts zu fuellen. Aber trotzdem, als Beispiel:
        d.debugMessage = "Die Daten kommen nicht aus der Datenbank, sondern werden manuell erstellt";
    }
}
