package oscana.s2n;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import oscana.s2n.common.web.util.FieldUtilsTest;
import oscana.s2n.struts.upload.FormFileTest;

/**
 * 個別に実行するため。
 *
 */
public class AllTest4 {

    public static Test suite() {
        TestSuite suite = new TestSuite();

        suite.addTest(new JUnit4TestAdapter(FieldUtilsTest.class));
        suite.addTest(new JUnit4TestAdapter(FormFileTest.class));

        return suite;
      }
}
