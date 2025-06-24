import static org.junit.Assert.*;
import static org.junit.Assume.*;
import org.junit.Before;
import org.junit.Test;




import java.util.List;

public class T_Pipeline_Jonas {

    private DBInterfaceBase db;

    @Before
    public void setUp() {
        // Wir testen über das Interface, aber mit der In-Memory-Implementierung
        db = new DBInterface_InMemory();
        db.reset();  // frischer Zustand vor jedem Test
    }

    // ----------------------
    // Modultests (JUnit 4)
    // ----------------------

    @Test
    public void testSetAnzGruppen_validValue() {
        boolean result = db.turnierKonf_setAnzGruppen(AppSettings.minAnzGroups);
        assertTrue("Mindestanzahl Gruppen sollte erlaubt sein", result);
        assertEquals("Anzahl Gruppen sollte korrekt gesetzt sein",
                     AppSettings.minAnzGroups,
                     db.turnierKonf_getAnzGruppen());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAnzGruppen_invalidValue() {
        db.turnierKonf_setAnzGruppen(-1); // Ungültiger Wert, sollte Exception werfen
    }

    @Test
    public void testSetAnzGruppen_maxValue() {
        boolean result = db.turnierKonf_setAnzGruppen(AppSettings.maxAnzGroups);
        assertTrue("Maximalanzahl Gruppen sollte erlaubt sein", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetAnzGruppen_aboveMaxValue() {
        db.turnierKonf_setAnzGruppen(AppSettings.maxAnzGroups + 1);
    }

    
    //Test T13 für Speichern und Laden eines Turniers

    @Test
    public void testSpeichernUndLaden_ÜberInterface() {
        // 1) Turnier konfigurieren
        db.turnierKonf_setAnzGruppen(3);
        db.turnierKonf_setAnzTeamsByGroupID(0, 4);
        db.turnierKonf_setNeedRueckspiele(true);

        // 2) Speichern unter Name "MeinCup"
        boolean saved = db.saveCurrentTurnierToArchive("MeinCup");
        assertTrue("Speichern sollte true zurückgeben", saved);

        // 3) Archivliste prüfen
        List<String> archive = db.getTurnierArchiveNames();
        assertNotNull("Archiv-Liste darf nicht null sein", archive);
        assertTrue("Archiv muss 'MeinCup' enthalten", archive.contains("MeinCup"));

        // 4) In neuen Zustand wechseln
        db.reset();
        assertNotEquals("Nach reset darf Gruppenanzahl nicht mehr 3 sein",
                       3, db.turnierKonf_getAnzGruppen());

        // 5) Turnier aus Archiv laden
        db.loadTurnierFromArchive( archive.indexOf("MeinCup") );

        // 6) Konfiguration nach Laden prüfen
        assertEquals("Anzahl Gruppen muss nach Laden 3 sein",
                     3, db.turnierKonf_getAnzGruppen());
        assertEquals("Team-Anzahl Gruppe 0 muss 2 sein",
                     4, db.turnierKonf_getAnzTeamsByGroupID(0));
        assertTrue("Rückspiel-Flag muss true sein",
                   db.turnierKonf_getNeedRueckspiele());
    }
 /**
     * T13: Interface-Test für Archiv-Funktion:
     * Speichern unter Name, Abfrage der Namen, Laden und Verifizieren.
     */
    @Test
    public void testArchivSaveLoad_Interface() {
        // Nur In-Memory-DB, SQLite überspringen
        assumeTrue("Nur InMemory-DB", !(db instanceof DBInterface_SQLite));

        // 1) Aktuelle Default-Konfiguration ändern
        db.turnierKonf_setAnzGruppen(4);
        db.turnierKonf_setAnzTeamsByGroupID(0, AppSettings.minAnzTeams + 1);
        db.turnierKonf_setNeedRueckspiele(false);

        // 2) Speichern unter einem eindeutigen Namen
        String name = "TestTurnier_" + System.currentTimeMillis();
        boolean saved = db.saveCurrentTurnierToArchive(name);
        assertTrue("saveCurrentTurnierToArchive sollte true liefern", saved);

        // 3) Prüfen, dass der Name im Archiv erscheint
        List<String> archive = db.getTurnierArchiveNames();
        assertNotNull("getTurnierArchiveNames darf nicht null sein", archive);
        assertTrue("Archiv muss enthalten: " + name, archive.contains(name));

        // 4) Reset, um sicherzustellen, dass wir wirklich neu laden
        db.reset();
        assertNotEquals("Gruppenanzahl nach reset darf nicht mehr 4 sein",
                       4, db.turnierKonf_getAnzGruppen());

        // 5) Laden aus dem Archiv anhand des gefundenen Index
        int idx = archive.indexOf(name);
        db.loadTurnierFromArchive(idx);

        // 6) Verifizieren, dass die gespeicherte Konfiguration wiederhergestellt wurde
        assertEquals("Gruppenanzahl nach Laden muss 4 sein",
                     4, db.turnierKonf_getAnzGruppen());
        assertEquals("Team-Anzahl Gruppe 0 muss minAnzTeams+1 sein",
                     AppSettings.minAnzTeams + 1,
                     db.turnierKonf_getAnzTeamsByGroupID(0));
        assertFalse("Rückspiel-Flag muss false sein",
                    db.turnierKonf_getNeedRueckspiele());
    }

    //Hier wird die Datenbank über das Interface getestet, um sicherzustellen, dass die Archiv-Funktionalität funktioniert.
    @Test
    public void testTurnierErgebnisseSpeichernUndLaden() {
    // Turnier konfigurieren
    db.turnierKonf_setAnzGruppen(1);
    db.turnierKonf_setAnzTeamsByGroupID(0, 4);
    db.turnierKonf_setAnzSpielfelder(1);

    // Ergebnis für ein Match setzen (Hinspiel)
    db.match_setPunkteTeam1Hinspiel(0, 0, 1, 15);
    db.match_setPunkteTeam2Hinspiel(0, 0, 1, 10);

    // Speichern ins Archiv
    boolean saved = db.saveCurrentTurnierToArchive("TestArchiv");
    assertTrue("Turnier wird nicht gespeichert", saved);

    // Reset und Laden aus Archiv
    db.reset();
    //db.loadTurnierFromArchive(0); // Annahme: 0 ist die Position von "TestArchiv"
    db.loadTurnierFromArchive("TestArchiv");

    // Ergebnis prüfen
   DBInterfaceBase.TurnierMatch match = db.getMatch(0, 0, 1);
    assertNotNull("Match sollte existieren", match);
    assertEquals(15, match.getTeam1PunkteHinspiel());
    assertEquals(10, match.getTeam2PunkteHinspiel());
}

 
   
}



