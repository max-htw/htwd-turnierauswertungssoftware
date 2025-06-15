import java.util.ArrayList;
import java.util.Map;

public class RoleAdmin_TaskEinstellungen_Controller extends RoleWithTaskBase_Controller<RoleAdmin_TaskEinstellungen_Renderer>{


  RoleAdmin_TaskEinstellungen_Controller(
    RoleAdmin_TaskEinstellungen_Renderer renderer, Map<String, String> params, DBInterfaceBase dbBackend)
  {
    super(renderer, params, StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, dbBackend);
  }

  @Override
  public void applyActions() {
    if(existsParam(StringsActions.setAnzGroups)){
      Integer p = validateIntParam(StringsActions.setAnzGroups);
      if(p != null){
        boolean erfolg = _dbInterface.turnierKonf_setAnzGruppen(p);
        if(!erfolg){
          _renderer.daten.fehlermeldung += " Fehler bei setAnzGroups().";
        }
      }
    }
    else if(existsParam(StringsActions.setAnzTeams) && existsParam(StringsActions.refGroupID)){
      Integer pTeams = validateIntParam(StringsActions.setAnzTeams);
      Integer pGid = validateIntParam(StringsActions.refGroupID);
      boolean erfolg = false;
      if(pTeams != null && pGid != null){
        try{
          erfolg = _dbInterface.turnierKonf_setAnzTeamsByGroupID(pGid, pTeams);
          if(!erfolg){
            _renderer.daten.fehlermeldung += " Fehler bei setAnzTeams().";
          }
        }
        catch(IllegalArgumentException e){
          _renderer.daten.fehlermeldung += "Fehler bei setAnzTeams(): ungueltiger Argument. TODO: mehr details anzeigen.";
        }
      }
    }
    else if(existsParam(StringsActions.setAnzFields)){
      Integer p = validateIntParam(StringsActions.setAnzFields);
      if(p != null){
        boolean erfolg = _dbInterface.turnierKonf_setAnzSpielfelder(p);
        if(!erfolg){
          _renderer.daten.fehlermeldung += " Fehler bei setAnzSpielfelder().";
        }
      }
    }
    else if(existsParam(StringsActions.needrueckspiel)){
      Boolean p = validateBoolParam(StringsActions.needrueckspiel);
      if(p != null){
        boolean erfolg = _dbInterface.turnierKonf_setNeedRueckspiele(p);
        if(!erfolg){
          _renderer.daten.fehlermeldung += " Fehler bei setNeedRueckspiele().";
        }
      }
    }
    else if(existsParam(StringsActions.setPrefillScores)){
      Boolean p = validateBoolParam(StringsActions.setPrefillScores);
     _dbInterface.turnierKonf_setNeedPrefillScores(p);
     _dbInterface.resetMatches();
    }
    else if(existsParam(StringsActions.setSpielDauer)){
      Integer p = validateIntParam(StringsActions.setSpielDauer);
      _dbInterface.turnierKonf_setTimeSlotDuration(p);
    }
    else if(existsParam(StringsActions.saveTurnier)){
        String p = _params.get(StringsActions.saveTurnier.name().toLowerCase());
        boolean erfolg = _dbInterface.saveCurrentTurnierToArchive(p);
        if(!erfolg){
          _renderer.daten.fehlermeldung += " Fehler bei saveCurrentTurnierToArchive().";
        }
    }

    //applyTestData(); //for debugging purposes

    RoleAdmin_TaskEinstellungen_Data d = _renderer.daten;

    d.anzGruppen = _dbInterface.turnierKonf_getAnzGruppen();
    for(int i = 1; i <= AppSettings.maxAnzGroups; i++){
      RoleWithTaskBase_Renderer.ActionForRoleAndTask action = null;
      boolean isActive = false;
      if(i != d.anzGruppen){
        action = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1 );
        action.parameters.put(StringsActions.setAnzGroups, "" + i);
        isActive = true;
      }
      d.anzahlGruppen_Links.add(new RoleWithTaskBase_Renderer.HyperLink("" + i, action, isActive ));
    }

    for(int i = 0; i < d.anzGruppen; i++) {
      d.anzTeams_proGruppe.put(i, _dbInterface.turnierKonf_getAnzTeamsByGroupID(i));

      ArrayList<RoleWithTaskBase_Renderer.HyperLink> l = new ArrayList<>();
      for (int j = AppSettings.minAnzTeams; j <= AppSettings.maxAnzTeams; j++) {
        RoleWithTaskBase_Renderer.ActionForRoleAndTask action = null;
        boolean isActive = false;
        if(j != d.anzTeams_proGruppe.get(i)){
          action = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
                  StringsRole.Admin,
                  StringsRole.AdminTasks.Einstellungen, -1, -1);
          action.parameters.put(StringsActions.setAnzTeams, "" + j);
          action.parameters.put(StringsActions.refGroupID, "" + i);
          isActive = true;
        }
        l.add(new RoleWithTaskBase_Renderer.HyperLink(Integer.toString(j), action, isActive));
      }
      d.anzTeams_Dictionary.put(i,l);
    }

    d.mitRueckspielen = _dbInterface.turnierKonf_getNeedRueckspiele();
    RoleWithTaskBase_Renderer.ActionForRoleAndTask a1 = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
            StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    a1.parameters.put(StringsActions.needrueckspiel, (!d.mitRueckspielen)?"true":"false");
    d.mit_RueckspielenLink = new RoleWithTaskBase_Renderer.HyperLink((!d.mitRueckspielen)?"true":"false", a1, true);

    d.anzSpielfelder = _dbInterface.turnierKonf_getAnzSpielfelder();
    for(int i = 1; i <= AppSettings.maxAnzSpielfelder; i++){
      boolean isActive = false;
      RoleWithTaskBase_Renderer.ActionForRoleAndTask a2 = null;
      if(i != d.anzSpielfelder){
        a2 = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
                StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
        a2.parameters.put(StringsActions.setAnzFields, "" + i);
        isActive = true;
      }
        d.anzSpielfelder_aendernLinks.add(new RoleWithTaskBase_Renderer.HyperLink("" + i, a2, isActive));
    }

    d.anfangZeitStr = _dbInterface.getTimeSlotString(0);

    d.spielDauer = _dbInterface.turnierKonf_getTimeSlotDuration();
    for(int i=AppSettings.minTimeSlotDuration; i<=AppSettings.maxTimeSlotDuration; i+=5){
      boolean isActive = false;
      RoleWithTaskBase_Renderer.ActionForRoleAndTask a2 = null;
      if(i != d.spielDauer){
        a2 = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
          StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
        a2.parameters.put((StringsActions.setSpielDauer), "" + i);
        isActive = true;
      };
      d.spielDauer_aendernLinks.add(new RoleWithTaskBase_Renderer.HyperLink("" + i, a2, isActive));
    }

    d.vorausfuellenData = _dbInterface.turnierKonf_getNeedPrefillScores();
    RoleWithTaskBase_Renderer.ActionForRoleAndTask a3 = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
            StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    a3.parameters.put(StringsActions.setPrefillScores, (!d.vorausfuellenData)?"true":"false");
    d.vorausfuellenData_aendernLink = new RoleWithTaskBase_Renderer.HyperLink((!d.vorausfuellenData)?"true":"false", a3, true);


    d.htmlFormAction = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
            StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    d.textBoxName = StringsActions.saveTurnier;

    for(int i = 0; i<_dbInterface.getTurnierArchiveNames().size(); i++){
      RoleWithTaskBase_Renderer.ActionForRoleAndTask a = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
              StringsRole.Admin, StringsRole.AdminTasks.Status, -1, -1);
      a.parameters.put(StringsActions.loadArchiv, "" + i);
      d.savedTurniereLinks.add(new RoleWithTaskBase_Renderer.HyperLink(_dbInterface.getTurnierArchiveNames().get(i), a, true));
    }


  }

  @Override
  public void applyTestData() {
    RoleAdmin_TaskEinstellungen_Data d = _renderer.daten;

    d.anzGruppen = 2;
    for(int i = 1; i <=4; i++){
      RoleWithTaskBase_Renderer.ActionForRoleAndTask action = null;
      boolean isActive = false;
      if(i != d.anzGruppen){
        action = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1 );
        action.parameters.put(StringsActions.setAnzGroups, "" + i);
        isActive = true;
      }
      d.anzahlGruppen_Links.add(new RoleWithTaskBase_Renderer.HyperLink("" + i, action, isActive ));
    }

    for(int i = 0; i < d.anzGruppen; i++) {
      d.anzTeams_proGruppe.put(i, i+3);

      ArrayList<RoleWithTaskBase_Renderer.HyperLink> l = new ArrayList<>();
      for (int j = 3; j <= AppSettings.maxAnzTeams; j++) {
        RoleWithTaskBase_Renderer.ActionForRoleAndTask action = null;
        boolean isActive = false;
        if(j != d.anzTeams_proGruppe.get(i)){
          action = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
            StringsRole.Admin,
            StringsRole.AdminTasks.Einstellungen, -1, -1);
          action.parameters.put(StringsActions.setAnzTeams, "" + j);
          action.parameters.put(StringsActions.refGroupID, "" + (i));
          isActive = true;
        }
        l.add(new RoleWithTaskBase_Renderer.HyperLink(Integer.toString(j), action, isActive));
      }
      d.anzTeams_Dictionary.put(i,l);
    }

    d.mitRueckspielen = false;
    RoleWithTaskBase_Renderer.ActionForRoleAndTask a1 = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
      StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    a1.parameters.put(StringsActions.needrueckspiel, "true");
    d.mit_RueckspielenLink = new RoleWithTaskBase_Renderer.HyperLink("true", a1, true);

    d.anzSpielfelder = 2;
    for(int i = 1; i <= AppSettings.maxAnzSpielfelder; i++){
      if(i != d.anzSpielfelder){
        RoleWithTaskBase_Renderer.ActionForRoleAndTask a2 = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
          StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
        a2.parameters.put(StringsActions.setAnzFields, "" + i);
        d.anzSpielfelder_aendernLinks.add(new RoleWithTaskBase_Renderer.HyperLink("" + i, a2, true));
      }
    }

    d.vorausfuellenData = false;
    RoleWithTaskBase_Renderer.ActionForRoleAndTask a3 = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
      StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    a3.parameters.put(StringsActions.setPrefillScores, "true");
    d.vorausfuellenData_aendernLink = new RoleWithTaskBase_Renderer.HyperLink("true", a3, true);

    d.htmlFormAction = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
      StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    d.textBoxName = StringsActions.saveTurnier;

    for(int i = 0; i<4; i++){
      RoleWithTaskBase_Renderer.ActionForRoleAndTask a = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
        StringsRole.Admin, StringsRole.AdminTasks.Status, -1, -1);
      a.parameters.put(StringsActions.loadArchiv, "" + i);
      d.savedTurniereLinks.add(new RoleWithTaskBase_Renderer.HyperLink("gespeichertes Turnier Nr " + i, a, true));
    }
  }
}
