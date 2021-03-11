package oscana.s2n.servlet;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Enumeration;
import java.util.EventListener;
import java.util.Map;
import java.util.Set;

import javax.inject.Inject;
import javax.servlet.Filter;
import javax.servlet.FilterRegistration;
import javax.servlet.RequestDispatcher;
import javax.servlet.Servlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;
import javax.servlet.ServletRegistration.Dynamic;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import javax.servlet.descriptor.JspConfigDescriptor;

import nablarch.fw.dicontainer.web.RequestScoped;
import oscana.s2n.handler.HttpResourceHolder;

/**
 * ServletContextのI/Fを提供するリソースホルダクラス。<br>
 * <br>
 * NablarchのDIコンテナには以下のサーブレットリソースは直接@Injectアノテーションで注入することができないが、<br>
 * 移行元のSAStrutsではこれらを@Resourceアノテーションにより直接注入することができるという差異がある。<br>
 * ・javax.servlet.ServletContext<br>
 * ・javax.servlet.http.HttpServletResponse<br>
 * ・javax.servlet.http.HttpServletRequest<br>
 * ・javax.servlet.http.HttpSession<br>
 * <br>
 * 本機能は以下の方式により、@Resourceにてサーブレットリソースを注入しているアプリケーションを移行できるようにしたものである。<br>
 * ・@Injectで注入するものはサーブレットリソースではなく、各サーブレットリソースと同じI/Fを有するリソースホルダーとする。<br>
 * ・リソースホルダーにはサーブレットリソースへの参照を持たせることができ、アプリケーションがリソースホルダーの各メソッドを呼び出すとサーブレットリソースに移譲される仕組みである。<br>
 * ・HttpResourceHolderHandler,HttpResourceHolderUpdateHandlerがリソースホルダーにサーブレットリソースへの参照を登録する。<br>
 * <br>
 *
 * [構成]<br>
 * ■リソースホルダ<br>
 * ・oscana.s2n.handler.HttpResourceHolder - サーブレットリソースへの参照を保持するリソースホルダ<br>
 * ・oscana.s2n.servlet.HttpServletRequestHolder - HttpServletのI/Fを提供するリソースホルダ<br>
 * ・oscana.s2n.servlet.HttpServletResponseHolder - HttpServletResponseのI/Fを提供するリソースホルダ<br>
 * ・oscana.s2n.servlet.HttpSessionHolder - HttpSessionのI/Fを提供するリソースホルダ<br>
 * ・oscana.s2n.servlet.ServletContextHolder - ServletContextのI/Fを提供するリソースホルダ<br>
 * ■ハンドラ<br>
 * ・oscana.s2n.handler.HttpResourceHolderHandler- サーブレットリソースの投入、削除を行う<br>
 * ・oscana.s2n.handler.HttpResourceHolderUpdateHandler- サーブレットリソースのリフレッシュを行う<br>
 *
 * @author Fumihiko Yamamoto
 */
@RequestScoped
public class ServletContextHolder implements ServletContext {

    @Inject
    public HttpResourceHolder holder;

    /**
     * @param uripath uriパス
     * @return ServletContext
     * @see javax.servlet.ServletContext#getContext(java.lang.String)
     */
    public ServletContext getContext(String uripath) {
        return holder.getServletContext().getContext(uripath);
    }

    /**
     * @return version
     * @see javax.servlet.ServletContext#getMajorVersion()
     */
    public int getMajorVersion() {
        return holder.getServletContext().getMajorVersion();
    }

    /**
     * @return minorVersion
     * @see javax.servlet.ServletContext#getMinorVersion()
     */
    public int getMinorVersion() {
        return holder.getServletContext().getMinorVersion();
    }

    /**
     * @param file ファイル
     * @return mimeType
     * @see javax.servlet.ServletContext#getMimeType(java.lang.String)
     */
    public String getMimeType(String file) {
        return holder.getServletContext().getMimeType(file);
    }

    /**
     * @param path パス
     * @return Set
     * @see javax.servlet.ServletContext#getResourcePaths(java.lang.String)
     */
    public Set<String> getResourcePaths(String path) {
        return holder.getServletContext().getResourcePaths(path);
    }

    /**
     * @param path パス
     * @return URL
     * @throws MalformedURLException  if the pathname is not given inthe correct form
     * @see javax.servlet.ServletContext#getResource(java.lang.String)
     */
    public URL getResource(String path) throws MalformedURLException {
        return holder.getServletContext().getResource(path);
    }

    /**
     * @param path パス
     * @return 入力ストリーム
     * @see javax.servlet.ServletContext#getResourceAsStream(java.lang.String)
     */
    public InputStream getResourceAsStream(String path) {
        return holder.getServletContext().getResourceAsStream(path);
    }

    /**
     * @param path パス
     * @return RequestDispatcher
     * @see javax.servlet.ServletContext#getRequestDispatcher(java.lang.String)
     */
    public RequestDispatcher getRequestDispatcher(String path) {
        return holder.getServletContext().getRequestDispatcher(path);
    }

    /**
     * @param name パラメータ名
     * @return RequestDispatcher
     * @see javax.servlet.ServletContext#getNamedDispatcher(java.lang.String)
     */
    public RequestDispatcher getNamedDispatcher(String name) {
        return holder.getServletContext().getNamedDispatcher(name);
    }

    /**
     * @param name パラメータ名
     * @return Servlet
     * @throws ServletException ServletException
     * @deprecated
     * @see javax.servlet.ServletContext#getServlet(java.lang.String)
     */
    public Servlet getServlet(String name) throws ServletException {
        return holder.getServletContext().getServlet(name);
    }

    /**
     * @return Enumeration
     * @deprecated
     * @see javax.servlet.ServletContext#getServlets()
     */
    public Enumeration<Servlet> getServlets() {
        return holder.getServletContext().getServlets();
    }

    /**
     * @return Enumeration
     * @deprecated
     * @see javax.servlet.ServletContext#getServletNames()
     */
    public Enumeration<String> getServletNames() {
        return holder.getServletContext().getServletNames();
    }

    /**
     * @param msg メッセージ
     * @see javax.servlet.ServletContext#log(java.lang.String)
     */
    public void log(String msg) {
        holder.getServletContext().log(msg);
    }

    /**
     * @param exception 例外
     * @param msg メッセージ
     * @deprecated
     * @see javax.servlet.ServletContext#log(java.lang.Exception, java.lang.String)
     */
    public void log(Exception exception, String msg) {
        holder.getServletContext().log(exception, msg);
    }

    /**
     * @param message メッセージ
     * @param throwable throwable
     * @see javax.servlet.ServletContext#log(java.lang.String, java.lang.Throwable)
     */
    public void log(String message, Throwable throwable) {
        holder.getServletContext().log(message, throwable);
    }

    /**
     * @param path パス
     * @return パス
     * @see javax.servlet.ServletContext#getRealPath(java.lang.String)
     */
    public String getRealPath(String path) {
        return holder.getServletContext().getRealPath(path);
    }

    /**
     * @return サーバー情報
     * @see javax.servlet.ServletContext#getServerInfo()
     */
    public String getServerInfo() {
        return holder.getServletContext().getServerInfo();
    }

    /**
     * @param name パラメータ名
     * @return パラメータ
     * @see javax.servlet.ServletContext#getInitParameter(java.lang.String)
     */
    public String getInitParameter(String name) {
        return holder.getServletContext().getInitParameter(name);
    }

    /**
     * @return Enumeration
     * @see javax.servlet.ServletContext#getInitParameterNames()
     */
    public Enumeration<String> getInitParameterNames() {
        return holder.getServletContext().getInitParameterNames();
    }

    /**
     * @param name パラメータ名
     * @return オブジェクト
     * @see javax.servlet.ServletContext#getAttribute(java.lang.String)
     */
    public Object getAttribute(String name) {
        return holder.getServletContext().getAttribute(name);
    }

    /**
     * @return Enumeration
     * @see javax.servlet.ServletContext#getAttributeNames()
     */
    public Enumeration<String> getAttributeNames() {
        return holder.getServletContext().getAttributeNames();
    }

    /**
     * @param name パラメータ名
     * @param object オブジェクト
     * @see javax.servlet.ServletContext#setAttribute(java.lang.String, java.lang.Object)
     */
    public void setAttribute(String name, Object object) {
        holder.getServletContext().setAttribute(name, object);
    }

    /**
     * @param name パラメータ名
     * @see javax.servlet.ServletContext#removeAttribute(java.lang.String)
     */
    public void removeAttribute(String name) {
        holder.getServletContext().removeAttribute(name);
    }

    /**
     * @return コンテキスト名
     * @see javax.servlet.ServletContext#getServletContextName()
     */
    public String getServletContextName() {
        return holder.getServletContext().getServletContextName();
    }

    /**
     * ServletContext().getContextPathに移譲する。
     */
    public String getContextPath() {
        return holder.getServletContext().getContextPath();
    }

    /**
     * ServletContext().getEffectiveMajorVersionに移譲する。
     */
    public int getEffectiveMajorVersion() {
        return holder.getServletContext().getEffectiveMajorVersion();
    }

    /**
     * ServletContext().getEffectiveMinorVersionに移譲する。
     */
    public int getEffectiveMinorVersion() {
        return holder.getServletContext().getEffectiveMinorVersion();
    }

    /**
     * ServletContext().setInitParameterに移譲する。
     */
    public boolean setInitParameter(String name, String value) {
        return holder.getServletContext().setInitParameter(name, value);
    }

    /**
     * ServletContext().addServletに移譲する。
     */
    public Dynamic addServlet(String servletName, String className) {
        return holder.getServletContext().addServlet(servletName, className);
    }

    /**
     * ServletContext().addServletに移譲する。
     */
    public Dynamic addServlet(String servletName, Servlet servlet) {
        return holder.getServletContext().addServlet(servletName, servlet);
    }

    /**
     * ServletContext().addServletに移譲する。
     */
    public Dynamic addServlet(String servletName, Class<? extends Servlet> servletClass) {
        return holder.getServletContext().addServlet(servletName, servletClass);
    }

    /**
     * ServletContext().createServletに移譲する。
     */
    public <T extends Servlet> T createServlet(Class<T> clazz) throws ServletException {
        return holder.getServletContext().createServlet(clazz);
    }

    /**
     * ServletContext().getServletRegistrationに移譲する。
     */
    public ServletRegistration getServletRegistration(String servletName) {
        return holder.getServletContext().getServletRegistration(servletName);
    }

    /**
     * ServletContext().getServletRegistrationsに移譲する。
     */
    public Map<String, ? extends ServletRegistration> getServletRegistrations() {
        return holder.getServletContext().getServletRegistrations();
    }

    /**
     * ServletContext().addFilterに移譲する。
     */
    public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, String className) {
        return holder.getServletContext().addFilter(filterName, className);
    }

    /**
     * ServletContext().addFilterに移譲する。
     */
    public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, Filter filter) {
        return holder.getServletContext().addFilter(filterName, filter);
    }

    /**
     * ServletContext().addFilterに移譲する。
     */
    public javax.servlet.FilterRegistration.Dynamic addFilter(String filterName, Class<? extends Filter> filterClass) {
        return holder.getServletContext().addFilter(filterName, filterClass);
    }

    /**
     * ServletContext().createFilterに移譲する。
     */
    public <T extends Filter> T createFilter(Class<T> clazz) throws ServletException {
        return holder.getServletContext().createFilter(clazz);
    }

    /**
     * ServletContext().getFilterRegistrationに移譲する。
     */
    public FilterRegistration getFilterRegistration(String filterName) {
        return holder.getServletContext().getFilterRegistration(filterName);
    }

    /**
     * ServletContext().getFilterRegistrationsに移譲する。
     */
    public Map<String, ? extends FilterRegistration> getFilterRegistrations() {
        return holder.getServletContext().getFilterRegistrations();
    }

    /**
     * ServletContext().getSessionCookieConfigに移譲する。
     */
    public SessionCookieConfig getSessionCookieConfig() {
        return holder.getServletContext().getSessionCookieConfig();
    }

    /**
     * ServletContext().setSessionTrackingModesに移譲する。
     */
    public void setSessionTrackingModes(Set<SessionTrackingMode> sessionTrackingModes) {
        holder.getServletContext().setSessionTrackingModes(sessionTrackingModes);
    }

    /**
     * ServletContext().getDefaultSessionTrackingModesに移譲する。
     */
    public Set<SessionTrackingMode> getDefaultSessionTrackingModes() {
        return holder.getServletContext().getDefaultSessionTrackingModes();
    }

    /**
     * ServletContext().getEffectiveSessionTrackingModesに移譲する。
     */
    public Set<SessionTrackingMode> getEffectiveSessionTrackingModes() {
        return holder.getServletContext().getEffectiveSessionTrackingModes();
    }

    /**
     * ServletContext().addListenerに移譲する。
     */
    public void addListener(String className) {
        holder.getServletContext().addListener(className);
    }

    /**
     * ServletContext().addListenerに移譲する。
     */
    public <T extends EventListener> void addListener(T t) {
        holder.getServletContext().addListener(t);
    }

    /**
     * ServletContext().addListenerに移譲する。
     */
    public void addListener(Class<? extends EventListener> listenerClass) {
        holder.getServletContext().addListener(listenerClass);
    }

    /**
     * ServletContext().createListenerに移譲する。
     */
    public <T extends EventListener> T createListener(Class<T> clazz) throws ServletException {
        return holder.getServletContext().createListener(clazz);
    }

    /**
     * ServletContext().getJspConfigDescriptorに移譲する。
     */
    public JspConfigDescriptor getJspConfigDescriptor() {
        return holder.getServletContext().getJspConfigDescriptor();
    }

    /**
     * ServletContext().getClassLoaderに移譲する。
     */
    public ClassLoader getClassLoader() {
        return holder.getServletContext().getClassLoader();
    }

    /**
     * ServletContext().declareRolesに移譲する。
     */
    public void declareRoles(String... roleNames) {
        holder.getServletContext().declareRoles(roleNames);
    }

    /**
     * ServletContext().getVirtualServerNameに移譲する。
     */
    public String getVirtualServerName() {
        return holder.getServletContext().getVirtualServerName();
    }
}
