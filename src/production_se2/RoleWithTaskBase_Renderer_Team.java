public abstract class RoleWithTaskBase_Renderer_Team<Class_of_Daten extends  RoleWithTaskBase_Data> extends RoleWithTaskBase_Renderer<Class_of_Daten> {

    RoleWithTaskBase_Renderer_Team(){
        super();
    }

    @Override
    public abstract Class_of_Daten getEmptyDaten();

    @Override
    public String RenderBodyAnfang_(){
        StringBuilder r = new StringBuilder();
        for(RoleWithTaskBase_Renderer.HyperLink hl : daten.navLinksGroup){
            r.append("<a href=\"").append(getHref(hl.linkAction)).append("\">").append(hl.linkText).append("</a><br>\n");
        }
        return r.toString();
    }
    
}
