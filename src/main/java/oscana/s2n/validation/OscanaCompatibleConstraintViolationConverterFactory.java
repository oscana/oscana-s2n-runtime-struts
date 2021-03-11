/*
 * 取り込み元
 *    ライブラリ名：     nablarch-core-validation-ee
 *    クラス名：         nablarch.core.validation.ee.ItemNamedConstraintViolationConverterFactory
 *    ソースリポジトリ： https://github.com/nablarch/nablarch-core-validation-ee/blob/master/src/main/java/nablarch/core/validation/ee/ItemNamedConstraintViolationConverterFactory.java
 *
 * 上記ファイルを取り込み、修正を加えた。
 *
 * Copyright 2020 TIS Inc.
 *
 */
package oscana.s2n.validation;

import java.util.Locale;

import javax.validation.ConstraintViolation;
import javax.validation.Path;

import nablarch.core.message.Message;
import nablarch.core.message.MessageLevel;
import nablarch.core.message.MessageUtil;
import nablarch.core.message.StringResource;
import nablarch.core.util.StringUtil;
import nablarch.core.validation.ValidationResultMessage;
import nablarch.core.validation.ee.ConstraintViolationConverter;
import nablarch.core.validation.ee.ConstraintViolationConverterFactory;

/**
 * Strutsの設定ファイル形式と互換性があるメッセージ変換モジュール。
 * <p/>
 * @FieldName("キー名")アノテーションで項目名のキーを指定できる。
 * @FieldName("キー名")アノテーションが指定された場合はメッセージリソースから指定されたキーに対応する項目名を取得し、
 * エラーメッセージの"{fieldName}"を置き換える。
 * <pre>
 * エラーメッセージ：{fieldName}は必須です。
 * アノテーション；@FieldName("userName")
 * メッセージリソース：userName=ユーザ名
 * 最終的なメッセージ：ユーザ名は必須です。
 * </pre>
 *
 * 本実装は、ItemNamedConstraintViolationConverterFactoryを複製、改造したものである。
 *
 * @author Fumihiko Yamamoto
 * @see nablarch.core.validation.ee.ItemNamedConstraintViolationConverterFactory
 */
public class OscanaCompatibleConstraintViolationConverterFactory extends ConstraintViolationConverterFactory {

    /**
     * {@link ConstraintViolationConverter}を生成する。
     *
     * @return {@code ConstraintViolationConverter}
     */
    @Override
    public ConstraintViolationConverter create() {
        return create(null);
    }

    /**
     * {@link ConstraintViolationConverter}を生成する。
     *
     * @param prefix プレフィックス
     * @return {@code ConstraintViolationConverter}
     */
    @Override
    public ConstraintViolationConverter create(final String prefix) {
        return new OscanaCompatibleConstraintViolationConverter(prefix);
    }

    /**
     * 項目名付きのメッセージ変換を行うクラス。
     */
    private static class OscanaCompatibleConstraintViolationConverter extends ConstraintViolationConverter {

        /** プロパティ名の先頭に付加するプレフィックス */
        private final String prefix;

        /**
         * プレフィックスなしで生成する。
         */
        @SuppressWarnings("unused")
        public OscanaCompatibleConstraintViolationConverter() {
            this(null);
        }

        /**
         * プレフィックス付きで生成する。
         *
         * @param prefix プレフィックス
         */
        public OscanaCompatibleConstraintViolationConverter(final String prefix) {
            this.prefix = StringUtil.hasValue(prefix) ? prefix + '.' : "";
        }

        /**
         * メッセージを生成する。
         *
         * @param violation {@code ConstraintViolation}
         * @return メッセージ
         */
        @Override
        public Message convert(final ConstraintViolation<?> violation) {
            final Object bean = violation.getLeafBean();

            final String messageId;
            if (bean != null) {
                messageId = createItemNameMessageId(bean.getClass(), getLeafPropertyName(violation));
            } else {
                messageId = "";
            }

            final StringResource stringResource = new OscanaCompatibleStringResourceWithItemName(messageId, violation);
            return new ValidationResultMessage(prefix + violation.getPropertyPath(), stringResource, null);
        }

        /**
         * 項目名を表すメッセージIDを生成する。
         *
         * @param clazz バリデーション対象のクラス
         * @param propertyName プロパティ名
         * @return 項目名を示すメッセージID
         */
        private static String createItemNameMessageId(final Class<?> clazz, final String propertyName) {

            if (StringUtil.isNullOrEmpty(propertyName)) {
                return null;
            }

            FieldName annotation = null;
            try {
                annotation = clazz.getField(propertyName).getAnnotation(FieldName.class);
            } catch (NoSuchFieldException e) {
                // do nothing
            }

            if (annotation == null) {
                try {
                    String methodName = "get" + propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
                    annotation = clazz.getMethod(methodName).getAnnotation(FieldName.class);
                } catch (NoSuchMethodException e) {
                    // do nothing
                }
            }

            return annotation != null ? annotation.value() : null;
        }

        /**
         * 末端のプロパティ名を取得する。
         *
         * @param violation {@code ConstraintViolation}
         * @return 末端のプロパティ名
         */
        private static String getLeafPropertyName(final ConstraintViolation<?> violation) {
            final Path path = violation.getPropertyPath();
            String propertyName = null;
            for (final Path.Node node : path) {
                propertyName = node.getName();
            }
            return propertyName;
        }
    }

    /**
     * 項目名付きのメッセージを持つ{@link StringResource}実装クラス。
     */
    private static class OscanaCompatibleStringResourceWithItemName implements StringResource {

        /** ID */
        private final String id;

        /** メッセージ */
        private final String message;

        /** 項目名を取得するためのメッセージID */
        private final String messageIdOfItemName;

        /**
         * 項目名付きメッセージを生成する。
         *
         * @param messageIdOfItemName 項目名のメッセージID
         * @param violation {@code ConstraintViolation}
         */
        public OscanaCompatibleStringResourceWithItemName(
                final String messageIdOfItemName, final ConstraintViolation<?> violation) {
            this.messageIdOfItemName = messageIdOfItemName;
            this.id = violation.getConstraintDescriptor()
                    .getAnnotation()
                    .annotationType()
                    .getName();
            this.message = violation.getMessage();
        }

        /**
         * IDを取得する。
         *
         * @return ID
         */
        @Override
        public String getId() {
            return id;
        }

        /**
         * メッセージを取得する。
         *
         * @param locale ローケール
         * @return メッセージ
         */
        @Override
        public String getValue(final Locale locale) {
            if (StringUtil.isNullOrEmpty(messageIdOfItemName)) {
                return message;
            }
            String fieldName = locale == null
                    ? MessageUtil.createMessage(MessageLevel.ERROR, messageIdOfItemName).formatMessage()
                    : MessageUtil.createMessage(MessageLevel.ERROR, messageIdOfItemName).formatMessage(locale);
            return message.replace("{fieldName}", fieldName);
        }
    }
}
