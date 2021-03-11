package oscana.s2n.handler;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.web.HttpResponse;

/**
 * サーブレットリソースへの参照を保持するクラス。
 * <p/>
 * 本クラスが必要な理由は{@link HttpResourceHolderHandler}のJavadocを参照。
 *
 * forcedNextResponseが設定されているときは、NablarchはActionのレスポンスにかかわらずこのレスポンスで応答する。
 *
 * @author Fumihiko Yamamoto
 */
@RequestScoped
public class HttpResourceHolder {

    private ServletContext servletContext;
    private HttpServletRequest httpServletRequest;
    private HttpServletResponse httpServletResponse;
    private HttpSession httpSession;
    private HttpResponse forcedNextResponse;
    private String currentRequestId = null;

    /**
     * サーブレットリソースへの参照を登録する。
     * @param servletContext サーブレットコンテキスト(javax.servlet.ServletContext - ServletNative)
     * @param httpServletRequest リクエスト(javax.servlet.http.HttpServletRequest - ServletNative)
     * @param httpServletResponse レスポンス(javax.servlet.http.HttpServletResponse - ServletNative)
     * @param httpSession セッション(javax.servlet.http.HttpSession - ServletNative)
     */
    void setResources(ServletContext servletContext, HttpServletRequest httpServletRequest,
            HttpServletResponse httpServletResponse, HttpSession httpSession) {
        this.servletContext = servletContext;
        this.httpServletRequest = httpServletRequest;
        this.httpServletResponse = httpServletResponse;
        this.httpSession = httpSession;
    }

    /**
     * ServletContextを取得する。
     * @return ServletContext サーブレットコンテキスト(javax.servlet.ServletContext - ServletNative)
     */
    public ServletContext getServletContext() {
        return servletContext;
    }

    /**
     * HttpServletRequestを取得する。
     * @return HttpServletRequest リクエスト(javax.servlet.http.HttpServletRequest - ServletNative)
     */
    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }

    /**
     * HttpServletResponseを取得する。
     * @return HttpServletResponse レスポンス(javax.servlet.http.HttpServletResponse - ServletNative)
     */
    public HttpServletResponse getHttpServletResponse() {
        return httpServletResponse;
    }

    /**
     * HttpSessionを取得する。
     * @return HttpSession セッション(javax.servlet.http.HttpSession - ServletNative)
     */
    public HttpSession getHttpSession() {
        return httpSession;
    }

    /**
     * forcedNextResponseを取得する。
     * @return forcedNextResponse
     */
    public HttpResponse getForcedNextResponse() {
        return forcedNextResponse;
    }

    /**
     * forcedNextResponseをセットする。
     * @param forcedNextResponse forcedNextResponse
     */
    public void setForcedNextResponse(HttpResponse forcedNextResponse) {
        this.forcedNextResponse = forcedNextResponse;
    }

    /**
     * サーブレットリソースへの参照を消去する。
     */
    public void clearResources() {
        this.httpServletRequest = null;
        this.httpServletResponse = null;
        this.servletContext = null;
        this.httpSession = null;
        this.forcedNextResponse = null;
    }

    /**
     * リクエストパスを取得する。
     * @return currentRequestId
     */
    public String getCurrentRequestId() {
        return currentRequestId;
    }

    /**
     * リクエストパスをセットする。
     * @param currentRequestId リクエストパス
     */
    public void setCurrentRequestId(String currentRequestId) {
        this.currentRequestId = currentRequestId;
    }
}