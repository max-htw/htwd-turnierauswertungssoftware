import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;
import javax.swing.*;

public abstract class RoleWithTaskBase_Controller<Class_of_Renderer extends RoleWithTaskBase_Renderer<?>> {

    protected Class_of_Renderer _renderer;
    protected Map<String, String> _params = new HashMap<>();
    protected DBInterfaceBase _dbInterface;

    private int _groupID = -1;
    public int getGroupID(){
        return _groupID;
    }
    public void setGroupID(int gid){
        _groupID = gid;
    }
    
    private int _teamNr = -1;
    public int getTeamNr(){
        return _teamNr;
    }
    public void setTeamNr(int tnr){
        _teamNr = tnr;
    }


    public boolean existsParam(StringsActions p){
        return _params.containsKey(p.name().toLowerCase());
    }

    public abstract void applyActions();

    public abstract void applyTestData();

    RoleWithTaskBase_Controller(RoleWithTaskBase_Renderer<?> renderer,
                                Map<String, String> params,
                                StringsRole role,
                                StringsRole.RoleTask task,
                                DBInterfaceBase dbBackend){
      _renderer = (Class_of_Renderer) (renderer);
      _renderer.daten.role = role;
      _renderer.daten.task = task;
      _params = params;
      if(dbBackend == null) AppSettings.getDatabaseBackend();
      _dbInterface = dbBackend;
    }



    public void fillNavLinks(){
        boolean active;
        if(_renderer.daten.role == StringsRole.Admin){
            RoleWithTaskBase_Renderer.ActionForRoleAndTask aHome = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Home, -1, -1);
            active = (_renderer.daten.role == aHome.role && _renderer.daten.task == aHome.task );
            _renderer.daten.navLinkHome = new RoleWithTaskBase_Renderer.HyperLink("Home", aHome, active);

            RoleWithTaskBase_Renderer.ActionForRoleAndTask aHistory = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Historie, -1, -1);
            active = (_renderer.daten.role == aHistory.role && _renderer.daten.task == aHistory.task);
            _renderer.daten.navLinkHistory = new RoleWithTaskBase_Renderer.HyperLink("Historie", aHistory, active);

            RoleWithTaskBase_Renderer.ActionForRoleAndTask aKonf = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
            active = (_renderer.daten.role == aKonf.role && _renderer.daten.task == aKonf.task);
            _renderer.daten.navLinksGroup.add(new RoleWithTaskBase_Renderer.HyperLink("Konfiguration", aKonf, active));

            RoleWithTaskBase_Renderer.ActionForRoleAndTask aTurnier = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Turnierplan, -1, -1);
            active = (_renderer.daten.role == aTurnier.role && _renderer.daten.task == aTurnier.task );
            _renderer.daten.navLinksGroup.add(new RoleWithTaskBase_Renderer.HyperLink("Turnierplan", aTurnier, active));

            RoleWithTaskBase_Renderer.ActionForRoleAndTask aResults = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Ergebnisse, -1, -1);
            active = (_renderer.daten.role == aResults.role && _renderer.daten.task == aResults.task );
            _renderer.daten.navLinksGroup.add(new RoleWithTaskBase_Renderer.HyperLink("Ergebnisse", aResults, active));
        }
        else if(_renderer.daten.role == StringsRole.Team){
            RoleWithTaskBase_Renderer.ActionForRoleAndTask aOverview = 
                new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Team, StringsRole.TeamTasks.Overview, this.getGroupID(), this.getTeamNr());
            active = (_renderer.daten.role == aOverview.role && _renderer.daten.task == aOverview.task );
            _renderer.daten.navLinksGroup.add(new RoleWithTaskBase_Renderer.HyperLink("Home", aOverview, active));

            RoleWithTaskBase_Renderer.ActionForRoleAndTask aTurnierPlan = 
                new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Team, StringsRole.TeamTasks.Turnierplan, this.getGroupID(), this.getTeamNr());
            active = (_renderer.daten.role == aTurnierPlan.role && _renderer.daten.task == aTurnierPlan.task );
            _renderer.daten.navLinksGroup.add(new RoleWithTaskBase_Renderer.HyperLink("Turnierplan", aTurnierPlan, active));

            RoleWithTaskBase_Renderer.ActionForRoleAndTask aStand = 
                new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Team, StringsRole.TeamTasks.Stand, this.getGroupID(), this.getTeamNr());
            active = (_renderer.daten.role == aStand.role && _renderer.daten.task == aStand.task );
            _renderer.daten.navLinksGroup.add(new RoleWithTaskBase_Renderer.HyperLink("Aktueller Stand", aStand, active));
        }
        
    }

    public Integer validateIntParam(StringsActions p){
        Integer ausgabe = null;
        try{
            String strParam = _params.get(p.name().toLowerCase());
            ausgabe = Integer.parseInt(strParam);
        }
        catch (Exception e){}
        return  ausgabe;
    }

    public Boolean validateBoolParam(StringsActions p){
        Boolean ausgabe = null;
        try{
            String strParam = _params.get(p.name().toLowerCase());
            if(strParam.equals("true")){
                ausgabe = true;
            }
            else if(strParam.equals("false")){
                ausgabe = false;
            }
        }
        catch (Exception e){}
        return  ausgabe;
    }

    public String getResponseHtml(RoleWithTaskBase_Renderer.ActionStringGenerator actionStringGenerator){
        _renderer.setActionStringGenerator(actionStringGenerator);
        String ausgabe = "";

        //wenn die Entwickler die Rendereinstellungen in DevSettings uebeschrieben haben,
        //rendern wir nach ihren Wunsch:
        RoleWithTaskBase_Renderer<?> devRenderer = DevSettings.getRenderer(_renderer.daten.role, _renderer.daten.task);
        if((devRenderer != null) || (devRenderer == null && !DevSettings.turnOffAllRenderer())){
            // der Entwickler moechte explizit, dass dieser Ansicht gerendert wird
            // oder er hat keine besonderen Wuensche bezueglich dieses Renderers.
            // Rendern ganz normal:
            ausgabe += _renderer.renderHtmlResponse().toString();
        }
        else{
            // den Daten-Standardrenderer verwenden:
            _renderer.turnOffCss();
            ausgabe = _renderer.daten.htmlUebersicht(actionStringGenerator).toString();
        }

        ausgabe = _renderer.RenderHtmlAnfang_() + _renderer.RenderBodyAnfang_() + ausgabe + _renderer.RenderBodyEnde_() + _renderer.RenderHtmlEnde_();
        return  ausgabe;
    }

    public JPanel getResponseJPanel(int width, int height, ActionListener actionListener,
                                    RoleWithTaskBase_Renderer.ActionStringGenerator actionStringGenerator){
        return _renderer.renderJPanel(width, height, actionListener);
    }

    public RoleWithTaskBase_Data getResponseData(){
      return _renderer.daten;
    }

    /*
    public  Class<?> getRendererClass(){
        return _renderer.getClass();
    }

     */


}
