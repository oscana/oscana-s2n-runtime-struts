package oscana.s2n.handler;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import nablarch.fw.dicontainer.nablarch.Containers;
import nablarch.fw.web.servlet.ServletExecutionContext;
import oscana.s2n.testCommon.S2NBaseTest;

/**
 * {@link HttpResourceHolder}のテスト。
 *
 */
public class HttpResourceHolderTest extends S2NBaseTest {

    /**
     * サーブレットリソースのセッションを取得できること
     */
    @Test
    public void testGetHttpSession() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            ServletExecutionContext cxt = (ServletExecutionContext) context;
            HttpResourceHolder httpResourceHolder = Containers.get().getComponent(HttpResourceHolder.class);
            assertEquals(cxt.getNativeHttpSession(true),httpResourceHolder.getHttpSession());
            assertEquals(cxt.getServletContext(),httpResourceHolder.getServletContext());
            assertEquals(cxt.getServletRequest(),httpResourceHolder.getHttpServletRequest());
            assertEquals(cxt.getServletResponse(),httpResourceHolder.getHttpServletResponse());
            return null;
        }));

    }

    @Override
    protected void setClassToRegist() {
        registClassList = Arrays.asList(HttpResourceHolder.class);

    }
}