package oscana.s2n.validation;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import nablarch.core.ThreadContext;
import nablarch.core.validation.ee.ValidatorUtil;
import oscana.s2n.common.S2NConstants;

/**
 * {@link DecimalRange}のテスト。
 *
 */
public class DecimalRangeTest {
    private static Validator validator;

    @Before
    public void setUp() {
        validator = ValidatorUtil.getValidator();
    }

    /**
     * 正常な値（最大値）を設定する場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsValidMax() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue("99");
        bean.setValue5(3.9);
        bean.setValue6(3.9f);
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 正常な値（最小値）を設定する場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsValidMin() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue("10");
        bean.setValue5(3.9);
        bean.setValue6(3.9f);
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 異常な値を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValid() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue("2");
        bean.setValue5(3.9);
        bean.setValue6(3.9f);
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value"));
        assertThat(v.getMessage(), is("{fieldName}は10から99の間で入力してください。"));
    }

    /**
     * nullを設定する場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsValidNull() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue(null);
        bean.setValue5(3.9);
        bean.setValue6(3.9f);
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 違うターゲットを設定する場合、チェックされないこと
     */
    @Test
    public void testWithTarget_NG() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue("2");
        bean.setValue2("2");
        bean.setValue5(3.9);
        bean.setValue6(3.9f);
        ThreadContext.setObject(S2NConstants.THREAD_CONTEXT_KEY_CALL_METHOD_NAME, "testMethod2");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value"));
        assertThat(v.getMessage(), is("{fieldName}は10から99の間で入力してください。"));
    }

    /**
     * ターゲットを設定する場合、チェックされること
     */
    @Test
    public void testWithTarget_OK() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue("2");
        bean.setValue2("2");
        bean.setValue5(3.9);
        bean.setValue6(3.9f);
        ThreadContext.setObject(S2NConstants.THREAD_CONTEXT_KEY_CALL_METHOD_NAME, "testMethod");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(2));
        Iterator<ConstraintViolation<ValueTestBean>> iterator = violations.iterator();
        boolean value2Flag = false;
        while (iterator.hasNext()) {
            ConstraintViolation<ValueTestBean> v = iterator.next();
            if (v.getPropertyPath().toString().equals("value2")) {
                value2Flag = true;
            }
            assertThat(v.getMessage(), is("{fieldName}は10から99の間で入力してください。"));
        }
        assertTrue(value2Flag);
    }

    /**
     *  DecimalRangeアノテーションで配列をチェックできること（精査エラー発生しないのパターン）。
     */
    @Test
    public void testEmptyArray_OK() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue3(new String[] {});
        bean.setValue5(3.9);
        bean.setValue6(3.9f);
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     *  DecimalRangeアノテーションで配列をチェックできること（精査エラー発生するのパターン）。
     */
    @Test
    public void testArray_NG() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue3(new String[] {"123","45"});
        bean.setValue5(3.9);
        bean.setValue6(3.9f);

        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}は1から4の間で入力してください。"));
    }

    /**
     * DecimalRangeアノテーションで集合をチェックできること（精査エラー発生しないのパターン）。
     */
    @Test
    public void testEmptyCollection_OK() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue4(new ArrayList<>());
        bean.setValue5(3.9);
        bean.setValue6(3.9f);
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * DecimalRangeアノテーションで集合をチェックできること（精査エラー発生のパターン）。
     */
    @Test
    public void testCollection_NG() {
        ValueTestBean bean = new ValueTestBean();
        List<String> val = new ArrayList<String>();
        val.add("1");
        bean.setValue4(val);
        bean.setValue5(3.9);
        bean.setValue6(3.9f);
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}は1から4の間で入力してください。"));
    }

    /**
     *  Double、floatをチェックできること（精査エラー発生しないのパターン）。
     */
    @Test
    public void testDecimal_OK() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue5(3.9);
        bean.setValue6(3.9f);
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     *  Double、floatをチェックできること（精査エラー発生しないのパターン）。
     */
    @Test
    public void testDecimal_NG() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue5(4.1);
        bean.setValue6(4.1f);
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(2));
    }

    private static class ValueTestBean {
        @DecimalRange(max = "99", min = "10")
        private String value;

        @DecimalRange(max = "99", min = "10", target = "testMethod")
        private String value2;

        @DecimalRange(max = "4", min = "1")
        private String[] value3;

        @DecimalRange(max = "4", min = "1")
        private List<?> value4;

        @DecimalRange(max = "4.0", min = "1")
        private double value5;

        @DecimalRange(max = "4.0", min = "1")
        private float value6;

        public void setValue(String value) {
            this.value = value;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

        public void setValue3(String[] value3) {
            this.value3 = value3;
        }

        public void setValue4(List<?> value4) {
            this.value4 = value4;
        }

        public void setValue5(double value5) {
            this.value5 = value5;
        }

        public void setValue6(float value6) {
            this.value6 = value6;
        }

    }
}
