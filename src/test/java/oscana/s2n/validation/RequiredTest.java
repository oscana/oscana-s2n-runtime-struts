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
 * {@link Required}のテスト。
 */
public class RequiredTest {
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
        bean.setValue2(2);
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 異常な値を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsValidKara() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue1("");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(2));
        Iterator<ConstraintViolation<ValueTestBean>> ite = violations.iterator();
        while (ite.hasNext()) {
            ConstraintViolation<ValueTestBean> v = ite.next();
            if (v.getPropertyPath().toString().equals("value2")) {
                assertThat(v.getMessage(), is("{fieldName}は必須です。"));
            }
            if (v.getPropertyPath().toString().equals("value1")) {
                assertThat(v.getMessage(), is("{fieldName}は必須です。"));
            }
        }
    }

    /**
     * nullを設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsValidNull() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue1(null);
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(2));
        Iterator<ConstraintViolation<ValueTestBean>> ite = violations.iterator();
        while (ite.hasNext()) {
            ConstraintViolation<ValueTestBean> v = ite.next();
            if (v.getPropertyPath().toString().equals("value2")) {
                assertThat(v.getMessage(), is("{fieldName}は必須です。"));
            }
            if (v.getPropertyPath().toString().equals("value1")) {
                assertThat(v.getMessage(), is("{fieldName}は必須です。"));
            }
        }
    }

    /**
     * 違うターゲットを設定する場合、チェックされないこと
     */
    @Test
    public void testWithTarget_NG() {
        ValueTestBean1 bean = new ValueTestBean1();
        bean.setValue("");
        bean.setValue2("");
        ThreadContext.setObject(S2NConstants.THREAD_CONTEXT_KEY_CALL_METHOD_NAME, "testMethod2");
        Set<ConstraintViolation<ValueTestBean1>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean1> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value"));
        assertThat(v.getMessage(), is("{fieldName}は必須です。"));
    }

    /**
     * ターゲットを設定する場合、チェックされること
     */
    @Test
    public void testWithTarget_OK() {
        ValueTestBean1 bean = new ValueTestBean1();
        bean.setValue("");
        bean.setValue2("");
        ThreadContext.setObject(S2NConstants.THREAD_CONTEXT_KEY_CALL_METHOD_NAME, "testMethod");
        Set<ConstraintViolation<ValueTestBean1>> violations = validator.validate(bean);
        assertThat(violations.size(), is(2));
        Iterator<ConstraintViolation<ValueTestBean1>> iterator = violations.iterator();
        boolean value2Flag = false;
        while (iterator.hasNext()) {
            ConstraintViolation<ValueTestBean1> v = iterator.next();
            if (v.getPropertyPath().toString().equals("value2")) {
                value2Flag = true;
            }
            assertThat(v.getMessage(), is("{fieldName}は必須です。"));
        }
        assertTrue(value2Flag);
    }

    private static class ValueTestBean {
        @Required
        private String value1;

        @Required
        private Integer value2;

        public void setValue1(String value1) {
            this.value1 = value1;
        }

        public void setValue2(int value2) {
            this.value2 = value2;
        }

    }

    private static class ValueTestBean1 {
        @Required
        private String value;

        @Required(target = "testMethod")
        private String value2;

        public void setValue(String value) {
            this.value = value;
        }

        public void setValue2(String value2) {
            this.value2 = value2;
        }

    }
}
