import java.awt.event.ActionListener;
import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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

      //wenn Turnierkonfigurationen des letzten Turnierplans sich vom den aktuellen Turnierkonfigurationen unterscheiden,
      //wird automatisch ein neuer Turnierplan berechnet. 
      //Aber nur, wenn der Nutzer nicht gerade die Turnierkonfigurationen vornimmt.
      if(!_dbInterface.isTurnierPlanAktuell() && !(this.getClass().equals(RoleAdmin_TaskEinstellungen_Controller.class))){
        //hier geht es darum den Turnierplan vom Turnierplangenerator (im Format List<TurnierplanGenerator.Spiel>)
        //in den Format des DBInterfaceBase Turniers (im Format ArrayList<DBInterfaceBase.FeldSchedule>) umzuwandeln.
        _dbInterface.reset();
        ArrayList<Integer> tgr = new ArrayList<>();
        for(int i = 0; i<_dbInterface.turnierKonf_getAnzGruppen(); i++){
            tgr.add(_dbInterface.turnierKonf_getAnzTeamsByGroupID(i));
        }
        List<TurnierplanGenerator.Spiel> tp = 
            TurnierplanGenerator.generierePlan(tgr, _dbInterface.turnierKonf_getAnzTimeSlots(),
                                                    _dbInterface.turnierKonf_getAnzSpielfelder(), 
                                                    _dbInterface.turnierKonf_getNeedRueckspiele());


        ArrayList<DBInterfaceBase.FeldSchedule> turnierPlan = new ArrayList<>();
        for( int idx = 0; idx < tp.size(); idx++){
            TurnierplanGenerator.Spiel s = tp.get(idx);
            //benoetigte Anzahl von Spielfelder im DBInterfaceBase-Turnierplan initialisieren
            if(s.getFeldNr() > turnierPlan.size() -1){
                for(int i = turnierPlan.size(); i <= s.getFeldNr(); i++){
                    turnierPlan.add(new DBInterfaceBase.FeldSchedule(i));
                }
            }
            //benoetigte Anzahl von Spiele im benoetigtem Spielfeld im DBInterfaceBase-Turnierplan initialisieren.
            //Sie werden mit dem leeren Spiel (mit allen TeamID = -1) initialisiert
            if(s.getTimeSlotNr() >= turnierPlan.get(s.getFeldNr()).getSpiele().size()){
                for(int i = turnierPlan.get(s.getFeldNr()).getSpiele().size(); i <= s.getTimeSlotNr(); i++){
                    turnierPlan.get(s.getFeldNr()).addSpiel(new DBInterfaceBase.SpielStats());
                }
            }

            //jetzt der eigenliche Mapping. 
            //im TurnierplanGenerator-Turnierplan sind die GruppenNr und TeamNr-in-der-Gruppe zusammen in dem TeamNr wie Folgt codiert:
            //jedes Team aus der Gruppe 0 bekommt den TeamNr = TeamNr-in-der-Gruppe
            //Teams aus den naechsten Gruppen bekommen fortlaufende Nummern.
            //z.B. Team 0 aus der Gruppe 1: TeamNr = Anzahl_Teams_in_Gruppe_0 + 0
            DBInterfaceBase.SpielStats stats = turnierPlan.get(s.getFeldNr()).getSpiele().get(s.getTimeSlotNr());
            int team1GrNr = -1;
            int team1TeamNr = -1;
            int team2GrNr = -1;
            int team2TeamNr = -1;
            int richterHinspielGrNr = -1;
            int richterHinspielTeamNr = -1;
            int richterRueckGrNr = -1;
            int richterRueckTeamNr = -1;
            ArrayList<Integer> firstNrOfNextGroup = new ArrayList<>();
            for(int i = 0; i<_dbInterface.turnierKonf_getAnzGruppen(); i++){
                int prevNr = 0;
                if(i>0){
                    prevNr = firstNrOfNextGroup.get(i-1);
                }
                firstNrOfNextGroup.add(_dbInterface.turnierKonf_getAnzTeamsByGroupID(i) + prevNr);
            }

            //vor der for-Schleife: s.getMatch().getTeamXnr() enthaelt die Fortlaufende TurnierplanGenerator-TeamNr
            //nach der for-Schleife: s.getMatch().getTeamXnr() enthaelt die TeamNr-in-der-Gruppe und die GruppenNr 
            //                       ist in einer Variable gespeichert
            int firstNrOfThisGroup = 0;
            int dbgBakTeam1Nr = s.getMatch().getTeam1Nr();
            int dbgBakTeam2Nr = s.getMatch().getTeam2Nr();
            for(int i = 0; i<_dbInterface.turnierKonf_getAnzGruppen(); i++){
                if((s.getMatch().getTeam1Nr() < firstNrOfNextGroup.get(i)) && team1GrNr < 0){
                    team1GrNr = i;
                    team1TeamNr = s.getMatch().getTeam1Nr() - firstNrOfThisGroup;
                }

                if((s.getMatch().getTeam2Nr() < firstNrOfNextGroup.get(i)) && team2GrNr < 0){
                    team2GrNr = i;
                    team2TeamNr = s.getMatch().getTeam2Nr() - firstNrOfThisGroup;
                }

                if((s.getMatch().getRichterHinspielTeamNr() < firstNrOfNextGroup.get(i)) && richterHinspielGrNr < 0){
                    richterHinspielGrNr = i;
                    richterHinspielTeamNr = s.getMatch().getRichterHinspielTeamNr() - firstNrOfThisGroup;
                }

                if((s.getMatch().getRichterRueckspielTeamNr() < firstNrOfNextGroup.get(i)) && (richterRueckGrNr < 0) && _dbInterface.turnierKonf_getNeedRueckspiele()){
                    richterRueckGrNr = i;
                    richterRueckTeamNr = s.getMatch().getRichterRueckspielTeamNr() - firstNrOfThisGroup;
                }
                firstNrOfThisGroup = firstNrOfNextGroup.get(i);
            }
            if((team1GrNr != team2GrNr) || (team1GrNr != s.getMatch().getGroupID())){
                throw new RuntimeException("Laut Programmlogik muessen team1GrNr und team2GrNr und getGroupID() gleich sein.");
            }

            stats.feldID = s.getFeldNr();
            stats.groupid = team1GrNr;
            stats.isHinspiel = s.getIstHinspiel();
            stats.team1 = team1TeamNr;
            stats.team2 = team2TeamNr;
            if(stats.isHinspiel){
                _dbInterface.getMatch(stats.groupid, stats.team1, stats.team2).setHinspielRichterGroupID(richterHinspielGrNr);
                _dbInterface.getMatch(stats.groupid, stats.team1, stats.team2).setHinspielRichterTeamID(richterHinspielTeamNr);
            }
            else if(_dbInterface.turnierKonf_getNeedRueckspiele()){
                _dbInterface.getMatch(stats.groupid, stats.team1, stats.team2).setRueckspielRichterGroupID(richterRueckGrNr);
                _dbInterface.getMatch(stats.groupid, stats.team1, stats.team2).setRueckspielRichterTeamID(richterRueckTeamNr);
            }
        }
        _dbInterface.fillTurnierPlan(turnierPlan);
      }
      int dummy = 5;
    }



    public void fillNavLinks(){
        boolean active;
        if(_renderer.daten.role == StringsRole.Admin){
            RoleWithTaskBase_Renderer.ActionForRoleAndTask aHome = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Hallo, -1, -1);
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
