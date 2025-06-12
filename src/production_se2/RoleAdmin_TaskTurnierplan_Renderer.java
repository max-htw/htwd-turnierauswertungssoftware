import java.util.ArrayList;

public class RoleAdmin_TaskTurnierplan_Renderer extends RoleWithTaskBase_Renderer_Admin<RoleAdmin_TaskTurnierplan_Data>{

    @Override
    public RoleAdmin_TaskTurnierplan_Data getEmptyDaten() {
        return new RoleAdmin_TaskTurnierplan_Data();
    } 

    public  static  class PlanItem{
        String uhrZeit;
        ArrayList<HyperLink> fieldLinks = new ArrayList<>();
        PlanItem(String zeit, ArrayList<HyperLink> links){
            this.uhrZeit = zeit;
            this.fieldLinks = links;
        }
    }

}
