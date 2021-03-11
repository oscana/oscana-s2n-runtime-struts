package oscana.s2n;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import oscana.s2n.common.ParamFilterTest;
import oscana.s2n.common.dao.S2NDaoContextFactoryTest;
import oscana.s2n.common.dao.S2NDaoContextTest;
import oscana.s2n.handler.HttpResourceHolderTest;
import oscana.s2n.handler.HttpResourceHolderUpdateHandlerTest;
import oscana.s2n.handler.S2NRoutesMethodBinderFactoryTest;
import oscana.s2n.handler.S2NRoutesMethodBinderTest;
import oscana.s2n.servlet.HttpServletRequestHolderTest;
import oscana.s2n.servlet.HttpServletResponseHolderTest;
import oscana.s2n.servlet.HttpSessionHolderTest;
import oscana.s2n.servlet.ServletContextHolderTest;
import oscana.s2n.struts.GenericsUtilTest;
import oscana.s2n.struts.OscanaHttpResourceConverUtilTest;
import oscana.s2n.struts.action.ActionErrorsTest;
import oscana.s2n.struts.action.ActionFormTest;
import oscana.s2n.struts.action.ActionForwardTest;
import oscana.s2n.struts.action.ActionMappingTest;
import oscana.s2n.struts.action.ActionMappingToolTest;
import oscana.s2n.struts.action.ActionMessageTest;
import oscana.s2n.struts.action.ActionMessagesTest;
import oscana.s2n.struts.action.ActionTest;
import oscana.s2n.struts.util.LabelValueBeanTest;
import oscana.s2n.struts.util.MessageResourcesTest;

/**
 * テストケースを順に実行するため。
 *
 */
public class AllTest1 {

    public static Test suite() {
        TestSuite suite = new TestSuite();

        //common
        suite.addTest(new JUnit4TestAdapter(ParamFilterTest.class));
        suite.addTest(new JUnit4TestAdapter(S2NDaoContextFactoryTest.class));
        suite.addTest(new JUnit4TestAdapter(S2NDaoContextTest.class));

        //handler
        suite.addTest(new JUnit4TestAdapter(HttpResourceHolderTest.class));
        suite.addTest(new JUnit4TestAdapter(HttpResourceHolderUpdateHandlerTest.class));
        suite.addTest(new JUnit4TestAdapter(S2NRoutesMethodBinderFactoryTest.class));
        suite.addTest(new JUnit4TestAdapter(S2NRoutesMethodBinderTest.class));

        //servlet
        suite.addTest(new JUnit4TestAdapter(HttpServletRequestHolderTest.class));
        suite.addTest(new JUnit4TestAdapter(HttpServletResponseHolderTest.class));
        suite.addTest(new JUnit4TestAdapter(HttpSessionHolderTest.class));
        suite.addTest(new JUnit4TestAdapter(ServletContextHolderTest.class));

        //struts
        suite.addTest(new JUnit4TestAdapter(ActionErrorsTest.class));
        suite.addTest(new JUnit4TestAdapter(ActionFormTest.class));
        suite.addTest(new JUnit4TestAdapter(ActionForwardTest.class));
        suite.addTest(new JUnit4TestAdapter(ActionMappingTest.class));
        suite.addTest(new JUnit4TestAdapter(ActionMappingToolTest.class));
        suite.addTest(new JUnit4TestAdapter(ActionMessagesTest.class));
        suite.addTest(new JUnit4TestAdapter(ActionMessageTest.class));
        suite.addTest(new JUnit4TestAdapter(ActionTest.class));
        suite.addTest(new JUnit4TestAdapter(LabelValueBeanTest.class));
        suite.addTest(new JUnit4TestAdapter(MessageResourcesTest.class));
        suite.addTest(new JUnit4TestAdapter(GenericsUtilTest.class));
        suite.addTest(new JUnit4TestAdapter(OscanaHttpResourceConverUtilTest.class));

        return suite;
      }
}
