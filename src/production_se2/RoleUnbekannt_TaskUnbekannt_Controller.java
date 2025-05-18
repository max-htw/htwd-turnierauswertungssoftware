import java.util.HashMap;

public class RoleUnbekannt_TaskUnbekannt_Controller extends RoleWithTaskBase_Controller<RoleWithTaskBase_Renderer<?>>{

    RoleUnbekannt_TaskUnbekannt_Controller(String fehlermeldung, DBInterfaceBase dbBackend) {
        super(new RoleUnbekannt_TaskUnbekannt_Renderer(), new HashMap<String, String>(), null, null, dbBackend);

        _renderer.daten.fehlermeldung = fehlermeldung;
    }

    @Override
    public void applyActions() {
    }

    @Override
    public void applyTestData() {

    }
}
