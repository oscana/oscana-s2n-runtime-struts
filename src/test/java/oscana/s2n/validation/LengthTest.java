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
 * {@link Length}のテスト。
 */
public class LengthTest {
    private static Validator validator;

    @Before
    public void setUp() {
        validator = ValidatorUtil.getValidator();
    }

    /**
     * 正常な値(指定した最大値)を設定する場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsValidMax() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue1("1234567890ab");
        bean.setValue2("12345");
        bean.setValue3("12");
        bean.setValue4("12345");
        bean.setValue5("123456789011");
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 異常な値(指定した最大値より大きい)を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValidMax() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue1("1234567890abc");
        bean.setValue3("123");
        bean.setValue4("123456");
        bean.setValue5("1234567890111");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(4));
        Iterator<ConstraintViolation<ValueTestBean>> ite = violations.iterator();
        while (ite.hasNext()) {
            ConstraintViolation<ValueTestBean> v = ite.next();
            if (v.getPropertyPath().toString().equals("value1")) {
                assertThat(v.getMessage(), is("8文字から12文字までの値を入力してください。"));
            }
            if (v.getPropertyPath().toString().equals("value3")) {
                assertThat(v.getMessage(), is("{fieldName}は2文字の値を入力してください。"));
            }
            if (v.getPropertyPath().toString().equals("value4")) {
                assertThat(v.getMessage(), is("{fieldName}は5文字以下の値を入力してください。"));
            }
            if (v.getPropertyPath().toString().equals("value5")) {
                assertThat(v.getMessage(), is("{fieldName}は8文字以上12文字以下の値を入力してください。"));
            }
        }
    }

    /**
     * 正常な値(指定した最小値)を設定する場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsValidMin() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue1("12345678");
        bean.setValue2("123");
        bean.setValue3("12");
        bean.setValue4("312");
        bean.setValue5("12345678");
        Set<ConstraintViolation<Object>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 異常な値(指定した最小値より小さい)を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValidMin() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue1("1234567");
        bean.setValue2("12");
        bean.setValue3("1");
        bean.setValue5("1234567");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(4));

        Iterator<ConstraintViolation<ValueTestBean>> ite = violations.iterator();
        while (ite.hasNext()) {
            ConstraintViolation<ValueTestBean> v = ite.next();
            if (v.getPropertyPath().toString().equals("value1")) {
                assertThat(v.getMessage(), is("8文字から12文字までの値を入力してください。"));
            }
            if (v.getPropertyPath().toString().equals("value2")) {
                assertThat(v.getMessage(), is("{fieldName}は3文字以上の値を入力してください。"));
            }
            if (v.getPropertyPath().toString().equals("value3")) {
                assertThat(v.getMessage(), is("{fieldName}は2文字の値を入力してください。"));
            }
            if (v.getPropertyPath().toString().equals("value5")) {
                assertThat(v.getMessage(), is("{fieldName}は8文字以上12文字以下の値を入力してください。"));
            }
        }
    }

    /**
     * nullを設定する場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsValidNull() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue1(null);
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 空を設定する場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsValidKara() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue1("");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 違うターゲットを設定する場合、チェックされないこと
     */
    @Test
    public void testWithTarget_NG() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue1("123");
        bean.setValue6("123");
        ThreadContext.setObject(S2NConstants.THREAD_CONTEXT_KEY_CALL_METHOD_NAME, "testMethod2");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value1"));
        assertThat(v.getMessage(), is("8文字から12文字までの値を入力してください。"));
    }

    /**
     * ターゲットを設定する場合、チェックされること
     */
    @Test
    public void testWithTarget_OK() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue1("123");
        bean.setValue6("123");
        ThreadContext.setObject(S2NConstants.THREAD_CONTEXT_KEY_CALL_METHOD_NAME, "testMethod");
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(2));
        Iterator<ConstraintViolation<ValueTestBean>> iterator = violations.iterator();
        boolean value2Flag = false;
        while (iterator.hasNext()) {
            ConstraintViolation<ValueTestBean> v = iterator.next();
            if (v.getPropertyPath().toString().equals("value6")) {
                value2Flag = true;
            }
            assertThat(v.getMessage(), is("8文字から12文字までの値を入力してください。"));
        }
        assertTrue(value2Flag);
    }

    /**
     *  配列をチェックできること（精査エラー発生するのパターン）。
     */
    @Test
    public void testEmptyArray_OK() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue7(new String[] {});
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     *  配列をチェックできること（精査エラー発生するのパターン）。
     */
    @Test
    public void testArray_NG() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue7(new String[] {"12311111","4111115"});

        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}は5文字以下の値を入力してください。"));
    }

    /**
     * 集合をチェックできること（精査エラー発生しないのパターン）。
     */
    @Test
    public void testEmptyCollection_OK() {
        ValueTestBean bean = new ValueTestBean();
        bean.setValue8(new ArrayList<>());
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 集合をチェックできること（精査エラー発生のパターン）。
     */
    @Test
    public void testCollection_NG() {
        ValueTestBean bean = new ValueTestBean();
        List<String> val = new ArrayList<String>();
        val.add("1234567");
        bean.setValue8(val);
        Set<ConstraintViolation<ValueTestBean>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}は5文字以下の値を入力してください。"));
    }

    private static class ValueTestBean {
        @Length(max = 12, min = 8, message = "8文字から12文字までの値を入力してください。")
        private String value1;

        @Length(min = 3, max = 0, message = "")
        private String value2;

        @Length(max = 2, min = 2, message = "")
        private String value3;

        @Length(max = 5, min = 0, message = "")
        private String value4;

        @Length(max = 12, min = 8, message = "")
        private String value5;

        @Length(max = 12, min = 8, message = "8文字から12文字までの値を入力してください。", target = "testMethod")
        private String value6;

        @Length(max = 5, min = 0, message = "")
        private String[] value7;

        @Length(max = 5, min = 0, message = "")
        private List<?> value8;

        public void setValue1(String value1) {
            this.value1 = value1;
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

        public void setValue5(String value5) {
            this.value5 = value5;
        }

        public void setValue6(String value6) {
            this.value6 = value6;
        }

        public void setValue7(String[] value7) {
            this.value7 = value7;
        }

        public void setValue8(List<?> value8) {
            this.value8 = value8;
        }

    }
}
