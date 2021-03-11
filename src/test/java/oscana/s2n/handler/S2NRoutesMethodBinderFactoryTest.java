package oscana.s2n.handler;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import nablarch.fw.ExecutionContext;
import nablarch.fw.MethodBinder;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpRequestHandler;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.MockHttpRequest;
import oscana.s2n.testCommon.S2NBaseTest;

/**
 * {@link S2NRoutesMethodBinderFactory}のテストクラス。
 */
public class S2NRoutesMethodBinderFactoryTest extends S2NBaseTest{

    private S2NRoutesMethodBinderFactory sut = new S2NRoutesMethodBinderFactory();

    /**
     * {@link S2NRoutesMethodBinderFactory#create(String)}のテストケース
     */
    @Test
    public void testCreate() throws Exception {
        MethodBinder<HttpRequest, Object> factory = sut.create("handle");
        assertThat("S2NRoutesMethodBinderが生成されること", factory, is(instanceOf(S2NRoutesMethodBinder.class)));

        // factoryに指定したメソッドが呼び出せることも確認する
        HttpResponse result = (HttpResponse)this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            return factory.bind(new Action()).handle(new MockHttpRequest(), new ExecutionContext());
        }));

        assertThat(result.getStatusCode(), is(200));
        assertThat(result.getBodyString(), is("success"));
    }

    /**
     * テスト用のアクションクラス。
     */
    public static class Action implements HttpRequestHandler {
        @Override
        public HttpResponse handle(HttpRequest request, ExecutionContext context) {
            HttpResponse response = new HttpResponse(200);
            response.write("success");
            return response;
        }
    }

    @Override
    protected void setClassToRegist() {
        registClassList = Arrays.asList(HttpResourceHolder.class);
    }
}
