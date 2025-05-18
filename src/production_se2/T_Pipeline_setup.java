import org.junit.After;
import org.junit.Before;

public class T_Pipeline_setup {

    DBInterface_InMemory db;
    WebserverMock ws;

    @Before
    public void setup(){
        db = new DBInterface_InMemory();
        ws = new WebserverMock(db);
    }
    
    @After
    public void teardown(){

    }

    
}
