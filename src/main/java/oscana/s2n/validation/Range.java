/*
 * 取り込み元
 *    ライブラリ名：     hibernate-validator
 *    クラス名：         org.hibernate.validator.constraints.Range
 *    ソースリポジトリ： https://github.com/hibernate/hibernate-validator/blob/master/engine/src/main/java/org/hibernate/validator/constraints/Range.java
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
import java.math.BigDecimal;
import java.math.BigInteger;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.OverridesAttribute;
import javax.validation.Payload;
import javax.validation.ReportAsSingleViolation;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

/**
 * 指定範囲内であるかどうかをチェックするためのアノテーション。
 *
 * target属性：<br>
 * このバリデーションを呼び出しているActionメソッド名が、バリデーションのtarget属性に設定されているターゲットリストに含まれてない場合、チェック処理を行わない。<br>
 * target属性が設定されてない場合は、無条件にチェック処理を行う。
 *
 * @author Ko Ho
 * @see org.hibernate.validator.constraints.Range
 */
@Documented
@Constraint(validatedBy = Range.RangeValidator.class)
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@ReportAsSingleViolation
public @interface Range {

    /** ターゲット（バリデーションを適用するメソッド名）  */
    String target() default "";

    /** メッセージID*/
    String message() default "{oscana.s2n.validation.Range.message}";

    /** グループ */
    Class<?>[] groups() default {};

    /** payload */
    Class<? extends Payload>[] payload() default {};

    @OverridesAttribute(constraint = Min.class, name = "value")
    long min() default 0;

    @OverridesAttribute(constraint = Max.class, name = "value")
    long max() default Long.MAX_VALUE;

    /** 複数指定用のアノテーション */
    @Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Range[] value();
    }

    /**
     * 指定範囲内であるかどうかをチェックするクラス。
     */
    public class RangeValidator extends ValidateTarget implements ConstraintValidator<Range, Object> {
        private long minValue;
        private long maxValue;

        /**
         * RangeValidator を初期化する。
         *
         * @param constraintAnnotation 対象プロパティに付与されたアノテーション
         */
        @Override
        public void initialize(Range constraintAnnotation) {
            this.minValue = constraintAnnotation.min();
            this.maxValue = constraintAnnotation.max();
            setTargets(constraintAnnotation.target());
        }

        /**
         * 指定範囲内であるかどうかをチェックする。<br>
         *
         * @param value バリデーション対象の値
         * @param context バリデーションコンテキスト
         * @return チェック結果
         */
        @Override
        public boolean isValid(Object value, ConstraintValidatorContext context) {
            /** チェック対象メソッドの判定*/
            if (!isTarget()) {
                return true;
            }
            // null values are valid
            if (value == null) {
                return true;
            }

            return isMinNumberValid(value, context) && isMaxNumberValid(value, context);
        }

        public boolean isMinNumberValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
            
            if (value instanceof String && ((String) value).matches("-?\\d+(\\.\\d+)?")) {
                long longValue = Long.parseLong((String)value);
                return longValue >= minValue;
            }
            //handling of NaN, positive infinity and negative infinity
            else if ( value instanceof Double ) {
                if ((Double) value == Double.POSITIVE_INFINITY) {
                    return true;
                }
                else if ( Double.isNaN( (Double) value ) || (Double) value == Double.NEGATIVE_INFINITY ) {
                    return false;
                }
            }
            else if ( value instanceof Float ) {
                if ((Float) value == Float.POSITIVE_INFINITY) {
                    return true;
                }
                else if ( Float.isNaN( (Float) value ) || (Float) value == Float.NEGATIVE_INFINITY ) {
                    return false;
                }
            }

            if (value instanceof BigDecimal) {
                return ((BigDecimal) value).compareTo(BigDecimal.valueOf(minValue)) != -1;
            }
            else if ( value instanceof BigInteger ) {
                return ((BigInteger) value).compareTo(BigInteger.valueOf(minValue)) != -1;
            }
            else if (value instanceof Number) {
                long longValue = ((Number) value).longValue();
                return longValue >= minValue;
            }
            return false;
        }

        public boolean isMaxNumberValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
            if (value instanceof String && ((String) value).matches("-?\\d+(\\.\\d+)?")) {
                long longValue = Long.parseLong((String)value);
                return longValue <= maxValue;
            }
            // handling of NaN, positive infinity and negative infinity
            else if (value instanceof Double) {
                if ((Double) value == Double.NEGATIVE_INFINITY) {
                    return true;
                }
                else if ( Double.isNaN( (Double) value ) || (Double) value == Double.POSITIVE_INFINITY ) {
                    return false;
                }
            }
            else if ( value instanceof Float ) {
                if ((Float) value == Float.NEGATIVE_INFINITY) {
                    return true;
                }
                else if ( Float.isNaN( (Float) value ) || (Float) value == Float.POSITIVE_INFINITY ) {
                    return false;
                }
            }
            if (value instanceof BigDecimal) {
                return ((BigDecimal) value).compareTo(BigDecimal.valueOf(maxValue)) != 1;
            }
            else if ( value instanceof BigInteger ) {
                return ((BigInteger) value).compareTo(BigInteger.valueOf(maxValue)) != 1;
            } else if (value instanceof Number) {
                long longValue = ((Number) value).longValue();
                return longValue <= maxValue;
            }
            return false;
        }
    }

}
