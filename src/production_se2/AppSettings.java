public class AppSettings {

    public static RoleWithTaskBase_Renderer<?> getRenderer(StringsRole role, StringsRole.RoleTask task){
        //Zuerst schauen wir ob der Entwickler die Einstellungen bei sich ueberschrieben hat.
        //wenn ja - geben wir seinen Renderer zurueck.
        //sonst wird die Standardzuordnung fuer Role+Task umgesetzt:
        RoleWithTaskBase_Renderer<?> ausgabe = DevSettings.getRenderer(role,task);
        if(ausgabe != null)
            return  ausgabe;

        if(role == StringsRole.Team){
            if(task == StringsRole.TeamTasks.Turnierplan){
                 ausgabe = new RoleTeam_TaskTurnierplan_Renderer();
            }
        }
        else if(role == StringsRole.Admin){
          if(task == StringsRole.AdminTasks.Einstellungen){
              ausgabe = new RoleAdmin_TaskEinstellungen_Renderer();
          }
          else if(task == StringsRole.AdminTasks.Hallo){
              ausgabe = new RoleAdmin_TaskHallo_Renderer();
          }
        }

        return ausgabe;
    }

    public enum SupportedWebServerTypes {
        cgi, internal, swing, mock;
    }

    public static SupportedWebServerTypes getWebServerType(){
        return SupportedWebServerTypes.internal;
    }

    public enum SupportedDatabaseTypes{
      inMemory, sqLite
    }

    public static DBInterfaceBase getDatabaseType(){
      SupportedDatabaseTypes dbType = DevSettings.getDatabaseType();
      if(dbType == null)
        dbType = SupportedDatabaseTypes.inMemory;

      DBInterfaceBase ausgabe;
      if(dbType == SupportedDatabaseTypes.sqLite) {
        //hier kommt spaeter DBInterface_sqLite, aber er ist noch nicht fertig
        ausgabe = new DBInterface_InMemory();
      }
      else{
        ausgabe = new DBInterface_InMemory();
      }
      return ausgabe;
    }

}
