import java.util.Map;

public class RoleAdmin_TaskTurnierplan_Controller extends RoleWithTaskBase_Controller<RoleAdmin_TaskTurnierplan_Renderer> {
    public RoleAdmin_TaskTurnierplan_Controller(RoleAdmin_TaskTurnierplan_Renderer renderer, Map<String, String> params, DBInterfaceBase dbBackend) {
        super(renderer, params, StringsRole.Admin, StringsRole.AdminTasks.Turnierplan, dbBackend);
    }
    @Override
    public void applyActions() {
        // Admin-spezifische Logik oder einfach super.applyActions() aufrufen
    }
    @Override
    public void applyTestData() {
        // Optional: Testdaten für Admin
    }
}