import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;

public class T_Radmir {

  @Test
  public void turnierConfig_TurnierPlan(){
    DBInterfaceBase db = new DBInterface_InMemory();

    db.resetKonfiguration();

    db.turnierKonf_setAnzGruppen(3);
    db.turnierKonf_setAnzTeamsByGroupID(2, 4);
    db.turnierKonf_setAnzSpielfelder(3);
    db.turnierKonf_setNeedRueckspiele(true);


    //Turnierplan muss sich automatisch an die neue Konfiguration anpassen
    assertEquals(3, db.getTurnierPlan().size()); //3 Spielfelder

    //3 Gruppen: jeweils mit 3, 4, 3 Teams
    // (3x3-3)+(4x4-4)+(3x3-3)=6+12+6=24
    assertEquals(6, db.getMatchesByGroupID(2).size());

    int anzMatches = 0;
    ArrayList<DBInterfaceBase.FeldSchedule> tp = db.getTurnierPlan();
    for(int i = 0; i < tp.size(); i++){
      for(DBInterfaceBase.SpielStats s: db.getFeldSchedule(i)){
        if(s.feldID >= 0){ // Platzhalter-Spiele ausschliessen
          anzMatches ++;
        }
      }
    }
    assertEquals(24,anzMatches);
  }

  @Test
  public void testVollständigerPlanBei8Teams() {
      int anzahlTeams = 8;
      int maxMatches = anzahlTeams * (anzahlTeams - 1) / 2; // 28 Spiele bei Round-Robin
      var anzTeamsInGruppe = java.util.List.of(anzahlTeams);

      var plan = TurnierplanGenerator.generierePlan_01(
          1, anzTeamsInGruppe, 20, 2, false);

      assertEquals("Plan sollte alle Spiele enthalten", maxMatches, plan.size());
  }

  @Test
  public void testJedesTeamSpieltMindestensEinmal() {
      var anzTeamsInGruppe = java.util.List.of(3, 5, 3);
      int totalTeams = anzTeamsInGruppe.stream().mapToInt(i -> i).sum();

      var plan = TurnierplanGenerator.generierePlan_01(
          anzTeamsInGruppe.size(), anzTeamsInGruppe, 1000, 3, false);

      long aktiveTeams = plan.stream()
          .flatMap(s -> java.util.List.of(s.getMatch().getTeam1Nr(), s.getMatch().getTeam2Nr()).stream())
          .distinct().count();

      assertEquals("Alle Teams sollen mindestens ein Spiel haben", totalTeams, aktiveTeams);
  }

}
