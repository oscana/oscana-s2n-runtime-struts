package oscana.s2n.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.math.BigDecimal;
import java.util.Collection;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import oscana.s2n.validation.DecimalRange.List;

/**
 * 数値が min、max の範囲内かどうか及び小数点以下桁数が min、max の大きい方の最大桁数以内かどうかをチェックするためのアノテーション。
 *
 * target属性：<br>
 * このバリデーションを呼び出しているActionメソッド名が、バリデーションのtarget属性に設定されているターゲットリストに含まれてない場合、チェック処理を行わない。<br>
 * target属性が設定されてない場合は、無条件にチェック処理を行う。
 *
 * @author Fumihiko Yamamoto
 */
@Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE, TYPE_USE })
@Retention(RUNTIME)
@Constraint(validatedBy = DecimalRange.DecimalRangeValidator.class)
@Documented
@Repeatable(List.class)
public @interface DecimalRange {

    /** ターゲット（バリデーションを適用するメソッド名）  */
    String target() default "";

    /** 小数の最小値。*/
    String min();

    /** 小数の最大値。*/
    String max();

    /** メッセージID*/
    String message() default "{oscana.s2n.validation.DecimalRange.message}";

    /** グループ */
    Class<?>[] groups() default {
    };

    /** payload */
    Class<? extends Payload>[] payload() default {
    };

    /** 複数指定用のアノテーション */
    @Target({ FIELD, METHOD, PARAMETER, ANNOTATION_TYPE })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        DecimalRange[] value();
    }

    /**
     * 数値が min、max の範囲内かどうか及び小数点以下桁数が min、max の大きい方の最大桁数以内かどうかをチェックするクラス。
     */
    public class DecimalRangeValidator extends ValidateTarget implements ConstraintValidator<DecimalRange, Object> {

        /** 最小値 */
        private String min;

        /** 最大値 */
        private String max;

        /**
         * DecimalRangeValidatorを初期化する。
         *
         * @param constraintAnnotation 対象プロパティに付与されたアノテーション
         */
        @Override
        public void initialize(DecimalRange constraintAnnotation) {

            min = constraintAnnotation.min();
            max = constraintAnnotation.max();
            setTargets(constraintAnnotation.target());
        }

        /**
         * 数値が min、max の範囲内かどうか及び小数点以下桁数が min、max の大きい方の最大桁数以内かどうかををチェックする。
         *
         * @param value バリデーション対象の値
         * @param constraintContext バリデーションコンテキスト
         * @return 範囲内であればtrue、そうではない場合：false
         */
        @Override
        public boolean isValid(Object value, ConstraintValidatorContext constraintContext) {
            /** チェック対象メソッドの判定*/
            if (!isTarget()) {
                return true;
            }

            if (value == null) {
                return true;
            }

            return isBetweenDecimal(value, min, max);
        }

        /**
         * 数値が minx、max の範囲内かどうか及び<br>
         * 小数点以下桁数が min、max の大きい方の最大桁数以内かどうかをチェックする。
         *
         * @param value 対象文字列
         * @param min 最小数値範囲 例：0.00.
         * @param max 最大少数範囲 例：99.99.
         * @return 範囲内であればtrue、それ以外：false
         */
        @SuppressWarnings("rawtypes")
        private boolean isBetweenDecimal(Object value, String min, String max) {
            String strValue = null;
            if (value instanceof String[]) {
                strValue = ((String[]) value).length > 0 ? value.toString() : "";

            } else if (value instanceof Collection) {
                strValue = ((Collection) value).isEmpty() ? "" : value.toString();

            } else {
                strValue = value.toString();
            }

            if (isEmpty(strValue)) {
                return true;
            }
            if (!isDecimal(strValue)) {
                return false;
            }

            int maxScale = 0;
            int scaleOfMin = 0;
            int scaleOfMax = 0;
            int temp = 0;

            // 小数部の最大桁数を取得
            temp = min.indexOf(".");
            if (temp >= 0) {
                scaleOfMin = min.length() - (temp + 1);
            }
            temp = max.indexOf(".");
            if (temp >= 0) {
                scaleOfMax = max.length() - (temp + 1);
            }
            if (scaleOfMin > scaleOfMax) {
                maxScale = scaleOfMin;
            } else {
                maxScale = scaleOfMax;
            }

            // 少数点チェック
            temp = strValue.indexOf(".");
            if (temp >= 0 && (strValue.length() - (temp + 1) > maxScale)) {
                return false;
            }

            // 範囲チェック
            BigDecimal val = new BigDecimal(strValue);
            if (val.compareTo(new BigDecimal(min)) == -1) {
                return false;
            }
            if (val.compareTo(new BigDecimal(max)) == 1) {
                return false;
            }
            return true;
        }

        /**
         * 対象文字列が半角数字（小数、負数を含む）のみであることをチェックする。<br>
         * <br>
         * 01、0.1、-01 等の表記も OK。 null、空文字のみの場合は OK。
         *
         * @param value 対象文字列
         * @return 数値である場合は true、それ以外は false。
         */
        private boolean isDecimal(String value) {
            if (isEmpty(value)) {
                return true;
            }

            if (!value.matches("[0-9.-]*")) {
                return false;
            }

            // 小数点の個数を精査
            int j = 0;
            for (int i = 0; i < value.length(); i++) {
                char c = value.charAt(i);
                if ((c == '.') && (++j > 1)) {
                    return false;
                }
            }

            // 先頭のマイナス符号を取り除く
            if (value.startsWith("-")) {
                value = value.substring(1, value.length());
            }
            if (value.length() == 0) {
                return false;
            }
            return value.matches("[0-9.]*");

        }

        /**
         * 文字列が<code>null</code>または空文字列なら<code>true</code>を返却する。
         *
         * @param text
         *            文字列
         * @return 文字列が<code>null</code>または空文字列なら<code>true</code>
         */
        private static final boolean isEmpty(final String text) {
            return text == null || text.length() == 0;
        }
    }

}
