import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class RoleWithTaskBase_Renderer<Class_of_Daten extends  RoleWithTaskBase_Data> {

    protected Class_of_Daten daten;
    public abstract Class_of_Daten getEmptyDaten();//{

    RoleWithTaskBase_Renderer(){
        daten = getEmptyDaten();
    }

    public StringBuilder renderResponse(){
        StringBuilder r = new StringBuilder();

        if(!this.getClass().getName().equals(RoleWithTaskBase_Renderer.class.getName())) {
            r.append("<p>Das ist die Standardimplementierung von RoleWithTaskBase_Renderer.renderResponse().<br>\n" +
                    "Sie soll in " + this.getClass().getName() + ".renderResponse() ueberschrieben werden.<br>\n" +
                    "</p>\n");
        }

        //einfache HTML-Ausgabe der Daten
        r.append(daten.htmlUebersicht().toString());

        return  r;
    }

    public String RenderHtmlAnfang_(){
        return "<!DOCTYPE html>\n<html>\n<head>\n</head>\n<body>\n\n";
    }

    public String RenderHtmlEnde_(){
        return "\n</body>\n</html>";
    }

    public  static  class NaviLink{
        NaviLink(String text, String url, boolean active) {
            linkText = text;
            linkUrl = url;
            isActive = active;
        }
        public String linkText;
        public String linkUrl;
        public boolean isActive;
    }
}
