/*
 * 取り込み元
 *    ライブラリ名：     hibernate-validator
 *    クラス名：         org.hibernate.validator.constraints.CreditCardNumber
 *    ソースリポジトリ： https://github.com/hibernate/hibernate-validator/blob/master/engine/src/main/java/org/hibernate/validator/constraints/CreditCardNumber.java
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
import java.util.ArrayList;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;

import org.hibernate.validator.constraints.LuhnCheck;
import org.hibernate.validator.internal.util.ModUtil;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/**
 * クレジットカードであるかどうかをチェックするためのアノテーション。
 *
 * target属性：<br>
 * このバリデーションを呼び出しているActionメソッド名が、バリデーションのtarget属性に設定されているターゲットリストに含まれてない場合、チェック処理を行わない。<br>
 * target属性が設定されてない場合は、無条件にチェック処理を行う。
 *
 * @author Ko Ho
 * @see org.hibernate.validator.constraints.CreditCardNumber
 */
@Documented
@Constraint(validatedBy = CreditCardNumber.CreditCardNumberValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface CreditCardNumber {

    /** ターゲット（バリデーションを適用するメソッド名）  */
    String target() default "";

    /** メッセージID*/
    String message() default "{oscana.s2n.validation.CreditCardNumber.message}";

    /** グループ */
    Class<?>[] groups() default {
    };

    /** payload */
    Class<? extends Payload>[] payload() default {
    };

    /**
     * @return the start index (inclusive) for calculating the checksum. If not specified 0 is assumed.
     */
    int startIndex() default 0;

    /**
     * @return the end index (inclusive) for calculating the checksum. If not specified the whole value is considered.
     */
    int endIndex() default Integer.MAX_VALUE;

    @OverridesAttribute(constraint = LuhnCheck.class, name = "ignoreNonDigitCharacters")
    boolean ignoreNonDigitCharacters() default false;

    /**
     * @return The index of the check digit in the input. Per default it is assumed that the check digit is the last
     * digit of the specified range. If set, the digit at the specified index is used. If set
     * the following must hold true:
     * {@code checkDigitIndex > 0 && (checkDigitIndex < startIndex || checkDigitIndex >= endIndex}.
     */
    int checkDigitIndex() default -1;

    /** 複数指定用のアノテーション */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        CreditCardNumber[] value();
    }

    /**
     * クレジットカードであるかどうかをチェックするクラス。
     */
    public class CreditCardNumberValidator extends ValidateTarget implements ConstraintValidator<CreditCardNumber, String> {

        private static final Log log = LoggerFactory.make();

        private static final java.util.regex.Pattern NUMBERS_ONLY_REGEXP = java.util.regex.Pattern.compile( "[^0-9]" );

        private static final int DEC_RADIX = 10;

        /**
         * The start index for the checksum calculation
         */
        private int startIndex;

        /**
         * The end index for the checksum calculation
         */
        private int endIndex;

        /**
         * The index of the checksum digit
         */
        private int checkDigitIndex;

        private boolean ignoreNonDigitCharacters;

        /**
         * CreditCardNumberValidatorを初期化する。
         *
         * @param constraintAnnotation 対象プロパティに付与されたアノテーション
         */
        @Override
        public void initialize(CreditCardNumber constraintAnnotation) {
            this.startIndex = constraintAnnotation.startIndex();
            this.endIndex = constraintAnnotation.endIndex();
            this.checkDigitIndex = constraintAnnotation.checkDigitIndex();
            this.ignoreNonDigitCharacters = constraintAnnotation.ignoreNonDigitCharacters();
            setTargets(constraintAnnotation.target());
        }

        /**
         * クレジットカードであるかどうかをチェックする。
         *
         * @param value バリデーション対象の値
         * @param constraintContext バリデーションコンテキスト
         * @return クレジットカードの場合：true、そうではない場合：false
         */
        @Override
        public boolean isValid(String value, ConstraintValidatorContext constraintContext) {

            /** チェック対象メソッドの判定*/
            if (!isTarget()) {
                return true;
            }

            if ( value == null ) {
                return true;
            }

            String valueAsString = value.toString();
            String digitsAsString;
            char checkDigit;
            try {
                digitsAsString = extractVerificationString( valueAsString );
                checkDigit = extractCheckDigit( valueAsString );
            }
            catch (IndexOutOfBoundsException e) {
                return false;
            }
            digitsAsString = stripNonDigitsIfRequired( digitsAsString );

            java.util.List<Integer> digits;
            try {
                digits = extractDigits( digitsAsString );
            }
            catch (NumberFormatException e) {
                return false;
            }

            return this.isCheckDigitValid( digits, checkDigit );
        }

        /**
         * Validate check digit using Luhn algorithm
         *
         * @param digits The digits over which to calculate the checksum
         * @param checkDigit the check digit
         *
         * @return {@code true} if the luhn check result matches the check digit, {@code false} otherwise
         */
        public boolean isCheckDigitValid(java.util.List<Integer> digits, char checkDigit) {
            int modResult = ModUtil.calculateLuhnMod10Check( digits );

            if ( !Character.isDigit( checkDigit ) ) {
                return false;
            }

            int checkValue = extractDigit( checkDigit );
            return checkValue == modResult;
        }

        /**
         * Returns the numeric {@code int} value of a {@code char}
         *
         * @param value the input {@code char} to be parsed
         *
         * @return the numeric {@code int} value represented by the character.
         * @throws Exception
         */
        protected int extractDigit(char value) throws NumberFormatException {
            if ( Character.isDigit(value)) {
                return Character.digit(value,DEC_RADIX);
            }
            else {
                throw log.getCharacterIsNotADigitException( value );
            }
        }

        /**
         * Parses the {@link String} value as a {@link List} of {@link Integer} objects
         *
         * @param value the input string to be parsed
         *
         * @return List of {@code Integer} objects.
         * @throws NumberFormatException
         */
        private java.util.List<Integer> extractDigits(final String value) throws NumberFormatException {
            java.util.List<Integer> digits = new ArrayList<Integer>( value.length() );
            char[] chars = value.toCharArray();
            for ( char c : chars ) {
                digits.add( extractDigit( c ) );
            }
            return digits;
        }

        private String stripNonDigitsIfRequired(String value) {
            if ( ignoreNonDigitCharacters ) {
                return NUMBERS_ONLY_REGEXP.matcher( value ).replaceAll( "" );
            }
            else {
                return value;
            }
        }

        private char extractCheckDigit(String value) throws IndexOutOfBoundsException {
            // take last character of string to be validated unless the index is given explicitly
            if ( checkDigitIndex == -1 ) {
                if ( endIndex == Integer.MAX_VALUE ) {
                    return value.charAt( value.length() - 1 );
                }
                else {
                    return value.charAt( endIndex );
                }
            }
            else {
                return value.charAt( checkDigitIndex );
            }
        }

        private String extractVerificationString(String value) throws IndexOutOfBoundsException {
            // the string contains the check digit, just return the digits to verify
            if ( endIndex == Integer.MAX_VALUE ) {
                return value.substring( 0, value.length() - 1 );
            }
            else if ( checkDigitIndex == -1 ) {
                return value.substring( startIndex, endIndex );
            }
            else {
                return value.substring( startIndex, endIndex + 1 );
            }
        }
    }

}
