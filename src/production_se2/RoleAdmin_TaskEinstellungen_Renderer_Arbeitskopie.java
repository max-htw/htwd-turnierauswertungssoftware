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
    r.append(daten.htmlConfig(get_actionStringGenerator()));
    r.append("</div>");

    r.append("<div>");
    r.append("<h2 class=\"text-xl text-secondary font-bold mb-2\">Teamnamen</h2>");
    r.append("<p>Hierfür fehlt aktuell noch die Datenschnittstelle.</p>");
    r.append("</div>");

    r.append("</div>");
    
    return r;
  }
}
