package oscana.s2n;

import junit.framework.JUnit4TestAdapter;
import junit.framework.Test;
import junit.framework.TestSuite;
import oscana.s2n.validation.ByteLengthTest;
import oscana.s2n.validation.CreditCardNumberTest;
import oscana.s2n.validation.DecimalRangeTest;
import oscana.s2n.validation.EmailTest;
import oscana.s2n.validation.LengthTest;
import oscana.s2n.validation.ParseByteTest;
import oscana.s2n.validation.ParseDateTest;
import oscana.s2n.validation.ParseDoubleTest;
import oscana.s2n.validation.ParseFloatTest;
import oscana.s2n.validation.ParseIntTest;
import oscana.s2n.validation.ParseLongTest;
import oscana.s2n.validation.ParseShortTest;
import oscana.s2n.validation.PatternTest;
import oscana.s2n.validation.RangeTest;
import oscana.s2n.validation.RequiredTest;
import oscana.s2n.validation.URLTest;
import oscana.s2n.validation.ValidateTargetTest;

/**
 * Validate系のテストを実行する。
 *
 */
public class AllTest2 {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        suite.addTest(new JUnit4TestAdapter(ByteLengthTest.class));
        suite.addTest(new JUnit4TestAdapter(CreditCardNumberTest.class));
        suite.addTest(new JUnit4TestAdapter(DecimalRangeTest.class));
        suite.addTest(new JUnit4TestAdapter(EmailTest.class));
        suite.addTest(new JUnit4TestAdapter(LengthTest.class));
        suite.addTest(new JUnit4TestAdapter(ParseByteTest.class));
        suite.addTest(new JUnit4TestAdapter(ParseDateTest.class));
        suite.addTest(new JUnit4TestAdapter(ParseDoubleTest.class));
        suite.addTest(new JUnit4TestAdapter(ParseFloatTest.class));
        suite.addTest(new JUnit4TestAdapter(ParseIntTest.class));
        suite.addTest(new JUnit4TestAdapter(ParseLongTest.class));
        suite.addTest(new JUnit4TestAdapter(ParseShortTest.class));
        suite.addTest(new JUnit4TestAdapter(PatternTest.class));
        suite.addTest(new JUnit4TestAdapter(RangeTest.class));
        suite.addTest(new JUnit4TestAdapter(RequiredTest.class));
        suite.addTest(new JUnit4TestAdapter(URLTest.class));
        suite.addTest(new JUnit4TestAdapter(ValidateTargetTest.class));
        return suite;
    }
}
