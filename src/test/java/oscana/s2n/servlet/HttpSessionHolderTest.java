package oscana.s2n.servlet;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.junit.Test;

import mockit.Expectations;
import mockit.Verifications;
import nablarch.fw.dicontainer.nablarch.Containers;
import oscana.s2n.handler.HttpResourceHolder;
import oscana.s2n.handler.HttpResourceHolderHandler;
import oscana.s2n.testCommon.S2NBaseTest;

/**
 * {@link HttpSessionHolder}のテスト。
 *
 */
public class HttpSessionHolderTest extends S2NBaseTest {

    /**
     * セッションに関するテスト
     */
    @SuppressWarnings("deprecation")
    @Test
    public void testHttpServletRequestHolder() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {

            List<String> attributeMap = new ArrayList<String>();
            Enumeration<String> attributeNames = Collections.enumeration(attributeMap);

            String[] valueNames = new String[] {"a","b"};

            HttpSessionHolder httpSessionHolder = new HttpSessionHolder();
            httpSessionHolder.holder = Containers.get().getComponent(HttpResourceHolder.class);

            HttpSession httpSession = httpSessionHolder.holder.getHttpSession();
            new Expectations() {{

                httpSession.getAttribute(anyString);
                result = "value";

                httpSession.getCreationTime();
                result = 100L;

                httpSession.getId();
                result = "id";

                httpSession.getLastAccessedTime();
                result = 10L;

                httpSession.getServletContext();
                result = servletContext;

                httpSession.getMaxInactiveInterval();
                result = 100;

                httpSession.getValue(anyString);
                result = "12345";

                httpSession.getAttributeNames();
                result = attributeNames;

                httpSession.getValueNames();
                result = valueNames;

                httpSession.isNew();
                result = true;

            }};
            setExecutionContext();

            httpSessionHolder.setMaxInactiveInterval(100);
            new Verifications() {
                {
                    httpSession.setMaxInactiveInterval(anyInt);
                    times = 1;
                }
            };

            httpSessionHolder.setAttribute("test", "value");
            new Verifications() {
                {
                    httpSession.setAttribute(anyString, any);
                    times = 1;
                }
            };

            httpSessionHolder.putValue("key", "12345");
            new Verifications() {
                {
                    httpSession.putValue(anyString, any);
                    times = 1;
                }
            };

            assertEquals("value", httpSessionHolder.getAttribute("test"));
            assertEquals(100L,httpSessionHolder.getCreationTime());
            assertEquals("id", httpSessionHolder.getId());
            assertEquals(10L,httpSessionHolder.getLastAccessedTime());
            assertEquals(servletContext, httpSessionHolder.getServletContext());
            assertEquals(100, httpSessionHolder.getMaxInactiveInterval());
            assertEquals("12345", httpSessionHolder.getValue("key"));
            assertEquals(attributeNames,httpSessionHolder.getAttributeNames());
            assertArrayEquals(valueNames, httpSessionHolder.getValueNames());
            assertTrue(httpSessionHolder.isNew());

            httpSessionHolder.removeAttribute("test");
            new Verifications() {
                {
                    httpSession.removeAttribute(anyString);
                    times = 1;
                }
            };

            httpSessionHolder.removeValue("key");
            new Verifications() {
                {
                    httpSession.removeValue(anyString);
                    times = 1;
                }
            };

            httpSessionHolder.invalidate();
            new Verifications() {
                {
                    httpSession.invalidate();
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