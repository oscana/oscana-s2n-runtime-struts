package oscana.s2n.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.inject.Inject;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import nablarch.fw.dicontainer.web.RequestScoped;
import oscana.s2n.handler.HttpResourceHolder;

/**
 * HttpServletResponseのI/Fを提供するリソースホルダクラス。<br>
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
public class HttpServletResponseHolder implements HttpServletResponse {

    @Inject
    public HttpResourceHolder holder;

    /**
     * HttpServletResponse().addCookieに移譲する。
     */
    public void addCookie(Cookie cookie) {
        holder.getHttpServletResponse().addCookie(cookie);
    }

    /**
     * HttpServletResponse().containsHeaderに移譲する。
     */
    public boolean containsHeader(String name) {
        return holder.getHttpServletResponse().containsHeader(name);
    }

    /**
     * HttpServletResponse().encodeURLに移譲する。
     */
    public String encodeURL(String url) {
        return holder.getHttpServletResponse().encodeURL(url);
    }

    /**
     * HttpServletResponse().getCharacterEncodingに移譲する。
     */
    public String getCharacterEncoding() {
        return holder.getHttpServletResponse().getCharacterEncoding();
    }

    /**
     * HttpServletResponse().encodeRedirectURLに移譲する。
     */
    public String encodeRedirectURL(String url) {
        return holder.getHttpServletResponse().encodeRedirectURL(url);
    }

    /**
     * HttpServletResponse().getContentTypeに移譲する。
     */
    public String getContentType() {
        return holder.getHttpServletResponse().getContentType();
    }

    /**
     * HttpServletResponse().encodeUrlに移譲する。
     */
    @SuppressWarnings("deprecation")
    public String encodeUrl(String url) {
        return holder.getHttpServletResponse().encodeUrl(url);
    }

    /**
     * HttpServletResponse().encodeRedirectUrlに移譲する。
     */
    @SuppressWarnings("deprecation")
    public String encodeRedirectUrl(String url) {
        return holder.getHttpServletResponse().encodeRedirectUrl(url);
    }

    /**
     * HttpServletResponse().getOutputStreamに移譲する。
     */
    public ServletOutputStream getOutputStream() throws IOException {
        return holder.getHttpServletResponse().getOutputStream();
    }

    /**
     * HttpServletResponse().sendErrorに移譲する。
     */
    public void sendError(int sc, String msg) throws IOException {
        holder.getHttpServletResponse().sendError(sc, msg);
    }

    /**
     * HttpServletResponse().getWriterに移譲する。
     */
    public PrintWriter getWriter() throws IOException {
        return holder.getHttpServletResponse().getWriter();
    }

    /**
     * HttpServletResponse().sendErrorに移譲する。
     */
    public void sendError(int sc) throws IOException {
        holder.getHttpServletResponse().sendError(sc);
    }

    /**
     * HttpServletResponse().sendRedirectに移譲する。
     */
    public void sendRedirect(String location) throws IOException {
        holder.getHttpServletResponse().sendRedirect(location);
    }

    /**
     * HttpServletResponse().setCharacterEncodingに移譲する。
     */
    public void setCharacterEncoding(String charset) {
        holder.getHttpServletResponse().setCharacterEncoding(charset);
    }

    /**
     * HttpServletResponse().setDateHeaderに移譲する。
     */
    public void setDateHeader(String name, long date) {
        holder.getHttpServletResponse().setDateHeader(name, date);
    }

    /**
     * HttpServletResponse().addDateHeaderに移譲する。
     */
    public void addDateHeader(String name, long date) {
        holder.getHttpServletResponse().addDateHeader(name, date);
    }

    /**
     * HttpServletResponse().setContentLengthに移譲する。
     */
    public void setContentLength(int len) {
        holder.getHttpServletResponse().setContentLength(len);
    }

    /**
     * HttpServletResponse().setHeaderに移譲する。
     */
    public void setHeader(String name, String value) {
        holder.getHttpServletResponse().setHeader(name, value);
    }

    /**
     * HttpServletResponse().setContentTypeに移譲する。
     */
    public void setContentType(String type) {
        holder.getHttpServletResponse().setContentType(type);
    }

    /**
     * HttpServletResponse().addHeaderに移譲する。
     */
    public void addHeader(String name, String value) {
        holder.getHttpServletResponse().addHeader(name, value);
    }

    /**
     * HttpServletResponse().setIntHeaderに移譲する。
     */
    public void setIntHeader(String name, int value) {
        holder.getHttpServletResponse().setIntHeader(name, value);
    }

    /**
     * HttpServletResponse().setBufferSizeに移譲する。
     */
    public void setBufferSize(int size) {
        holder.getHttpServletResponse().setBufferSize(size);
    }

    /**
     * HttpServletResponse().addIntHeaderに移譲する。
     */
    public void addIntHeader(String name, int value) {
        holder.getHttpServletResponse().addIntHeader(name, value);
    }

    /**
     * HttpServletResponse().setStatusに移譲する。
     */
    public void setStatus(int sc) {
        holder.getHttpServletResponse().setStatus(sc);
    }

    /**
     * HttpServletResponse().setStatusに移譲する。
     */
    @SuppressWarnings("deprecation")
    public void setStatus(int sc, String sm) {
        holder.getHttpServletResponse().setStatus(sc, sm);
    }

    /**
     * HttpServletResponse().getBufferSizeに移譲する。
     */
    public int getBufferSize() {
        return holder.getHttpServletResponse().getBufferSize();
    }

    /**
     * HttpServletResponse().flushBufferに移譲する。
     */
    public void flushBuffer() throws IOException {
        holder.getHttpServletResponse().flushBuffer();
    }

    /**
     * HttpServletResponse().resetBufferに移譲する。
     */
    public void resetBuffer() {
        holder.getHttpServletResponse().resetBuffer();
    }

    /**
     * HttpServletResponse().isCommittedに移譲する。
     */
    public boolean isCommitted() {
        return holder.getHttpServletResponse().isCommitted();
    }

    /**
     * HttpServletResponse().resetに移譲する。
     */
    public void reset() {
        holder.getHttpServletResponse().reset();
    }

    /**
     * HttpServletResponse().setLocaleに移譲する。
     */
    public void setLocale(Locale loc) {
        holder.getHttpServletResponse().setLocale(loc);
    }

    /**
     * HttpServletResponse().getLocaleに移譲する。
     */
    public Locale getLocale() {
        return holder.getHttpServletResponse().getLocale();
    }

    /**
     * HttpServletResponse().setContentLengthLongに移譲する。
     */
    public void setContentLengthLong(long len) {
        holder.getHttpServletResponse().setContentLengthLong(len);
    }

    /**
     * HttpServletResponse().getStatusに移譲する。
     */
    public int getStatus() {
        return holder.getHttpServletResponse().getStatus();
    }

    /**
     * HttpServletResponse().getHeaderに移譲する。
     */
    public String getHeader(String name) {
        return holder.getHttpServletResponse().getHeader(name);
    }

    /**
     * HttpServletResponse().getHeadersに移譲する。
     */
    public Collection<String> getHeaders(String name) {
        return holder.getHttpServletResponse().getHeaders(name);
    }

    /**
     * HttpServletResponse().getHeaderNamesに移譲する。
     */
    public Collection<String> getHeaderNames() {
        return holder.getHttpServletResponse().getHeaderNames();
    }

}
