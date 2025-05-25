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
  Die renderHtmlResponse()-Methode soll das HTML-Teil zwischen den body-Tags liefern:
  <html><head></head><body> >>>renderHtmlResponse()<<< </body></html>

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

    // Definition der Links
    ActionForRoleAndTask einstellungenAction = new ActionForRoleAndTask(
      StringsRole.Admin,
      StringsRole.AdminTasks.Einstellungen,
      0,
      0
    );

    ActionForRoleAndTask homeAction = new ActionForRoleAndTask(
      StringsRole.Admin,
      StringsRole.AdminTasks.Hallo,
      0,
      0
    );

    ActionForRoleAndTask turnierplanAction = new ActionForRoleAndTask(
      StringsRole.Admin,
      StringsRole.AdminTasks.Turnierplan,
      0,
      0
    );

    ActionForRoleAndTask ergebnisseAction = new ActionForRoleAndTask(
      StringsRole.Team,
      StringsRole.TeamTasks.Stand,
      0,
      0
    );

    ActionForRoleAndTask historieAction = new ActionForRoleAndTask(
      StringsRole.Admin,
      StringsRole.AdminTasks.Status, //tbd
      0,
      0
    );

    r.append("<div class=\"w-full mx-auto px-20\">");
    
    // Navigationsleiste
    r.append("<nav class=\"bg-white text-secondary py-3\">");
    r.append("<div class=\"grid grid-cols-3 items-center\">");

    // Linke Seite: statischer Text
    r.append("<div class=\"text-primary uppercase text-xl font-bold\">Turnierauswertungssoftware</div>");

    // Mitte: Konfiguration, Turnierplan, Ergebnisse
    r.append("<div class=\"flex justify-center space-x-6\">");
    r.append("<a href=\"").append(getHref(einstellungenAction)).append("\" class=\"hover:text-main hover:font-bold px-3 py-2 rounded\">Konfiguration</a>");
    r.append("<a href=\"").append(getHref(turnierplanAction)).append("\" class=\"hover:text-main hover:font-bold px-3 py-2 rounded\">Turnierplan</a>");
    r.append("<a href=\"").append(getHref(ergebnisseAction)).append("\" class=\"hover:text-main hover:font-bold px-3 py-2 rounded\">Ergebnisse</a>");
    r.append("</div>");

    // Rechte Seite: Historie + Home mit Abstand
    r.append("<div class=\"flex justify-end items-center space-x-6\">");
    r.append("<a href=\"").append(getHref(historieAction)).append("\" class=\"hover:text-main hover:font-bold px-3 py-2 rounded\">Historie</a>");
    r.append("<a href=\"").append(getHref(homeAction)).append("\" class=\"border border-main text-primary hover:text-main hover:font-bold px-3 py-2 rounded\">Home</a>");
    r.append("</div>");

    r.append("</div>");
    r.append("</nav>");
    

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

    // Link zum Vorausfüllen von zufälligen Daten
    r.append(" <a href=\"");
    r.append(getHref(daten.vorausfuellenData_aendernLink.linkAction));
    r.append("\">Beispiellink zum Vorausfüllen von zufälligen Daten</a><br><br>\n");


    r.append("</div>"); 
    
   

    return r;
  }
}
