package oscana.s2n.servlet;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.DispatcherType;

import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import nablarch.fw.dicontainer.nablarch.Containers;
import oscana.s2n.handler.HttpResourceHolder;
import oscana.s2n.handler.HttpResourceHolderHandler;
import oscana.s2n.testCommon.S2NBaseTest;

/**
 * {@link HttpServletRequestHolder}のテスト。
 *
 */
public class HttpServletRequestHolderTest extends S2NBaseTest {

    @Mocked
    Principal principal;

    /**
     * リクエストに関するテスト
     * @throws IOException
     */
    @SuppressWarnings({ "static-access", "deprecation" })
    @Test
    public void testHttpServletRequestHolder() throws IOException {

        /** テストデータ*/
        List<String> headerMap = new ArrayList<String>();
        headerMap.add("X-Forwarded-Proto");
        Enumeration<String> enumerations = Collections.enumeration(headerMap);

        String[] parameterValues =  new String[] {"a","b"};
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        parameterMap.put("test", parameterValues);

        List<String> attributeMap = new ArrayList<String>();
        Enumeration<String> attributes = Collections.enumeration(attributeMap);

        Enumeration<Locale> locales = Collections.enumeration(new ArrayList<Locale>());

        new Expectations() {{

            httpServletRequest.getAttribute("test");
            result = "123";

            httpServletRequest.getAuthType();
            result = httpServletRequest.BASIC_AUTH;

            httpServletRequest.getCookies();
            result = null;

            httpServletRequest.getAttributeNames();
            result = attributes;

            httpServletRequest.getDateHeader(anyString);
            result = 1L;

            httpServletRequest.getCharacterEncoding();
            result = "UTF-8";

            httpServletRequest.getHeader(anyString);
            result = "http";

            httpServletRequest.getContentLength();
            result = 1;

            httpServletRequest.getContentType();
            result = "text/html";

            httpServletRequest.getHeaders(anyString);
            result = enumerations;

            httpServletRequest.getHeaderNames();
            result = enumerations;

            httpServletRequest.getIntHeader(anyString);
            result = 2;

            httpServletRequest.getMethod();
            result = "GET";

            httpServletRequest.getPathInfo();
            result = "test/1";

            httpServletRequest.getParameterMap();
            result = parameterMap;

            httpServletRequest.getPathTranslated();
            result = "C:\\Program Files\\Apache Software Foundation\\Tomcat";

            httpServletRequest.getProtocol();
            result = "HTTP/1.1";

            httpServletRequest.getScheme();
            result = "http";

            httpServletRequest.getContextPath();
            result = "test/2";

            httpServletRequest.getServerName();
            result = "localhost";

            httpServletRequest.getQueryString();
            result = "abc";

            httpServletRequest.getServerPort();
            result = 80;

            httpServletRequest.getRemoteUser();
            result = "user1";

            httpServletRequest.isUserInRole(anyString);
            result = true;

            httpServletRequest.getRemoteAddr();
            result = "0.0.0.0";

            httpServletRequest.getRemoteHost();
            result = "0.0.0.0";

            httpServletRequest.getRequestedSessionId();
            result = "id123";

            httpServletRequest.getRequestURI();
            result = "test/3";

            httpServletRequest.getRequestURL();
            result = new StringBuffer("http://127.0.0.1/test/");

            httpServletRequest.getLocale();
            result = "en";

            httpServletRequest.getLocales();
            result = locales;

            httpServletRequest.isSecure();
            result = true;

            httpServletRequest.getRealPath(anyString);
            result = "realPath";

            httpServletRequest.getLocalName();
            result = "localName";

            httpServletRequest.getLocalAddr();
            result = "0.0.0.0";

            httpServletRequest.isAsyncStarted();
            result = true;

            httpServletRequest.isAsyncSupported();
            result = true;

            httpServletRequest.getDispatcherType();
            result = DispatcherType.REQUEST;

            httpServletRequest.changeSessionId();
            result = "changeSessionId";
        }};
        setExecutionContext();

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            HttpServletRequestHolder httpServletRequestHolder = new HttpServletRequestHolder();
            HttpResourceHolder holder = Containers.get().getComponent(HttpResourceHolder.class);
            httpServletRequestHolder.holder = holder;
            holder.setCurrentRequestId("/test");

            httpServletRequestHolder.setAttribute("test", "123");
            new Verifications() {
                {
                    httpServletRequest.setAttribute(anyString, any);
                    times = 1;
                }
            };

            try {
                httpServletRequestHolder.setCharacterEncoding("UTF-8");
                new Verifications() {
                    {
                        httpServletRequest.setCharacterEncoding(anyString);
                        times = 1;
                    }
                };
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }

            assertEquals("123", httpServletRequestHolder.getAttribute("test"));
            assertEquals(httpServletRequest.BASIC_AUTH,httpServletRequestHolder.getAuthType());
            assertNull(httpServletRequestHolder.getCookies());
            assertNotNull(httpServletRequestHolder.getAttributeNames());
            assertEquals(1L, httpServletRequestHolder.getDateHeader("test"));
            assertEquals("UTF-8", httpServletRequestHolder.getCharacterEncoding());
            assertEquals("http",httpServletRequestHolder.getHeader("X-Forwarded-Proto"));
            assertEquals(1, httpServletRequestHolder.getContentLength());
            assertEquals("text/html",httpServletRequestHolder.getContentType());
            assertNotNull(httpServletRequestHolder.getHeaders("test"));
            try {
                assertNotNull(httpServletRequestHolder.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
            assertEquals("a",httpServletRequestHolder.getParameter("test"));
            assertNotNull(httpServletRequestHolder.getHeaderNames());
            assertEquals(2, httpServletRequestHolder.getIntHeader("test"));
            assertNotNull(httpServletRequestHolder.getParameterNames());
            assertEquals("GET", httpServletRequestHolder.getMethod());
            assertArrayEquals(parameterValues,httpServletRequestHolder.getParameterValues("test"));
            assertEquals("test/1",httpServletRequestHolder.getPathInfo());
            assertNotNull(httpServletRequestHolder.getParameterMap());
            assertEquals("C:\\Program Files\\Apache Software Foundation\\Tomcat",httpServletRequestHolder.getPathTranslated());
            assertEquals("HTTP/1.1", httpServletRequestHolder.getProtocol());
            assertEquals("http", httpServletRequestHolder.getScheme());
            assertEquals("test/2", httpServletRequestHolder.getContextPath());
            assertEquals("localhost", httpServletRequestHolder.getServerName());
            assertEquals("abc",httpServletRequestHolder.getQueryString());
            assertEquals(80, httpServletRequestHolder.getServerPort());
            assertEquals("user1",httpServletRequestHolder.getRemoteUser());
            assertTrue(httpServletRequestHolder.isUserInRole("test"));
            assertEquals("0.0.0.0", httpServletRequestHolder.getRemoteAddr());
            assertEquals("0.0.0.0", httpServletRequestHolder.getRemoteHost());
            assertEquals(principal,httpServletRequestHolder.getUserPrincipal());
            assertEquals("id123",httpServletRequestHolder.getRequestedSessionId());
            assertEquals("test/3", httpServletRequestHolder.getRequestURI());
            assertEquals("http://127.0.0.1/test/", httpServletRequestHolder.getRequestURL().toString());
            assertEquals("en", httpServletRequestHolder.getLocale().toString());
            assertNotNull(httpServletRequestHolder.getLocales());
            assertEquals("/test", httpServletRequestHolder.getServletPath());
            assertTrue(httpServletRequestHolder.isSecure());
            assertNotNull(httpServletRequestHolder.getRequestDispatcher("/"));
            assertNotNull(httpServletRequestHolder.getSession(false));
            assertNotNull(httpServletRequestHolder.getSession());
            assertEquals("realPath", httpServletRequestHolder.getRealPath("/"));
            assertEquals(0, httpServletRequestHolder.getRemotePort());
            assertFalse(httpServletRequestHolder.isRequestedSessionIdValid());
            assertEquals("localName", httpServletRequestHolder.getLocalName());
            assertFalse(httpServletRequestHolder.isRequestedSessionIdFromCookie());
            assertEquals("0.0.0.0", httpServletRequestHolder.getLocalAddr());
            assertFalse(httpServletRequestHolder.isRequestedSessionIdFromURL());
            assertEquals(0, httpServletRequestHolder.getLocalPort());
            assertFalse(httpServletRequestHolder.isRequestedSessionIdFromUrl());
            httpServletRequestHolder.removeAttribute("test");
            new Verifications() {
                {
                    httpServletRequest.removeAttribute(anyString);
                    times = 1;
                }
            };

            assertEquals(0, httpServletRequestHolder.getContentLengthLong());
            assertNotNull(httpServletRequestHolder.startAsync());
            assertNotNull(httpServletRequestHolder.getServletContext());
            assertTrue(httpServletRequestHolder.isAsyncStarted());
            assertTrue(httpServletRequestHolder.isAsyncSupported());
            assertNotNull(httpServletRequestHolder.getAsyncContext());
            assertEquals("REQUEST", httpServletRequestHolder.getDispatcherType().toString());
            assertEquals("changeSessionId", httpServletRequestHolder.changeSessionId());

            return null;
        }));
    }

    /**
     * startAsyncに関するテスト
     */
    @Test
    public void testHttpServletRequestHolder_startAsync() {
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            HttpServletRequestHolder httpServletRequestHolder = new HttpServletRequestHolder();
            httpServletRequestHolder.holder = Containers.get().getComponent(HttpResourceHolder.class);

            assertNotNull(httpServletRequestHolder.startAsync(httpServletRequestHolder.holder.getHttpServletRequest(),
                    httpServletRequestHolder.holder.getHttpServletResponse()));
            return null;
        }));
    }

    /**
     * getReaderに関するテスト
     */
    @Test
    public void testHttpServletRequestHolder_getReader() {
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            HttpServletRequestHolder httpServletRequestHolder = new HttpServletRequestHolder();
            httpServletRequestHolder.holder = Containers.get().getComponent(HttpResourceHolder.class);

            try {
                assertNotNull(httpServletRequestHolder.getReader());
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