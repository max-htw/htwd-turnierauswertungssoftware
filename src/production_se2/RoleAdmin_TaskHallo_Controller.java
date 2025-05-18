import java.util.Map;

public class RoleAdmin_TaskHallo_Controller extends RoleWithTaskBase_Controller<RoleAdmin_TaskHallo_Renderer>{
    RoleAdmin_TaskHallo_Controller(RoleAdmin_TaskHallo_Renderer renderer, Map<String, String> params, DBInterfaceBase dbBackend) {
        super(renderer, params, StringsRole.Admin, StringsRole.AdminTasks.Hallo, dbBackend);
    }

    @Override
    public void applyActions() {

    }

    @Override
    public void applyTestData() {
        RoleAdmin_TaskHallo_Data d = _renderer.daten;
        //eigentlich hier gibt es nichts zu fuellen. Aber trotzdem, als Beispiel:
        d.debugMessage = "Die Daten kommen nicht aus der Datenbank, sondern werden manuell erstellt";
    }
}
