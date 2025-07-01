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

@Test
public void testGesamttabelleGenerierung() {
    // 1. Turnier konfigurieren: 1 Gruppe, 3 Teams
    db.turnierKonf_setAnzGruppen(1);
    db.turnierKonf_setAnzTeamsByGroupID(0, 3);
    db.turnierKonf_setNeedRueckspiele(false);

    // 2. Ergebnisse setzen: Team 0 schlägt Team 1, Team 1 schlägt Team 2, Team 2 schlägt Team 0
    db.match_setPunkteTeam1Hinspiel(0, 0, 1, 10); // Team 0: 10, Team 1: 5
    db.match_setPunkteTeam2Hinspiel(0, 0, 1, 5);

    db.match_setPunkteTeam1Hinspiel(0, 1, 2, 8);  // Team 1: 8, Team 2: 12
    db.match_setPunkteTeam2Hinspiel(0, 1, 2, 12);

    db.match_setPunkteTeam1Hinspiel(0, 2, 0, 15); // Team 2: 15, Team 0: 7
    db.match_setPunkteTeam2Hinspiel(0, 2, 0, 7);

    // 3. Gesamttabelle berechnen
    java.util.ArrayList<DBInterfaceBase.AuswertungsEintrag> tabelle = db.calculateAuswertung(0);

    // 4. Prüfen, ob alle Teams in der Tabelle sind
    assertEquals(3, tabelle.size());

    // 5. Prüfen, ob die Werte stimmen (Siege, Spiele, Punktdifferenz)
    // Beispiel: Team 0 sollte 1 Sieg, 2 Spiele, Punktdifferenz -3 haben
    DBInterfaceBase.AuswertungsEintrag team0 = tabelle.stream().filter(e -> e.teamId == 0).findFirst().get();
    assertEquals(1, team0.anzGewonnen);
    assertEquals(2, team0.anzGespielt);
    assertEquals(-3, team0.pktDifferenz);

    // Du kannst analog für Team 1 und Team 2 prüfen
}


@Test
public void testPdfExport_Spielplan() throws Exception {
    // Nur In-Memory-DB, SQLite überspringen
    assumeTrue("Nur InMemory-DB", db.getClass().getSimpleName().equals("DBInterface_InMemory"));

    // 1) Turnier konfigurieren (minimal)
    db.turnierKonf_setAnzGruppen(2);
    db.turnierKonf_setAnzTeamsByGroupID(0, 4);
    db.turnierKonf_setAnzTeamsByGroupID(1, 4);
    db.turnierKonf_setNeedRueckspiele(false);

    // 2) Turnierplan aus DB holen
    List<DBInterfaceBase.FeldSchedule> plan = db.getTurnierPlan();
    assertNotNull("Turnierplan darf nicht null sein", plan);
    assertTrue("Mindestens ein Spielfeld erwartet", plan.size() > 0);

    // 3) PDF-Export-Service aufrufen
    PDFExportService pdfService = new PDFExportService();
    byte[] pdfBytes = pdfService.exportSpielplanAsPdf(plan);

    // 4) Grundlegende Assertions am PDF
    assertNotNull("PDF-Bytes dürfen nicht null sein", pdfBytes);
    assertTrue("PDF darf nicht leer sein", pdfBytes.length > 1000);

    // 5) PDF-Kopf prüfen (erste 4 Zeichen müssen \"%PDF\" sein)
    String header = new String(pdfBytes, 0, 4, "ISO-8859-1");
    assertEquals("PDF-Kopfzeile stimmt nicht", "%PDF", header);
}


}

    

