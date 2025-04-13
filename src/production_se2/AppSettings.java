import java.util.ArrayList;

public class AppSettings {

    public static RoleWithTaskBase_Renderer getRenderer(StringsRole role, StringsRole.RoleTask task){
        //Zuerst schauen wir ob der Entwickler die Einstellungen bei sich ueberschrieben hat.
        //wenn ja - geben wir seinen Renderer zurueck.
        //sonst wird die Standardzuordnung fuer Role+Task umgesetzt:
        RoleWithTaskBase_Renderer ausgabe = DevSettings.getRenderer(role,task);
        if(ausgabe != null)
            return  ausgabe;

        if(role == StringsRole.Team){
            if(task == StringsRole.TeamTasks.Turnierplan){
                 ausgabe = new RoleTeam_TaskTurnierplan_Renderer();
            }
        }
        else if(role == StringsRole.Admin){
            if(task == StringsRole.AdminTasks.Hallo){
                ausgabe = new RoleAdmin_TaskHallo_Renderer();
            }
        }

        return ausgabe;
    }

    public enum SupportedWebServerTypes {
        cgi, internal, mock;
    }

    public static SupportedWebServerTypes getWebServerType(){
        return SupportedWebServerTypes.internal;
    }


}
