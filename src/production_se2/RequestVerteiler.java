import java.util.Map;

public class RequestVerteiler {

    private RoleWithTaskBase_Controller _requestController;
    private StringsRole _role;
    private StringsRole.RoleTask _task;

    RequestVerteiler(StringsRole role, StringsRole.RoleTask task,
                     int groupNr, int teamNr, Map<String,String> params){
        _role = role;
        _task = task;

        RoleWithTaskBase_Renderer r = AppSettings.getRenderer(role, task);

        if(r == null){
            //AppSettings.getRenderer() Methode konnte mit der Role und dem Task nicht anfangen.
            //deswegen geben wir die Kontrolle dem:
            _requestController = new RoleUnbekannt_TaskUnbekannt_Controller(
                    "Meldung vom RequestVerteiler: Check AppSettings.getRenderer() for Role=" + role.toString() +
                    ", Task=" + task.toString());
        }
        else if(role == StringsRole.Admin){
            if(task == StringsRole.AdminTasks.Status){

            }else if(task == StringsRole.AdminTasks.Einstellungen){
              _requestController = validateRendererType(RoleAdmin_TaskEinstellungen_Renderer.class, r);
              if(_requestController == null)
                _requestController = new
                  RoleAdmin_TaskEinstellungen_Controller((RoleAdmin_TaskEinstellungen_Renderer)r, params);
            }else if(task == StringsRole.AdminTasks.Hallo){
                _requestController = validateRendererType(RoleAdmin_TaskHallo_Renderer.class, r);
                if(_requestController == null)
                    _requestController = new
                            RoleAdmin_TaskHallo_Controller((RoleAdmin_TaskHallo_Renderer)r, params);
            }
            else{ // unbekannter Admin-Task

            }
        }
        else if (role == StringsRole.Team){
            if(task == StringsRole.TeamTasks.Stand){

            }
            else if (task == StringsRole.TeamTasks.Turnierplan) {
                _requestController = validateRendererType(RoleTeam_TaskTurnierplan_Renderer.class, r);
                if(_requestController == null)
                    _requestController = new
                            RoleTeam_TaskTurnierplan_Controller((RoleTeam_TaskTurnierplan_Renderer)r, params);
            }
        }

        if(_requestController == null){
            _requestController =
                    new RoleUnbekannt_TaskUnbekannt_Controller(
               "Check RequestVerteiler.Constructor() for Role=" + role.toString() +
                    ", Task=" + task.toString());
        }

        if(DevSettings.useTestDataForRendering()){
            _requestController.applyTestData();
        }
        else {
            _requestController.applyActions();
        }
    }

    public byte[] getResponseBytes(){
        if(_requestController != null) {
            return _requestController.getResponse().toString().getBytes();
        }
        else{
            return  "RequestVerteiler: _requestController is null.".getBytes();
        }
    }

    public RoleUnbekannt_TaskUnbekannt_Controller validateRendererType(
            Class<?> rendererType,
            RoleWithTaskBase_Renderer renderer
            )
    {
        RoleUnbekannt_TaskUnbekannt_Controller r = null;

        if(!rendererType.isInstance(renderer)){
            String s = "Message vom RequestVerteiler.Constructor(): fuer Role=" + _role.toString()
                    + " und Task=" + _task.toString()
                    + " wird ein Renderer vom Typ " + rendererType.getName() + " benoetigt, "
                    + " aber der von AppSettings.getRenderer() bereitgestellte Typ ist "
                    + renderer.getClass().getName();
            r = new RoleUnbekannt_TaskUnbekannt_Controller(s);
        }

        return r;
    }
}
