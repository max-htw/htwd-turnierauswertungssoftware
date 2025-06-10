import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class RoleAdmin_TaskEinstellungen_Renderer_Arbeitskopie extends RoleAdmin_TaskEinstellungen_Renderer{

  /*
  Der Entwickler, der an dem View arbeitet, soll in seiner DevSettings.java
  folgende 3 Zeilen hinzufuegen:

        if(role == StringsRole.Admin && task == StringsRole.AdminTasks.Einstellungen){
          return new RoleAdmin_TaskEinstellungen_Renderer_Arbeitskopie();
        }

  Die Methode renderHtmlResponse soll implementiert werden so,
  dass sie die HTML-Code fuer die Seite liefert.

  HTML-Anfang und HTML-Ende werden automatisch erstellt.
  Die renderHtmlResponse()-Methode soll im Allgemeinen das HTML-Teil zwischen den body-Tags liefern,
  aber in einigen Fällen sind noch einige Inhalte innerhalb von Body-Tags schon vorhanden:
  <html><head></head><body><... einige Inhalte> >>>renderHtmlResponse()<<< </ ... einige Inhalte></body></html>

  Die Daten fuer diese View kommen vorausgefuellt vom Controller.
  Sie sind in der localen Klassen-Variable "daten" vom Typ RoleAdmin_TaskEinstellungen_Data gespeichert.

  Bitte, aendert die Klasse RoleAdmin_TaskEinstellungen_Data nicht ohne Absprache mit den Backend-Entwickler,
  weil die Klasse RoleAdmin_TaskEinstellungen_Data als Schnittstelle zwischen dem Renderer und dem Controller
  dient und die Backend-Entwickler verwenden die Klasse auch im Controller.

  Als Dokumentation darueber, welche Daten in der Variable "daten" gespeichert sind
  und auch als Beispiel fuer die HTML-Erstellung schaut euch die Methode
  htmlOfDerrivedClass() der Klasse RoleAdmin_TaskEinstellungen_Data an.

  Die URLs fuer die Hrefs (von Hyperlinks und von Form-Tags) sollt ihr mithilfe der Methode getHref()
  erstellen (das ist Wrapper fuer get_actionStringGenerator().generateActionString().
  */

  @Override
  public StringBuilder renderHtmlResponse() {
    StringBuilder r = new StringBuilder();

    // Hauptinhalt
    r.append("<div class=\"grid grid-cols-3 gap-6 py-6\">");

    r.append("<div>");
    r.append("<h2 class=\"text-xl text-secondary font-bold mb-2\">Konfiguration</h2>");
    r.append(htmlConfig(get_actionStringGenerator()));
    r.append("</div>");

    r.append("<div>");
    r.append("<h2 class=\"text-xl text-secondary font-bold mb-2\">Generierung</h2>");
    r.append("<button class=\"bg-transparent hover:bg-primary-light text-primary font-semibold hover:text-white hover:cursor-pointer py-1 px-2 border border-primary hover:border-transparent rounded-full\" type=\"submit\">Generieren!</button>\n</form>");
    r.append("<img class=\"mt-5\" src=\"https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=Platzhalter\" alt=\"QR-Code\" class=\"w-36 h-36\" />");

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
    r.append("<p>Hin- und Rückspiel: <select onchange=\"location.href=this.value\">");

    boolean aktuellTrue = daten.mitRueckspielen == true;

    RoleWithTaskBase_Renderer.ActionForRoleAndTask aTrue = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
        StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    aTrue.parameters.put(StringsActions.needrueckspiel, "ja");
    String urlTrue = actionStringGenerator.generateActionString(aTrue);
    r.append("<option value=\"").append(urlTrue).append("\"").append(aktuellTrue ? " selected" : "").append(">true</option>");

    RoleWithTaskBase_Renderer.ActionForRoleAndTask aFalse = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
        StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    aFalse.parameters.put(StringsActions.needrueckspiel, "nein");
    String urlFalse = actionStringGenerator.generateActionString(aFalse);
    r.append("<option value=\"").append(urlFalse).append("\"").append(!aktuellTrue ? " selected" : "").append(">false</option>");

    r.append("</select></p>\n");

    // Dropdown-Menü für Anzahl der Spielfelder
    r.append("<p>Anzahl Spielfelder: <select onchange=\"location.href=this.value\">");
    for(RoleWithTaskBase_Renderer.HyperLink n: daten.anzSpielfelder_aendernLinks){
        String actionUrl = actionStringGenerator.generateActionString(n.linkAction);
        boolean selected = !n.isActive;
        r.append("<option value=\"").append(actionUrl).append("\"")
        .append(selected ? " selected" : "")
        .append(">")
        .append(n.linkText)
        .append("</option>");
    }
    r.append("</select></p>\n");

    // Eingabefeld für die Startzeit des Turniers
    r.append("<p>Startzeit des Turniers: <input type=\"time\" name=\"startzeit\" value=\"08:00\"></p>\n");


    // Dropdown-Menp zur Auswahl der Spieldauer
    r.append("<p>Spieldauer (Minuten): <select name=\"spieldauer\">\n");
    r.append("<option value=\"15\">15 Minuten</option>\n");
    r.append("<option value=\"20\">20 Minuten</option>\n");
    r.append("<option value=\"25\">25 Minuten</option>\n");
    r.append("<option value=\"30\">30 Minuten</option>\n");
    r.append("</select></p>\n");


    // Dropdown für Vorausfüllen mit Zufallsdaten
    r.append("<p>(((Vorausfüllen aktivieren:))) <select onchange=\"location.href=this.value\">");

    boolean vorausfuellenAktuellTrue = daten.vorausfuellenData == true;

    RoleWithTaskBase_Renderer.ActionForRoleAndTask aPrefillTrue = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
        StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    aPrefillTrue.parameters.put(StringsActions.setPrefillScores, "true");
    String urlPrefillTrue = actionStringGenerator.generateActionString(aPrefillTrue);
    r.append("<option value=\"").append(urlPrefillTrue).append("\"").append(vorausfuellenAktuellTrue ? " selected" : "").append(">true</option>");

    RoleWithTaskBase_Renderer.ActionForRoleAndTask aPrefillFalse = new RoleWithTaskBase_Renderer.ActionForRoleAndTask(
        StringsRole.Admin, StringsRole.AdminTasks.Einstellungen, -1, -1);
    aPrefillFalse.parameters.put(StringsActions.setPrefillScores, "false");
    String urlPrefillFalse = actionStringGenerator.generateActionString(aPrefillFalse);
    r.append("<option value=\"").append(urlPrefillFalse).append("\"").append(!vorausfuellenAktuellTrue ? " selected" : "").append(">false</option>");

    r.append("</select></p>\n");

    // Aktuelles Turnier speichern
    r.append("<br>Aktuelles Turnier speichern unter:<br>\n")
      .append("<form action=\"").append(actionStringGenerator.generateActionString(daten.htmlFormAction)).append("\" >\n")
      .append("<input type=\"text\" name=\"").append(daten.textBoxName.name().toLowerCase()).append("\" value=\"")
      .append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"))).append("\">\n")
      .append("<button class=\"bg-transparent hover:bg-primary-light text-primary font-semibold hover:text-white hover:cursor-pointer py-1 px-2 border border-primary hover:border-transparent rounded-full\" type=\"submit\">Speichern</button>\n</form>");

    return r;
  }

}
