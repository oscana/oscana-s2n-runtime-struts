package oscana.s2n.testCommon;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;

import nablarch.core.ThreadContext;
import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.fw.ExecutionContext;
import nablarch.fw.Handler;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpRequestHandler;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.HttpServer;
import nablarch.fw.web.MockHttpCookie;
import nablarch.fw.web.MockHttpRequest;
import nablarch.fw.web.upload.PartInfo;

/**
 * テストを実行するための共通クラス
 *
 *
 */
@SuppressWarnings("rawtypes")
public abstract class S2NNablarchTestCase {

    private DiContainer container;

    protected HttpServer server;

    protected List<Handler> handlerList;

    protected String componentDefFile = "web-boot-forTest.xml";

    private Map<String, List<PartInfo>> multipart = new HashMap<String, List<PartInfo>>();

    @Before
    public void setUp() {
        //コンポーネント設定ファイル設定
        setComponentDefFile();
        //コンポーネント設定ファイルをロード
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(componentDefFile);
        container = new DiContainer(loader, true);
        SystemRepository.load(container);

        ThreadContext.clear();

        setHandlerList();

        // 内蔵サーバ作成
        server = new S2NHttpServerJetty9()
                .setServletContextPath("/test")
                .setHandlerQueue(handlerList);
    }

    /**
     * 処理ハンドラキューを定義
     */
    protected abstract void setHandlerList();

    /**
     * コンポーネント設定ファイルを定義
     */
    protected void setComponentDefFile() {
    };

    /**
     * リクエストを送るのデフォルト実装
     */
    protected HttpResponse sendReq(String uri, String... userAgent) {
        // サーバー起動
        server.startLocal();
        // リクエスト送信
        MockHttpCookie cookie = new MockHttpCookie();
        cookie.put("NABLARCH_SID", "11111");

        MockHttpRequest request = new MockHttpRequest("GET " + server.getServletContextPath() + uri + " HTTP/1.1");
        if (userAgent.length != 0) {
            request.getHeaderMap().put("User-Agent", userAgent[0]);
        }
        request.setCookie(cookie);

        //ファイル送信
        if(multipart.size() > 0) {
            request.getHeaderMap().put("Content-Type", "multipart/form-data");
            request.setMultipart(multipart);
        }

        HttpResponse res = server.handle(request, new ExecutionContext());

        return res;
    }

    protected void addHandler(FuncNoParam c) {

        // リクエストを処理するハンドラ
        HttpRequestHandler handler = new HttpRequestHandler() {
            public HttpResponse handle(HttpRequest req, ExecutionContext ctx) {
                try {
                    Object rslt = c.apply();
                    if (rslt == null) {
                        return new HttpResponse(200);
                    } else {
                        return new HttpResponse(200).write(rslt.toString());
                    }
                } catch (Exception e) {
                    return new HttpResponse(500);
                }
            }
        };

        server.addHandler("/", handler);
    }

    protected interface FuncNoParam {
        public Object apply() throws Exception;
    }


    /**
     * @param multipart セットする multipart
     */
    public void addMultipart(String key, List<PartInfo> file) {
        this.multipart.put(key, file);
    }
}
