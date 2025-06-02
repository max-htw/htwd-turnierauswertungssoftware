public class RoleAdmin_TaskErgebnisse_Renderer extends RoleWithTaskBase_Renderer_Admin<RoleAdmin_TaskErgebnisse_Data>{
  @Override
    public RoleAdmin_TaskErgebnisse_Data getEmptyDaten() {
          RoleAdmin_TaskErgebnisse_Data ausgabe = new RoleAdmin_TaskErgebnisse_Data();
          ausgabe.htmlTitel = "Ergebnisseite";
          return ausgabe;
  }


  @Override
  public StringBuilder renderHtmlResponse() {
    StringBuilder r = new StringBuilder();

    r.append("<div class='p-6 space-y-6'>");

    // Oben: Überschrift + Dropdown
    r.append("<div class='flex flex-col md:flex-row justify-start items-center md:items-center gap-4'>");
    r.append("<h2 class='text-2xl font-semibold'>Auswahl der Gruppe:</h2>");
    r.append("<form method='get'>");
    r.append("<select name='gruppe' onchange='this.form.submit()' ");
    r.append("class='px-4 py-2 border rounded-md shadow-sm focus:outline-none focus:ring-2 focus:ring-blue-500'>");
    r.append("<option value='A'>Gruppe A</option>");
    r.append("<option value='B'>Gruppe B</option>");
    r.append("</select>");
    r.append("</form>");
    r.append("</div>");

    // Mitte: Tabelle
    r.append("<div class='overflow-x-auto'>");
    r.append("<table class='w-full text-left border border-gray-200 shadow-sm mt-4'>");

        // Tabellenkopf
        r.append("<thead class='bg-primary'>");
        r.append("<tr>");
        r.append("<th class='px-4 py-2 border'></th>");
        r.append("<th class='px-4 py-2 border'>Team</th>");
        r.append("<th class='px-4 py-2 border'>Gewonnene Spiele</th>");
        r.append("<th class='px-4 py-2 border'>Gespielte Spiele</th>");
        r.append("<th class='px-4 py-2 border'>Punktedifferenz</th>");
        r.append("</tr>");
        r.append("</thead>");

        // Tabellenkörper (leer)
        r.append("<tbody>");


        // Beispielhafte Datenzeilen
        r.append("<tr class='hover:bg-gray-50'>");
        r.append("<td class='px-4 py-2 border'>1.</td>");
        r.append("<td class='px-4 py-2 border'>Alte Hasen</td>");
        r.append("<td class='px-4 py-2 border'>5</td>");
        r.append("<td class='px-4 py-2 border'>6</td>");
        r.append("<td class='px-4 py-2 border'>+37</td>");
        r.append("</tr>");

        r.append("<tr class='hover:bg-gray-50'>");
        r.append("<td class='px-4 py-2 border'>2.</td>");
        r.append("<td class='px-4 py-2 border'>Blitz Kicker</td>");
        r.append("<td class='px-4 py-2 border'>4</td>");
        r.append("<td class='px-4 py-2 border'>6</td>");
        r.append("<td class='px-4 py-2 border'>+15</td>");
        r.append("</tr>");

        r.append("<tr class='hover:bg-gray-50'>");
        r.append("<td class='px-4 py-2 border'>3.</td>");
        r.append("<td class='px-4 py-2 border'>Die Tornados</td>");
        r.append("<td class='px-4 py-2 border'>3</td>");
        r.append("<td class='px-4 py-2 border'>6</td>");
        r.append("<td class='px-4 py-2 border'>+2</td>");
        r.append("</tr>");

        r.append("<tr class='hover:bg-gray-50'>");
        r.append("<td class='px-4 py-2 border'>4.</td>");
        r.append("<td class='px-4 py-2 border'>FC Chaos</td>");
        r.append("<td class='px-4 py-2 border'>2</td>");
        r.append("<td class='px-4 py-2 border'>6</td>");
        r.append("<td class='px-4 py-2 border'>-11</td>");
        r.append("</tr>");

        r.append("<tr class='hover:bg-gray-50'>");
        r.append("<td class='px-4 py-2 border'>5.</td>");
        r.append("<td class='px-4 py-2 border'>Youngstars</td>");
        r.append("<td class='px-4 py-2 border'>1</td>");
        r.append("<td class='px-4 py-2 border'>6</td>");
        r.append("<td class='px-4 py-2 border'>-43</td>");
        r.append("</tr>");

        r.append("</tbody>");
        r.append("</table>");
        r.append("</div>");

    // Unten: Download-Links
    r.append("<div class='flex gap-4 mt-6'>");
    r.append("<a href='?action=downloadPdf' target='_blank' ");
    r.append("class='px-3 py-1.5 rounded bg-primary-light text-gray-800 text-sm hover:bg-gray-200 transition'>📄 PDF herunterladen</a>");
    r.append("<a href='?action=downloadExcel' target='_blank' ");
    r.append("class='px-3 py-1.5 rounded bg-secondary-light text-gray-800 text-sm hover:bg-gray-200 transition'>📊 Excel herunterladen</a>");
    r.append("</div>");

    r.append("</div>");

    return r;
  }
}