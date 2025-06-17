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
        db.turnierKonf_setAnzTeamsByGroupID(0, 3);
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
                     2, db.turnierKonf_getAnzTeamsByGroupID(0));
        assertTrue("Rückspiel-Flag muss true sein",
                   db.turnierKonf_getNeedRueckspiele());
    }
}

