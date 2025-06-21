import java.util.Map;

public class RoleAdmin_TaskHistorie_Controller extends RoleWithTaskBase_Controller<RoleAdmin_TaskHistorie_Renderer>{
    RoleAdmin_TaskHistorie_Controller(RoleAdmin_TaskHistorie_Renderer renderer, Map<String, String> params, DBInterfaceBase dbBackend) {
        super(renderer, params, StringsRole.Admin, StringsRole.AdminTasks.Historie, dbBackend);
    }

    @Override
    public void applyActions() {

    }

    @Override
    public void applyTestData() {
        RoleAdmin_TaskHistorie_Data d = _renderer.daten;
        //eigentlich hier gibt es nichts zu fuellen. Aber trotzdem, als Beispiel:
        d.debugMessage = "Die Daten kommen nicht aus der Datenbank, sondern werden manuell erstellt";
    }
}
