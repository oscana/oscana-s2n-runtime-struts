/*
 * 取り込み元
 *    ライブラリ名：     sa-struts
 *    クラス名：         org.seasar.struts.annotation.ByteType
 *    ソースリポジトリ： https://github.com/seasarorg/sa-struts/blob/master/src/main/java/org/seasar/struts/annotation/ByteType.java
 *
 * 上記ファイルを取り込み、修正を加えた。
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
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.Collection;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * バイトの型へ解析できるかどうかをチェックするためのアノテーション。
 *
 * target属性：<br>
 * このバリデーションを呼び出しているActionメソッド名が、バリデーションのtarget属性に設定されているターゲットリストに含まれてない場合、チェック処理を行わない。<br>
 * target属性が設定されてない場合は、無条件にチェック処理を行う。
 *
 * @author Fumihiko Yamamoto
 * @see org.seasar.struts.annotation.ByteType
 */
@Documented
@Constraint(validatedBy = { ParseByte.ParseByteValidator.class })
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
public @interface ParseByte {

    /** ターゲット（バリデーションを適用するメソッド名）  */
    String target() default "";

    /** メッセージID*/
    String message() default "{oscana.s2n.validation.ParseByte.message}";

    /** グループ */
    Class<?>[] groups() default {};

    /** payload */
    Class<? extends Payload>[] payload() default {};

    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    public @interface List {
        ParseByte[] value();
    }

    /**
     * バイトの型へ解析できるかどうかをチェックするクラス。
     */
    class ParseByteValidator extends ValidateTarget implements ConstraintValidator<ParseByte, Object> {

        /**
         * ParseByteValidatorを初期化する。
         *
         * @param constraintAnnotation 対象プロパティに付与されたアノテーション
         */
        @Override
        public void initialize(ParseByte constraintAnnotation) {
            setTargets(constraintAnnotation.target());
        }

        /**
         * バイトの型かどうかをチェックする。
         *
         * @param value バリデーション対象の値
         * @param context バリデーションコンテキスト
         * @return バイトの型であればtrue、それ以外は false
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

            try {
                Byte.parseByte(strValue);
            } catch (NumberFormatException e) {
                return false;
            }

            return true;

        }

    }
}