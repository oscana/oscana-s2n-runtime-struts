/*
 * 取り込み元
 *    ライブラリ名：     sa-struts
 *    クラス名：         org.seasar.struts.annotation.Minbytelength、org.seasar.struts.annotation.Maxbytelength
 *    ソースリポジトリ： https://github.com/seasarorg/sa-struts/blob/master/src/main/java/org/seasar/struts/annotation/Maxbytelength.java
 *                       https://github.com/seasarorg/sa-struts/blob/master/src/main/java/org/seasar/struts/annotation/Minbytelength.java
 *
 * 上記ファイルを取り込み、修正を加えた。
 *
 * Copyright 2020 TIS Inc.
 *
 * Copyright 2004-2009 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
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
 * 文字列のバイト長が min、max の範囲内かどうかをチェックするためのアノテーション。<br>
 *<br>
 *target属性：<br>
 * このバリデーションを呼び出しているActionメソッド名が、バリデーションのtarget属性に設定されているターゲットリストに含まれてない場合、チェック処理を行わない。<br>
 * target属性が設定されてない場合は、無条件にチェック処理を行う。
 *
 * @author Fumihiko Yamamoto
 * @see org.seasar.struts.annotation.Minbytelength、org.seasar.struts.annotation.Maxbytelength
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = { ByteLength.ByteLengthValidator.class })
public @interface ByteLength {

    /** ターゲット（バリデーションを適用するメソッド名）  */
    String target() default "";

    /** グループ */
    Class<?>[] groups() default {};

    /** メッセージ */
    String message() default "";

    /** payload */
    Class<? extends Payload>[] payload() default {};

    /** 複数指定用のアノテーション */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        ByteLength[] value();
    }

    /** 文字列の最小長。*/
    int min() default 0;

    /** 文字列の最大長。*/
    int max() default 0;

    /**
     * 文字列長のバリデーションを行うため、{@link ConstraintValidator}を実装するクラス。<br>
     */
    class ByteLengthValidator extends ValidateTarget implements ConstraintValidator<ByteLength, Object> {

        /** 最大値 */
        private int max;

        /** 最小値 */
        private int min;

        /** メッセージ */
        private String message;

        /** デフォルトメッセージ(最小値のみ) */
        private static final String MIN_MESSAGE = "{oscana.s2n.validation.ByteLength.min.message}";

        /** デフォルトメッセージ(最大値のみ) */
        private static final String MAX_MESSAGE = "{oscana.s2n.validation.ByteLength.max.message}";

        /** デフォルトメッセージ(固定長) */
        private static final String FIXED_MESSAGE = "{oscana.s2n.validation.ByteLength.fixed.message}";

        /** デフォルトメッセージ(可変長) */
        private static final String MIN_MAX_MESSAGE = "{oscana.s2n.validation.ByteLength.min.max.message}";

        /**
         * ByteLengthValidatorを初期化する。
         *
         * @param constraintAnnotation 対象プロパティに付与されたアノテーション
         */
        @Override
        public void initialize(ByteLength constraintAnnotation) {
            min = constraintAnnotation.min();
            max = constraintAnnotation.max();
            message = constraintAnnotation.message();
            setTargets(constraintAnnotation.target());
        }

        /**
         * 引数の長さが有効な桁数かどうかをチェックする。
         *
         * @param value バリデーション対象の値
         * @param context コンテキスト
         * @return 有効の場合：true、そうではない場合：false
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

            String strValue = null;
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
            final int length = strValue.getBytes().length;

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
         * <p>
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
