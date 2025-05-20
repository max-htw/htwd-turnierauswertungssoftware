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
}