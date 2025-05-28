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
    r.append("<h2 class=\"text-xl text-secondary font-bold mb-2\">Generierung</h2>");
    r.append("<button class=\"bg-transparent hover:bg-primary-light text-primary font-semibold hover:text-white hover:cursor-pointer py-1 px-2 border border-primary hover:border-transparent rounded-full\" type=\"submit\">Generieren!</button>\n</form>");
    r.append("<img class=\"mt-5\" src=\"https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=Platzhalter\" alt=\"QR-Code\" class=\"w-36 h-36\" />");

    r.append("</div>");

    r.append("</div>");
    
    return r;
  }
}
