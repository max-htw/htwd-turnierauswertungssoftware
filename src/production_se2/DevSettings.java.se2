//Die Klasse ermoeglicht einzelnen Entwickler eigene Einstellungen zu verwenden, ohne die Einstellungen von
//anderen zu beeinfluessen.
//Die Datei DevSettings.java ist in .gitignore eingetragen.
//Damit ist fuer jeden Entwickler eine eigene Kopie der Datei sichtbar.
public class DevSettings {
    public static boolean turnOffAllRenderer(){
        //wenn "true" - einfach die Uebersicht der Daten anzeigen, statt der ordentlich aussehender Seite.
        return false;
    }

    public static RoleWithTaskBase_Renderer getRenderer(StringsRole role, StringsRole.RoleTask task){
        //wenn jemand einen anderen Renderer fuer bestimmte Seite nutzen moechte,
        //statt der in AppSettings festdefinierten Renderer.
        RoleWithTaskBase_Renderer ausgabe = null;

        /*
        if(role == StringsRole.Admin && task == StringsRole.AdminTasks.Hallo){
            return new RoleAdmin_TaskHallo_Renderer_v02();
        }
         */

        return ausgabe;
    }

    public static boolean useTestDataForRendering(){
        //controller koennen ein paar manuell erstellte Daten in den Renderer geben.
        //Damit kann man an dem Renderler programmieren,
        //wenn die DataBaseQueries noch nicht fertig sind.
        return false;
    }

    public static AppSettings.SupportedWebServerTypes getWebServerType(){
        //wird in Main() abgefragt.
        return null;
    }

}
