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
import oscana.s2n.validation.Pattern.Flag;

/**
 * {@link Pattern}のテスト。
 */
public class PatternTest {
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
        bean.setValue1("1");
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * nullを設定する場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsValidNull() {
        ValueTestBean bean = new ValueTestBean();
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 異常な値を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsInValid() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue1("2");
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<Object> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value1"));
        assertThat(v.getMessage(), is("{fieldName}は入力形式が不正です。入力形式=0|1。"));
    }

    /**
     * 違うターゲットを設定する場合、チェックされないこと
     */
    @Test
    public void testWithTarget_NG() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue1("2");
        bean.setValue2("2");
        ThreadContext.setObject(S2NConstants.THREAD_CONTEXT_KEY_CALL_METHOD_NAME, "testMethod2");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value1"));
        assertThat(v.getMessage(), is("{fieldName}は入力形式が不正です。入力形式=0|1。"));
    }

    /**
     * ターゲットを設定する場合、チェックされること
     */
    @Test
    public void testWithTarget_OK() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue1("2");
        bean.setValue2("2");
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
            assertThat(v.getMessage(), is("{fieldName}は入力形式が不正です。入力形式=0|1。"));
        }
        assertTrue(value2Flag);
    }

    private static class ValueTestBean {
        @Pattern(regexp = "0|1", flags = Flag.CASE_INSENSITIVE)
        private String value1;

        @Pattern(regexp = "0|1", flags = Flag.CASE_INSENSITIVE, target = "testMethod")
        private String value2;

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }
    }

}
