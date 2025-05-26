public abstract class RoleWithTaskBase_Renderer_Admin<Class_of_Daten extends  RoleWithTaskBase_Data> extends RoleWithTaskBase_Renderer<Class_of_Daten>{    

    @Override
    public abstract Class_of_Daten getEmptyDaten();

    @Override
    public String RenderBodyAnfang_(){
        StringBuilder a = new StringBuilder();
        a.append("<div class=\"w-full mx-auto px-20\">\n"); //Anfang Haupt-div
        a.append("<nav class=\"bg-white text-secondary py-3\">\n");
        a.append("<div class=\"grid grid-cols-3 items-center\">\n");
        a.append("<div class=\"text-primary uppercase text-xl font-bold\">")
         .append("Turnierauswertungssoftware")
         .append("</div>\n");
        a.append("<div class=\"flex justify-center space-x-6\">\n");
        for(HyperLink l: daten.navLinksGroup){
            a.append(_renderNaviLink(l)).append("\n");
        }
        a.append("</div>\n");
        a.append("<div class=\"flex justify-end items-center space-x-6\">\n");
        a.append(_renderNaviLink(daten.navLinkHistory)).append("\n");
        
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
        a.append("</nav>\n");

        if(!daten.fehlermeldung.isEmpty()){
            a.append("<div class=\"flex items-center p-4 mb-4 text-sm text-yellow-800 border border-yellow-300 rounded-lg bg-yellow-50\">")
             .append("<span class=\"font-medium\">")
             .append(daten.fehlermeldung).append("</span></div>\n");
        }

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
         .append("hover:text-main hover:font-bold px-3 py-2 rounded\">" )
         .append(l.linkText).append("</a>");
        return a;
    }
}