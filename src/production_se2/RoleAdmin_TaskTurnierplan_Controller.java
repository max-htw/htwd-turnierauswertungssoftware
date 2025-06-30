import java.util.ArrayList;
import java.util.Map;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

public class RoleAdmin_TaskTurnierplan_Controller
    extends RoleWithTaskBase_Controller<RoleAdmin_TaskTurnierplan_Renderer>{

    RoleAdmin_TaskTurnierplan_Controller(RoleAdmin_TaskTurnierplan_Renderer renderer,
                                    Map<String,String> params, DBInterfaceBase dbBackend){

        super(renderer, params,StringsRole.Admin, StringsRole.AdminTasks.Turnierplan,dbBackend);
    }

    private boolean _needDebug = false;

    @Override
    public void applyActions() {
        // I tried to debug, because i have problems with the savings
        if(_needDebug) System.out.println("PARAMS: " + _params);
        String action = _params.get("action");
        if(_needDebug) System.out.println("DEBUG: action = " + action);

        if ("setScore".equals(action)) {
            try {
                String slotStr = _params.get("slot");
                String feldStr = _params.get("feld");
                String scoreAStr = _params.get("scoreA");
                String scoreBStr = _params.get("scoreB");
                if(_needDebug) System.out.println("DEBUG: action=" + action + ", slotStr=" + slotStr + ", feldStr=" + feldStr + ", scoreA=" + scoreAStr + ", scoreB=" + scoreBStr);

                if (scoreAStr != null && scoreBStr != null && !scoreAStr.trim().isEmpty() && !scoreBStr.trim().isEmpty() && slotStr != null && feldStr != null) {
                    int slot = Integer.parseInt(slotStr);
                    int feld = Integer.parseInt(feldStr);
                    int punkteA = Integer.parseInt(scoreAStr.trim());
                    int punkteB = Integer.parseInt(scoreBStr.trim());

                    ArrayList<DBInterfaceBase.FeldSchedule> plan = _dbInterface.getTurnierPlan();
                    DBInterfaceBase.SpielStats st = plan.get(feld).getSpiele().get(slot);
                    DBInterfaceBase.TurnierMatch tm = _dbInterface.getMatch(st.groupid, st.team1, st.team2);

                    if (st.isHinspiel) {
                        tm.setTeam1PunkteHinspiel(punkteA);
                        tm.setTeam2PunkteHinspiel(punkteB);
                    } else {
                        tm.setTeam1PunkteRueckspiel(punkteA);
                        tm.setTeam2PunkteRueckspiel(punkteB);
                    }
                    _dbInterface.updateMatch(tm);
                } else {
                    if(_needDebug) System.out.println("Fehler: Ungültige Eingabewerte – ein der Werten ist null");
                }
            } catch (Exception e) {
                if(_needDebug) System.out.println("Fehler beim Parsen oder Speichern: " + e.getMessage());
            }
        }
        RoleAdmin_TaskTurnierplan_Data d = _renderer.daten;
        ArrayList<DBInterfaceBase.FeldSchedule> tp = _dbInterface.getTurnierPlan();
        int maxRows = 0;
        for (int i = 0; i< tp.size(); i++){
            maxRows = tp.get(i).getSpiele().size() > maxRows? tp.get(i).getSpiele().size() : maxRows;
        }
        for(int i = 0; i < maxRows; i++){

            ArrayList<RoleWithTaskBase_Renderer.HyperLink> fieldLinks = new ArrayList<>();
            ArrayList<String> teamANames = new ArrayList<>();
            ArrayList<String> teamBNames = new ArrayList<>();
            ArrayList<String> schiriNames = new ArrayList<>();
            ArrayList<RoleWithTaskBase_Renderer.HyperLink> ergebnisLinks = new ArrayList<>();

            String timeText = _dbInterface.getTimeSlotString(i);
            for(int j = 0; j < tp.size(); j++){
                if(tp.get(j).getSpiele().size() > i && tp.get(j).getSpiele().get(i) != null && tp.get(j).getSpiele().get(i).groupid>=0){
                    DBInterfaceBase.SpielStats st = tp.get(j).getSpiele().get(i);
                    DBInterfaceBase.TurnierMatch tm = _dbInterface.getMatch(st.groupid, st.team1, st.team2);
                    RoleWithTaskBase_Renderer.ActionForRoleAndTask a =
                        new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Matchdetails, -1, -1);
                    a.parameters.put(StringsActions.refGroupID,"" + st.groupid);
                    a.parameters.put(StringsActions.refTeam1Nr,"" + st.team1);
                    a.parameters.put(StringsActions.refTeam2Nr,"" + st.team2);

                    RoleWithTaskBase_Renderer.HyperLink fieldL =
                        new RoleWithTaskBase_Renderer.HyperLink("Gr." + (st.groupid+1) + ": " +
                            (char)('A' + st.team1) + " vs. " + (char)('A' + st.team2) + " | " +
                            (char)('1' + (st.isHinspiel? tm.getHinspielRichterGroupID() : tm.getRueckspielRichterGroupID())) +
                            (char)('A' + (st.isHinspiel? tm.getHinspielRichterTeamID() : tm.getRueckspielRichterTeamID())), a, false);
                    fieldLinks.add(fieldL);
                    String teamAname = "" + (st.groupid + 1) + (char)('A' + st.team1);
                    teamANames.add(teamAname);
                    String teamBname = "" + (st.groupid + 1) + (char)('A' + st.team2);
                    teamBNames.add(teamBname);
                    String schiriName = "" + (char)('1' + (st.isHinspiel? tm.getHinspielRichterGroupID() : tm.getRueckspielRichterGroupID())) +
                                             (char)('A' + (st.isHinspiel? tm.getHinspielRichterTeamID() : tm.getRueckspielRichterTeamID()));
                    schiriNames.add(schiriName);

                    int teamApunkte = st.isHinspiel? tm.getTeam1PunkteHinspiel() : tm.getTeam1PunkteRueckspiel();
                    String teamApktStr = teamApunkte>=0?(String.format("%02d",teamApunkte)) : "--";

                    int teamBpunkte = st.isHinspiel? tm.getTeam2PunkteHinspiel() : tm.getTeam2PunkteRueckspiel();
                    String teamBpktStr = teamBpunkte>=0?(String.format("%02d",teamBpunkte)) : "--";

                    RoleWithTaskBase_Renderer.HyperLink ergebnL;
                    //ergebnL = new RoleWithTaskBase_Renderer.HyperLink(teamApktStr + " / " + teamBpktStr  , a, false);
                    ergebnL = new RoleWithTaskBase_Renderer.HyperLink(teamApktStr + ":" + teamBpktStr  , a, false);
                    ergebnisLinks.add(ergebnL);
                }
                else{
                    fieldLinks.add(null);
                    teamANames.add("");
                    teamBNames.add("");
                    schiriNames.add("");
                    ergebnisLinks.add(null);
                }
            }
            RoleAdmin_TaskTurnierplan_Renderer.TimeSlotPlanItem p =
            new RoleAdmin_TaskTurnierplan_Renderer.TimeSlotPlanItem(i, timeText, fieldLinks,  teamANames, teamBNames, schiriNames, ergebnisLinks);
            d.planItems.add(p);
        }

    }

    @Override
    public void applyTestData() {
        RoleAdmin_TaskTurnierplan_Data d = _renderer.daten;

        for(int i = 0; i<12; i++){
            ArrayList<RoleWithTaskBase_Renderer.HyperLink> fieldLinks = new ArrayList<>();

            ArrayList<String> teamANames = new ArrayList<>();
            ArrayList<String> teamBNames = new ArrayList<>();
            ArrayList<String> schiriNames = new ArrayList<>();
            ArrayList<RoleWithTaskBase_Renderer.HyperLink> ergebnisLinks = new ArrayList<>();

            for(int j = 0; j < 3; j++){
                RoleWithTaskBase_Renderer.HyperLink hl = null;
                String ta = null;
                String tb = null;
                String shi = null;
                RoleWithTaskBase_Renderer.HyperLink ergL = null;
                if((j == 0) || (j == 1 && i < 8) || (j == 2 && i < 4)){
                    RoleWithTaskBase_Renderer.ActionForRoleAndTask a =
                        new RoleWithTaskBase_Renderer.ActionForRoleAndTask(StringsRole.Admin, StringsRole.AdminTasks.Matchdetails, -1, -1);
                    a.parameters.put(StringsActions.matchID , "" + 333);
                    hl = new RoleWithTaskBase_Renderer.HyperLink("1A vs. 1B | 1C", a, true);
                    ta = "1A";
                    tb = "1B";
                    shi = "1C";
                    ergL = new RoleWithTaskBase_Renderer.HyperLink("15 / 20", a, false);
                }
                fieldLinks.add(hl);
                teamANames.add(ta);
                teamBNames.add(tb);
                schiriNames.add(shi);
                ergebnisLinks.add(ergL);
            }
            RoleAdmin_TaskTurnierplan_Renderer.TimeSlotPlanItem p = new RoleAdmin_TaskTurnierplan_Renderer.TimeSlotPlanItem(i, "00:00", fieldLinks, teamANames, teamBNames, schiriNames, ergebnisLinks);
            d.planItems.add(p);
        }

    }

}
