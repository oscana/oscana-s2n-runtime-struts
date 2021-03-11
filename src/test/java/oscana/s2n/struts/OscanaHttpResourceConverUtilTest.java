package oscana.s2n.struts;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Arrays;
import java.util.HashMap;

import org.junit.Before;
import org.junit.Test;

import nablarch.fw.dicontainer.nablarch.Containers;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.MockHttpRequest;
import nablarch.fw.web.servlet.ServletExecutionContext;
import net.unit8.http.router.Routes;
import oscana.s2n.common.web.download.S2NStreamResponse;
import oscana.s2n.handler.HttpResourceHolder;
import oscana.s2n.handler.HttpResourceHolderHandler;
import oscana.s2n.struts.action.ActionForm;
import oscana.s2n.testCommon.S2NBaseTest;

/**
 * {@link OscanaHttpResourceConverUtil}のテスト。
 *
 */
public class OscanaHttpResourceConverUtilTest extends S2NBaseTest {

    private MockHttpRequest mockHttpRequest = new MockHttpRequest();

    @Before
    public void init() {
        try {
            final ClassLoader loader = Thread.currentThread().getContextClassLoader();
            URL routesUrl = loader.getResource("routes.xml");
            Routes.load(new File(URLDecoder.decode(routesUrl.getPath(), "UTF-8")));
        } catch (UnsupportedEncodingException e) {
            fail();
        }
    }

    /**
     * NablarchのExecutionContextからHttpSessionを取得できること
     */
    @SuppressWarnings("static-access")
    @Test
    public void testGetHttpSession() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            OscanaHttpResourceConverUtil sAStrutsUtil = new OscanaHttpResourceConverUtil();
            HttpResourceHolder resource = Containers.get().getComponent(HttpResourceHolder.class);
            ServletExecutionContext executionContext = new ServletExecutionContext(resource.getHttpServletRequest(),
                    resource.getHttpServletResponse(), resource.getServletContext());
            assertNotNull(sAStrutsUtil.getHttpSession(executionContext));
            return null;
        }));
    }

    /**
     * パスの前後に「/」を付けてない場合、正常に変換されること。
     */
    @SuppressWarnings("static-access")
    @Test
    public void testCreateHttpResponse_urlPattern1() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            String text = "test";
            OscanaHttpResourceConverUtil sAStrutsUtil = new OscanaHttpResourceConverUtil();
            String actionName = "actionName";

            HttpResponse httpResponse = sAStrutsUtil.createHttpResponse(text, sAStrutsUtil, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals("application/octet-stream", httpResponse.getContentType().toString());
            assertEquals("servlet:///WEB-INF/view/test", httpResponse.getContentPath().toString());

            return null;
        }));

    }

    /**
     * パスの前後に「/」を付けていると「/index/」を含まれない場合、正常に変換されること。
     */
    @SuppressWarnings("static-access")
    @Test
    public void testCreateHttpResponse_urlPattern2() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            String text = "/test/";
            OscanaHttpResourceConverUtil sAStrutsUtil = new OscanaHttpResourceConverUtil();
            String actionName = "actionName";

            HttpResponse httpResponse = sAStrutsUtil.createHttpResponse(text, sAStrutsUtil, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals("application/octet-stream", httpResponse.getContentType().toString());
            assertEquals("forward:///test/index", httpResponse.getContentPath().toString());
            return null;
        }));
    }

    /**
     * パスにJSPファイルを指定する場合、正常に変換されること。
     */
    @SuppressWarnings("static-access")
    @Test
    public void testCreateHttpResponse_urlPattern3() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            String text = "/test.jsp";
            OscanaHttpResourceConverUtil sAStrutsUtil = new OscanaHttpResourceConverUtil();
            String actionName = "actionName";

            HttpResponse httpResponse = sAStrutsUtil.createHttpResponse(text, sAStrutsUtil, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals("application/octet-stream", httpResponse.getContentType().toString());
            assertEquals("servlet:///WEB-INF/view/test.jsp", httpResponse.getContentPath().toString());
            return null;
        }));
    }

    /**
     * パスの先頭に「https://」を付けている場合、正常に変換されること。
     */
    @SuppressWarnings("static-access")
    @Test
    public void testCreateHttpResponse_urlPattern4() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            String text = "https://test/";
            OscanaHttpResourceConverUtil sAStrutsUtil = new OscanaHttpResourceConverUtil();
            String actionName = "actionName";

            HttpResponse httpResponse = sAStrutsUtil.createHttpResponse(text, sAStrutsUtil, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals("application/octet-stream", httpResponse.getContentType().toString());
            assertEquals("https://test/index.jsp", httpResponse.getContentPath().toString());
            return null;
        }));
    }

    /**
     * パスの先頭に「http://」を付けている場合、正常に変換されること。
     */
    @SuppressWarnings("static-access")
    @Test
    public void testCreateHttpResponse_urlPattern5() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            String text = "http://test";
            OscanaHttpResourceConverUtil sAStrutsUtil = new OscanaHttpResourceConverUtil();
            String actionName = "actionName";

            HttpResponse httpResponse = sAStrutsUtil.createHttpResponse(text, sAStrutsUtil, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals("application/octet-stream", httpResponse.getContentType().toString());
            assertEquals("http://test", httpResponse.getContentPath().toString());
            return null;
        }));
    }

    /**
     * パスの先頭に「/action」、後尾に「/indexBack」を付けてない場合、正常に変換されること。
     */
    @SuppressWarnings("static-access")
    @Test
    public void testCreateHttpResponse_urlPattern6() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            String text = "/action/test/indexBack";
            OscanaHttpResourceConverUtil sAStrutsUtil = new OscanaHttpResourceConverUtil();
            String actionName = "actionName";

            HttpResponse httpResponse = sAStrutsUtil.createHttpResponse(text, sAStrutsUtil, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals("application/octet-stream", httpResponse.getContentType().toString());
            assertEquals("forward:///action/test/indexBack", httpResponse.getContentPath().toString());
            return null;
        }));
    }

    /**
     * パスの前後に「/」を付けていると「/index/」を含まれる場合、正常に変換されること。
     */
    @Test
    public void testCreateHttpResponse_urlPattern7() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {

            String text = "/sample/sampleSearchList/index/";
            String actionName = "actionName";

            HttpResponse httpResponse = OscanaHttpResourceConverUtil.createHttpResponse(text, this, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals("application/octet-stream", httpResponse.getContentType().toString());
            assertEquals("forward:///sample/sampleSearchList/index", httpResponse.getContentPath().toString());
            return null;
        }));
    }

    /**
     * パスの先頭に「/」を付けていると「/index」を含まれる場合、正常に変換されること。
     */
    @SuppressWarnings("static-access")
    @Test
    public void testCreateHttpResponse_urlPattern8() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            String text = "/action/index";
            OscanaHttpResourceConverUtil sAStrutsUtil = new OscanaHttpResourceConverUtil();
            mockHttpRequest.setRequestPath("/action");
            String actionName = "actionName";

            HttpResponse httpResponse = sAStrutsUtil.createHttpResponse(text, sAStrutsUtil, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals("application/octet-stream", httpResponse.getContentType().toString());
            assertEquals("servlet:///WEB-INF/view/action/index", httpResponse.getContentPath().toString());
            return null;
        }));
    }

    /**
     * パスの先頭に「/」を付けてない場合、正常に変換されること。
     */
    @SuppressWarnings("static-access")
    @Test
    public void testCreateHttpResponse_urlPattern9() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            String text = "action/index";
            OscanaHttpResourceConverUtil sAStrutsUtil = new OscanaHttpResourceConverUtil();
            mockHttpRequest.setRequestPath("action/test");
            String actionName = "actionName";

            HttpResponse httpResponse = sAStrutsUtil.createHttpResponse(text, sAStrutsUtil, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals("application/octet-stream", httpResponse.getContentType().toString());
            assertEquals("servlet:///WEB-INF/view/action/index", httpResponse.getContentPath().toString());
            return null;
        }));
    }

    /**
     * パスの先頭に「http://」を付けていると「redirect=true」を含まれる場合、正常に変換されること。
     */
    @SuppressWarnings("static-access")
    @Test
    public void testCreateHttpResponse_urlPattern10() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            String text = "http://test?redirect=true";
            OscanaHttpResourceConverUtil sAStrutsUtil = new OscanaHttpResourceConverUtil();
            String actionName = "actionName";

            HttpResponse httpResponse = sAStrutsUtil.createHttpResponse(text, sAStrutsUtil, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals("application/octet-stream", httpResponse.getContentType().toString());
            assertEquals("redirect:http://test?redirect=true", httpResponse.getContentPath().toString());
            return null;
        }));
    }

    /**
     * パラメータobjがnullの場合、リスポンス200が返すこと。
     */
    @SuppressWarnings("static-access")
    @Test
    public void testCreateHttpResponse_obj_null() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            OscanaHttpResourceConverUtil sAStrutsUtil = new OscanaHttpResourceConverUtil();
            mockHttpRequest.setRequestPath("/action//te/st");
            String actionName = "actionName";

            HttpResponse httpResponse = sAStrutsUtil.createHttpResponse(null, sAStrutsUtil, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals(200, httpResponse.getStatusCode());
            return null;
        }));
    }

    /**
     * パラメータobjがresponseの場合、そのまま返すこと。
     */
    @SuppressWarnings("static-access")
    @Test
    public void testCreateHttpResponse_obj_response() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            OscanaHttpResourceConverUtil sAStrutsUtil = new OscanaHttpResourceConverUtil();
            mockHttpRequest.setRequestPath("/action//te/st");
            String actionName = "actionName";

            HttpResponse httpResponse = sAStrutsUtil.createHttpResponse(new HttpResponse(200), sAStrutsUtil, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals(200, httpResponse.getStatusCode());
            return null;
        }));
    }

    /**
     * パラメータobjがmapの場合、エラーがすること。
     */
    @SuppressWarnings("static-access")
    @Test
    public void testCreateHttpResponse_obj_map() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            OscanaHttpResourceConverUtil sAStrutsUtil = new OscanaHttpResourceConverUtil();
            mockHttpRequest.setRequestPath("/action//te/st");
            String actionName = "actionName";
            try {
                sAStrutsUtil.createHttpResponse(new HashMap<>(), sAStrutsUtil, mockHttpRequest, context,
                        actionName);
                fail();
            }catch(Exception e) {
                assertThat(true, is(e instanceof UnsupportedOperationException));
            }

            return null;
        }));
    }

    /**
     * パスの前後に「/」を付けてない場合、正常に変換されること。
     */
    @SuppressWarnings("static-access")
    @Test
    public void testCreateHttpResponse_urlPattern11() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            String text = "test";
            OscanaHttpResourceConverUtil sAStrutsUtil = new OscanaHttpResourceConverUtil();
            mockHttpRequest.setRequestPath("/action/test");
            String actionName = "actionName";

            HttpResponse httpResponse = sAStrutsUtil.createHttpResponse(text, new ActionForm(), mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals("application/octet-stream", httpResponse.getContentType().toString());
            assertEquals("servlet:///WEB-INF/view/acti/test", httpResponse.getContentPath().toString());
            return null;
        }));
    }

    /**
     * パスの前後に「/」を付けていると「/index/」を含まらない場合、正常に変換されること。
     */
    @Test
    public void testCreateHttpResponse_urlPattern12() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            String text = "/sample/sampleSearchList/";
            String actionName = "actionName";

            HttpResponse httpResponse = OscanaHttpResourceConverUtil.createHttpResponse(text, this, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals("application/octet-stream", httpResponse.getContentType().toString());
            assertEquals("forward:///sample/sampleSearchList/index", httpResponse.getContentPath().toString());
            return null;
        }));
    }

    /**
     * パスの前後に「/」を付けているとroutes.xmlにはパラメータがついているを含まれる場合、正常に変換されること。
     */
    @Test
    public void testCreateHttpResponse_urlPattern13() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            String text = "/sample/sampleSearchList/doSearch/1/";
            String actionName = "actionName";

            HttpResponse httpResponse = OscanaHttpResourceConverUtil.createHttpResponse(text, this, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals("application/octet-stream", httpResponse.getContentType().toString());
            assertEquals("forward:///sample/sampleSearchList/doSearch/1", httpResponse.getContentPath().toString());
            return null;
        }));
    }

    /**
     * パスの前後に「/」を付けているとroutes.xmlには複数パラメータがついているを含まれる場合、正常に変換されること。
     */
    @Test
    public void testCreateHttpResponse_urlPattern14() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            String text = "/sample/sampleSearchList/fwCreate/1/2/3/4/";
            String actionName = "actionName";

            HttpResponse httpResponse = OscanaHttpResourceConverUtil.createHttpResponse(text, this, mockHttpRequest, context,
                    actionName);
            assertNotNull(httpResponse);
            assertEquals("application/octet-stream", httpResponse.getContentType().toString());
            assertEquals("forward:///sample/sampleSearchList/fwCreate/1/2/3/4", httpResponse.getContentPath().toString());
            return null;
        }));
    }

    /**
     * リソースホルダーにforcedNextResponse指定されている場合、正常に変換されること。
     */
    @Test
    public void testStreamResponse() throws Exception {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            String text = "test";

            String actionName = "actionName";

            Containers.get().getComponent(HttpResourceHolder.class).setForcedNextResponse(
                    new S2NStreamResponse(new ByteArrayInputStream("abcdefg".getBytes())));

            Object rtn = OscanaHttpResourceConverUtil.createHttpResponse(text, new ActionForm(), mockHttpRequest, context, actionName);
            assertNotNull(rtn);
            assertTrue(rtn instanceof S2NStreamResponse);
            assertEquals("abcdefg", ((S2NStreamResponse) rtn).getBodyString());
            return rtn;
        }));

    }

    @Override
    protected void setClassToRegist() {
        registClassList = Arrays.asList(HttpResourceHolder.class);
    }

}