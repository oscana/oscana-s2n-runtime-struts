package oscana.s2n.servlet;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.Locale;

import javax.servlet.http.Cookie;

import org.junit.Test;

import mockit.Expectations;
import mockit.Verifications;
import nablarch.fw.dicontainer.nablarch.Containers;
import oscana.s2n.handler.HttpResourceHolder;
import oscana.s2n.handler.HttpResourceHolderHandler;
import oscana.s2n.testCommon.S2NBaseTest;

/**
 * {@link HttpServletResponseHolder}のテスト。
 *
 */
public class HttpServletResponseHolderTest extends S2NBaseTest {

    /**
     * レスポンスに関するテスト
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testHttpServletRequestHolder() {

        new Expectations() {{

            httpServletResponse.getContentType();
            result = "text/html;charset=utf-8";

            httpServletResponse.getCharacterEncoding();
            result = "UTF-8";

            httpServletResponse.getBufferSize();
            result = 1000;

            httpServletResponse.getLocale();
            result = "ja_jp";

            httpServletResponse.getStatus();
            result = 301;

            httpServletResponse.getHeader(anyString);
            result = "header";

            httpServletResponse.encodeURL(anyString);
            result = "test/1";

            httpServletResponse.encodeRedirectURL(anyString);
            result = "test/2";

            httpServletResponse.encodeUrl(anyString);
            result = "/test";

            httpServletResponse.encodeRedirectUrl(anyString);
            result="test";
        }};
        setExecutionContext();

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            HttpServletResponseHolder httpServletResponseHolder = new HttpServletResponseHolder();
            httpServletResponseHolder.holder = Containers.get().getComponent(HttpResourceHolder.class);

            Cookie cookie = new Cookie("key", "123");
            cookie.setMaxAge(1);
            cookie.setPath("/");
            httpServletResponseHolder.addCookie(cookie);
            new Verifications() {
                {
                    httpServletResponse.addCookie(cookie);
                    times = 1;
                }
            };
            httpServletResponseHolder.setCharacterEncoding("UTF-8");
            new Verifications() {
                {
                    httpServletResponse.setCharacterEncoding(anyString);
                    times = 1;
                }
            };
            httpServletResponseHolder.setContentType("text/html; charset=UTF-8");
            new Verifications() {
                {
                    httpServletResponse.setContentType(anyString);
                    times = 1;
                }
            };
            httpServletResponseHolder.setStatus(200);
            new Verifications() {
                {
                    httpServletResponse.setStatus(anyInt);
                    times = 1;
                }
            };
            httpServletResponseHolder.setStatus(301, "test");
            new Verifications() {
                {
                    httpServletResponse.setStatus(anyInt, anyString);
                    times = 1;
                }
            };
            httpServletResponseHolder.setIntHeader("Location", 1);
            new Verifications() {
                {
                    httpServletResponse.setIntHeader(anyString, anyInt);
                    times = 1;
                }
            };
            httpServletResponseHolder.addIntHeader("Location1", 2);
            new Verifications() {
                {
                    httpServletResponse.addIntHeader(anyString, anyInt);
                    times = 1;
                }
            };
            httpServletResponseHolder.setDateHeader("Exprise", 0);
            new Verifications() {
                {
                    httpServletResponse.setDateHeader(anyString, anyLong);
                    times = 1;
                }
            };
            httpServletResponseHolder.addDateHeader("Exprise2", 0);
            new Verifications() {
                {
                    httpServletResponse.addDateHeader(anyString, anyLong);
                    times = 1;
                }
            };
            httpServletResponseHolder.setHeader("Cache-Control", "no-cache");
            new Verifications() {
                {
                    httpServletResponse.setHeader(anyString, anyString);
                    times = 1;
                }
            };
            httpServletResponseHolder.addHeader("Cache-Control", "no-cache");
            new Verifications() {
                {
                    httpServletResponse.addHeader(anyString, anyString);
                    times = 1;
                }
            };
            httpServletResponseHolder.setContentLength(10000);
            new Verifications() {
                {
                    httpServletResponse.setContentLength(anyInt);
                    times = 1;
                }
            };
            httpServletResponseHolder.setContentLengthLong(10000);
            new Verifications() {
                {
                    httpServletResponse.setContentLengthLong(anyLong);
                    times = 1;
                }
            };
            httpServletResponseHolder.setBufferSize(1000);
            new Verifications() {
                {
                    httpServletResponse.setBufferSize(anyInt);
                    times = 1;
                }
            };
            assertEquals(1000, httpServletResponseHolder.getBufferSize());
            assertEquals("UTF-8", httpServletResponseHolder.getCharacterEncoding());
            assertEquals("text/html;charset=utf-8", httpServletResponseHolder.getContentType());
            assertEquals("ja_jp", httpServletResponseHolder.getLocale().toString());
            try {
                assertNotNull(httpServletResponseHolder.getOutputStream());
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            assertFalse(httpServletResponseHolder.isCommitted());
            assertEquals(301, httpServletResponseHolder.getStatus());
            assertEquals("header",httpServletResponseHolder.getHeader("Location"));
            assertNotNull(httpServletResponseHolder.getHeaders("Location"));
            assertNotNull(httpServletResponseHolder.getHeaderNames());

            httpServletResponseHolder.setLocale(new Locale("test"));
            new Verifications() {
                {
                    httpServletResponse.setLocale((Locale) any);
                    times = 1;
                }
            };

            assertFalse(httpServletResponseHolder.containsHeader("test"));

            assertEquals("test/1",httpServletResponseHolder.encodeURL("/"));
            assertEquals("test/2",httpServletResponseHolder.encodeRedirectURL("/test"));
            assertEquals("/test",httpServletResponseHolder.encodeUrl("/"));
            assertEquals("test",httpServletResponseHolder.encodeRedirectUrl("/test"));

            try {
                httpServletResponseHolder.flushBuffer();
                new Verifications() {
                    {
                        httpServletResponse.flushBuffer();
                        times = 1;
                    }
                };
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }));
    }

    @Override
    protected void setClassToRegist() {
        registClassList = Arrays.asList(HttpResourceHolder.class);
    }

}