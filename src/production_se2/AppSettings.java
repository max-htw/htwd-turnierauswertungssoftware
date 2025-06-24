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
          else if(task == StringsRole.AdminTasks.Turnierplan){
              ausgabe = new RoleAdmin_TaskTurnierplan_Renderer();
          }
          else if(task == StringsRole.AdminTasks.Home){
              ausgabe = new RoleAdmin_TaskHome_Renderer();
          }
          else if(task == StringsRole.AdminTasks.Historie){
              ausgabe = new RoleAdmin_TaskHistorie_Renderer();
          }
          else if(task == StringsRole.AdminTasks.Ergebnisse){
              ausgabe = new RoleAdmin_TaskErgebnisse_Renderer();
          }
          else if(task == StringsRole.AdminTasks.Turnierplan){
              ausgabe = new RoleAdmin_TaskTurnierplan_Renderer();
          }
        }
        else if(role == StringsRole.Stranger){
          if(task == StringsRole.KeineRoleTasks.SelectRole){
            ausgabe = new RoleUnbekannt_TaskUnbekannt_Renderer();
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

    public static DBInterfaceBase getDatabaseBackend(){
      SupportedDatabaseTypes dbType = DevSettings.getDatabaseType();
      if(dbType == null)
        dbType = SupportedDatabaseTypes.inMemory;

      DBInterfaceBase ausgabe;
      if(dbType == SupportedDatabaseTypes.sqLite) {
        ausgabe = new DBInterface_SQLite();
      }
      else{
        ausgabe = new DBInterface_InMemory();
      }
      return ausgabe;
    }


    public static final int maxAnzTeams = 8;
    public static final int minAnzTeams = 3;
    public static final int maxAnzGroups = 4;
    public static final int minAnzGroups = 1;
    public static final int maxAnzSpielfelder = 4;
    public static final int minAnzSpielfelder = 1;
    public static final int  minTimeSlotDuration = 15;
    public static final int  maxTimeSlotDuration = 30;

    public static final String archiveSubfolderName = "turnierArchiv";
}
