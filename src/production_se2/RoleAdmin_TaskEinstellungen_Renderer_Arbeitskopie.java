public class RoleAdmin_TaskEinstellungen_Renderer_Arbeitskopie extends RoleAdmin_TaskEinstellungen_Renderer{

  /*
  Der Entwickler, der an dem View arbeitet, soll in seiner DevSeittings.java
  folgende 3 Zeilen hinzufuegen:

        if(role == StringsRole.Admin && task == StringsRole.AdminTasks.Einstellungen){
          return new RoleAdmin_TaskEinstellungen_Renderer_Arbeitskopie();
        }

  Die Methode renderHtmlResponse soll implementiert werden so,
  dass sie die HTML-Code fuer die Seite liefert.

  HTML-Anfang und HTML-Ende werden automatisch erstellt.
  Die renderHtmlResponse()-Methode soll das HTML-Teil zwischen den body-Tags liefern:
  <html><head></head><body> >>>renderHtmlResponse()<<< </body></html>

  Die Daten fuer diese View kommen vorausgefuellt vom Controller.
  Sie sind in der localen Klassen-Variable "daten" vom Typ RoleAdmin_TaskEinstellungen_Data gespeichert.

  Bitte, aendert die Klasse RoleAdmin_TaskEinstellungen_Data nicht ohne Absprache mit den Backend-Entwickler,
  weil die Klasse RoleAdmin_TaskEinstellungen_Data als Schnittstelle zwischen dem Renderer und dem Controller
  dient und die Backend-Entwickler verwenden die Klasse auch im Controller.

  Als Dokumentation darueber, welche Daten in der Wariable "daten" gespeichert sind
  und auch als Beispiel fuer die HTML-Erstellung schaut euch die Methode
  htmlOfDerrivedClass() der Klasse RoleAdmin_TaskEinstellungen_Data an.

  Die URLs fuer die Hrefs (von Hyperlinks und von Form-Tags) sollt ihr mithilfe der Methode getHref()
  erstellen (das ist Wrapper fuer get_actionStringGenerator().generateActionString().
  */

  @Override
  public StringBuilder renderHtmlResponse() {
    StringBuilder r = new StringBuilder();

    // >>> Beispielinhalt:
    r.append("<h1>").append(daten.htmlTitel).append("</h1>\n<a href=\"");
    r.append(getHref(daten.vorausfuellenData_aendernLink.linkAction));
    r.append("\">Beispiellink zum Vorausfuellen von zufälligen Daten</a><br><br>\n");

    r.append(daten.htmlUebersicht(get_actionStringGenerator()));
    // <<< Beispielinhalt

    return r;
  }
}
