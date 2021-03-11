package oscana.s2n.testCommon;

import java.util.AbstractMap;

public class S2NPair<K, V> extends AbstractMap.SimpleEntry<K, V> {
    /** serialVersionUID. */
    private static final long serialVersionUID = 6411527075103472113L;

    public S2NPair(final K key, final V value) {
        super(key, value);
    }
}
