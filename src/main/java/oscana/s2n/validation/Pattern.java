/*
 * 取り込み元
 *    ライブラリ名：     hibernate-validator
 *    クラス名：         org.hibernate.validator.internal.constraintvalidators.bv.PatternValidator
 *    ソースリポジトリ： https://github.com/hibernate/hibernate-validator/blob/master/engine/src/main/java/org/hibernate/validator/internal/constraintvalidators/bv/PatternValidator.java
 *
 * 上記ファイルを取り込み、修正を加えた。
 *
 * Copyright 2020 TIS Inc.
 *
 * Hibernate Validator, declare and validate application constraints
 *
 * License: Apache License, Version 2.0
 * See the license.txt file in the root directory or <http://www.apache.org/licenses/LICENSE-2.0>.
 */
package oscana.s2n.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.regex.Matcher;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * 正規表現に一致するかどうかをチェックするためのアノテーション。<br>
 * <br>
 *
 * BeanValidationの標準アノテーションに、target属性の拡張機能を追加する。<br>
 *
 * target属性：<br>
 * このバリデーションを呼び出しているActionメソッド名が、バリデーションのtarget属性に設定されているターゲットリストに含まれてない場合、チェック処理を行わない。<br>
 * target属性が設定されてない場合は、無条件にチェック処理を行う。
 *
 * @author Rai Shuu
 * @see org.hibernate.validator.internal.constraintvalidators.bv.PatternValidator
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {Pattern.PatternValidator.class})
public @interface Pattern {

    /** ターゲット（バリデーションを適用するメソッド名） */
    String target() default "";

    /** 正規表現 */
    String regexp();

    /** 正規表現を解決するときに考慮される Flag の配列 */
    Flag[] flags() default { };

    /** エラーメッセージテンプレート */
    String message() default "{oscana.s2n.validation.Pattern.message}";

    /** 制約が属するグループ */
    Class<?>[] groups() default { };

    /** 制約に関連付けられたペイロード */
    Class<? extends Payload>[] payload() default { };

    /** 可能な正規表現フラグ */
    public static enum Flag {

        /**
         * Unix ラインモードを有効にする
         *
         * @see java.util.regex.Pattern#UNIX_LINES
         */
        UNIX_LINES( java.util.regex.Pattern.UNIX_LINES ),

        /**
         * 大文字と小文字を区別しない一致を有効にする
         *
         * @see java.util.regex.Pattern#CASE_INSENSITIVE
         */
        CASE_INSENSITIVE( java.util.regex.Pattern.CASE_INSENSITIVE ),

        /**
         * パターン内の空白とコメントを許可する
         *
         * @see java.util.regex.Pattern#COMMENTS
         */
        COMMENTS( java.util.regex.Pattern.COMMENTS ),

        /**
         * 複数行モードを有効にする
         *
         * @see java.util.regex.Pattern#MULTILINE
         */
        MULTILINE( java.util.regex.Pattern.MULTILINE ),

        /**
         * dotall モードを有効にする
         *
         * @see java.util.regex.Pattern#DOTALL
         */
        DOTALL( java.util.regex.Pattern.DOTALL ),

        /**
         * Unicode 対応の大文字と小文字の変換を有効にする
         *
         * @see java.util.regex.Pattern#UNICODE_CASE
         */
        UNICODE_CASE( java.util.regex.Pattern.UNICODE_CASE ),

        /**
         * 正規等価を有効ににする
         *
         * @see java.util.regex.Pattern#CANON_EQ
         */
        CANON_EQ( java.util.regex.Pattern.CANON_EQ );

        //JDK flag value
        private final int value;

        private Flag(int value) {
            this.value = value;
        }

        /**
         * @return {@link java.util.regex.Pattern} で定義されているフラグ値
         */
        public int getValue() {
            return value;
        }
    }

    /** 複数指定用のアノテーション */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {

        Pattern[] value();
    }

    /**
     * 正規表現に一致するかどうかをチェックするクラス。
     *
     */
    class PatternValidator extends ValidateTarget implements ConstraintValidator<Pattern, CharSequence> {

        private java.util.regex.Pattern pattern;

        /**
         * PatternValidator を初期化する。
         *
         * @param parameters 対象プロパティに付与されたパラメータ
         */
        public void initialize(Pattern parameters) {
            setTargets(parameters.target());

            Pattern.Flag[] flags = parameters.flags();
            int intFlag = 0;
            for ( Pattern.Flag flag : flags ) {
                intFlag = intFlag | flag.getValue();
            }

            pattern = java.util.regex.Pattern.compile( parameters.regexp(), intFlag );

        }

        /**
         * 正規表現に一致するかどうかをチェックする。<br>
         * <br>
         *  ・チェックターゲット対象外の場合、trueを返却する。<br>
         *  ・チェックする値はNullの場合、trueを返却する。<br>
         *  ・上記以外の場合、正規表現に一致するかどうかチェックする、一致する場合、trueを返却する、一致しない場合、falseを返却する。<br>
         *
         *
         * @param value バリデーション対象の値
         * @param constraintValidatorContext バリデーションコンテキスト
         * @return チェック結果
         */
        public boolean isValid(CharSequence value, ConstraintValidatorContext constraintValidatorContext) {
            /** チェック対象メソッドの判定*/
            if (!isTarget()) {
                return true;
            }

            if ( value == null ) {
                return true;
            }
            Matcher m = pattern.matcher( value );
            return m.matches();
        }
    }
}