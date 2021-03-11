package oscana.s2n.handler;

import static org.hamcrest.core.Is.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import mockit.Mocked;
import nablarch.fw.dicontainer.nablarch.Containers;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.servlet.ServletExecutionContext;
import oscana.s2n.testCommon.S2NBaseTest;

/**
 * {@link HttpResourceHolderUpdateHandler}のテスト。
 *
 */
public class HttpResourceHolderUpdateHandlerTest extends S2NBaseTest {

    @Mocked
    private HttpRequest request;

    /**
     * handleメソッドを呼び出して、HttpResourceHolderにリソースを正しく設定されたことをテストする。
     */
    @Test
    public void testHandle() {
        HttpResponse response = (HttpResponse)this.handle(Arrays.asList(new HttpResourceHolderUpdateHandler(),(data, context) -> {
            HttpResourceHolder resource = Containers.get().getComponent(HttpResourceHolder.class);
            ServletExecutionContext cxt = (ServletExecutionContext) context;
            assertEquals(resource.getServletContext(), cxt.getServletContext());
            assertEquals(resource.getHttpServletRequest(), cxt.getServletRequest());
            assertEquals(resource.getHttpSession(), cxt.getNativeHttpSession(true));
            return new HttpResponse().setStatusCode(200).write("testHandle");
        }));

        assertNotNull(response);
        assertThat(response.getStatusCode(), is(200));
        assertThat(response.getBodyString(), is("testHandle"));
    }

    @Override
    protected void setClassToRegist() {
        registClassList = Arrays.asList(HttpResourceHolder.class);
    }

}
