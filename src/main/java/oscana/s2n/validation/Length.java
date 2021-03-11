/*
 * 取り込み元
 *    ライブラリ名：     nablarch-core-validation-ee
 *    クラス名：         nablarch.core.validation.ee.Length
 *    ソースリポジトリ： https://github.com/nablarch/nablarch-core-validation-ee/blob/master/src/main/java/nablarch/core/validation/ee/Length.java
 *
 * 上記ファイルを取り込み、修正を加えた。
 *
 * Copyright 2020 TIS Inc.
 *
 */
package oscana.s2n.validation;

import static java.lang.annotation.ElementType.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

import nablarch.core.util.StringUtil;


/**
 * 文字列が指定された範囲内の長さであることをチェックするためのアノテーション。<br>
 *<br>
 * NablarchのLengthアノテーションに、target属性を追加の拡張実装である。
 *
 * <br>
 * 入力値がnull又は空文字の場合は、validと判定する。
 *
 * エラー時のメッセージは、以下のルールにより決定される。<br>
 * <br>
 *     ・{@link #message()}が指定されている場合は、その値を使用する。<br>
 *     ・{@link #message()}が未指定で{@link #min()}のみ指定の場合は、{nablarch.core.validation.ee.Length.min.message}<br>
 *     ・{@link #message()}が未指定で{@link #max()}のみ指定の場合は、{nablarch.core.validation.ee.Length.max.message}<br>
 *     ・{@link #message()}が未指定で{@link #max()}と{@link #min()}に指定した値が同じ場合は、{nablarch.core.validation.ee.Length.fixed.message}<br>
 *     ・{@link #message()}が未指定で{@link #min()}と{@link #max()}に指定した値が異なる場合は{nablarch.core.validation.ee.Length.min.max.message}<br>
 * <br>
 *
 * 文字列長の計算はサロゲートペアを考慮して行う。<br>
 * <br>
 * target属性：<br>
 * このバリデーションを呼び出しているActionメソッド名が、バリデーションのtarget属性に設定されているターゲットリストに含まれてない場合、チェック処理を行わない。<br>
 * target属性が設定されてない場合は、無条件にチェック処理を行う。
 *
 * @author Rai Shuu
 * @see nablarch.core.validation.ee.Length
 */
@Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = {Length.LengthValidator.class})
public @interface Length {

    /** ターゲット（バリデーションを適用するメソッド名）  */
    String target() default "";

    /** グループ */
    Class<?>[] groups() default {};

    /** メッセージ */
    String message() default "";

    /** payload */
    Class<? extends Payload>[] payload() default {};

    /** 複数指定用のアノテーション */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        Length[] value();
    }

    /** 文字列の最小長。*/
    int min() default 0;

    /** 文字列の最大長。*/
    int max() default 0;

    /**
     * 文字列長のバリデーションを行うため、{@link ConstraintValidator}を実装するクラス。<br>
     */
    class LengthValidator extends ValidateTarget implements ConstraintValidator<Length, Object> {

        /** 最大値 */
        private int max;

        /** 最小値 */
        private int min;

        /** メッセージ */
        private String message;

        /** デフォルトメッセージ(最小値のみ) */
        private static final String MIN_MESSAGE = "{oscana.s2n.validation.Length.min.message}";

        /** デフォルトメッセージ(最大値のみ) */
        private static final String MAX_MESSAGE = "{oscana.s2n.validation.Length.max.message}";

        /** デフォルトメッセージ(固定長) */
        private static final String FIXED_MESSAGE = "{oscana.s2n.validation.Length.fixed.message}";

        /** デフォルトメッセージ(可変長) */
        private static final String MIN_MAX_MESSAGE = "{oscana.s2n.validation.Length.min.max.message}";

        /**
         * LengthValidatorを初期化する。
         *
         * @param constraintAnnotation 対象プロパティに付与されたアノテーション
         */
        @Override
        public void initialize(Length constraintAnnotation) {
            min = constraintAnnotation.min();
            max = constraintAnnotation.max();
            message = constraintAnnotation.message();
            setTargets(constraintAnnotation.target());
        }

        /**
         * 対象文字列の長さが有効な桁数かどうかをチェックする。
         *
         * @param value バリデーション対象の値
         * @param context バリデーションコンテキスト
         * @return 有効であればtrue、そうではない場合はfalse
         */
        @SuppressWarnings("rawtypes")
        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {

            /** チェック対象メソッドの判定*/
            if (!isTarget()) {
                return true;
            }

            if (value == null) {
                return true;
            }

            CharSequence strValue = null;
            if (value instanceof String[]) {
                strValue = ((String[]) value).length > 0 ? value.toString() : "";

            } else if (value instanceof Collection) {
                strValue = ((Collection) value).isEmpty() ? "" : value.toString();

            } else {
                strValue = value.toString();
            }

            if (strValue.length() == 0) {
                return true;
            }

            final int length = Character.codePointCount(strValue, 0, strValue.length());

            if (isValid(length)) {
                return true;
            }

            if (StringUtil.isNullOrEmpty(message)) {
                // メッセージが指定されていない場合は、デフォルトのメッセージを構築する。
                context.disableDefaultConstraintViolation();
                context.buildConstraintViolationWithTemplate(getDefaultMessage())
                       .addConstraintViolation();
            }
            return false;
        }

        /**
         * デフォルトのメッセージ定義を返却する。
         *
         * @return デフォルトのメッセージ定義
         */
        private String getDefaultMessage() {
            final String defaultMessage;
            if (min == 0) {
                defaultMessage = MAX_MESSAGE;
            } else if (max == 0) {
                defaultMessage = MIN_MESSAGE;
            } else if (min == max) {
                defaultMessage = FIXED_MESSAGE;
            } else {
                defaultMessage = MIN_MAX_MESSAGE;
            }
            return defaultMessage;
        }

        /**
         * 値の長さが有効な桁数かどうかをチェックする。
         *
         * @param length 値の桁数。
         * @return 有効な桁数の場合は{@code true}
         */
        private boolean isValid(final int length) {
            return min <= length && length <= max();
        }

        /**
         * 許容する最大の文字数を返却する。
         * <br>
         * {@link #max}が0の場合は{@link Integer#MAX_VALUE}を返却する。
         * {@link #max}が0以外の場合は、その値を返却する。
         *
         * @return 最大の文字数。
         */
        private int max() {
            return max == 0 ? Integer.MAX_VALUE : max;
        }
    }
}

