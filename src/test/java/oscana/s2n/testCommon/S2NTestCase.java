/*
 * Copyright 2004-2013 the Seasar Foundation and the Others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND,
 * either express or implied. See the License for the specific language
 * governing permissions and limitations under the License.
 */
package oscana.s2n.testCommon;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.Before;

import nablarch.core.ThreadContext;
import nablarch.core.repository.SystemRepository;
import nablarch.core.repository.di.ComponentDefinition;
import nablarch.core.repository.di.DiContainer;
import nablarch.core.repository.di.config.xml.XmlComponentDefinitionLoader;
import nablarch.fw.ExecutionContext;
import nablarch.fw.Handler;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpRequestHandler;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.HttpServer;
import nablarch.fw.web.MockHttpRequest;
import nablarch.fw.web.handler.HttpResponseHandler;

/**
 * テストを実行するための共通クラス
 *
 *
 */
@SuppressWarnings({ "rawtypes" })
public abstract class S2NTestCase {

    private S2NMockHttpRequest request;

    private S2NMockServletContext servletContext;

    private HttpResponse response;

    private DiContainer container;

    private HttpServer server;

    @Before
    public void setUp() {
        //コンテナ作成
        setUpContainer();

        ThreadContext.clear();
        // 内蔵サーバ作成
        setUpServer();

        request = new S2NMockHttpRequest();

        servletContext = new S2NMockServletContext();

    }

    private void setUpContainer() {
        //コンポーネント設定ファイルをロード
        XmlComponentDefinitionLoader loader = new XmlComponentDefinitionLoader(
                "component-configuration-forTest.xml");
        container = new DiContainer(loader);
        SystemRepository.load(container);
    }

    private void setUpServer() {

        server = new S2NHttpServerJetty9()
                .setServletContextPath("/test")
                .setHandlerQueue(Arrays.asList(
                        SystemRepository.get("nablarchWebContextHandler"),
                        SystemRepository.get("httpResourceHolderHandler"),
                        (Handler) (new HttpResponseHandler())));

    }

    @SafeVarargs
    protected final HttpResponse sendReq(FuncNoParam c, ArrayList<S2NPair>... params) {

        String requestUri = request.getRequestUri();
        StringBuilder sb = new StringBuilder();

        if (params.length > 0) {
            sb.append("?");

            for (List<S2NPair> data : params) {
                for (S2NPair pair : data) {
                    sb.append(pair.getKey()).append("=").append(pair.getValue()).append("&");
                }
            }

            requestUri = requestUri + sb.toString().substring(0, sb.toString().length() - 1);
        }

        String reqPath = "GET " + server.getServletContextPath() + requestUri + " HTTP/1.1";

        // 初期パラメータをサーバーに設定
        for (Map.Entry<String, String> entry : servletContext.getInitParams().entrySet()) {
            ((S2NHttpServerJetty9) server).setInitParameter(entry.getKey(), entry.getValue());
        }

        // リクエストを処理するハンドラ
        HttpRequestHandler handler = new HttpRequestHandler() {
            public HttpResponse handle(HttpRequest req, ExecutionContext ctx) {
                try {
                    Object rslt = c.apply();
                    if (rslt == null) {
                        return new HttpResponse(200);
                    } else if(rslt instanceof HttpResponse) {
                        return (HttpResponse)rslt;
                    } else {
                        return new HttpResponse(200).write(rslt.toString());
                    }
                } catch (Exception e) {
                    return new HttpResponse(500);
                }
            }
        };

        if (requestUri.contains("?")) {
            server.addHandler(requestUri.substring(0, requestUri.indexOf("?")), handler);
        } else {
            server.addHandler(requestUri, handler);
        }

        server.startLocal();
        HttpResponse res = server.handle(new MockHttpRequest(reqPath), new ExecutionContext());

        return res;
    }

    @SafeVarargs
    protected final Exception sendReqException(FuncNoParam c, ArrayList<S2NPair>... params) {

        String requestUri = request.getRequestUri();
        StringBuilder sb = new StringBuilder();

        if (params.length > 0) {
            sb.append("?");

            for (List<S2NPair> data : params) {
                for (S2NPair pair : data) {
                    sb.append(pair.getKey()).append("=").append(pair.getValue()).append("&");
                }
            }

            requestUri = requestUri + sb.toString().substring(0, sb.toString().length() - 1);
        }

        String reqPath = "GET " + server.getServletContextPath() + requestUri + " HTTP/1.1";

        // 初期パラメータをサーバーに設定
        for (Map.Entry<String, String> entry : servletContext.getInitParams().entrySet()) {
            ((S2NHttpServerJetty9) server).setInitParameter(entry.getKey(), entry.getValue());
        }

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

                    ByteArrayOutputStream bout = new ByteArrayOutputStream();
                    ObjectOutputStream objOut;
                    try {
                        objOut = new ObjectOutputStream(bout);
                        objOut.writeObject(e);
                        objOut.close();
                    } catch (IOException e1) {
                        return new HttpResponse(500);
                    }

                    return new HttpResponse(200).write(bout.toByteArray());
                }
            }
        };

        if (requestUri.contains("?")) {
            server.addHandler(requestUri.substring(0, requestUri.indexOf("?")), handler);
        } else {
            server.addHandler(requestUri, handler);
        }
        server.startLocal();

        HttpResponse res = server.handle(new MockHttpRequest(reqPath), new ExecutionContext());

        Object obj = null;
        InputStream is = res.getBodyStream();
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        int nRead;
        byte[] data = new byte[16384];
        try {
            while ((nRead = is.read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }

            ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
            obj = objectInputStream.readObject();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

        if (obj instanceof Exception) {
            return (Exception) obj;
        }

        return null;
    }

    protected interface FuncNoParam {
        public Object apply() throws Exception;
    }

    @After
    public void tearDown() {

    }

    /**
     * {@link ComponentDef}を返します。
     *
     * @param componentName
     * @return {@link ComponentDef}
     */
    public ComponentDefinition getComponentDef(String componentName) {
        return container.getComponentByName(componentName);
    }

    /**
     * コンポーネントを返します。
     *
     * @param componentName
     * @return
     */
    public Object getComponent(String componentName) {
        return container.getComponentByName(componentName);
    }

    protected S2NMockHttpRequest getRequest() {
        return request;
    }

    protected void setRequest(S2NMockHttpRequest request) {
        this.request = request;
    }

    protected HttpResponse getResponse() {
        return response;
    }

    protected void setResponse(HttpResponse response) {
        this.response = response;
    }

    protected DiContainer getContainer() {
        return container;
    }

    protected void setContainer(DiContainer container) {
        this.container = container;
    }

    protected S2NMockServletContext getServletContext() {
        return servletContext;
    }

    protected void setServletContext(S2NMockServletContext servletContext) {
        this.servletContext = servletContext;
    }
}