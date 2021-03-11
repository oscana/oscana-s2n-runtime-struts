package oscana.s2n.common.web.util;

import static org.junit.Assert.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.apache.commons.lang.ArrayUtils;
import org.junit.Before;
import org.junit.Test;

/**
 * {@link FieldUtils}のテスト。
 *
 */
public class FieldUtilsTest {

    static final Integer I0 = Integer.valueOf(0);
    static final Integer I1 = Integer.valueOf(1);
    static final Double D0 = Double.valueOf(0.0);
    static final Double D1 = Double.valueOf(1.0);

    private static final String JACOCO_DATA_FIELD_NAME = "$jacocoData";

    private PublicChild publicChild;
    private PubliclyShadowedChild publiclyShadowedChild;
    private PrivatelyShadowedChild privatelyShadowedChild;
    private final Class<? super PublicChild> parentClass = PublicChild.class.getSuperclass();

    @Before
    public void setUp() {
        publicChild = new PublicChild();
        publiclyShadowedChild = new PubliclyShadowedChild();
        privatelyShadowedChild = new PrivatelyShadowedChild();
    }

    /**
     * FieldUtilsのコンストラクタのテスト
     */
    @Test
    public void testConstructor() {
        assertNotNull(new FieldUtils());
        final Constructor<?>[] cons = FieldUtils.class.getDeclaredConstructors();
        assertEquals(1, cons.length);
        assertTrue(Modifier.isPublic(cons[0].getModifiers()));
        assertTrue(Modifier.isPublic(FieldUtils.class.getModifiers()));
        assertFalse(Modifier.isFinal(FieldUtils.class.getModifiers()));
    }

    /**
     * フィールドを取得する（ForceAccessはtrueの場合）のテスト
     */
    @Test
    public void testGetFieldForceAccess() {
        assertEquals(PublicChild.class, FieldUtils.getField(PublicChild.class, "VALUE", true).getDeclaringClass());
        assertEquals(parentClass, FieldUtils.getField(PublicChild.class, "s", true).getDeclaringClass());
        assertEquals(parentClass, FieldUtils.getField(PublicChild.class, "b", true).getDeclaringClass());
        assertEquals(parentClass, FieldUtils.getField(PublicChild.class, "i", true).getDeclaringClass());
        assertEquals(parentClass, FieldUtils.getField(PublicChild.class, "d", true).getDeclaringClass());
        assertEquals(Foo.class, FieldUtils.getField(PubliclyShadowedChild.class, "VALUE", true).getDeclaringClass());
        assertEquals(PubliclyShadowedChild.class,
                FieldUtils.getField(PubliclyShadowedChild.class, "s", true).getDeclaringClass());
        assertEquals(PubliclyShadowedChild.class,
                FieldUtils.getField(PubliclyShadowedChild.class, "b", true).getDeclaringClass());
        assertEquals(PubliclyShadowedChild.class,
                FieldUtils.getField(PubliclyShadowedChild.class, "i", true).getDeclaringClass());
        assertEquals(PubliclyShadowedChild.class,
                FieldUtils.getField(PubliclyShadowedChild.class, "d", true).getDeclaringClass());
        assertEquals(Foo.class, FieldUtils.getField(PrivatelyShadowedChild.class, "VALUE", true).getDeclaringClass());
        assertEquals(PrivatelyShadowedChild.class,
                FieldUtils.getField(PrivatelyShadowedChild.class, "s", true).getDeclaringClass());
        assertEquals(PrivatelyShadowedChild.class,
                FieldUtils.getField(PrivatelyShadowedChild.class, "b", true).getDeclaringClass());
        assertEquals(PrivatelyShadowedChild.class,
                FieldUtils.getField(PrivatelyShadowedChild.class, "i", true).getDeclaringClass());
        assertEquals(PrivatelyShadowedChild.class,
                FieldUtils.getField(PrivatelyShadowedChild.class, "d", true).getDeclaringClass());
    }

    /**
     * フィールドを取得する（ForceAccessはfalseの場合）のテスト
     */
    @Test
    public void testGetFieldForceAccess_isFalse() {
        assertEquals(Foo.class, FieldUtils.getField(PublicChild.class, "VALUE", false).getDeclaringClass());
    }

    /**
     * フィールドを取得異常（クラス未設定の場合）のテスト
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldForceAccessIllegalArgumentException1() {
        FieldUtils.getField(null, "none", true);
    }

    /**
     * フィールドを取得異常（フィールド名未設定の場合）のテスト
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldForceAccessIllegalArgumentException2() {
        FieldUtils.getField(PublicChild.class, null, true);
    }

    /**
     * フィールドを取得異常（クラス名空白の場合）のテスト
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldForceAccessIllegalArgumentException3() {
        FieldUtils.getField(PublicChild.class, "", true);
    }

    /**
     * フィールドを取得異常（クラス名スペースの場合）のテスト
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldForceAccessIllegalArgumentException4() {
        FieldUtils.getField(PublicChild.class, " ", true);
    }

    /**
     * 全フィールドを取得異常（クラス未設定の場合）のテスト
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetAllFieldsListIllegalArgumentException1() {
        FieldUtils.getAllFieldsList(null);
    }

    /**
     * フィールドの値を取得異常（存在しない項目）のテスト
     */
    @Test(expected = IllegalArgumentException.class)
    public void testReadFieldIllegalArgumentException1() throws IllegalAccessException {
        FieldUtils.readField(publicChild, "x", true);
    }

    /**
     * フィールドの値を取得異常（forceAccessはfalseの場合private項目を取得）のテスト
     */
    @Test(expected = IllegalAccessException.class)
    public void testReadFieldIllegalArgumentException2() throws IllegalAccessException {

        try {
            Field parentB;
            parentB = parentClass.getDeclaredField("d");
            FieldUtils.readField(parentB, publicChild, false);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

    }

    /**
     * フィールドの値を設定異常（フィールド未設定の場合）のテスト
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWriteFieldIllegalArgumentException1() throws IllegalAccessException {
        FieldUtils.writeField(null, publicChild, "", true);
    }

    /**
     * フィールドの値を設定異常（フィールド名は空白の場合）のテスト
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWriteFieldIllegalArgumentException2() throws IllegalAccessException {
        Object obj = null;
        FieldUtils.writeField(obj, "", "", true);
    }

    /**
     * フィールドの値を設定異常（存在しない項目の場合）のテスト
     */
    @Test(expected = IllegalArgumentException.class)
    public void testWriteFieldIllegalArgumentException3() throws IllegalAccessException {
        FieldUtils.writeField(publicChild, "x", "", true);
    }

    /**
     * フィールドの値を設定異常（forceAccessはfalseの場合private項目を設定）のテスト
     */
    @Test(expected = IllegalAccessException.class)
    public void testWriteFieldIllegalArgumentException4() throws IllegalAccessException {

        try {
            Field parentB;
            parentB = parentClass.getDeclaredField("d");
            FieldUtils.writeField(parentB, publicChild, "", false);
        } catch (NoSuchFieldException | SecurityException e) {
            e.printStackTrace();
        }

    }

    /**
     * フィールドを取得異常（変数ではない場合）のテスト
     */
    @Test(expected = IllegalArgumentException.class)
    public void testGetFieldIllegalArgumentException2() throws IllegalAccessException {
        FieldUtils.getField(DoubleImp.class, "VALUE", true);
    }

    /**
     * 全フィールド（親クラス含む）を正しく配列に取得するのテスト
     */
    @Test
    public void testGetAllFields() {
        assertArrayEquals(new Field[0], FieldUtils.getAllFields(Object.class));
        final Field[] fieldsNumber = Number.class.getDeclaredFields();
        assertArrayEquals(fieldsNumber, FieldUtils.getAllFields(Number.class));
        final Field[] fieldsInteger = Integer.class.getDeclaredFields();
        assertArrayEquals(ArrayUtils.addAll(fieldsInteger, fieldsNumber), FieldUtils.getAllFields(Integer.class));
        final Field[] allFields = FieldUtils.getAllFields(PublicChild.class);
        // Under Jacoco,0.8.1 and Java 10, the field count is 7.
        int expected = 5;
        for (final Field field : allFields) {
            if (field.getName().equals(JACOCO_DATA_FIELD_NAME)) {
                expected++;
            }
        }
        assertEquals(expected, allFields.length);
    }

    /**
     * 全フィールド（親クラス含む）を正しくリストに取得するのテスト
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void testGetAllFieldsList() {
        assertEquals(0, FieldUtils.getAllFieldsList(Object.class).size());
        final List<Field> fieldsNumber = Arrays.asList(Number.class.getDeclaredFields());
        assertEquals(fieldsNumber, FieldUtils.getAllFieldsList(Number.class));
        final List<Field> fieldsInteger = Arrays.asList(Integer.class.getDeclaredFields());
        final List<Field> allFieldsInteger = new ArrayList<>(fieldsInteger);
        allFieldsInteger.addAll(fieldsNumber);
        assertEquals(new HashSet(allFieldsInteger), new HashSet(FieldUtils.getAllFieldsList(Integer.class)));
        final List<Field> allFields = FieldUtils.getAllFieldsList(PublicChild.class);
        // Under Jacoco,0.8.1 and Java 10, the field count is 7.
        int expected = 5;
        for (final Field field : allFields) {
            if (field.getName().equals(JACOCO_DATA_FIELD_NAME)) {
                expected++;
            }
        }
        assertEquals(expected, FieldUtils.getAllFieldsList(PublicChild.class).size());
    }

    /**
     * 各タイプのフィールドを正しく取得できるのテスト
     */
    @Test
    public void testReadFieldForceAccess() throws Exception {
        final Field parentS = parentClass.getDeclaredField("s");
        parentS.setAccessible(false);
        assertEquals("s", FieldUtils.readField(parentS, publicChild, true));
        assertEquals("s", FieldUtils.readField(parentS, publiclyShadowedChild, true));
        assertEquals("s", FieldUtils.readField(parentS, privatelyShadowedChild, true));
        final Field parentB = parentClass.getDeclaredField("b");
        parentB.setAccessible(false);
        assertEquals(Boolean.FALSE, FieldUtils.readField(parentB, publicChild, true));
        assertEquals(Boolean.FALSE, FieldUtils.readField(parentB, publiclyShadowedChild, true));
        assertEquals(Boolean.FALSE, FieldUtils.readField(parentB, privatelyShadowedChild, true));

        final Field parentI = parentClass.getDeclaredField("i");
        parentI.setAccessible(false);
        assertEquals(I0, FieldUtils.readField(parentI, publicChild, true));
        assertEquals(I0, FieldUtils.readField(parentI, publiclyShadowedChild, true));
        assertEquals(I0, FieldUtils.readField(parentI, privatelyShadowedChild, true));

        final Field parentD = parentClass.getDeclaredField("d");
        parentD.setAccessible(false);
        assertEquals(D0, FieldUtils.readField(parentD, publicChild, true));
        assertEquals(D0, FieldUtils.readField(parentD, publiclyShadowedChild, true));
        assertEquals(D0, FieldUtils.readField(parentD, privatelyShadowedChild, true));

        try {
            FieldUtils.readField(null, publicChild, true);
            fail("a null field should cause an IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * 各タイプのフィールドの値を正しく取得できるのテスト
     */
    @Test
    public void testReadNamedFieldForceAccess() throws Exception {
        assertEquals("s", FieldUtils.readField(publicChild, "s", true));
        assertEquals("ss", FieldUtils.readField(publiclyShadowedChild, "s", true));
        assertEquals("ss", FieldUtils.readField(privatelyShadowedChild, "s", true));
        assertEquals(Boolean.FALSE, FieldUtils.readField(publicChild, "b", true));
        assertEquals(Boolean.TRUE, FieldUtils.readField(publiclyShadowedChild, "b", true));
        assertEquals(Boolean.TRUE, FieldUtils.readField(privatelyShadowedChild, "b", true));
        assertEquals(I0, FieldUtils.readField(publicChild, "i", true));
        assertEquals(I1, FieldUtils.readField(publiclyShadowedChild, "i", true));
        assertEquals(I1, FieldUtils.readField(privatelyShadowedChild, "i", true));
        assertEquals(D0, FieldUtils.readField(publicChild, "d", true));
        assertEquals(D1, FieldUtils.readField(publiclyShadowedChild, "d", true));
        assertEquals(D1, FieldUtils.readField(privatelyShadowedChild, "d", true));

        try {
            FieldUtils.readField(publicChild, null, true);
            fail("a null field name should cause an IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            // expected
        }

        try {
            FieldUtils.readField(publicChild, "", true);
            fail("an empty field name should cause an IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            // expected
        }

        try {
            FieldUtils.readField(publicChild, " ", true);
            fail("a blank field name should cause an IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            // expected
        }

        try {
            FieldUtils.readField((Object) null, "none", true);
            fail("a null target should cause an IllegalArgumentException");
        } catch (final IllegalArgumentException e) {
            // expected
        }
    }

    /**
     * 各タイプのフィールドを正しく設定できるのテスト
     */
    @Test
    public void testWriteFieldForceAccess() throws Exception {
        Field field = parentClass.getDeclaredField("s");
        FieldUtils.writeField(field, publicChild, "S", true);
        assertEquals("S", field.get(publicChild));
        field = parentClass.getDeclaredField("b");
        FieldUtils.writeField(field, publicChild, Boolean.TRUE, true);
        assertEquals(Boolean.TRUE, field.get(publicChild));
        field = parentClass.getDeclaredField("i");
        FieldUtils.writeField(field, publicChild, Integer.valueOf(Integer.MAX_VALUE), true);
        assertEquals(Integer.valueOf(Integer.MAX_VALUE), field.get(publicChild));
        field = parentClass.getDeclaredField("d");
        FieldUtils.writeField(field, publicChild, Double.valueOf(Double.MAX_VALUE), true);
        assertEquals(Double.valueOf(Double.MAX_VALUE), field.get(publicChild));
    }

    /**
     * 各タイプのフィールドの値を正しく設できるのテスト
     */
    @Test
    public void testWriteNamedFieldForceAccess() throws Exception {
        FieldUtils.writeField(publicChild, "s", "S", true);
        assertEquals("S", FieldUtils.readField(publicChild, "s", true));
        FieldUtils.writeField(publicChild, "b", Boolean.TRUE, true);
        assertEquals(Boolean.TRUE, FieldUtils.readField(publicChild, "b", true));
        FieldUtils.writeField(publicChild, "i", Integer.valueOf(1), true);
        assertEquals(Integer.valueOf(1), FieldUtils.readField(publicChild, "i", true));
        FieldUtils.writeField(publicChild, "d", Double.valueOf(1.0), true);
        assertEquals(Double.valueOf(1.0), FieldUtils.readField(publicChild, "d", true));

        FieldUtils.writeField(publiclyShadowedChild, "s", "S", true);
        assertEquals("S", FieldUtils.readField(publiclyShadowedChild, "s", true));
        FieldUtils.writeField(publiclyShadowedChild, "b", Boolean.FALSE, true);
        assertEquals(Boolean.FALSE, FieldUtils.readField(publiclyShadowedChild, "b", true));
        FieldUtils.writeField(publiclyShadowedChild, "i", Integer.valueOf(0), true);
        assertEquals(Integer.valueOf(0), FieldUtils.readField(publiclyShadowedChild, "i", true));
        FieldUtils.writeField(publiclyShadowedChild, "d", Double.valueOf(0.0), true);
        assertEquals(Double.valueOf(0.0), FieldUtils.readField(publiclyShadowedChild, "d", true));

        FieldUtils.writeField(privatelyShadowedChild, "s", "S", true);
        assertEquals("S", FieldUtils.readField(privatelyShadowedChild, "s", true));
        FieldUtils.writeField(privatelyShadowedChild, "b", Boolean.FALSE, true);
        assertEquals(Boolean.FALSE, FieldUtils.readField(privatelyShadowedChild, "b", true));
        FieldUtils.writeField(privatelyShadowedChild, "i", Integer.valueOf(0), true);
        assertEquals(Integer.valueOf(0), FieldUtils.readField(privatelyShadowedChild, "i", true));
        FieldUtils.writeField(privatelyShadowedChild, "d", Double.valueOf(0.0), true);
        assertEquals(Double.valueOf(0.0), FieldUtils.readField(privatelyShadowedChild, "d", true));
    }

    /**
     * インタフェース取得する際Nullを返す（パラメータはnullの場合）のテスト
     */
    @Test
    public void testGetAllInterfacesNull() {
        assertNull(FieldUtils.getAllInterfaces(null));
    }

    /**
     * インタフェース正しく取得する（同じインタフェース複数存在する場合）のテスト
     */
    @Test
    public void testDupInterface() {
        assertEquals(1, FieldUtils.getAllInterfaces(DupInterface.class).size());
    }

}

class PubliclyShadowedChild extends Parent {
    public String s = "ss";
    public boolean b = true;
    public int i = 1;
    public double d = 1.0;
}

@SuppressWarnings("unused")
class PrivatelyShadowedChild extends Parent {
    private final String s = "ss";
    private final boolean b = true;
    private final int i = 1;
    private final double d = 1.0;
}

class PublicChild extends Parent {
    static final String VALUE = "child";
}

class FinalChild extends Parent {
    public static final String VAL = "final";
}

class Parent implements Foo {
    public String s = "s";
    protected boolean b = false;
    int i = 0;
    @SuppressWarnings("unused")
    private final double d = 0.0;

    @Override
    public void doIt() {
    }
}

class DoubleImp implements Foo, Bar {
    @Override
    public void doIt() {
    }
}

class DupInterface extends Parent implements Foo {
}

interface Foo {
    public static final String VALUE = "foo";

    void doIt();
}

interface Bar {
    public static final String VALUE = "bar";

    void doIt();
}
