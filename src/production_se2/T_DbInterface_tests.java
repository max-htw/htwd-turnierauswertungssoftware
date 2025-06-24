import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.statements.ExpectException;
import org.junit.rules.ExpectedException;

import static org.junit.Assert.*;
import static org.junit.Assume.*;

import java.util.ArrayList;

public class T_DbInterface_tests extends T_DbInterface_setup {

  @Test
  public void db_resetKonfiguration(){
    //assumeTrue(!(db instanceof DBInterface_InMemory));
    assumeTrue(!((db instanceof DBInterface_SQLite)));

    //einige zufaellige Konfigurationsaenderungen:
    db.turnierKonf_setAnzGruppen(AppSettings.minAnzGroups + 2);
    db.turnierKonf_setAnzTeamsByGroupID(AppSettings.minAnzGroups + 1, AppSettings.minAnzTeams + 2);
    db.turnierKonf_setNeedRueckspiele(false);
    db.turnierKonf_setAnzSpielfelder(AppSettings.minAnzSpielfelder + 2);
    db.turnierKonf_setTimeSlotDuration(35);
    db.turnierKonf_setTurnierStartAsMinutes(480);
    db.match_setPunkteTeam1Hinspiel(0, 0, 1, 15);
    

    db.resetKonfiguration();

    //Am Anfang, nach dem reset(), muss die Danenbank automatisch mit einem minimalen Turnier initializiert werden:
    assertEquals(db.turnierKonf_getAnzGruppen(), AppSettings.minAnzGroups);
    for(int i = 0; i < db.turnierKonf_getAnzGruppen(); i++){
      int teamsAnz = db.turnierKonf_getAnzTeamsByGroupID(i);
      assertEquals(teamsAnz, AppSettings.minAnzTeams);
    }
    assertEquals(AppSettings.minAnzSpielfelder, db.turnierKonf_getAnzSpielfelder());
    assertEquals(AppSettings.minTimeSlotDuration, db.turnierKonf_getTimeSlotDuration());

    //beim Zuruecksetzen der Konfiguration, werden Auch Matches zurueckgesetzt
    assertEquals(-1, db.getMatch(0, 0, 1).getTeam1PunkteHinspiel());
  }

  @Test
  public void db_resetMatches(){
    //assumeTrue(!(db instanceof DBInterface_InMemory));
    assumeTrue(!((db instanceof DBInterface_SQLite)));

    db.resetKonfiguration();
    db.turnierKonf_setAnzGruppen(3);
    db.match_setPunkteTeam1Hinspiel(2, 0, 1, 15);

    db.resetMatches();

    assertEquals(3, db.turnierKonf_getAnzGruppen());
    DBInterfaceBase.TurnierMatch tm = db.getMatch(2, 0, 1);
    assertEquals(-1, tm.getTeam1PunkteHinspiel());
  }

  @Test
  public void turnierConfig_invalidConfig(){
    //assumeTrue(!(db instanceof DBInterface_InMemory));
    assumeTrue(!((db instanceof DBInterface_SQLite)));

    db.resetKonfiguration();

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
  }

  @Test
  public void turnierConfig_validConfig(){
    //assumeTrue(!(db instanceof DBInterface_InMemory));
    assumeTrue(!((db instanceof DBInterface_SQLite)));

    db.resetKonfiguration();

    db.turnierKonf_setAnzGruppen(3);
    assertEquals(3, db.turnierKonf_getAnzGruppen());

    db.turnierKonf_setAnzTeamsByGroupID(2, 4);
    assertEquals(4, db.turnierKonf_getAnzTeamsByGroupID(2));

    db.turnierKonf_setAnzSpielfelder(3);
    assertEquals(3, db.turnierKonf_getAnzSpielfelder());

    db.turnierKonf_setNeedRueckspiele(true);
    assertTrue(db.turnierKonf_getNeedRueckspiele());
  }

  @Test
  //der Test ist bei Radmir in toDo (stand 2025-06-03)  
  public void turnierConfig_TurnierPlan(){
    assumeTrue(!(db instanceof DBInterface_InMemory));
    assumeTrue(!((db instanceof DBInterface_SQLite)));

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
  public void match_Update(){
    //assumeTrue(!(db instanceof DBInterface_InMemory));
    assumeTrue(!((db instanceof DBInterface_SQLite)));

    db.resetKonfiguration();

    db.match_setPunkteTeam1Hinspiel(0, 0, 1, 9);
    db.match_setPunkteTeam2Hinspiel(0, 0, 1, 10);
    db.match_setPunkteTeam1Rueckspiel(0, 0, 1, 11);
    db.match_setPunkteTeam2Rueckspiel(0, 0, 1, 12);

    DBInterfaceBase.TurnierMatch m = db.getMatch(0, 0, 1);
    assertEquals(9, m.getTeam1PunkteHinspiel());
    assertEquals(10, m.getTeam2PunkteHinspiel());
    assertEquals(11, m.getTeam1PunkteRueckspiel());
    assertEquals(12, m.getTeam2PunkteRueckspiel());
  }

}