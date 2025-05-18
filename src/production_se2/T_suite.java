import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
    // Hier kommen alle Testklassen rein
    T_DbInterface_tests.class,
    T_Pipeline_tests.class
})

public class T_suite {
   // bleibt leer!!!    
}
