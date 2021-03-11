package oscana.s2n.struts.action;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import oscana.s2n.struts.action.ActionMessage;


/**
 * {@link ActionMessage}のテスト。
 */
public class ActionMessageTest {

    protected ActionMessage amWithNoValue = null;
    protected ActionMessage amWithOneValue = null;
    protected ActionMessage amWithTwoValues = null;
    protected ActionMessage amWithThreeValues = null;
    protected ActionMessage amWithFourValues = null;
    protected ActionMessage amWithArrayValues = null;
    protected ActionMessage amWithTwoIntegerValues = null;
    protected ActionMessage amNoResource = null;
    protected Object[] test_values =
        new Object[] {
            "stringValue1", "stringValue2", "stringValue3", "stringValue4"
        };

    @Before
    public void setUp() {
        amWithNoValue = new ActionMessage("amWithNoValue");
        amWithOneValue =
            new ActionMessage("amWithOneValue", new String("stringValue"));
        amWithTwoValues =
            new ActionMessage("amWithTwoValues", new String("stringValue1"),
                new String("stringValue2"));
        amWithThreeValues =
            new ActionMessage("amWithThreeValues", new String("stringValue1"),
                new String("stringValue2"), new String("stringValue3"));
        amWithFourValues =
            new ActionMessage("amWithFourValues", new String("stringValue1"),
                new String("stringValue2"), new String("stringValue3"),
                new String("stringValue4"));
        amWithArrayValues = new ActionMessage("amWithArrayValues", test_values);
        amWithTwoIntegerValues =
            new ActionMessage("amWithTwoIntegerValues", new Integer(5),
                new Integer(10));
        amNoResource = new ActionMessage("amNoResource", false);
    }

    @Test
    public void tearDown() {
        amWithNoValue = null;
        amWithOneValue = null;
        amWithTwoValues = null;
        amWithThreeValues = null;
        amWithFourValues = null;
        amWithArrayValues = null;
        amWithTwoIntegerValues = null;
        amNoResource = null;
    }

    @Test
    public void testActionMessageWithNoValue() {
        assertTrue(amWithNoValue.getValues() == null);
        assertTrue(amWithNoValue.isResource());
        assertTrue(amWithNoValue.getKey() == "amWithNoValue");
        assertTrue(amWithNoValue.toString().equals("amWithNoValue[]"));
    }

    @Test
    public void testActionMessageWithAStringValue() {
        Object[] values = amWithOneValue.getValues();

        assertTrue(values != null);
        assertTrue(values.length == 1);
        assertTrue(values[0].equals("stringValue"));
        assertTrue(amWithOneValue.isResource());
        assertTrue(amWithOneValue.getKey() == "amWithOneValue");
        assertTrue(amWithOneValue.toString().equals("amWithOneValue[stringValue]"));
    }

    @Test
    public void testActionMessageWithTwoValues() {
        Object[] values = amWithTwoValues.getValues();

        assertTrue(values != null);
        assertTrue(values.length == 2);
        assertTrue(values[0].equals("stringValue1"));
        assertTrue(values[1].equals("stringValue2"));
        assertTrue(amWithTwoValues.isResource());
        assertTrue(amWithTwoValues.getKey() == "amWithTwoValues");
        assertTrue(amWithTwoValues.toString().equals("amWithTwoValues[stringValue1, stringValue2]"));
    }

    @Test
    public void testActionMessageWithThreeValues() {
        Object[] values = amWithThreeValues.getValues();

        assertTrue(values != null);
        assertTrue(values.length == 3);
        assertTrue(values[0].equals("stringValue1"));
        assertTrue(values[1].equals("stringValue2"));
        assertTrue(values[2].equals("stringValue3"));
        assertTrue(amWithThreeValues.getKey() == "amWithThreeValues");
        assertTrue(amWithThreeValues.isResource());
        assertTrue(amWithThreeValues.toString().equals("amWithThreeValues[stringValue1, stringValue2, stringValue3]"));
    }

    @Test
    public void testActionMessageWithFourValues() {
        Object[] values = amWithFourValues.getValues();

        assertTrue(values != null);
        assertTrue(values.length == 4);
        assertTrue(values[0].equals("stringValue1"));
        assertTrue(values[1].equals("stringValue2"));
        assertTrue(values[2].equals("stringValue3"));
        assertTrue(values[3].equals("stringValue4"));
        assertTrue(amWithFourValues.isResource());
        assertTrue(amWithFourValues.getKey() == "amWithFourValues");
        assertTrue(amWithFourValues.toString().equals("amWithFourValues[stringValue1, stringValue2, stringValue3, stringValue4]"));
    }

    @Test
    public void testActionMessageWithArrayValues() {
        Object[] values = amWithArrayValues.getValues();

        assertTrue(values != null);
        assertTrue(values.length == test_values.length);

        for (int i = 0; i < values.length; i++) {
            assertTrue(values[i] == test_values[i]);
        }

        assertTrue(amWithArrayValues.isResource());
        assertTrue(amWithArrayValues.getKey() == "amWithArrayValues");
        assertTrue(amWithArrayValues.toString().equals("amWithArrayValues[stringValue1, stringValue2, stringValue3, stringValue4]"));
    }

    @Test
    public void testActionWithTwoIntegers() {
        Object[] values = amWithTwoIntegerValues.getValues();

        assertTrue(values != null);
        assertTrue(values.length == 2);
        assertTrue(values[0] instanceof Integer);
        assertTrue(values[0].toString().equals("5"));
        assertTrue(values[1] instanceof Integer);
        assertTrue(values[1].toString().equals("10"));
        assertTrue(amWithTwoIntegerValues.isResource());
        assertTrue(amWithTwoIntegerValues.getKey() == "amWithTwoIntegerValues");
        assertTrue(amWithTwoIntegerValues.toString().equals("amWithTwoIntegerValues[5, 10]"));
    }

    @Test
    public void testActionNoResource() {
        assertTrue(amNoResource.getValues() == null);
        assertTrue(amNoResource.isResource() == false);
        assertTrue(amNoResource.getKey() == "amNoResource");
        assertTrue(amNoResource.toString().equals("amNoResource[]"));
    }
}
