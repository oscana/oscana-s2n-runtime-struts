package oscana.s2n.validation;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Before;
import org.junit.Test;

import nablarch.core.ThreadContext;
import nablarch.core.validation.ee.ValidatorUtil;
import oscana.s2n.common.S2NConstants;

/**
 * {@link Range}のテスト。
 */
public class RangeTest {

    private static Validator validator;

    @Before
    public void setUp() {
        validator = ValidatorUtil.getValidator();
    }

    /**
     * 正常な値を設定する場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsValid() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue(3);
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 異常な値を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValid() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue(1);
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value"));
        assertThat(v.getMessage(), is("{fieldName}は3以上10以下の数値を入力してください。"));
    }

    /**
     * 異常な値を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValid_02() {
        ValueTestBean2 bean = new ValueTestBean2();
        bean.setValue1(new Double(11));
        bean.setValue2(new Float(11f));
        bean.setValue3(new BigDecimal(11));
        bean.setValue4(new BigInteger("11"));
        Set<ConstraintViolation<ValueTestBean2>> violations = validator.validate(bean);
        assertThat(violations.size(), is(5));
        ConstraintViolation<ValueTestBean2> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}は3以上10以下の数値を入力してください。"));
    }

    /**
     * 異常な値を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValid_03() {
        ValueTestBean2 bean = new ValueTestBean2();
        bean.setValue1(Double.POSITIVE_INFINITY);
        bean.setValue2(Float.POSITIVE_INFINITY);
        bean.setValue3(new BigDecimal(11));
        bean.setValue4(new BigInteger("11"));
        Set<ConstraintViolation<ValueTestBean2>> violations = validator.validate(bean);
        assertThat(violations.size(), is(5));
        ConstraintViolation<ValueTestBean2> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}は3以上10以下の数値を入力してください。"));
    }

    /**
     * 異常な値を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValid_04() {
        ValueTestBean2 bean = new ValueTestBean2();
        bean.setValue1(Double.NEGATIVE_INFINITY);
        bean.setValue2(Float.NEGATIVE_INFINITY);
        bean.setValue3(new BigDecimal(2));
        bean.setValue4(new BigInteger("2"));
        Set<ConstraintViolation<ValueTestBean2>> violations = validator.validate(bean);
        assertThat(violations.size(), is(5));
        ConstraintViolation<ValueTestBean2> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}は3以上10以下の数値を入力してください。"));
    }

    /**
     * nullを設定する場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsValidNull() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue(null);
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 違うターゲットを設定する場合、チェックされないこと
     */
    @Test
    public void testWithTarget_NG() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue(1);
        bean.setValue2(1);
        ThreadContext.setObject(S2NConstants.THREAD_CONTEXT_KEY_CALL_METHOD_NAME, "testMethod2");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}は3以上10以下の数値を入力してください。"));
    }

    /**
     * ターゲットを設定する場合、チェックされること
     */
    @Test
    public void testWithTarget_OK() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue(1);
        bean.setValue2(1);
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
            assertThat(v.getMessage(), is("{fieldName}は3以上10以下の数値を入力してください。"));
        }
        assertTrue(value2Flag);
    }

    /**
     * 異常なlong値を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValid_05() {
        ValueTestBean2 bean = new ValueTestBean2();
        bean.setValue5(11L);
        Set<ConstraintViolation<ValueTestBean2>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean2> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}は3以上10以下の数値を入力してください。"));
    }

    /**
     * 異常なString値を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValid_06() {
        ValueTestBean2 bean = new ValueTestBean2();
        bean.setValue5(3L);
        bean.setValue6("11");
        Set<ConstraintViolation<ValueTestBean2>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean2> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}は3以上10以下の数値を入力してください。"));
    }

    /**
     * サポート対象外の値を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValid_07() {
        ValueTestBean2 bean = new ValueTestBean2();
        bean.setValue5(3L);
        bean.setValue7(new Object());
        Set<ConstraintViolation<ValueTestBean2>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean2> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}は3以上10以下の数値を入力してください。"));
    }

    /**
     * 正常な値を設定する場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsValidBean2() {
        ValueTestBean2 bean = new ValueTestBean2();
        bean.setValue1(new Double(3));
        bean.setValue2(new Float(10f));
        bean.setValue3(new BigDecimal(3));
        bean.setValue4(new BigInteger("10"));
        bean.setValue5(10L);
        bean.setValue6("10");
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    private static class ValueTestBean {
        @Range(min = 3, max = 10)
        private Integer value;

        @Range(min = 3, max = 10, target = "testMethod")
        private Integer value2;

        public void setValue(Integer value) {
            this.value = value;
        }

        public void setValue2(Integer value2) {
            this.value2 = value2;
        }

    }

    private static class ValueTestBean2 {
        @Range(min = 3, max = 10)
        private Double value1;

        @Range(min = 3, max = 10)
        private Float value2;

        @Range(min = 3, max = 10)
        private BigDecimal value3;

        @Range(min = 3, max = 10)
        private BigInteger value4;

        @Range(min = 3, max = 10)
        private long value5;

        @Range(min = 3, max = 10)
        private String value6;

        @Range(min = 3, max = 10)
        private Object value7;

        public void setValue1(Double value1) {
            this.value1 = value1;
        }

        public void setValue2(Float value2) {
            this.value2 = value2;
        }

        public void setValue3(BigDecimal value3) {
            this.value3 = value3;
        }

        public void setValue4(BigInteger value4) {
            this.value4 = value4;
        }

        /**
         * @param value5 セットする value5
         */
        public void setValue5(Long value5) {
            this.value5 = value5;
        }

        /**
         * @param value6 セットする value6
         */
        public void setValue6(String value6) {
            this.value6 = value6;
        }

        /**
         * @param value7 セットする value7
         */
        public void setValue7(Object value7) {
            this.value7 = value7;
        }

    }
}
