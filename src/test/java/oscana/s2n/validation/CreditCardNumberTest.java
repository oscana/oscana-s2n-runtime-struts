package oscana.s2n.validation;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

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
 * {@link CreditCardNumber}のテスト。
 */
public class CreditCardNumberTest {

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
        bean.setValue("4062540301082212");
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 異常な値を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValid() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue("20191231");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value"));
        assertThat(v.getMessage(), is("{fieldName}はクレジットカードとして不正です。"));
    }

    /**
     * 異常な値を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValid_02() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue3("adasdersf");
        bean.setValue4("adasdersf");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(2));
        Iterator<ConstraintViolation<ValueTestBean>> iterator = violations.iterator();
        boolean value3Flag = false;
        boolean value4Flag = false;
        while (iterator.hasNext()) {
            ConstraintViolation<ValueTestBean> v = iterator.next();
            if (v.getPropertyPath().toString().equals("value3")) {
                value3Flag = true;
            }
            if (v.getPropertyPath().toString().equals("value4")) {
                value4Flag = true;
            }
            assertThat(v.getMessage(), is("{fieldName}はクレジットカードとして不正です。"));
        }
        assertTrue(value3Flag);
        assertTrue(value4Flag);
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
        bean.setValue("20191231");
        bean.setValue2("20191231");
        ThreadContext.setObject(S2NConstants.THREAD_CONTEXT_KEY_CALL_METHOD_NAME, "testMethod2");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}はクレジットカードとして不正です。"));
    }

    /**
     * ターゲットを設定する場合、チェックされること
     */
    @Test
    public void testWithTarget_OK() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue("20191231");
        bean.setValue2("20191231");
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
            assertThat(v.getMessage(), is("{fieldName}はクレジットカードとして不正です。"));
        }
        assertTrue(value2Flag);
    }

    private static class ValueTestBean {
        @CreditCardNumber
        private String value;

        @CreditCardNumber(target = "testMethod")
        private String value2;

        @CreditCardNumber(checkDigitIndex = 1, endIndex = 1, ignoreNonDigitCharacters = true)
        private String value3;

        @CreditCardNumber(checkDigitIndex = -1, endIndex = 1, ignoreNonDigitCharacters = true)
        private String value4;

        public void setValue(String value) {
            this.value = value;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

        public void setValue3(String value3) {
            this.value3 = value3;
        }

        public void setValue4(String value4) {
            this.value4 = value4;
        }

    }
}
