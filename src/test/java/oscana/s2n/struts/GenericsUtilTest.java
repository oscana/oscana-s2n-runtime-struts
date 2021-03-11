package oscana.s2n.struts;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * {@link GenericsUtil}のテスト。
 *
 */
public class GenericsUtilTest {

    /**
     * ジェネリクスユーティリティのオブジェクトを正常に生成する上、クラスタイプを取得できること
     */
    @Test
    public void testGenericsUtil() {
        GenericsUtil<Object> genericsUtil = new GenericsUtil<Object>();
        assertNotNull(genericsUtil.getType());
    }
}