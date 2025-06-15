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


}
