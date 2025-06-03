public abstract class RoleWithTaskBase_Renderer_Admin<Class_of_Daten extends  RoleWithTaskBase_Data> extends RoleWithTaskBase_Renderer<Class_of_Daten>{    

    @Override
    public abstract Class_of_Daten getEmptyDaten();

    @Override
    public String RenderBodyAnfang_(){
        StringBuilder a = new StringBuilder();

        // Haupt-Wrapper
        a.append("<div class=\"w-full mx-auto px-4 md:px-10 lg:px-20\">\n"); 

        // Navigation
        a.append("<nav class=\"bg-white text-secondary py-3\">\n");
        a.append("<div class=\"grid grid-cols-[150px_1fr_150px] items-center\">\n");

        // Titel oben links
        a.append("<div class=\"text-primary uppercase xl:text-xl font-bold\">")
         .append("Turnierauswertungssoftware")
         .append("</div>\n");

        // Navigationslinks in der Mitte 
        a.append("<div class=\"hidden md:flex justify-end lg:justify-center space-x-0 lg:space-x-6\">\n");
        for(HyperLink l: daten.navLinksGroup){
            a.append(_renderNaviLink(l)).append("\n");
        }
        a.append("</div>\n");

        // Rechte Navigationslinks
        a.append("<div class=\"flex justify-end items-center space-x-0 lg:space-x-6\">\n");

        // Historie-Link
        a.append("<div class=\"hidden md:flex space-x-4 items-center\">\n");
        a.append(_renderNaviLink(daten.navLinkHistory)).append("\n");
        
        // Home-Link
        String activeStyle = "";
        if(daten.navLinkHome.isActive){
            activeStyle = "font-bold ";
        }
        a.append("<a href=\"").append(getHref(daten.navLinkHome.linkAction)).append("\" class=\"")
         .append(activeStyle)
         .append("border border-Main text-primary hover:text-main hover:font-bold px-3 py-2 rounded\">")
         .append(daten.navLinkHome.linkText).append("</a>\n");
        a.append("</div>\n");
        a.append("</div>\n");

        // Hamburger-Button (nur Mobil sichtbar)
        a.append("<div class=\"md:hidden flex justify-end items-center space-x-4\">\n");
        a.append("<button id=\"menu-toggle\" class=\"md:hidden text-2xl text-gray-600\">☰</button>\n");
        a.append("</div>\n");
        a.append("</div>\n");

        // Mobiles Menü (hidden by default)
        a.append("<div id=\"mobile-menu\" class=\"md:hidden hidden px-4 pt-2 pb-4 space-y-2\">\n");
        for (HyperLink l : daten.navLinksGroup) {
            a.append("<div>").append(_renderNaviLink(l)).append("</div>\n");
        }
        a.append("<div>").append(_renderNaviLink(daten.navLinkHistory)).append("</div>\n");
        a.append("<div><a href=\"").append(getHref(daten.navLinkHome.linkAction)).append("\" class=\"mx-3 max-w-[69px] block border border-Main text-primary hover:text-main hover:font-bold px-3 py-2 rounded\">")
        .append(daten.navLinkHome.linkText).append("</a></div>\n");
        a.append("</div>\n");

        a.append("</nav>\n");

        if(!daten.fehlermeldung.isEmpty()){
            a.append("<div class=\"flex items-center p-4 mb-4 text-sm text-yellow-800 border border-yellow-300 rounded-lg bg-yellow-50\">")
             .append("<span class=\"font-medium\">")
             .append(daten.fehlermeldung).append("</span></div>\n");
        }

        // Toggle-Skript fürs mobile Menü
        a.append("<script>\n")
        .append("document.addEventListener('DOMContentLoaded', function() {\n")
        .append("  document.getElementById('menu-toggle').addEventListener('click', function() {\n")
        .append("    document.getElementById('mobile-menu').classList.toggle('hidden');\n")
        .append("  });\n")
        .append("});\n")
        .append("</script>\n");

        return a.toString();
    }

    @Override
    public String RenderBodyEnde_(){
        return "</div>\n"; //Ende Haupt-div
    }

    private StringBuilder _renderNaviLink(HyperLink l){
        StringBuilder a = new StringBuilder();

        String href = getHref(l.linkAction);
        String activeStyle = "";
        if(l.isActive){
            activeStyle = "font-bold ";
        }
        a.append("<a href=\"").append(href).append( "\" class=\"").append(activeStyle)
         .append("hover:text-main hover:font-bold px-1 xl:px-3 py-2 rounded\">" )
         .append(l.linkText).append("</a>");
        return a;
    }
}