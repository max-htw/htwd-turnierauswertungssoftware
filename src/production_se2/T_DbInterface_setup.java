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

    @Parameters(name = "{index}: fib({0})={1}")
    public static Iterable<Object[]> data() {
      Iterable<Object[]> a = Arrays.asList(new Object[][]{{new DBInterface_InMemory()}, {new DBInterface_SQLite()}});
      return a;
    }


}


