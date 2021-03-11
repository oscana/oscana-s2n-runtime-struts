/*
 * 取り込み元
 *    ライブラリ名：     commons-lang
 *    クラス名：         org.apache.commons.lang3.reflect.FieldUtils
 *    ソースリポジトリ： https://github.com/apache/commons-lang/blob/master/src/main/java/org/apache/commons/lang3/reflect/FieldUtils.java
 *
 * 上記ファイルを取り込み、修正を加えた。
 *
 * Copyright 2020 TIS Inc.
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package oscana.s2n.common.web.util;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

/**
 * リフレクションを使ってフィールドを操作するユーティリティクラス。<br>
 * <br>
 * 外部ライブラリに依存しないようにするため、{@link org.apache.commons.lang3.reflect.FieldUtils}から移植する。<br>
 *
 * * <br>
 * 移植内容の変更点：<br>
 * <br>
 *    ・getAllFieldsメソッド：toArrayメソッドのパラメータはnew Field()の形で渡す。<br>
 *    ・readFieldメソッド：実装方式はMemberUtils.setAccessibleWorkaround使わないように修正する。<br>
 *    ・writeFieldメソッド：実装方式はMemberUtils.setAccessibleWorkaround使わないように修正する。<br>
 *    ・getFieldメソッド：nullチェックと空文字チェックを消す。<br>
 *
 *  @author Rai Shuu
 */
public class FieldUtils {

    /** フィールドのアクセスできる修飾子*/
    private static final int ACCESS_TEST = Modifier.PUBLIC | Modifier.PROTECTED | Modifier.PRIVATE;

    /**
     * クラスの全フィールドを配列の形で取得する。
     * @param cls クラス
     * @return Fieldの配列
     */
    public static Field[] getAllFields(final Class<?> cls) {
        final List<Field> allFieldsList = getAllFieldsList(cls);
        return allFieldsList.toArray(new Field[allFieldsList.size()]);
    }

    /**
     * クラスの全フィールドをリストの形で取得する。
     * @param cls クラス
     * @return Fieldのリスト
     */
    public static List<Field> getAllFieldsList(final Class<?> cls) {
        if (cls == null) {
            throw new IllegalArgumentException(String.format("The class must not be null"));
        }

        final List<Field> allFields = new ArrayList<>();
        Class<?> currentClass = cls;
        while (currentClass != null) {
            final Field[] declaredFields = currentClass.getDeclaredFields();
            Collections.addAll(allFields, declaredFields);
            currentClass = currentClass.getSuperclass();
        }
        return allFields;
    }

    /**
     * オブジェクトの指定フィールドの値を取得する。
     * @param field フィルド
     * @param target オブジェクト
     * @param forceAccess boolean
     * @return Object オブジェクト
     * @throws IllegalAccessException IllegalAccessException例外
     */
    public static Object readField(Field field, Object target, boolean forceAccess) throws IllegalAccessException {

        if (field == null) {
            throw new IllegalArgumentException(String.format("The field must not be null"));
        }

        if (forceAccess && !field.isAccessible()) {
            field.setAccessible(true);
        } else {
            if (!field.isAccessible()) {
                final Member m = (Member) field;
                if (Modifier.isPublic(m.getModifiers()) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
                    try {
                        field.setAccessible(true);
                    } catch (final SecurityException e) { // NOPMD
                        // 何もしない
                    }
                }
            }
        }
        return field.get(target);
    }

    /**
     * アクセスできるかどうかを判定する。
     * @param modifiers
     * @return アクセスできる場合はtrue、その以外はfalse
     */
    private static boolean isPackageAccess(final int modifiers) {
        return (modifiers & ACCESS_TEST) == 0;
    }

    /**
     * オブジェクトの指定フィールドの値を取得する。（事前にフィールド存在を判定）
     * @param target ターゲット
     * @param fieldName フィルド名
     * @param forceAccess boolean
     * @return オブジェクト
     * @throws IllegalAccessException IllegalAccessException例外
     */
    public static Object readField(Object target, String fieldName, boolean forceAccess) throws IllegalAccessException {
        if (target == null) {
            throw new IllegalArgumentException(String.format("target object must not be null"));
        }
        final Class<?> cls = target.getClass();
        final Field field = getField(cls, fieldName, forceAccess);
        if (field == null) {
            throw new IllegalArgumentException(String.format("Cannot locate field %s on %s", fieldName, cls));
        }

        return readField(field, target, false);
    }


    /**
     * オブジェクトの指定フィールドに値を設定する。
     * @param field フィルド
     * @param target ターゲットオブジェクト
     * @param value オブジェクト
     * @param forceAccess boolean
     * @throws IllegalAccessException IllegalAccessException例外
     */
    public static void writeField(final Field field, final Object target, final Object value, final boolean forceAccess)
            throws IllegalAccessException {
        if (field == null) {
            throw new IllegalArgumentException(String.format("The field must not be null"));
        }

        if (!field.isAccessible()) {
            if (forceAccess) {
                field.setAccessible(true);
            } else {
                final Member m = (Member) field;
                if (Modifier.isPublic(m.getModifiers()) && isPackageAccess(m.getDeclaringClass().getModifiers())) {
                    try {
                        field.setAccessible(true);
                    } catch (final SecurityException e) { // NOPMD
                        // 何もしない
                    }
                }
            }
        }

        field.set(target, value);
    }

    /**
     * オブジェクトの指定フィールドに値を設定する。（事前にフィールド存在を判定）
     * @param target ターゲット
     * @param fieldName フィルド名
     * @param value オブジェクト
     * @param forceAccess boolean
     * @throws IllegalAccessException IllegalAccessException例外
     */
    public static void writeField(final Object target, final String fieldName, final Object value,
            final boolean forceAccess)
            throws IllegalAccessException {
        if (target == null) {
            throw new IllegalArgumentException(String.format("target object must not be null"));
        }
        final Class<?> cls = target.getClass();
        final Field field = getField(cls, fieldName, forceAccess);

        if (field == null) {
            throw new IllegalArgumentException(
                    String.format("Cannot locate declared field %s.%s", cls.getName(), fieldName));
        }
        // already forced access above, don't repeat it here:
        writeField(field, target, value, false);
    }

    /**
     * フィールドを取得する。（クラス⇒親クラス順番で探す）
     * @param cls クラス
     * @param fieldName フィルド名
     * @param forceAccess boolean
     * @return Field
     */
    public static Field getField(final Class<?> cls, final String fieldName, final boolean forceAccess) {
        if (cls == null) {
            throw new IllegalArgumentException(String.format("The class must not be null"));
        }
        if (!isNotBlank(fieldName)) {
            throw new IllegalArgumentException(String.format("The field name must not be blank/empty"));
        }

        // check up the superclass hierarchy
        for (Class<?> acls = cls; acls != null; acls = acls.getSuperclass()) {
            try {
                final Field field = acls.getDeclaredField(fieldName);
                // getDeclaredField checks for non-public scopes as well
                // and it returns accurate results
                if (!Modifier.isPublic(field.getModifiers())) {
                    if (forceAccess) {
                        field.setAccessible(true);
                    } else {
                        continue;
                    }
                }
                return field;
            } catch (final NoSuchFieldException ex) { // NOPMD
                // ignore
            }
        }
        // check the public interface case. This must be manually searched for
        // incase there is a public supersuperclass field hidden by a private/package
        // superclass field.
        Field match = null;
        for (final Class<?> class1 : getAllInterfaces(cls)) {
            try {
                final Field test = class1.getField(fieldName);
                if (match != null) {
                    throw new IllegalArgumentException(String.format("Reference to field %s is ambiguous relative to %s"
                            + "; a matching field exists on two or more implemented interfaces.", fieldName, cls));
                }

                match = test;
            } catch (final NoSuchFieldException ex) { // NOPMD
                // ignore
            }
        }
        return match;
    }

    /**
     * 指定クラスのすべてのインタフェースを取得して、リストの形で返却する。
     * @param cls クラス
     * @return Classクラスのリスト
     */
    public static List<Class<?>> getAllInterfaces(final Class<?> cls) {
        if (cls == null) {
            return null;
        }

        final LinkedHashSet<Class<?>> interfacesFound = new LinkedHashSet<>();
        getAllInterfaces(cls, interfacesFound);

        return new ArrayList<>(interfacesFound);
    }

    /**
     * 指定クラスのすべてのインタフェースを取得する。
     *
     * @param cls  the class to look up, may be {@code null}
     * @param interfacesFound the {@code Set} of interfaces for the class
     */
    private static void getAllInterfaces(Class<?> cls, final HashSet<Class<?>> interfacesFound) {
        while (cls != null) {
            final Class<?>[] interfaces = cls.getInterfaces();

            for (final Class<?> i : interfaces) {
                if (interfacesFound.add(i)) {
                    getAllInterfaces(i, interfacesFound);
                }
            }

            cls = cls.getSuperclass();
        }
    }

    /**
     * ブランクではないかどうか返却する。
     *
     * @param str
     *            文字列
     * @return ブランクではないかどうか
     */
    private static boolean isNotBlank(final String str) {
        if (str == null || str.length() == 0) {
            return false;
        }
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return true;
            }
        }
        return false;
    }

}
