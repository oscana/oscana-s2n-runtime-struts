package oscana.s2n;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import oscana.s2n.common.web.interceptor.ExecuteTest;

/**
 * ExecuteTestを個別に実行するため。
 *
 */
public class AllTest3 {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new JUnit4TestAdapter(ExecuteTest.class));

        return suite;
      }
}
