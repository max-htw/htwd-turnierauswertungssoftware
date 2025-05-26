import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.junit.runners.Parameterized.Parameter;

@RunWith(Parameterized.class)
public class T_DbInterface_setup {

    @Parameter(0)
    public DBInterfaceBase db;

    //naming: https://github.com/junit-team/junit4/wiki/parameterized-tests#identify-individual-test-cases
    @Parameters(name = "{0}")
    public static Iterable<Object[]> data() {
      Iterable<Object[]> a = Arrays.asList(new Object[][]{{new DBInterface_InMemory()}, {new DBInterface_SQLite()}});
      return a;
    }


}


