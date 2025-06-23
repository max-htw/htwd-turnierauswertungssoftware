public class RoleAdmin_TaskHistorie_Renderer extends RoleWithTaskBase_Renderer_Admin<RoleAdmin_TaskHistorie_Data>{
    @Override
    public RoleAdmin_TaskHistorie_Data getEmptyDaten() {
      RoleAdmin_TaskHistorie_Data ausgabe = new RoleAdmin_TaskHistorie_Data();
      ausgabe.htmlTitel = "Historie";
      return  ausgabe;
    }


    @Override
      public StringBuilder renderHtmlResponse(){
        StringBuilder r = new StringBuilder();

        r.append("<div class='max-w-2xl mx-auto mt-10'>");
        r.append("<h2 class=\"text-xl text-secondary font-bold mb-5 \">Letzte 5 Turniere</h2>");

        // Fünf Beispiel-Turniere ---
        for (int index = 1; index <= 5; index++) {
            String name = "2025-06-21" + "#" + index;
            String ladenUrl = "/admin/historie/laden?id=" + index;
            String loeschenUrl = "/admin/historie/loeschen?id=" + index;

            r.append("<div class='flex items-center justify-between border-b'>");
            // Turnier-Info
            r.append("<span class='font-mono'>")
            .append(index).append(". ")
            .append(name)
            .append("</span>");
            // Buttons
            r.append("<div class='flex gap-2'>");
            r.append("<form method='post' action='").append(ladenUrl).append("'>");
            r.append("<button type='submit' class='bg-transparent hover:bg-primary-light text-primary font-semibold hover:text-white hover:cursor-pointer py-1 px-2 border border-primary hover:border-transparent rounded-full my-2'>Laden</button>");
            r.append("</form>");
            r.append("<form method='post' action='").append(loeschenUrl).append("'>");
            r.append("<button type='submit' class='bg-transparent hover:bg-primary-light text-primary font-semibold hover:text-white hover:cursor-pointer py-1 px-2 border border-primary hover:border-transparent rounded-full my-2'>Löschen</button>");
            r.append("</form>");
            r.append("</div>");
            r.append("</div>");
        }

        r.append("</div>");
        

        return r;
    }
}
