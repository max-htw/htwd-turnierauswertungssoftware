import javax.swing.*;
import java.awt.event.ActionListener;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class RequestVerteiler {

    private RoleWithTaskBase_Controller<?> _requestController;
    private StringsRole _role;
    private StringsRole.RoleTask _task;
    private int _groupID;
    private int _teamNr;
    private DBInterfaceBase _dbBackend;


  RequestVerteiler(StringsRole role, StringsRole.RoleTask task,
                   int groupNr, int teamNr, Map<String,String> params) {
    this(role,task,groupNr,teamNr,params, AppSettings.getDatabaseBackend());
  }

    RequestVerteiler(StringsRole role, StringsRole.RoleTask task,
                     int groupID, int teamNr, Map<String,String> params, DBInterfaceBase dbBackend ){
        _role = role;
        _task = task;
        _groupID = groupID;
        _teamNr = teamNr;
        _dbBackend = dbBackend;

        RoleWithTaskBase_Renderer<?> r = AppSettings.getRenderer(role, task);

        if(r == null){
            //AppSettings.getRenderer() Methode konnte mit der Role und dem Task nicht anfangen.
            //deswegen geben wir die Kontrolle dem:
            _requestController = new RoleUnbekannt_TaskUnbekannt_Controller(
                    "Meldung vom RequestVerteiler: Check AppSettings.getRenderer() for Role=" + role.toString() +
                    ", Task=" + task.toString(), dbBackend);
        }
        else if(role == StringsRole.Admin){
            if(task == StringsRole.AdminTasks.Status){

            }else if(task == StringsRole.AdminTasks.Einstellungen){
              _requestController = validateRendererType(RoleAdmin_TaskEinstellungen_Renderer.class, r);
              if(_requestController == null) {
                _requestController = new
                  RoleAdmin_TaskEinstellungen_Controller((RoleAdmin_TaskEinstellungen_Renderer) r, params, dbBackend);
              }
            }else if(task == StringsRole.AdminTasks.Hallo){
                _requestController = validateRendererType(RoleAdmin_TaskHallo_Renderer.class, r);
                if(_requestController == null)
                    _requestController = new
                            RoleAdmin_TaskHallo_Controller((RoleAdmin_TaskHallo_Renderer)r, params, dbBackend);
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
                            RoleTeam_TaskTurnierplan_Controller(_groupID, _teamNr, (RoleTeam_TaskTurnierplan_Renderer)r, params, dbBackend);
            }
        }
        else if (role == StringsRole.Stranger){
          if(task == StringsRole.KeineRoleTasks.SelectRole){
            _requestController = validateRendererType(RoleUnbekannt_TaskUnbekannt_Renderer.class, r);
            if(_requestController == null){
              _requestController = new
                            RoleUnbekannt_TaskUnbekannt_Controller("", dbBackend);
            }

          }
        }

        if(_requestController == null){
            _requestController =
                    new RoleUnbekannt_TaskUnbekannt_Controller(
               "Check RequestVerteiler.Constructor() for Role=" + role.toString() +
                    ", Task=" + task.toString(), dbBackend);
        }

        if(DevSettings.useTestDataForRendering()){
            _requestController.applyTestData();
        }
        else {
            _requestController.applyActions();
        }
    }

    public byte[] getResponseHtmlBytes(RoleWithTaskBase_Renderer.ActionStringGenerator actionStringGenerator){
        if(_requestController != null) {
            return _requestController.getResponseHtml(actionStringGenerator).toString().getBytes(StandardCharsets.UTF_8);
        }
        else{
            return  "RequestVerteiler: _requestController is null.".getBytes();
        }
    }

    public JPanel getResponseJPanel(int width, int height, ActionListener actionListener,
                                    RoleWithTaskBase_Renderer.ActionStringGenerator actionStringGenerator){
      if(_requestController != null) {
        return _requestController.getResponseJPanel(width, height, actionListener, actionStringGenerator);
      }
      else{
        JPanel p = new JPanel();
        JLabel l = new JLabel("RequestVerteiler.getResponseJPanel(): requestController is null.");
        p.add(l);
        return  p;
      }
    }

    public RoleWithTaskBase_Data getResponseData(){
      return _requestController.getResponseData();
    }

    //diese Methode prueft, ob der uebergebener Renderer vom Typ rendererType ist,
    //oder vom einem geerbten Typ. In diesem Fall wird null zurueckgegeben.
    //wenn die Typen nicht verwandt sind, wird ein RoleUnbekannt_TaskUnbekannt_Controller zurueckgegeben.
    public RoleUnbekannt_TaskUnbekannt_Controller validateRendererType(
            Class<?> rendererType,
            RoleWithTaskBase_Renderer<?> renderer
            )
    {
        RoleUnbekannt_TaskUnbekannt_Controller r = null;

        if(!rendererType.isInstance(renderer)){
            String s = "Message vom RequestVerteiler.Constructor(): fuer Role=" + _role.toString()
                    + " und Task=" + _task.toString()
                    + " wird ein Renderer vom Typ " + rendererType.getName() + " benoetigt, "
                    + " aber der von AppSettings.getRenderer() bereitgestellte Typ ist "
                    + renderer.getClass().getName();
            r = new RoleUnbekannt_TaskUnbekannt_Controller(s, _dbBackend);
        }

        return r;
    }
}
