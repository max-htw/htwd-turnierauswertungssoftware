import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.util.ArrayList;

public class T_DbInterface_tests extends T_DbInterface_setup {

  @Test
  public void empty_db_initialization(){
    //assumeTrue(!(db instanceof DBInterface_InMemory));
    assumeTrue(!((db instanceof DBInterface_SQLite)));


    db.reset();

    //Am Anfang, nach dem reset(), muss die Danenbank automatisch mit einem minimalen Turnier initializiert werden:
    assertEquals(db.turnierKonf_getAnzGruppen(), AppSettings.minAnzGroups);
    for(int i = 0; i < db.turnierKonf_getAnzGruppen(); i++){
      int teamsAnz = db.turnierKonf_getAnzTeamsByGroupID(i);
      assertEquals(teamsAnz, AppSettings.minAnzTeams);
    }
    assertEquals(db.turnierKonf_getAnzSpielfelder(), AppSettings.minAnzSpielfelder);

  }

  @Test
  public void turnier_konfiguration(){

    //assumeTrue(!(db instanceof DBInterface_InMemory));
    assumeTrue(!((db instanceof DBInterface_SQLite)));

    db.reset();

    //Versuch einiger ungueltigen Konfigurationen:
    IllegalArgumentException thrown;
    thrown = assertThrows(IllegalArgumentException.class,
          () -> db.turnierKonf_setAnzGruppen(AppSettings.minAnzGroups - 1));
    thrown = assertThrows(IllegalArgumentException.class,
          () -> db.turnierKonf_setAnzGruppen(AppSettings.maxAnzGroups + 1));
    thrown = assertThrows(IllegalArgumentException.class,
          () -> db.turnierKonf_setAnzTeamsByGroupID(0, AppSettings.minAnzTeams - 1));
    thrown = assertThrows(IllegalArgumentException.class,
          () -> db.turnierKonf_setAnzTeamsByGroupID(0, AppSettings.maxAnzTeams + 1));

    //nach den ungueltigen Konfigurationen muss die DB so bleiben wie vorher:
    assertEquals(AppSettings.minAnzGroups, db.turnierKonf_getAnzGruppen());
    assertEquals(AppSettings.minAnzTeams, db.turnierKonf_getAnzTeamsByGroupID(0));

    //gueltige Konfigurationen
    db.turnierKonf_setAnzGruppen(3);
    assertEquals(3, db.turnierKonf_getAnzGruppen());

    db.turnierKonf_setAnzTeamsByGroupID(2, 4);
    assertEquals(4, db.turnierKonf_getAnzTeamsByGroupID(2));

    db.turnierKonf_setAnzSpielfelder(3);
    assertEquals(3, db.turnierKonf_getAnzSpielfelder());

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
