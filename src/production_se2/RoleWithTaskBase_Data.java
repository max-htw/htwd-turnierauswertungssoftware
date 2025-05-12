public class RoleWithTaskBase_Data {
    public String htmlTitel = "";
    public String fehlermeldung = "";
    public String debugMessage = "";
    public StringsRole role;
    public StringsRole.RoleTask task;


    public final StringBuilder htmlUebersicht(RoleWithTaskBase_Renderer.ActionStringGenerator actionStringGenerator){
        StringBuilder r = new StringBuilder();

        //Role
        r.append("<p>Role: " );
        if(role!=null){
            r.append(role.toString());
        }
        else{
            r.append("nicht definiert");
        }
        r.append("</p>\n");

        //Task
        r.append("<p>Task: " );
        if(task!=null){
            r.append(task.toString());
        }
        else{
            r.append("nicht definiert");
        }
        r.append("</p>\n");

        //HTML-Titel
        r.append("<h1>Titel: ");
        if(!htmlTitel.isEmpty()){
            r.append(htmlTitel);
        }
        else{
            r.append("nicht definiert");
        }
        r.append("</h1>\n");

        //Fehlermeldung
        if(!fehlermeldung.isEmpty()){
            r.append("<p>Fehler: <span style=\"color:red;\">" + fehlermeldung + "</span></p>\n");
        }
        else {
            r.append("<p>Fehler: leer</p>\n");
        }

        //Debug-Message
        if(!debugMessage.isEmpty()) {
            r.append("<p>Debug-Message: " + debugMessage + "</p>\n");
        }
        else {
            r.append("<p>Debug-Message: leer</p>\n");
        }

        r.append(htmlOfDerrivedClass(actionStringGenerator));

        return  r;
    }

    public StringBuilder htmlOfDerrivedClass(RoleWithTaskBase_Renderer.ActionStringGenerator actionStringGenerator){
        StringBuilder r = new StringBuilder();

        r.append("<p>Um alle Daten anzuzeigen, soll die Methode " + this.getClass().getName()
                + ".htmlOfDerrivedClass() ueberschrieben werden.</p>\n");

        return r;
    }
}
