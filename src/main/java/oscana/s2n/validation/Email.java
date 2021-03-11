/*
 * 取り込み元
 *    ライブラリ名：     hibernate-validator
 *    クラス名：         org.hibernate.validator.constraints.Email
 *    ソースリポジトリ： https://github.com/hibernate/hibernate-validator/blob/master/engine/src/main/java/org/hibernate/validator/constraints/Email.java
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
import java.net.IDN;
import java.util.regex.Matcher;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

/**
 * メールであるかどうかをチェックするためのアノテーション。
 *
 * target属性：<br>
 * このバリデーションを呼び出しているActionメソッド名が、バリデーションのtarget属性に設定されているターゲットリストに含まれてない場合、チェック処理を行わない。<br>
 * target属性が設定されてない場合は、無条件にチェック処理を行う。
 *
 * @author Ko Ho
 * @see org.hibernate.validator.constraints.Email
 */
@Documented
@Constraint(validatedBy = Email.EmailValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
@Pattern(regexp = "")
public @interface Email {

    /** ターゲット（バリデーションを適用するメソッド名）  */
    String target() default "";

    /** メッセージID*/
    String message() default "{oscana.s2n.validation.Email.message}";

    /** グループ */
    Class<?>[] groups() default {};

    /** payload */
    Class<? extends Payload>[] payload() default {};

    /**
     * @return an additional regular expression the annotated string must match. The default is any string ('.*')
     */
    @OverridesAttribute(constraint = Pattern.class, name = "regexp") String regexp() default ".*";

    /**
     * @return used in combination with {@link #regexp()} in order to specify a regular expression option
     */
    @OverridesAttribute(constraint = Pattern.class, name = "flags") Pattern.Flag[] flags() default { };

    /** 複数指定用のアノテーション */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Email[] value();
    }

    /**
     * メールであるかどうかをチェックするクラス。
     */
    public class EmailValidator extends ValidateTarget implements ConstraintValidator<Email, String> {
        private static String ATOM = "[a-z0-9!#$%&'*+/=?^_`{|}~-]";
        private static String DOMAIN = ATOM + "+(\\." + ATOM + "+)*";
        private static String IP_DOMAIN = "\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\]";

        /**
         * Regular expression for the local part of an email address (everything before '@')
         */
        private final java.util.regex.Pattern localPattern = java.util.regex.Pattern.compile(
                ATOM + "+(\\." + ATOM + "+)*", java.util.regex.Pattern.CASE_INSENSITIVE
        );

        /**
         * Regular expression for the domain part of an email address (everything after '@')
         */
        private final java.util.regex.Pattern domainPattern = java.util.regex.Pattern.compile(
                DOMAIN + "|" + IP_DOMAIN, java.util.regex.Pattern.CASE_INSENSITIVE
        );

        /**
         * EmailValidator を初期化する。
         *
         * @param constraintAnnotation 対象プロパティに付与されたアノテーション
         */
        @Override
        public void initialize(Email constraintAnnotation) {

            setTargets(constraintAnnotation.target());
        }

        /**
         * メールであるかどうかをチェックする。
         *
         * @param value バリデーション対象の値
         * @param constraintContext バリデーションコンテキスト
         * @return メールの場合：true、そうではない場合：false
         */
        @Override
        public boolean isValid(String value, ConstraintValidatorContext constraintContext) {

            /** チェック対象メソッドの判定*/
            if (!isTarget()) {
                return true;
            }

            if ( value == null || value.length() == 0 ) {
                return true;
            }

            // split email at '@' and consider local and domain part separately;
            // note a split limit of 3 is used as it causes all characters following to an (illegal) second @ character to
            // be put into a separate array element, avoiding the regex application in this case since the resulting array
            // has more than 2 elements
            String[] emailParts = value.toString().split( "@", 3 );
            if ( emailParts.length != 2 ) {
                return false;
            }

            // if we have a trailing dot in local or domain part we have an invalid email address.
            // the regular expression match would take care of this, but IDN.toASCII drops trailing the trailing '.'
            // (imo a bug in the implementation)
            if ( emailParts[0].endsWith( "." ) || emailParts[1].endsWith( "." ) ) {
                return false;
            }

            if ( !matchPart( emailParts[0], localPattern ) ) {
                return false;
            }

            return matchPart( emailParts[1], domainPattern );
        }

        private boolean matchPart(String part, java.util.regex.Pattern pattern) {
            try {
                part = IDN.toASCII( part );
            }
            catch ( IllegalArgumentException e ) {
                // occurs when the label is too long (>63, even though it should probably be 64 - see http://www.rfc-editor.org/errata_search.php?rfc=3696,
                // practically that should not be a problem)
                return false;
            }
            Matcher matcher = pattern.matcher( part );
            return matcher.matches();
        }
    }

}
