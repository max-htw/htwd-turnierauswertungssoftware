import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RoleAdmin_TaskEinstellungen_Renderer extends RoleWithTaskBase_Renderer_Admin<RoleAdmin_TaskEinstellungen_Data>{

  /*
  Mitteilung an das FrontEnd-Team:
  Diese Klasse bitte waehrend der Entwicklung unveraendert lassen.
  Die Entwicklungsarbeiten sollen in der Klasse "RoleAdmin_TaskEinstellungen_Renderer_Arbeitskopie"
  durchgefuehrt werden. Wenn das View fertig ist, kopieren wir die Code in diese Klasse.
  So koennen andere Entwickler, waehrend an der Arbeitskopie gearbeitet wird,
  mit der vereinfachte Version des Views weiterarbeiten.
   */

  @Override
  public RoleAdmin_TaskEinstellungen_Data getEmptyDaten() {
        RoleAdmin_TaskEinstellungen_Data ausgabe = new RoleAdmin_TaskEinstellungen_Data();
        ausgabe.htmlTitel = "Turnierkonfiguration";
        return ausgabe;
  }

  @Override
  public StringBuilder renderHtmlResponse() {
    StringBuilder r = new StringBuilder();

    String baseUrl = "http://localhost:8450"; // Dieser Wert sollte ggf. dynamisch gesetzt werden
    String qrUrl = baseUrl + "/admin/home";


    // Hauptinhalt
    r.append("<div class=\"grid grid-cols-2 gap-6 py-6 place-items-center\">");

    r.append("<div>");
    r.append("<h2 class=\"text-xl text-secondary font-bold mb-2\">Konfiguration</h2>");
    r.append(htmlConfig(get_actionStringGenerator()));
    r.append("</div>");

    r.append("<div>");
    r.append("<h2 class=\"text-xl text-secondary font-bold mb-2\">Teilen</h2>\n");
    r.append("<img class=\"mt-5\" src=\"https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=")
     .append(qrUrl)
     .append("\" alt=\"QR-Code\" class=\"w-36 h-36\" />");

    r.append("</div>");

    r.append("</div>");
    
    return r;
  }

  // Turnierkonfiguration
  public StringBuilder htmlConfig(RoleWithTaskBase_Renderer.ActionStringGenerator actionStringGenerator) {
    StringBuilder r = new StringBuilder();

    // Dropdown-Menü zur Auswahl der Gruppen-Anzahl
    r.append("<p>Anzahl Gruppen: <select onchange=\"location.href=this.value\">");
    for(RoleWithTaskBase_Renderer.HyperLink n: daten.anzahlGruppen_Links){
        String actionUrl = actionStringGenerator.generateActionString(n.linkAction);
        boolean selected = !n.isActive; // Nur das aktuelle Element ist "nicht aktiv" → also gerade gewählt
        r.append("<option value=\"").append(actionUrl).append("\"")
        .append(selected ? " selected" : "")
        .append(">")
        .append(n.linkText)
        .append("</option>");
    }
    r.append("</select></p>\n");

    // Dropdown-Menü zur Auswahl der Teams pro Gruppe
    for(int i = 0; i < daten.anzGruppen; i++){
        r.append("<p>Anzahl Teams für Gruppe ").append(i + 1).append(": ");
        r.append("<select onchange=\"location.href=this.value\">");
        for(RoleWithTaskBase_Renderer.HyperLink n: daten.anzTeams_Dictionary.get(i)){
            String actionUrl = actionStringGenerator.generateActionString(n.linkAction);
            boolean selected = !n.isActive;
            r.append("<option value=\"").append(actionUrl).append("\"")
            .append(selected ? " selected" : "")
            .append(">")
            .append(n.linkText)
            .append("</option>");
        }
        r.append("</select></p>\n");
    }

    // Dropdown-Menü für Hin-/Rückspiel
    String selTxt = "ja";
    String notSelText = "nein";
    if(!daten.mitRueckspielen){
      selTxt = "nein";
      notSelText = "ja";
    }
    r.append("<p>Hin- und Rückspiel: <select onchange=\"location.href=this.value\">");
    r.append("<option value=\"\">").append(selTxt).append("</option>\n");
    r.append("<option value=\"").append(getHref(daten.mit_RueckspielenLink.linkAction)).append("\">").append(notSelText).append("</option>");
    r.append("</select></p>\n");

    // Dropdown-Menü für Anzahl der Spielfelder
    r.append("<p>Anzahl Spielfelder: <select onchange=\"location.href=this.value\">");
    for(RoleWithTaskBase_Renderer.HyperLink n: daten.anzSpielfelder_aendernLinks){
        String actionUrl = getHref(n.linkAction);
        boolean selected = !n.isActive;
        r.append("<option value=\"").append(actionUrl).append("\"")
        .append(selected ? " selected" : "")
        .append(">")
        .append(n.linkText)
        .append("</option>");
    }
    r.append("</select></p>\n");

    // Eingabefeld für die Startzeit des Turniers
    r.append("<p>Startzeit des Turniers: <input type=\"time\" name=\"startzeit\" value=\""+ daten.anfangZeitStr + "\"></p>\n");


    // Dropdown-Menp zur Auswahl der Spieldauer
    r.append("<p>Spieldauer (Minuten): <select onchange=\"location.href=this.value\">\n");
    for(RoleWithTaskBase_Renderer.HyperLink n: daten.spielDauer_aendernLinks){
        String actionUrl = getHref(n.linkAction);
        boolean selected = !n.isActive;
        r.append("<option value=\"").append(actionUrl).append("\"")
        .append(selected ? " selected" : "")
        .append(">")
        .append(n.linkText)
        .append("</option>");
    }

    r.append("</select></p>\n");


    // Dropdown für Vorausfüllen mit Zufallsdaten
    /*selTxt = "ja";
    notSelText = "nein";
    if(!daten.vorausfuellenData){
      selTxt = "nein";
      notSelText = "ja";
    }
    r.append("<p>(((Vorausfüllen aktivieren:))) <select onchange=\"location.href=this.value\">");
    r.append("<option value=\"\">").append(selTxt).append("</option>\n");
    r.append("<option value=\"").append(getHref(daten.vorausfuellenData_aendernLink.linkAction)).append("\">").append(notSelText).append("</option>");
    r.append("</select></p>\n");*/

    // Aktuelles Turnier speichern
    r.append("<br>Aktuelles Turnier speichern unter:<br>\n")
      .append("<form id=\"saveForm\" action=\"").append(getHref(daten.htmlFormAction)).append("\" >\n")
      .append("<input class=\"text-center border rounded px-2 py-1\" type=\"text\" name=\"").append(daten.textBoxName.name().toLowerCase()).append("\" value=\"")
      .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))).append("\">\n")
      .append("<button class=\"bg-transparent hover:bg-primary-light text-primary font-semibold hover:text-white hover:cursor-pointer py-1 px-2 border border-primary hover:border-transparent rounded-full\" type=\"submit\">Speichern</button></form><br>\n");

      if(daten.savedTurniereLinks.size() > 0){
        r.append("Gespeicherte Turniere:<br>\n");
        r.append("<form id=\"loadForm\" action=\"").append(getHref(daten.htmlFormAction)).append("\" >\n");
        //r.append("<select onchange=\"document.getElementById('loadForm').submit();\" ");
        r.append("<select ");
        r.append("class=\"px-4 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500\" ");
        r.append("name=\"").append(StringsActions.loadArchiv.name().toLowerCase()).append("\">\n");

        for(RoleWithTaskBase_Renderer.HyperLink l: daten.savedTurniereLinks){
          String selected = "'>";
          if(l.isActive){
            selected = "' selected>";
          }
        r.append("<option value='").append(l.linkAction.parameters.get(StringsActions.loadArchiv)).append(selected).append(l.linkText).append("</option>\n");
        }
        r.append("</select>");
        r.append("<button class=\"bg-transparent hover:bg-primary-light text-primary font-semibold hover:text-white hover:cursor-pointer py-1 px-2 border border-primary hover:border-transparent rounded-full\" type=\"submit\">Laden</button></form><br>\n");
        r.append("</form>");
      }

    return r;
  }

  
  
}
