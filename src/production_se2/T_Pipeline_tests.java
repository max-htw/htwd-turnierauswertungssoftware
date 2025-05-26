import static org.junit.Assert.*;

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
        
        db.reset();
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

    
}
