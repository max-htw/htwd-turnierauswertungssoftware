import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public abstract class RoleWithTaskBase_Controller<Class_of_Renderer extends RoleWithTaskBase_Renderer> {

    protected Class_of_Renderer _renderer;
    protected Map<String, String> _params = new HashMap<>();

    public abstract void applyActions();

    public abstract void applyTestData();

    RoleWithTaskBase_Controller(RoleWithTaskBase_Renderer renderer,
                                Map<String, String> params,
                                StringsRole role,
                                StringsRole.RoleTask task){
        _renderer = (Class_of_Renderer)(renderer);
        _renderer.daten.role = role;
        _renderer.daten.task = task;
        _params = params;

    }

    public String getResponse(){
        String ausgabe = "";

        //wenn die Entwickler die Rendereinstellungen in DevSettings uebeschrieben haben,
        //rendern wir nach ihren Wunsch:
        RoleWithTaskBase_Renderer devRenderer = DevSettings.getRenderer(_renderer.daten.role, _renderer.daten.task);
        if((devRenderer != null) || (devRenderer == null && !DevSettings.turnOffAllRenderer())){
            // der Entwickler moechte explizit, dass dieser Ansicht gerendert wird
            // oder er hat keine besonderen Wuensche bezueglich dieses Renderers.
            // Rendern ganz normal:
            ausgabe += _renderer.renderResponse().toString();
        }
        else{
            // den Daten-Standardrenderer verwenden:
            ausgabe = _renderer.daten.htmlUebersicht().toString();
        }

        ausgabe = _renderer.RenderHtmlAnfang_() + ausgabe + _renderer.RenderHtmlEnde_();
        return  ausgabe;
    }

    public  Class<?> getRendererClass(){
        return _renderer.getClass();
    }


}
