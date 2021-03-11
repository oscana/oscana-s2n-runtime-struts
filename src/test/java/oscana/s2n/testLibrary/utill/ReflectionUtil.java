package oscana.s2n.testLibrary.utill;

import java.lang.reflect.Method;

/**
 * Java5のgenericsや可変長を活用する、リフレクションのためのユーティリティクラス。
 *
 * @author koichik
 */
public abstract class ReflectionUtil {

    /**
     * {@link Class}オブジェクトが表すクラスまたはインタフェースの指定されたメンバメソッドをリフレクトする{@link Method}オブジェクトを返却する。
     *
     * @param clazz
     *            クラスの{@link Class}オブジェクト
     * @param name
     *            メソッドの名前
     * @param argTypes
     *            パラメータのリスト
     * @return 指定された{@code name}および{@code argTypes}と一致する{@link Method}オブジェクト
     * @see Class#getDeclaredMethod(String, Class[])
     */
    public static Method getDeclaredMethod(final Class<?> clazz,
            final String name, final Class<?>... argTypes) {
        try {
            return clazz.getDeclaredMethod(name, argTypes);
        } catch (final NoSuchMethodException e) {
            throw new RuntimeException(clazz.getName() + name + argTypes, e);
        }
    }
}