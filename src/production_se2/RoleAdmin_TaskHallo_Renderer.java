import java.io.IOException;

public class RoleAdmin_TaskHallo_Renderer extends RoleWithTaskBase_Renderer<RoleAdmin_TaskHallo_Data>{
    @Override
    public RoleAdmin_TaskHallo_Data getEmptyDaten() {
        return new RoleAdmin_TaskHallo_Data();
    }


    @Override
    public StringBuilder renderResponse(){
        StringBuilder r = new StringBuilder();

        r.append("<h1>Hallo!</h1>\n");

        return r;
    }

}
