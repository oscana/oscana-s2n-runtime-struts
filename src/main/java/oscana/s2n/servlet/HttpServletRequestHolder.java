package oscana.s2n.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.Collection;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.AsyncContext;
import javax.servlet.DispatcherType;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletInputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpUpgradeHandler;
import javax.servlet.http.Part;

import nablarch.fw.dicontainer.web.RequestScoped;
import oscana.s2n.handler.HttpResourceHolder;

/**
 * HttpServletのI/Fを提供するリソースホルダクラス。<br>
 * <br>
 * NablarchのDIコンテナには以下のサーブレットリソースは直接@Injectアノテーションで注入することができない。<br>
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
public class HttpServletRequestHolder implements HttpServletRequest {

    /**
     * HttpResourceHolder(DI用)
     */
    @Inject
    public HttpResourceHolder holder;

    /**
     * 属性を取得する。
     * @param name 名
     * @return オブジェクト
     */
    public Object getAttribute(String name) {
        return holder.getHttpServletRequest().getAttribute(name);
    }

    /**
     * AuthTypeを取得する。
     * @return AuthType
     */
    public String getAuthType() {
        return holder.getHttpServletRequest().getAuthType();
    }

    /**
     * クッキーを取得する。
     *
     * @return クッキー配列
     */
    public Cookie[] getCookies() {
        return holder.getHttpServletRequest().getCookies();
    }

    /**
     * 属性名に含まれるEnumerationオブジェクトを取得する。
     * @return Enumerationオブジェクト
     */
    public Enumeration<String> getAttributeNames() {
        return holder.getHttpServletRequest().getAttributeNames();
    }

    /**
     * 日付のヘッダーを取得する。
     * @param name パラメータ名
     * @return 結果
     */
    public long getDateHeader(String name) {
        return holder.getHttpServletRequest().getDateHeader(name);
    }

    /**
     * エンコーディングを取得する。
     * @return エンコーディング
     */
    public String getCharacterEncoding() {
        return holder.getHttpServletRequest().getCharacterEncoding();
    }

    /**
     * エンコーディングを設定する。
     * @param env エンコーディング
     * @throws UnsupportedEncodingException UnsupportedEncodingException例外
     */
    public void setCharacterEncoding(String env) throws UnsupportedEncodingException {
        holder.getHttpServletRequest().setCharacterEncoding(env);
    }

    /**
     * ヘッダーを取得する。
     *
     * @param name パラメータ名
     * @return ヘッダー
     */
    public String getHeader(String name) {
        return holder.getHttpServletRequest().getHeader(name);
    }

    /**
     * コンテンツの長さを取得する。
     *
     * @return コンテンツの長さ
     */
    public int getContentLength() {
        return holder.getHttpServletRequest().getContentLength();
    }

    /**
     * コンテンツのタイプを取得する。
     *
     * @return コンテンツのタイプ
     */
    public String getContentType() {
        return holder.getHttpServletRequest().getContentType();
    }

    /**
     * ヘッダーに含まれるEnumerationを取得する。
     * @param name パラメータ名
     * @return Enumeration
     */
    public Enumeration<String> getHeaders(String name) {
        return holder.getHttpServletRequest().getHeaders(name);
    }

    /**
     * 入力ストリームを取得する。
     * @return 入力ストリーム
     */
    public ServletInputStream getInputStream() throws IOException {
        return holder.getHttpServletRequest().getInputStream();
    }

    /**
     * パラメータを取得する。
     *
     * @param name 名
     * @return パラメータ
     */
    public String getParameter(String name) {
        return holder.getHttpServletRequest().getParameter(name);
    }

    /**
     * Enumerationを取得する。
     * @return Enumeration
     */
    public Enumeration<String> getHeaderNames() {
        return holder.getHttpServletRequest().getHeaderNames();
    }

    /**
     * int型のヘッダー値を取得する。
     * @param name 名
     * @return int型のヘッダー値
     */
    public int getIntHeader(String name) {
        return holder.getHttpServletRequest().getIntHeader(name);
    }

    /**
     * Enumerationを取得する。
     * @return Enumeration
     */
    public Enumeration<String> getParameterNames() {
        return holder.getHttpServletRequest().getParameterNames();
    }

    /**
     * メソッドを取得する。
     * @return メソッド
     */
    public String getMethod() {
        return holder.getHttpServletRequest().getMethod();
    }

    /**
     * パラメータの値を取得する。
     * @param name 名
     * @return パラメータの値
     */
    public String[] getParameterValues(String name) {
        return holder.getHttpServletRequest().getParameterValues(name);
    }

    /**
     * パス情報を取得する。
     * @return パス情報
     */
    public String getPathInfo() {
        return holder.getHttpServletRequest().getPathInfo();
    }

    /**
     * マップ型のパラメータを取得する。
     *
     * @return マップ型のパラメータ
     */
    public Map<String, String[]> getParameterMap() {
        return holder.getHttpServletRequest().getParameterMap();
    }

    /**
     * パスを取得する。
     *
     * @return パスを取得する。
     */
    public String getPathTranslated() {
        return holder.getHttpServletRequest().getPathTranslated();
    }

    /**
     * プロトコルを取得する。
     *
     * @return プロトコル
     */
    public String getProtocol() {
        return holder.getHttpServletRequest().getProtocol();
    }

    /**
     * スキーマを取得する。
     *
     * @return スキーマ
     */
    public String getScheme() {
        return holder.getHttpServletRequest().getScheme();
    }

    /**
     * コンテストパスを取得する。
     *
     * @return コンテストパス
     */
    public String getContextPath() {
        return holder.getHttpServletRequest().getContextPath();
    }

    /**
     * サーバー名を取得する。
     *
     * @return サーバー名
     */
    public String getServerName() {
        return holder.getHttpServletRequest().getServerName();
    }

    /**
     * QueryStringを取得する。
     *
     * @return 文字列
     */
    public String getQueryString() {
        return holder.getHttpServletRequest().getQueryString();
    }

    /**
     * サーバーポートを取得する。
     *
     * @return サーバーポート
     */
    public int getServerPort() {
        return holder.getHttpServletRequest().getServerPort();
    }

    /**
     * Readerを取得する。
     *
     * @return Reader
     */
    public BufferedReader getReader() throws IOException {
        return holder.getHttpServletRequest().getReader();
    }

    /**
     * リモートユーザを取得する。
     *
     * @return リモートユーザ
     */
    public String getRemoteUser() {
        return holder.getHttpServletRequest().getRemoteUser();
    }

    /**
     * ユーザかどうかを判定する。
     *
     * @param role 役割
     * @return ユーザかどうか
     */
    public boolean isUserInRole(String role) {
        return holder.getHttpServletRequest().isUserInRole(role);
    }

    /**
     * リモートアドレスを取得する。
     *
     * @return リモートアドレス
     */
    public String getRemoteAddr() {
        return holder.getHttpServletRequest().getRemoteAddr();
    }

    /**
     * リモートホストを取得する。
     *
     * @return リモートホスト
     */
    public String getRemoteHost() {
        return holder.getHttpServletRequest().getRemoteHost();
    }

    /**
     * ユーザPrincipalを取得する。
     *
     *
     * @return ユーザPrincipal
     */
    public Principal getUserPrincipal() {
        return holder.getHttpServletRequest().getUserPrincipal();
    }

    /**
     * リクエストセッションidを取得する。
     *
     * @return リクエストセッションid
     */
    public String getRequestedSessionId() {
        return holder.getHttpServletRequest().getRequestedSessionId();
    }

    /**
     * パラメータを設定する。
     * @param name パラメータ名
     * @param o オブジェクト
     */
    public void setAttribute(String name, Object o) {
        holder.getHttpServletRequest().setAttribute(name, o);
    }

    /**
     * リクエストURIを取得する。
     *
     * @return リクエストURI`
     */
    public String getRequestURI() {
        return holder.getHttpServletRequest().getRequestURI();
    }

    /**
     * パラメータを削除する。
     * @param name パラメータ名
     */
    public void removeAttribute(String name) {
        holder.getHttpServletRequest().removeAttribute(name);
    }

    /**
     * ロケールを取得する。
     *
     * @return ロケール
     */
    public Locale getLocale() {
        return holder.getHttpServletRequest().getLocale();
    }

    /**
     * HttpServletRequest().getRequestURLに移譲する。
     */
    public StringBuffer getRequestURL() {
        return holder.getHttpServletRequest().getRequestURL();
    }

    /**
     * HttpServletRequest().getLocalesに移譲する。
     */
    public Enumeration<Locale> getLocales() {
        return holder.getHttpServletRequest().getLocales();
    }

    /**
     * HttpServletRequest().getServletPathに移譲する。
     */
    public String getServletPath() {
        return holder.getCurrentRequestId();
    }

    /**
     * HttpServletRequest().isSecureに移譲する。
     */
    public boolean isSecure() {
        return holder.getHttpServletRequest().isSecure();
    }

    /**
     * HttpServletRequest().getRequestDispatcherに移譲する。
     */
    public RequestDispatcher getRequestDispatcher(String path) {
        return holder.getHttpServletRequest().getRequestDispatcher(path);
    }

    /**
     * HttpServletRequest().getSessionに移譲する。
     */
    public HttpSession getSession(boolean create) {
        return holder.getHttpServletRequest().getSession(create);
    }

    /**
     * HttpServletRequest().getSessionに移譲する。
     */
    public HttpSession getSession() {
        return holder.getHttpServletRequest().getSession();
    }

    /**
     * HttpServletRequest().getRealPathに移譲する。
     */
    @SuppressWarnings("deprecation")
    public String getRealPath(String path) {
        return holder.getHttpServletRequest().getRealPath(path);
    }

    /**
     * HttpServletRequest().getRemotePortに移譲する。
     */
    public int getRemotePort() {
        return holder.getHttpServletRequest().getRemotePort();
    }

    /**
     * HttpServletRequest().isRequestedSessionIdValidに移譲する。
     */
    public boolean isRequestedSessionIdValid() {
        return holder.getHttpServletRequest().isRequestedSessionIdValid();
    }

    /**
     * HttpServletRequest().getLocalNameに移譲する。
     */
    public String getLocalName() {
        return holder.getHttpServletRequest().getLocalName();
    }

    /**
     * HttpServletRequest().isRequestedSessionIdFromCookieに移譲する。
     */
    public boolean isRequestedSessionIdFromCookie() {
        return holder.getHttpServletRequest().isRequestedSessionIdFromCookie();
    }

    /**
     * HttpServletRequest().getLocalAddrに移譲する。
     */
    public String getLocalAddr() {
        return holder.getHttpServletRequest().getLocalAddr();
    }

    /**
     * HttpServletRequest().isRequestedSessionIdFromURLに移譲する。
     */
    public boolean isRequestedSessionIdFromURL() {
        return holder.getHttpServletRequest().isRequestedSessionIdFromURL();
    }

    /**
     * HttpServletRequest().getLocalPortに移譲する。
     */
    public int getLocalPort() {
        return holder.getHttpServletRequest().getLocalPort();
    }

    /**
     * HttpServletRequest().isRequestedSessionIdFromUrlに移譲する。
     */
    @SuppressWarnings("deprecation")
    public boolean isRequestedSessionIdFromUrl() {
        return holder.getHttpServletRequest().isRequestedSessionIdFromUrl();
    }

    /**
     * HttpServletRequest().getContentLengthLongに移譲する。
     */
    public long getContentLengthLong() {
        return holder.getHttpServletRequest().getContentLengthLong();
    }

    /**
     * HttpServletRequest().getServletContextに移譲する。
     */
    public ServletContext getServletContext() {
        return holder.getHttpServletRequest().getServletContext();
    }

    /**
     * HttpServletRequest().startAsyncに移譲する。
     */
    public AsyncContext startAsync() throws IllegalStateException {
        return holder.getHttpServletRequest().startAsync();
    }

    /**
     * HttpServletRequest().startAsyncに移譲する。
     */
    public AsyncContext startAsync(ServletRequest servletRequest, ServletResponse servletResponse)
            throws IllegalStateException {
        return holder.getHttpServletRequest().startAsync(servletRequest, servletResponse);
    }

    /**
     * HttpServletRequest().isAsyncStartedに移譲する。
     */
    public boolean isAsyncStarted() {
        return holder.getHttpServletRequest().isAsyncStarted();
    }

    /**
     * HttpServletRequest().isAsyncSupportedに移譲する。
     */
    public boolean isAsyncSupported() {
        return holder.getHttpServletRequest().isAsyncSupported();
    }

    /**
     * HttpServletRequest().getAsyncContextに移譲する。
     */
    public AsyncContext getAsyncContext() {
        return holder.getHttpServletRequest().getAsyncContext();
    }

    /**
     * HttpServletRequest().getDispatcherTypeに移譲する。
     */
    public DispatcherType getDispatcherType() {
        return holder.getHttpServletRequest().getDispatcherType();
    }

    /**
     * HttpServletRequest().changeSessionIdに移譲する。
     */
    public String changeSessionId() {
        return holder.getHttpServletRequest().changeSessionId();
    }

    /**
     * HttpServletRequest().authenticateに移譲する。
     */
    public boolean authenticate(HttpServletResponse response) throws IOException, ServletException {
        return holder.getHttpServletRequest().authenticate(response);
    }

    /**
     * HttpServletRequest().loginに移譲する。
     */
    public void login(String username, String password) throws ServletException {
        holder.getHttpServletRequest().login(username, password);
    }

    /**
     * HttpServletRequest().logoutに移譲する。
     */
    public void logout() throws ServletException {
        holder.getHttpServletRequest().logout();
    }

    /**
     * HttpServletRequest().getPartsに移譲する。
     */
    public Collection<Part> getParts() throws IOException, ServletException {
        return holder.getHttpServletRequest().getParts();
    }

    /**
     * HttpServletRequest().getPartに移譲する。
     */
    public Part getPart(String name) throws IOException, ServletException {
        return holder.getHttpServletRequest().getPart(name);
    }

    /**
     * HttpServletRequest().upgradeに移譲する。
     */
    public <T extends HttpUpgradeHandler> T upgrade(Class<T> handlerClass) throws IOException, ServletException {
        return holder.getHttpServletRequest().upgrade(handlerClass);
    }

}
