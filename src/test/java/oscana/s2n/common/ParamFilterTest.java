package oscana.s2n.common;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import nablarch.test.core.db.DbAccessTestSupport;

/**
 * {@link ParamFilter}のテスト。
 */
public class ParamFilterTest extends DbAccessTestSupport {

    ParamFilter paramFilter = new ParamFilter();

    /**
     * リストが渡された場合、INPUTをリストに正常変換できること
     *
     */
    @SuppressWarnings("static-access")
    @Test
    public void testToList1() {

        List<String> except = Arrays.asList("a", "b", "c");
        List<String> actual = paramFilter.toList(except);

        assertEquals(except, actual);
    }

    /**
     * 可変長型引数が渡された場合、INPUTをリストに正常変換できること
     *
     */
    @SuppressWarnings("static-access")
    @Test
    public void testToList2() {

        List<String> except = new ArrayList<String>();
        except.add("aa");
        except.add("bb");
        except.add("cc");
        List<String> actual = paramFilter.toList("aa", "bb", "cc");

        assertEquals(except, actual);
    }

    /**
     * 変換元FWのSQLファイル名が存在する場合、NablarchのSQLキーに変換できること
     * 変換元FWのSQLファイル名がnullの場合、NablarchのSQLキーに変換できないこと
     */
    @SuppressWarnings("static-access")
    @Test
    public void testSqlFileNameToKey() {
        String actual1 = paramFilter.sqlFileNameToKey("app0001.sql");
        assertEquals("app0001", actual1);

        String actual2 = paramFilter.sqlFileNameToKey("app0002");
        assertEquals("app0002", actual2);

        String actual3 = paramFilter.sqlFileNameToKey(null);
        assertEquals(null, actual3);
    }
}
