package oscana.s2n.servlet;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import mockit.Expectations;
import mockit.Verifications;
import nablarch.fw.dicontainer.nablarch.Containers;
import oscana.s2n.handler.HttpResourceHolder;
import oscana.s2n.handler.HttpResourceHolderHandler;
import oscana.s2n.testCommon.S2NBaseTest;

/**
 * {@link ServletContextHolder}のテスト。
 *
 */
public class ServletContextHolderTest extends S2NBaseTest {

    /**
     * コンテキストに関するテスト
     * @throws MalformedURLException
     * @throws FileNotFoundException
     */
    @Test
    public void testHttpServletRequestHolder() throws MalformedURLException, FileNotFoundException {
        Set<String> resourcePaths = new HashSet<String>();
        URL url = new URL("http://test");
        InputStream is = System.in;

        Enumeration<String> initParameterNames = Collections.enumeration(new ArrayList<String>());
        Enumeration<String> attributeNames = Collections.enumeration(new ArrayList<String>());

        Set<Object> defaultSessionTrackingModes = new HashSet<Object>();
        Set<Object> effectiveSessionTrackingModes = new HashSet<Object>();

        new Expectations() {{
            servletContext.getContext(anyString);
            result = servletContext;

            servletContext.getMajorVersion();
            result = 3;

            servletContext.getMinorVersion();
            result = 1;

            servletContext.getMimeType(anyString);
            result = "mimeType";

            servletContext.getResourcePaths(anyString);
            result = resourcePaths;

            servletContext.getResource(anyString);
            result = url;

            servletContext.getResourceAsStream(anyString);
            result = is;

            servletContext.getRealPath(anyString);
            result = "/";

            servletContext.getServerInfo();
            result = "server";

            servletContext.getInitParameterNames();
            result = initParameterNames;

            servletContext.getAttributeNames();
            result = attributeNames;

            servletContext.getServletContextName();
            result = "test";

            servletContext.getInitParameter(anyString);
            result = "param1";

            servletContext.getAttribute(anyString);
            result = "value";

            servletContext.getContextPath();
            result = "test1";

            servletContext.getEffectiveMajorVersion();
            result = 2;

            servletContext.getEffectiveMinorVersion();
            result = 5;

            servletContext.getDefaultSessionTrackingModes();
            result = defaultSessionTrackingModes;

            servletContext.getEffectiveSessionTrackingModes();
            result = effectiveSessionTrackingModes;

            servletContext.getVirtualServerName();
            result = "server";
        }};
        setExecutionContext();

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            ServletContextHolder servletContextHolder = new ServletContextHolder();
            servletContextHolder.holder = Containers.get().getComponent(HttpResourceHolder.class);

            assertEquals(servletContext, servletContextHolder.getContext("test"));
            assertEquals(3, servletContextHolder.getMajorVersion());
            assertEquals(1, servletContextHolder.getMinorVersion());
            assertEquals("mimeType", servletContextHolder.getMimeType("test"));
            assertEquals(resourcePaths, servletContextHolder.getResourcePaths("/"));
            try {
                assertEquals(url, servletContextHolder.getResource("/"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            assertEquals(is, servletContextHolder.getResourceAsStream("/"));
            assertEquals(servletContext.getRequestDispatcher("/"), servletContextHolder.getRequestDispatcher("/"));
            assertEquals(servletContext.getNamedDispatcher("test"), servletContextHolder.getNamedDispatcher("test"));
            assertEquals("/",servletContextHolder.getRealPath("/"));
            assertEquals("server", servletContextHolder.getServerInfo());
            assertEquals(initParameterNames, servletContextHolder.getInitParameterNames());
            assertEquals(attributeNames, servletContextHolder.getAttributeNames());
            assertEquals("test", servletContextHolder.getServletContextName());

            servletContextHolder.log("test");
            new Verifications() {
                {
                    servletContext.log(anyString);
                    times = 1;
                }
            };
            servletContextHolder.setAttribute("test", "value");
            new Verifications() {
                {
                    servletContext.setAttribute(anyString, any);
                    times = 1;
                }
            };
            assertEquals("param1", servletContextHolder.getInitParameter("key"));
            assertEquals("value", servletContextHolder.getAttribute("test"));
            assertEquals("test1", servletContextHolder.getContextPath());
            assertEquals(2, servletContextHolder.getEffectiveMajorVersion());
            assertEquals(5, servletContextHolder.getEffectiveMinorVersion());
            assertEquals(servletContext.getServletRegistration("test"), servletContextHolder.getServletRegistration("test"));
            assertEquals(servletContext.getServletRegistrations(), servletContextHolder.getServletRegistrations());
            assertEquals(servletContext.getFilterRegistration("test"), servletContextHolder.getFilterRegistration("test"));
            assertEquals(servletContext.getFilterRegistrations(), servletContextHolder.getFilterRegistrations());
            assertEquals(servletContext.getSessionCookieConfig(), servletContextHolder.getSessionCookieConfig());
            assertEquals(defaultSessionTrackingModes, servletContextHolder.getDefaultSessionTrackingModes());
            assertEquals(effectiveSessionTrackingModes, servletContextHolder.getEffectiveSessionTrackingModes());
            assertEquals(servletContext.getJspConfigDescriptor(), servletContextHolder.getJspConfigDescriptor());
            assertEquals(servletContext.getClassLoader(), servletContextHolder.getClassLoader());
            assertEquals("server", servletContextHolder.getVirtualServerName());

            servletContextHolder.removeAttribute("test");
            new Verifications() {
                {
                    servletContext.removeAttribute(anyString);
                    times = 1;
                }
            };

            return null;
        }));
    }

    @Override
    protected void setClassToRegist() {
        registClassList = Arrays.asList(HttpResourceHolder.class);
    }

}