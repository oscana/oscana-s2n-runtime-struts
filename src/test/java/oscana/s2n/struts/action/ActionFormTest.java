package oscana.s2n.struts.action;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.junit.Test;

import oscana.s2n.handler.HttpResourceHolder;
import oscana.s2n.handler.HttpResourceHolderHandler;
import oscana.s2n.testCommon.S2NBaseTest;
import oscana.s2n.testLibrary.utill.RequestUtil;

/**
 * {@link ActionForm}のテスト。
 *
 */
public class ActionFormTest extends S2NBaseTest {

    /**
     * HttpServletRequestの場合、nullを返却すること
     */
    @Test
    public void testValidate() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(),(data, context) -> {
            ActionForm actionForm = new ActionForm();
            Map<String, ActionForward> forwardMap = new HashMap<String, ActionForward>();
            ActionMapping mapping = new ActionMapping(forwardMap);

            assertNull(actionForm.validate(mapping, RequestUtil.getRequest()));
            return null;
        }));
    }

    /**
     * ServletRequestの場合、nullを返却すること
     */
    @Test
    public void testValidate_throwException() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(),(data, context) -> {
            ActionForm actionForm = new ActionForm();
            Map<String, ActionForward> forwardMap = new HashMap<String, ActionForward>();
            ActionMapping mapping = new ActionMapping(forwardMap);

            S2NServletRequest request = new S2NServletRequest();
            assertNull(actionForm.validate(mapping, request));
            return null;
        }));
    }

    /**
     * HttpServletRequestの場合、リセットのテスト
     */
    @Test
    public void testReset() {
        this.handle(Arrays.asList(new HttpResourceHolderHandler(),(data, context) -> {
            ActionForm actionForm = new ActionForm();
            Map<String, ActionForward> forwardMap = new HashMap<String, ActionForward>();
            ActionMapping mapping = new ActionMapping(forwardMap);

            actionForm.reset(mapping, RequestUtil.getRequest());
            return null;
        }));
    }

    /**
     * ServletRequestの場合、リセットのテスト
     */
    @Test
    public void testReset_throwException() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(),(data, context) -> {
            ActionForm actionForm = new ActionForm();
            Map<String, ActionForward> forwardMap = new HashMap<String, ActionForward>();
            ActionMapping mapping = new ActionMapping(forwardMap);

            S2NServletRequest request = new S2NServletRequest();
            actionForm.reset(mapping, request);
            return null;
        }));
    }

    /**
     * HttpServletRequestの場合、リセットのテスト
     */
    @Test
    public void testReset_withoutParam() {
        this.handle(Arrays.asList(new HttpResourceHolderHandler(),(data, context) -> {
            ActionForm actionForm = new ActionForm();
            actionForm.reset();
            return null;
        }));
    }

    /**
     * テスト用クラス。
     *
     */
    public class S2NServletRequest implements ServletRequest {

        @Override
        public Object getAttribute(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getAttributeNames() {
            return null;
        }

        @Override
        public String getCharacterEncoding() {
            return null;
        }

        @Override
        public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        }

        @Override
        public int getContentLength() {
            return 0;
        }

        @Override
        public long getContentLengthLong() {
            return 0;
        }

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public ServletInputStream getInputStream() throws IOException {
            return null;
        }

        @Override
        public String getParameter(String name) {
            return null;
        }

        @Override
        public Enumeration<String> getParameterNames() {
            return null;
        }

        @Override
        public String[] getParameterValues(String name) {
            return null;
        }

        @Override
        public Map<String, String[]> getParameterMap() {
            return null;
        }

        @Override
        public String getProtocol() {
            return null;
        }

        @Override
        public String getScheme() {
            return null;
        }

        @Override
        public String getServerName() {
            return null;
        }

        @Override
        public int getServerPort() {
            return 0;
        }

        @Override
        public BufferedReader getReader() throws IOException {
            return null;
        }

        @Override
        public String getRemoteAddr() {
            return null;
        }

        @Override
        public String getRemoteHost() {
            return null;
        }

        @Override
        public void setAttribute(String name, Object o) {
        }

        @Override
        public void removeAttribute(String name) {
        }

        @Override
        public Locale getLocale() {
            return null;
        }

        @Override
        public Enumeration<Locale> getLocales() {
            return null;
        }

        @Override
        public boolean isSecure() {
            return false;
        }

        @Override
        public RequestDispatcher getRequestDispatcher(String path) {
            return null;
        }

        @Override
        public String getRealPath(String path) {
            return null;
        }

        @Override
        public int getRemotePort() {
            return 0;
        }

        @Override
        public String getLocalName() {
            return null;
        }

        @Override
        public String getLocalAddr() {
            return null;
        }

        @Override
        public int getLocalPort() {
            return 0;
        }

        @Override
        public ServletContext getServletContext() {
            return null;
        }

        @Override
        public AsyncContext startAsync() throws IllegalStateException {
            return null;
        }

        @Override
        public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
                throws IllegalStateException {
            return null;
        }

        @Override
        public boolean isAsyncStarted() {
            return false;
        }

        @Override
        public boolean isAsyncSupported() {
            return false;
        }

        @Override
        public AsyncContext getAsyncContext() {
            return null;
        }

        @Override
        public DispatcherType getDispatcherType() {
            return null;
        }}

    @Override
    protected void setClassToRegist() {
        registClassList = Arrays.asList(HttpResourceHolder.class);
    }
}