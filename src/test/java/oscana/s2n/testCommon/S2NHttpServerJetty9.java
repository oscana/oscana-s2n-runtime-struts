package oscana.s2n.testCommon;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import javax.servlet.DispatcherType;
import javax.servlet.Filter;

import org.apache.tomcat.JarScanner;
import org.apache.tomcat.util.scan.StandardJarScanner;
import org.eclipse.jetty.annotations.AnnotationConfiguration;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.LocalConnector;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.session.SessionHandler;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.util.resource.ResourceCollection;
import org.eclipse.jetty.webapp.Configuration;
import org.eclipse.jetty.webapp.WebAppContext;
import org.eclipse.jetty.webapp.WebInfConfiguration;
import org.eclipse.jetty.webapp.WebXmlConfiguration;

import nablarch.core.util.annotation.Published;
import nablarch.fw.ExecutionContext;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.HttpServer;
import nablarch.fw.web.MockHttpRequest;
import nablarch.fw.web.ResourceLocator;
import nablarch.fw.web.httpserver.LazySessionInvalidationFilter;
import nablarch.test.core.http.HttpRequestTestSupportHandler;

/**
 * Jetty9対応の{@link HttpServer}サブクラス。
 *
 * @author Taichi Uragami
 * @author Yutaka Kanayama
 */
@Published(tag = "architect")
public class S2NHttpServerJetty9 extends HttpServer {

    /** アプリケーションサーバの実体 */
    private Server jetty;

    /** 自動テスト実行用コネクター */
    private LocalConnector localConnector;

    /** 初期パラメータ*/
    private final Map<String, String> initParams = new HashMap<String, String>();

    /**
     * サーバを起動する。
     * <pre>
     * サーバスレッドを生成し、port()メソッドで指定されたポート番号上の
     * HTTPリクエストに対して処理を行う。
     * </pre>
     *
     * @return このオブジェクト自体
     */
    public S2NHttpServerJetty9 start() {
        jetty = new Server(getPort());
        Connector conn = new ServerConnector(jetty);
        initialize(conn);
        try {
            jetty.start();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * このサーバをテストモードで起動する。
     * @return このオブジェクト自体
     */
    public S2NHttpServerJetty9 startLocal() {
        jetty = new Server();
        localConnector = new LocalConnector(jetty);
        initialize(localConnector);
        try {
            jetty.start();
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return this;
    }

    /**
     * サーバスレッドが終了するまでカレントスレッドをwaitさせる。
     *
     * @return このオブジェクト自体
     */
    public HttpServer join() {
        try {
            jetty.join();

        } catch (InterruptedException e) {
            // カレントスレッドに割り込み要求を行ってから抜ける。
            Thread.currentThread().interrupt();
        }
        return this;
    }

    /**
     * {@inheritDoc}
     * <pre>
     * このクラスの実装では、
     * 引数のHTTPリクエストオブジェクトをHTTPメッセージにシリアライズし、
     * ローカルコネクションに送信する。
     * 内蔵アプリケーションサーバでの処理後、返信されたHTTPレスポンスメッセージを
     * HTTPレスポンスオブジェクトにパースし、この関数の戻り値として返す。
     * また、HTTPダンプ出力が有効である場合、
     * そのレスポンスボディの内容を所定のディレクトリに出力する。
     * </pre>
     */
    public HttpResponse handle(HttpRequest req, ExecutionContext sourceContext) {
        if (localConnector == null) {
            throw new RuntimeException(
                    "this server is not running on a local connector. "
                            + "you must call startLocal() method beforehand.");
        }

        String host = req.getHost();
        if (host == null || host.isEmpty()) {
            ((MockHttpRequest) req).setHost("127.0.0.1");
        }

        final CountDownLatch latch = new CountDownLatch(1);
        sourceContext.setRequestScopedVar(HttpRequestTestSupportHandler.NABLARCH_JETTY_CONNECTOR_LATCH, latch);
        try {
            byte[] rawReq = req.toString().getBytes();
            ByteBuffer response = localConnector.getResponse(ByteBuffer.wrap(rawReq));
            latch.await(1L, TimeUnit.SECONDS);
            byte[] rawRes = response.array();
            HttpResponse res = HttpResponse.parse(rawRes);
            if (isHttpDumpEnabled()) {
                dumpHttpMessage(req, res);
            }
            return res;
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 初期パラメータを設定。
     */
    public void setInitParameter(String key, String value) {
        this.initParams.put(key, value);
    }

    /**
     * Jettyサーバインスタンスの初期化を行う。
     *
     * @param conn このサーバがacceptするコネクタ
     */
    private void initialize(Connector conn) {
        jetty.addConnector(conn);
        deploy();
    }

    /**
     * 内部サーバにWARをデプロイする。
     * <pre>
     * エントリポイントサーブレットと、
     * {@link #setWarBasePath(String)}で指定されたパス上に存在するWARをデプロイする。
     * </pre>
     */
    private void deploy() {
        WebAppContext webApp = new WebAppContext();
        SessionHandler sessionHandler = new SessionHandler();
        sessionHandler.setSessionIdPathParameterName("none");
        webApp.setSessionHandler(sessionHandler);
        webApp.setContextPath(getServletContextPath());
        webApp.setBaseResource(toResourceCollection(getWarBasePaths()));
        webApp.setClassLoader(Thread.currentThread().getContextClassLoader());
        StandardJarScanner scanner = new StandardJarScanner();
        scanner.setScanManifest(false);
        webApp.setAttribute(JarScanner.class.getName(), scanner);
        webApp.setPersistTempDirectory(true);

        webApp.addFilter(LazySessionInvalidationFilter.class, "/*",
                EnumSet.of(DispatcherType.REQUEST));
        Filter webFrontController = getWebFrontController();
        webApp.addFilter(
                new FilterHolder(webFrontController), "/*", EnumSet.of(DispatcherType.REQUEST));
        Configuration[] configurations = {
                new WebInfConfiguration(),
                new WebXmlConfiguration(),
                new AnnotationConfiguration()
        };
        webApp.setConfigurations(configurations);

        // 初期パラメータの設定
        for (Map.Entry<String, String> entry : this.initParams.entrySet()) {
            webApp.setInitParameter(entry.getKey(), entry.getValue());
        }
        //　制限のuriを外す
        webApp.setProtectedTargets(null);

        File tmpDir = getTempDirectory();
        if (tmpDir != null) {
            webApp.setTempDirectory(tmpDir);
        }

        jetty.setHandler(webApp);
    }

    /**
     * {@link ResourceLocator}のリストを{@link ResourceCollection}に変換する。
     * @param warBasePaths 変換元のリスト
     * @return 変換後の {@link ResourceCollection}
     */
    private ResourceCollection toResourceCollection(List<ResourceLocator> warBasePaths) {
        String[] realPaths = new String[warBasePaths.size()];
        for (int i = 0; i < warBasePaths.size(); i++) {
            realPaths[i] = warBasePaths.get(i).getRealPath();
        }
        try {
            return new ResourceCollection(realPaths);
        } catch (RuntimeException e) {
            throw new IllegalStateException(
                    "invalid warBasePath. " + warBasePaths, e);
        }
    }

}