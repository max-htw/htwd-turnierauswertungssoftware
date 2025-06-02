import static org.junit.Assert.*;
import org.junit.Test;

public class T_Pipeline_Jonas {

    // Beispiel-Test für turnierKonf_setAnzGruppen
@Test
public void testSetAnzGruppen_validValue() {
    DBInterface_InMemory db = new DBInterface_InMemory();
    boolean result = db.turnierKonf_setAnzGruppen(AppSettings.minAnzGroups);
    assertTrue(result);
}

@Test(expected = IllegalArgumentException.class)
public void testSetAnzGruppen_invalidValue() {
    DBInterface_InMemory db = new DBInterface_InMemory();
    db.turnierKonf_setAnzGruppen(-1); // Ungültiger Wert, sollte Exception werfen
}

@Test
public void testSetAnzGruppen_minValue() {
    DBInterface_InMemory db = new DBInterface_InMemory();
    boolean result = db.turnierKonf_setAnzGruppen(AppSettings.minAnzGroups);
    assertTrue(result); // Mindestanzahl sollte erlaubt sein
}

@Test
public void testSetAnzGruppen_maxValue() {
    DBInterface_InMemory db = new DBInterface_InMemory();
    boolean result = db.turnierKonf_setAnzGruppen(AppSettings.maxAnzGroups);
    assertTrue(result); // Maximalanzahl sollte erlaubt sein
}

@Test(expected = IllegalArgumentException.class)
public void testSetAnzGruppen_aboveMaxValue() {
    DBInterface_InMemory db = new DBInterface_InMemory();
    db.turnierKonf_setAnzGruppen(AppSettings.maxAnzGroups + 1); // Über Maximalwert, sollte Exception werfen
}
// Bis hier hin Logik Tests

 @Test
    public void testTurnierSpeichernUndLaden() {
        // Setup: Neues Turnier anlegen und konfigurieren
        DBInterface db = new DBInterface_SQLite(); // echte DB oder Test-DB
        Turnier turnier = new Turnier("Integrationstest-Cup");
        turnier.setGruppenAnzahl(4);
        turnier.addTeam("Team A");
        turnier.addTeam("Team B");
        db.speichereTurnier(turnier);

        // Aktion: Turnier laden
        Turnier geladen = db.ladeTurnier("Integrationstest-Cup");

        // Überprüfen, ob die geladenen Daten korrekt sind
        assertNotNull(geladen);
        assertEquals("Integrationstest-Cup", geladen.getName());
        assertEquals(4, geladen.getGruppenAnzahl());
        assertTrue(geladen.getTeams().contains("Team A"));
        assertTrue(geladen.getTeams().contains("Team B"));
    }
}