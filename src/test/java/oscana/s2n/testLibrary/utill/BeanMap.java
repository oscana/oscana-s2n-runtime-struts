package oscana.s2n.testLibrary.utill;

import java.util.LinkedHashMap;

/**
 *
 * Stringがキーで、存在しないキーにアクセスする(get)と例外を投げるマップクラス。<br>
 *
 * @author higa
 *
 */
public class BeanMap extends LinkedHashMap<String, Object> {

    private static final long serialVersionUID = 1;

    /**
     * マップからオブジェクトを取得する。
     *
     * @param key 条件を持つオブジェクト
     * @return 結果オブジェクト
     */
    @Override
    public Object get(Object key) {
        if (!containsKey(key)) {
            throw new IllegalArgumentException(key + " is not found in "
                    + keySet());
        }
        return super.get(key);
    }

}