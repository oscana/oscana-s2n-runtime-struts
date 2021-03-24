package oscana.s2n.servlet;

import java.util.Enumeration;

import javax.inject.Inject;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;

import nablarch.fw.dicontainer.web.RequestScoped;
import oscana.s2n.handler.HttpResourceHolder;

/**
 * HttpSessionのI/Fを提供するリソースホルダクラス。<br>
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
@SuppressWarnings("deprecation")
@RequestScoped
public class HttpSessionHolder implements HttpSession {
    @Inject
    public HttpResourceHolder holder;

    /**
     * HttpSession().getCreationTimeに移譲する。
     */
    public long getCreationTime() {
        return holder.getHttpSession().getCreationTime();
    }

    /**
     * HttpSession().getIdに移譲する。
     */
    public String getId() {
        return holder.getHttpSession().getId();
    }

    /**
     * HttpSession().getLastAccessedTimeに移譲する。
     */
    public long getLastAccessedTime() {
        return holder.getHttpSession().getLastAccessedTime();
    }

    /**
     * HttpSession().getServletContextに移譲する。
     */
    public ServletContext getServletContext() {
        return holder.getHttpSession().getServletContext();
    }

    /**
     * HttpSession().setMaxInactiveIntervalに移譲する。
     */
    public void setMaxInactiveInterval(int interval) {
        holder.getHttpSession().setMaxInactiveInterval(interval);
    }

    /**
     * HttpSession().getMaxInactiveIntervalに移譲する。
     */
    public int getMaxInactiveInterval() {
        return holder.getHttpSession().getMaxInactiveInterval();
    }

    /**
     * HttpSession().getSessionContextに移譲する。
     */
    public HttpSessionContext getSessionContext() {
        return holder.getHttpSession().getSessionContext();
    }

    /**
     * HttpSession().getAttributeに移譲する。
     */
    public Object getAttribute(String name) {
        return holder.getHttpSession().getAttribute(name);
    }

    /**
     * HttpSession().getValueに移譲する。
     */
    public Object getValue(String name) {
        return holder.getHttpSession().getValue(name);
    }

    /**
     * HttpSession().getAttributeNamesに移譲する。
     */
    public Enumeration<String> getAttributeNames() {
        return holder.getHttpSession().getAttributeNames();
    }

    /**
     * HttpSession().getValueNamesに移譲する。
     */
    public String[] getValueNames() {
        return holder.getHttpSession().getValueNames();
    }

    /**
     * HttpSession().setAttributeに移譲する。
     */
    public void setAttribute(String name, Object value) {
        holder.getHttpSession().setAttribute(name, value);
    }

    /**
     * HttpSession().putValueに移譲する。
     */
    public void putValue(String name, Object value) {
        holder.getHttpSession().putValue(name, value);
    }

    /**
     * HttpSession().removeAttributeに移譲する。
     */
    public void removeAttribute(String name) {
        holder.getHttpSession().removeAttribute(name);
    }

    /**
     * HttpSession().removeValueに移譲する。
     */
    public void removeValue(String name) {
        holder.getHttpSession().removeValue(name);
    }

    /**
     * HttpSession().invalidateに移譲する。
     */
    public void invalidate() {
        holder.getHttpSession().invalidate();
    }

    /**
     * HttpSession().isNewに移譲する。
     */
    public boolean isNew() {
        return holder.getHttpSession().isNew();
    };

}
