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
 * {@link ParseDate}のテスト。
 */
public class ParseDateTest {

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
        bean.setValue("2020/05/18");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 異常な値を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsValidThrowExcepution() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue("20200518");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value"));
        assertThat(v.getMessage(), is("{fieldName}はDate型として不正です。"));
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
        bean.setValue("20200518");
        bean.setValue2("20200518");
        ThreadContext.setObject(S2NConstants.THREAD_CONTEXT_KEY_CALL_METHOD_NAME, "testMethod2");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value"));
        assertThat(v.getMessage(), is("{fieldName}はDate型として不正です。"));
    }

    /**
     * ターゲットを設定する場合、チェックされること
     */
    @Test
    public void testWithTarget_OK() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue("20200518");
        bean.setValue2("20200518");
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
            assertThat(v.getMessage(), is("{fieldName}はDate型として不正です。"));
        }
        assertTrue(value2Flag);
    }

    /**
     *  配列をチェックできること（精査エラー発生するのパターン）。
     */
    @Test
    public void testEmptyArray_OK() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue3(new String[] {});
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
    }

    /**
     *  配列をチェックできること（精査エラー発生するのパターン）。
     */
    @Test
    public void testArray_NG() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue3(new String[] {"20200518","45"});

        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}はDate型として不正です。"));
    }

    /**
     * 集合をチェックできること（精査エラー発生しないのパターン）。
     */
    @Test
    public void testEmptyCollection_OK() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue4(new ArrayList<>());
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
    }

    /**
     * 集合をチェックできること（精査エラー発生のパターン）。
     */
    @Test
    public void testCollection_NG() {
        ValueTestBean bean = new ValueTestBean();
        List<String> val = new ArrayList<String>();
        val.add("20200518");
        bean.setValue4(val);
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}はDate型として不正です。"));
    }

    private static class ValueTestBean {
        @ParseDate(datePattern = "yyyy/MM/dd")
        private String value;

        @ParseDate(datePattern = "yyyy/MM/dd", target = "testMethod")
        private String value2;

        @ParseDate(datePattern = "yyyy/MM/dd")
        private String[] value3;

        @ParseDate(datePattern = "yyyy/MM/dd")
        private List<?> value4;

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
    }
}
