/*
 * 取り込み元
 *    ライブラリ名：     nablarch-core-validation-ee
 *    クラス名：         nablarch.core.validation.ee.Required
 *    ソースリポジトリ： https://github.com/nablarch/nablarch-core-validation-ee/blob/master/src/main/java/nablarch/core/validation/ee/Required.java
 *
 * 上記ファイルを取り込み、修正を加えた。
 *
 * Copyright 2020 TIS Inc.
 *
 */
package oscana.s2n.validation;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.*;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import javax.validation.Constraint;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import javax.validation.Payload;

/**
 * 必須項目が設定されているかどうかをチェックするためのアノテーション。<br>
 * <br>
 * NablarchのRequiredアノテーション（nablarch.core.validation.ee.Required）に、target属性の拡張機能を追加する。<br>
 *
 * target属性：<br>
 * このバリデーションを呼び出しているActionメソッド名が、バリデーションのtarget属性に設定されているターゲットリストに含まれてない場合、チェック処理を行わない。<br>
 * target属性が設定されてない場合は、無条件にチェック処理を行う。
 *
 * @author Rai Shuu
 * @see nablarch.core.validation.ee.Required
 */
@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {Required.RequiredValidator.class})
public @interface Required {

    /** ターゲット（バリデーションを適用するメソッド名）  */
    String target() default "";

    /** グループ */
    Class<?>[] groups() default {};

    /** メッセージ */
    String message() default "{nablarch.core.validation.ee.Required.message}";

    /** payload */
    Class<? extends Payload>[] payload() default {};

    /** 複数指定用のアノテーション */
    @Target({METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        Required[] value();
    }

    /**
     * 必須項目が設定されているかどうかをチェックするクラス。<br>
     * <br>
     *
     * {@link Required}アノテーションを指定されたプロパティに値が入力されているかをチェックする。
     *
     * @author Rai Shuu
     */
    class RequiredValidator extends ValidateTarget implements ConstraintValidator<Required, Object> {

        /**
         * RequiredValidator を初期化する。
         *
         * @param constraintAnnotation 対象プロパティに付与されたアノテーション
         */
        @Override
        public void initialize(Required constraintAnnotation) {
            setTargets(constraintAnnotation.target());
        }

        /**
         * 値を入力したかどうかをチェックする。<br>
         * <br>
         * ・チェックターゲット対象外の場合、trueを返却する。<br>
         * ・チェックする値はNullの場合、falseを返却する。<br>
         * ・入力した値は{@link CharSequence}の場合、入力した値をチェックする。length() == 0 の場合は false 、length() > 0 の場合は true を返却する。<br>
         * ・上記以外の場合、trueを返却する。<br>
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

            if (value == null) {
                return false;
            }

            if (value instanceof CharSequence) {
                CharSequence c = (CharSequence) value;
                return c.length() > 0;
            }
            return true;
        }
    }
}
