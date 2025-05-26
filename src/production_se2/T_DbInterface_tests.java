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
    IllegalArgumentException thrown = assertThrows(
      IllegalArgumentException.class,
      () -> db.turnierKonf_setAnzGruppen(AppSettings.minAnzGroups - 1));

    assertEquals(AppSettings.minAnzGroups, db.turnierKonf_getAnzGruppen());

    //gueltige Konfigurationen
    db.turnierKonf_setAnzGruppen(3);
    assertEquals(3, db.turnierKonf_getAnzGruppen());

    db.turnierKonf_setAnzTeamsByGroupID(2, 4);
    assertEquals(4, db.turnierKonf_getAnzTeamsByGroupID(2));
  
  }


}
