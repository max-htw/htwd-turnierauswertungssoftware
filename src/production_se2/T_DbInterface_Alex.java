import static org.junit.Assert.assertEquals;
import static org.junit.Assume.assumeTrue;

import org.junit.Test;

public class T_DbInterface_Alex extends T_DbInterface_setup {

      @Test
  public void turnier_konfiguration(){
    assumeTrue(!((db instanceof DBInterface_SQLite)));

    db.resetKonfiguration();

    db.turnierKonf_setAnzGruppen(4);
    assertEquals(4, db.turnierKonf_getAnzGruppen());

    
  }


}
