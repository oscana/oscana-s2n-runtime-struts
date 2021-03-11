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
 * {@link ByteLength}のテスト。
 */
public class ByteLengthTest {

    private static Validator validator;

    @Before
    public void setUp() {
        validator = ValidatorUtil.getValidator();
    }

    /**
     * 正常な値（バイト数が8から12バイトの間）場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsValid() {
        ValueTestBean1 bean = new ValueTestBean1("stringValue");
        Set<ConstraintViolation<ValueTestBean1>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 異常な値（5バイト）を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValid_pattern1() {
        ValueTestBean1 bean = new ValueTestBean1("value");
        Set<ConstraintViolation<ValueTestBean1>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean1> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value"));
        assertThat(v.getMessage(), is("{fieldName}は8バイト以上12バイト以下の値を入力してください。"));
    }

    /**
     * 異常な値（5バイト）を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValid_pattern2() {
        ValueTestBean2 bean = new ValueTestBean2("value");
        Set<ConstraintViolation<ValueTestBean2>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean2> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value"));
        assertThat(v.getMessage(), is("{fieldName}は8バイトの値を入力してください。"));
    }

    /**
     * 異常な値（11バイト）を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValid_pattern3() {
        ValueTestBean3 bean = new ValueTestBean3("stringValue");
        Set<ConstraintViolation<ValueTestBean3>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean3> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value"));
        assertThat(v.getMessage(), is("{fieldName}は8バイト以下の値を入力してください。"));
    }

    /**
     * 異常な値（1バイト）を設定する場合、精査エラーが発生すること
     */
    @Test
    public void testIsNotValid_pattern4() {
        ValueTestBean4 bean = new ValueTestBean4("s");
        Set<ConstraintViolation<ValueTestBean4>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean4> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value"));
        assertThat(v.getMessage(), is("{fieldName}は2バイト以上の値を入力してください。"));
    }

    /**
     * 正常な値（バイト数が3）場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsNotValid_pattern5() {
        ValueTestBean4 bean = new ValueTestBean4("str");
        Set<ConstraintViolation<ValueTestBean4>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * エラーメッセージを定義した上、異常な値（1バイト）を設定する場合、精査エラーが発生すること</br>
     */
    @Test
    public void testIsNotValid_pattern6() {
        ValueTestBean5 bean = new ValueTestBean5("r");
        Set<ConstraintViolation<ValueTestBean5>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean5> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value"));
        assertThat(v.getMessage(), is("ここでメッセージを定義することが可能です。"));
    }

    /**
     * 空を設定する場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsValidEmpty() {
        ValueTestBean1 bean = new ValueTestBean1("");
        Set<ConstraintViolation<ValueTestBean1>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * nullを設定する場合、精査エラーが発生しないこと
     */
    @Test
    public void testIsValidNull() {
        ValueTestBean1 bean = new ValueTestBean1(null);
        Set<ConstraintViolation<ValueTestBean1>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * 違うターゲットを設定する場合、チェックされないこと
     */
    @Test
    public void testWithTarget_NG() {
        ValueTestBean1 bean = new ValueTestBean1("value");
        bean.setValue2("value");
        ThreadContext.setObject(S2NConstants.THREAD_CONTEXT_KEY_CALL_METHOD_NAME, "testMethod2");
        Set<ConstraintViolation<ValueTestBean1>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean1> v = violations.iterator().next();
        assertThat(v.getPropertyPath().toString(), is("value"));
        assertThat(v.getMessage(), is("{fieldName}は8バイト以上12バイト以下の値を入力してください。"));
    }

    /**
     * ターゲットを設定する場合、チェックされること
     */
    @Test
    public void testWithTarget_OK() {
        ValueTestBean1 bean = new ValueTestBean1("value");
        bean.setValue2("value");
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
            assertThat(v.getMessage(), is("{fieldName}は8バイト以上12バイト以下の値を入力してください。"));
        }
        assertTrue(value2Flag);
    }

    /**
     *  ByteLengthアノテーションで配列をチェックできること（精査エラー発生しないのパターン）。
     */
    @Test
    public void testEmptyArray_OK() {
        ValueTestBean1 bean = new ValueTestBean1("stringValue");
        bean.setValue3(new String[] {});
        Set<ConstraintViolation<ValueTestBean1>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     *  ByteLengthアノテーションで配列をチェックできること（精査エラー発生するのパターン）。
     */
    @Test
    public void testArray_NG() {
        ValueTestBean1 bean = new ValueTestBean1("stringValue");
        bean.setValue3(new String[] {"123","45"});
        Set<ConstraintViolation<ValueTestBean1>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean1> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}は4バイト以下の値を入力してください。"));
    }

    /**
     * ByteLengthアノテーションで集合をチェックできること（精査エラー発生しないのパターン）。
     */
    @Test
    public void testEmptyCollection_OK() {
        ValueTestBean1 bean = new ValueTestBean1("stringValue");
        bean.setValue4(new ArrayList<>());
        Set<ConstraintViolation<ValueTestBean1>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * ByteLengthアノテーションで集合をチェックできること（精査エラー発生のパターン）。
     */
    @Test
    public void testCollection_NG() {
        ValueTestBean1 bean = new ValueTestBean1("stringValue");
        List<String> val = new ArrayList<String>();
        val.add("1");
        bean.setValue4(val);
        Set<ConstraintViolation<ValueTestBean1>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean1> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}は2バイト以下の値を入力してください。"));
    }

    /**
     * intをチェックする場合、精査エラーが発生しないこと。
     */
    @Test
    public void testInt_OK() {
        ValueTestBean1 bean = new ValueTestBean1("stringValue");
        bean.setValue5(12);
        Set<ConstraintViolation<ValueTestBean1>> violations = validator.validate(bean);
        assertThat(violations.size(), is(0));
    }

    /**
     * intをマイナス数字に設定、半角数字charsetでチェックする場合、精査エラーが発生すること。
     */
    @Test
    public void testInt_NG() {
        ValueTestBean1 bean = new ValueTestBean1("stringValue");
        bean.setValue5(-12);
        Set<ConstraintViolation<ValueTestBean1>> violations = validator.validate(bean);
        assertThat(violations.size(), is(1));
        ConstraintViolation<ValueTestBean1> v = violations.iterator().next();
        assertThat(v.getMessage(), is("{fieldName}は2バイト以下の値を入力してください。"));
    }


    private static class ValueTestBean1 {
        @ByteLength(max = 12, min = 8, message = "")
        private String value;

        @ByteLength(max = 12, min = 8, message = "", target = "testMethod")
        private String value2;

        @ByteLength(max = 4)
        private String[] value3;

        @ByteLength(max = 2)
        private List<?> value4;

        @ByteLength(max = 2)
        private int value5;

        ValueTestBean1(String value) {
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

        public void setValue5(Integer value5) {
            this.value5 = value5;
        }
    }

    private static class ValueTestBean2 {
        @ByteLength(max = 8, min = 8, message = "")
        private String value;

        ValueTestBean2(String value) {
            this.value = value;
        }
    }

    private static class ValueTestBean3 {
        @ByteLength(max = 8, min = 0, message = "")
        private String value;

        ValueTestBean3(String value) {
            this.value = value;
        }
    }

    private static class ValueTestBean4 {
        @ByteLength(max = 0, min = 2, message = "")
        private String value;

        ValueTestBean4(String value) {
            this.value = value;
        }
    }

    private static class ValueTestBean5 {
        @ByteLength(max = 0, min = 2, message = "ここでメッセージを定義することが可能です。")
        private String value;

        ValueTestBean5(String value) {
            this.value = value;
        }
    }

}
