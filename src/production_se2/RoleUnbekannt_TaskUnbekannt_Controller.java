import java.util.HashMap;
import java.util.Map;

public class RoleUnbekannt_TaskUnbekannt_Controller extends RoleWithTaskBase_Controller<RoleWithTaskBase_Renderer>{

    RoleUnbekannt_TaskUnbekannt_Controller(String fehlermeldung) {
        super(new RoleUnbekannt_TaskUnbekannt_Renderer(), new HashMap<String, String>(), null, null);

        _renderer.daten.fehlermeldung = fehlermeldung;
    }

    @Override
    public void applyActions() {
    }

    @Override
    public void applyTestData() {

    }
}
