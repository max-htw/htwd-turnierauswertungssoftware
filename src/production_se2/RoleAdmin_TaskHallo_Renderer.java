import java.io.IOException;

public class RoleAdmin_TaskHallo_Renderer extends RoleWithTaskBase_Renderer<RoleAdmin_TaskHallo_Data>{
    @Override
    public RoleAdmin_TaskHallo_Data getEmptyDaten() {
      RoleAdmin_TaskHallo_Data ausgabe = new RoleAdmin_TaskHallo_Data();
      ausgabe.htmlTitel = "\"Hallo\" Demo-Seite";
      return  ausgabe;
    }


    @Override
    public StringBuilder renderHtmlResponse(){
        StringBuilder r = new StringBuilder();

        r.append("<h1>Hallo!</h1>\n");
        r.append("<p>Titel dieser Seite: ").append(daten.htmlTitel).append("</p>\n");

        return r;
    }

}
