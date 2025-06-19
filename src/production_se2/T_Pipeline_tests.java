import static org.junit.Assert.*;
import static org.junit.Assume.*;
import java.util.List;
import org.junit.Test;

import org.junit.Ignore;
import org.junit.Test;

import org.junit.Assert.*;

public class T_Pipeline_tests extends T_Pipeline_setup {

    //im Parent-Class T_Pipeline_setup werden die Webserver und Datenbank-Variablen definiert:
    // DBInterface_InMemory db;
    // WebserverMock ws = new WebserverMock(db);;

    @Test
    public void RoleAdmin_TaskEinstellungen_SetAnzGroups(){
        //mit dem Mock-Webserver wird die RoleAdmin_TaskEinstellungen Seite aufgerufen 
        //und die User-Action für das aendern der Anzahl der Gruppen aufgefordert:
        
        db.resetKonfiguration();
        ws.requestRole = StringsRole.Admin;
        ws.requestTask = StringsRole.AdminTasks.Einstellungen;
        ws.requestGroupID = -1;
        ws.requestTeamNr = -1;
        ws.requestParams.put(StringsActions.setAnzGroups, "" + (AppSettings.maxAnzGroups - 1));

        RoleWithTaskBase_Data rendererData = ws.serveUserRequest();

        assertTrue(rendererData instanceof RoleAdmin_TaskEinstellungen_Data);
        RoleAdmin_TaskEinstellungen_Data d = (RoleAdmin_TaskEinstellungen_Data)rendererData;

        assertEquals(AppSettings.maxAnzGroups - 1, d.anzGruppen);
        assertTrue(d.anzahlGruppen_Links.get(AppSettings.maxAnzGroups - 1 - 1).linkAction == null);

    }
    
@Ignore("Speichern noch nicht implementiert")
@Test
    public void testSpeichernUndLaden_ÜberInterface() {
        // Nur In-Memory testen, nicht SQLite
        assumeTrue(!DBInterface_SQLite.class.isInstance(db));

        // 1) Konfiguration anpassen
        db.turnierKonf_setAnzGruppen(3);
        db.turnierKonf_setAnzTeamsByGroupID(0, AppSettings.minAnzTeams);
        db.turnierKonf_setNeedRueckspiele(true);

        // 2) Speichern unter "MeinCup"
        boolean saved = db.saveCurrentTurnierToArchive("MeinCup");
        assertTrue("Speichern sollte true zurückgeben", saved);
         

        // 3) Archivliste prüfen
        List<String> archive = db.getTurnierArchiveNames();
        assertNotNull("Archiv-Liste darf nicht null sein", archive);
        assertTrue("Archiv muss 'MeinCup' enthalten", archive.contains("MeinCup"));

        // 4) Reset und sicherstellen, dass Default-Werte zurückkehren
        db.reset();
        assertNotEquals("Nach reset darf Gruppenanzahl nicht 3 sein",
                       3, db.turnierKonf_getAnzGruppen());

        // 5) Laden aus Archiv
        int idx = archive.indexOf("MeinCup");
        db.loadTurnierFromArchive(idx);

        // 6) Geladene Konfiguration prüfen
        assertEquals("Anzahl Gruppen muss nach Laden 3 sein",
                     3, db.turnierKonf_getAnzGruppen());
        assertEquals("Team-Anzahl Gruppe 0 muss minAnzTeams sein",
                     AppSettings.minAnzTeams, db.turnierKonf_getAnzTeamsByGroupID(0));
        assertTrue("Rückspiel-Flag muss true sein",
                   db.turnierKonf_getNeedRueckspiele());
    }
}

    

