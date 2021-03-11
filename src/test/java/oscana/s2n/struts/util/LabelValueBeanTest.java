package oscana.s2n.struts.util;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * {@link LabelValueBean}のテスト。
 *
 */
public class LabelValueBeanTest {

    /**
     * 引数なしのオブジェクトを生成する場合、ラベルと値を取得できること
     */
    @Test
    public void testLabelValueBean_defualt() {
        LabelValueBean labelValueBean = new LabelValueBean();
        assertNull(labelValueBean.getLabel());
        assertNull(labelValueBean.getValue());

        labelValueBean.setLabel("test");
        labelValueBean.setValue("value");

        assertEquals("test", labelValueBean.getLabel());
        assertEquals("value", labelValueBean.getValue());
    }

    /**
     * 引数ありのオブジェクトを生成する場合、ラベルと値を取得できること
     */
    @Test
    public void testLabelValueBean_setLableAndValue() {
        LabelValueBean labelValueBean = new LabelValueBean("test", "value");
        assertEquals("test", labelValueBean.getLabel());
        assertEquals("value", labelValueBean.getValue());
    }

    /**
     * ラベルと値を設定する場合、比較できること
     */
    @Test
    public void testCompareTo() {
        LabelValueBean labelValueBean = new LabelValueBean("test", "value");
        assertEquals(0, labelValueBean.compareTo(labelValueBean));
    }

    /**
     * 値だけを設定する場合、比較できること
     */
    @Test
    public void testCompareTo_empty() {
        LabelValueBean labelValueBean = new LabelValueBean("", "value");
        assertEquals(0, labelValueBean.compareTo(labelValueBean));
    }

    /**
     * ラベルと値を設定する場合、Stringのオブジェクトを正常に戻ること
     */
    @Test
    public void testToString() {
        LabelValueBean labelValueBean = new LabelValueBean("test", "value");
        assertEquals("LabelValueBean[test, value]", labelValueBean.toString());
    }

    /**
     * ラベルと値を空に設定する場合、Stringのオブジェクトを正常に戻ること
     */
    @Test
    public void testToString_empty() {
        LabelValueBean labelValueBean = new LabelValueBean("", "");
        assertEquals("LabelValueBean[, ]", labelValueBean.toString());
    }

    /**
     * ラベルと値をnullに設定する場合、Stringのオブジェクトを正常に戻ること
     */
    @Test
    public void testToString_null() {
        LabelValueBean labelValueBean = new LabelValueBean();
        assertEquals("LabelValueBean[null, null]", labelValueBean.toString());
    }

    /**
     * 引数なしの前提で同じオブジェクトを比較する場合、Trueを戻ること
     */
    @Test
    public void testEquals_sameObject() {
        LabelValueBean labelValueBean = new LabelValueBean();
        assertTrue(labelValueBean.equals(labelValueBean));
    }

    /**
     * 引数なしの前提で違いオブジェクトを比較する場合、Falseを戻ること
     */
    @SuppressWarnings("unlikely-arg-type")
    @Test
    public void testEquals_notAssignmentObject() {
        LabelValueBean labelValueBean = new LabelValueBean();
        assertFalse(labelValueBean.equals(new LabelValueBeanTest()));
    }

    /**
     * 二つオブジェクトのラベルと値に同じ値を設定している場合、Trueを戻ること
     */
    @Test
    public void testEquals_diffObject1() {
        LabelValueBean labelValueBean1 = new LabelValueBean("test", "value");
        LabelValueBean labelValueBean2 = new LabelValueBean("test", "value");

        assertTrue(labelValueBean2.equals(labelValueBean1));
    }

    /**
     * 二つオブジェクトのラベルと値に同じように設定しない場合、Trueを戻ること
     */
    @Test
    public void testEquals_diffObject2() {
        LabelValueBean labelValueBean1 = new LabelValueBean();
        LabelValueBean labelValueBean2 = new LabelValueBean();

        assertTrue(labelValueBean2.equals(labelValueBean1));
    }

    /**
     * 二つオブジェクトのラベルと値に違い値を設定する場合、Falseを戻ること
     */
    @Test
    public void testEquals_diffObject3() {
        LabelValueBean labelValueBean1 = new LabelValueBean("test", "value");
        LabelValueBean labelValueBean2 = new LabelValueBean();

        assertFalse(labelValueBean2.equals(labelValueBean1));
    }

    /**
     * 二つオブジェクトのラベルと値に違い値を設定する場合、Falseを戻ること
     */
    @Test
    public void testEquals_diffObject4() {
        LabelValueBean labelValueBean1 = new LabelValueBean();
        LabelValueBean labelValueBean2 = new LabelValueBean("test", "value");

        assertFalse(labelValueBean2.equals(labelValueBean1));
    }

    /**
     * ラベルと値に値を設定しない場合、オブジェクトのハッシュ・コード値を返すこと
     */
    @Test
    public void testHashCode_null() {
        LabelValueBean labelValueBean = new LabelValueBean();
        assertEquals(17, labelValueBean.hashCode());
    }

    /**
     * ラベルと値に値を設定する場合、オブジェクトのハッシュ・コード値を返すこと
     */
    @Test
    public void testHashCode_notNull() {
        LabelValueBean labelValueBean = new LabelValueBean("test", "value");
        assertEquals(111972721, labelValueBean.hashCode());
    }

    /**
     * LabelValueBeanの複数のオブジェクトを比較する
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testComparator() {
        LabelValueBean[] sortableListFrom = new LabelValueBean[] {
                new LabelValueBean("Not Lowercase", "Nl"),
                new LabelValueBean("Capitalized", "Cap"),
        };
        LabelValueBean[] sortableListTo = new LabelValueBean[] {
                new LabelValueBean("Capitalized", "Cap"),
                new LabelValueBean("Not Lowercase", "Nl"),
        };
        java.util.Arrays.sort(sortableListFrom, LabelValueBean.CASE_INSENSITIVE_ORDER);
        for (int i = 0; i < sortableListFrom.length; i++) {
            assertEquals(sortableListFrom[i].getLabel(), sortableListTo[i].getLabel());
            assertEquals(sortableListFrom[i].getValue(), sortableListTo[i].getValue());
        }
    }

}