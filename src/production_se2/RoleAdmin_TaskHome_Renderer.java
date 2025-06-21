public class RoleAdmin_TaskHome_Renderer extends RoleWithTaskBase_Renderer_Admin<RoleAdmin_TaskHome_Data>{
    @Override
    public RoleAdmin_TaskHome_Data getEmptyDaten() {
      RoleAdmin_TaskHome_Data ausgabe = new RoleAdmin_TaskHome_Data();
      ausgabe.htmlTitel = "Startseite";
      return  ausgabe;
    }


    @Override
      public StringBuilder renderHtmlResponse(){
        StringBuilder r = new StringBuilder();

        r.append("<div class='max-w-5xl mx-auto p-6'>");

        // Titel und Einleitungstext
        r.append("<h1 class='text-xl sm:text-3xl font-bold text-center text-primary mb-4'>Willkommen zur Turnierauswertungssoftware!</h1>");
        r.append("<p class='text-center text-gray-700 mb-8'>");
        r.append("Die Software ermöglicht die effiziente digitale Erfassung von Spielständen und die Auswertung von Sportturnieren.");
        r.append("</p>");

        // Link-Sektion mit kurzer Erklärung
        r.append("<div class='space-y-6'>");

        for (RoleWithTaskBase_Renderer.HyperLink l : daten.navLinksGroup) {
            r.append("<div class='border border-gray-200 rounded-lg p-4 shadow hover:shadow-md transition text-center'>");
            r.append("<a href='").append(getHref(l.linkAction)).append("' class='text-xl text-primary hover:underline font-semibold'>").append(l.linkText).append("</a>");
            // Beschreibung je nach Linktext
            if (l.linkText.toLowerCase().contains("konfiguration")) {
                r.append("<p class='text-gray-600 mt-1'>Admin-Seite zum Einstellen der Turnierparameter</p>");
            } else if (l.linkText.toLowerCase().contains("turnierplan")) {
                r.append("<p class='text-gray-600 mt-1'>Zeigt den vollständigen Spielplan mit Zeiten, Feldern und Paarungen. Hier könnte ihr auch eure Ergebnisse eintragen</p>");
            } else if (l.linkText.toLowerCase().contains("ergebnis")) {
                r.append("<p class='text-gray-600 mt-1'>Anzeige der aktuellen Rangliste inklusive Statistiken</p>");
            } else {
                r.append("<p class='text-gray-600 mt-1'>Zur Seite \"").append(l.linkText).append("\".</p>");
            }
            r.append("</div>");
        }

        // Historie-Card
        r.append("<div class='border border-gray-200 rounded-lg p-4 shadow hover:shadow-md transition text-center'>");
        r.append("<a href='").append(getHref(daten.navLinkHistory.linkAction)).append("' class='text-xl text-primary hover:underline font-semibold'>Historie</a>");
        r.append("<p class='text-gray-600 mt-1'>Ein Archiv vergangener Turniere zur Einsicht und Analyse.</p>");
        r.append("</div>");

        r.append("</div>"); // Ende Link-Sektion

        // Benutzerhandbuch-Link unten
        r.append("<div class='mt-10 text-center'>");
        r.append("<a href='./benutzerhandbuch.pdf' target='_blank' class='text-blue-500 underline hover:text-blue-700'>Benutzerhandbuch öffnen (PDF)</a>");
        r.append("</div>");

        r.append("</div>");

        return r;
    }
}
