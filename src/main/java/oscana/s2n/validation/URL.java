/*
 * 取り込み元
 *    ライブラリ名：     hibernate-validator
 *    クラス名：         org.hibernate.validator.constraints.URL
 *    ソースリポジトリ： https://github.com/hibernate/hibernate-validator/blob/master/engine/src/main/java/org/hibernate/validator/constraints/URL.java
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
import java.net.MalformedURLException;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

/**
 * URLであるかどうかをチェックするためのアノテーション。
 *
 * target属性：<br>
 * このバリデーションを呼び出しているActionメソッド名が、バリデーションのtarget属性に設定されているターゲットリストに含まれてない場合、チェック処理を行わない。<br>
 * target属性が設定されてない場合は、無条件にチェック処理を行う。
 *
 * @author Ko Ho
 * @see org.hibernate.validator.constraints.URL
 */
@Documented
@Constraint(validatedBy = URL.URLValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
@Pattern(regexp = "")
public @interface URL {

    /** ターゲット（バリデーションを適用するメソッド名）  */
    String target() default "";

    /** メッセージID*/
    String message() default "{oscana.s2n.validation.URL.message}";

    /** グループ */
    Class<?>[] groups() default {};

    /** payload */
    Class<? extends Payload>[] payload() default {};

    String host() default "";

    String protocol() default "";

    int port() default -1;

    /**
     * @return an additional regular expression the annotated URL must match. The default is any string ('.*')
     */
    @OverridesAttribute(constraint = Pattern.class, name = "regexp") String regexp() default ".*";

    /**
     * @return used in combination with {@link #regexp()} in order to specify a regular expression option
     */
    @OverridesAttribute(constraint = Pattern.class, name = "flags") Pattern.Flag[] flags() default { };


    /** 複数指定用のアノテーション */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        URL[] value();
    }

    /**
     * URLであるかどうかをチェックするクラス。
     */
    public class URLValidator extends ValidateTarget implements ConstraintValidator<URL, String> {
        private String protocol;
        private String host;
        private int port;

        /**
         * URLValidator を初期化する。
         *
         * @param constraintAnnotation 対象プロパティに付与されたアノテーション
         */
        @Override
        public void initialize(URL constraintAnnotation) {
            this.protocol = constraintAnnotation.protocol();
            this.host = constraintAnnotation.host();
            this.port = constraintAnnotation.port();
            setTargets(constraintAnnotation.target());
        }

        /**
         * URLであるかどうかをチェックする。
         *
         * @param value バリデーション対象の値
         * @param constraintContext バリデーションコンテキスト
         * @return 最小日付以降の場合：true、そうではない場合：false
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

            java.net.URL url;
            try {
                url = new java.net.URL( value.toString() );
            }
            catch ( MalformedURLException e ) {
                return false;
            }

            if ( protocol != null && protocol.length() > 0 && !url.getProtocol().equals( protocol ) ) {
                return false;
            }

            if ( host != null && host.length() > 0 && !url.getHost().equals( host ) ) {
                return false;
            }

            if ( port != -1 && url.getPort() != port ) {
                return false;
            }

            return true;
        }
    }

}
