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

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.After;
import org.junit.Before;

import mockit.Expectations;
import mockit.Mocked;
import nablarch.fw.ExecutionContext;
import nablarch.fw.Handler;
import nablarch.fw.dicontainer.annotation.AnnotationContainerBuilder;
import nablarch.fw.dicontainer.annotation.AnnotationScopeDecider;
import nablarch.fw.dicontainer.nablarch.AnnotationAutoContainerProvider;
import nablarch.fw.dicontainer.nablarch.Containers;
import nablarch.fw.dicontainer.nablarch.NablarchWebContextHandler;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.dicontainer.web.SessionScoped;
import nablarch.fw.dicontainer.web.scope.RequestScope;
import nablarch.fw.dicontainer.web.scope.SessionScope;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.servlet.ServletExecutionContext;

/**
 * テストを実行するための共通クラス
 *
 *
 */
public abstract class S2NBaseTest {
    private RequestScope requestScope;
    private SessionScope sessionScope;
    private AnnotationContainerBuilder builder;
    private NablarchWebContextHandler supplier;

    protected List<Class<?>> registClassList;
    protected ExecutionContext executionContext;
    // ExecutionContext作成用
    @Mocked
    protected HttpServletResponse httpServletResponse;
    @Mocked
    protected HttpServletRequest httpServletRequest;
    @Mocked
    protected ServletContext servletContext;

    @Before
    public void setUp() throws Exception {

        new Expectations() {{
            httpServletRequest.getContextPath();
            result = "";
            minTimes = 0;

            httpServletRequest.getRequestURI();
            result = "/dummy";
            minTimes = 0;

        }};

        //ExecutionContextを設定
        setExecutionContext();

        supplier = new NablarchWebContextHandler();
        requestScope = new RequestScope(supplier);
        sessionScope = new SessionScope(supplier);
        final AnnotationScopeDecider decider = AnnotationScopeDecider.builder()
                .addScope(RequestScoped.class, requestScope).addScope(SessionScoped.class, sessionScope)
                .build();
        builder = AnnotationContainerBuilder.builder().scopeDecider(decider).build();
        AnnotationAutoContainerProvider provider = new AnnotationAutoContainerProvider();

        //クラスをregist
        setClassToRegist();
        if(registClassList == null) {
            fail("コンテナに事前ロードする必要クラスを設定してください。");
        }
        //コンポーネント（クラス）をコンテナにロードする
        for(Class<?> clazz : registClassList) {
            builder.register(clazz);
        }

        provider.setAnnotationContainerBuilder(builder);
        provider.initialize();

    }

    @After
    public void tearDown() throws Exception {
        Containers.clear();
    }

    /**
     * テストコードを実施する
     * - handlerListで定義しているハンドラを順番的に実施
     * - 実行できるのはインタフェースHandler<TData, TResult>を実装したもののみ
     */
    protected Object handle(List<Handler<HttpRequest, Object>> handlerList) {
        if(executionContext == null ) {
            fail("ExecutionContextを設定してください。");
        }
        if(handlerList.size()==0) {
            fail("処理するハンドラを設定してください。");
        }
        for(Handler<HttpRequest, Object> handler : handlerList) {
            executionContext.addHandler(handler);
        }

      return supplier.handle(null, executionContext);
    }

    /**
     * 事前にコンテナにregist必要のクラスを定義
     */
    protected abstract void setClassToRegist();

    /**
     * ExecutionContextを定義
     * ハンドラを実施する際ExecutionContextが必要
     */
    protected void setExecutionContext() {
        executionContext = new ServletExecutionContext(httpServletRequest, httpServletResponse,
                servletContext);
        new Expectations(executionContext) {{
            Map<String, Object> requestScope = new HashMap<String, Object>();
            executionContext.getRequestScopeMap();
            result = requestScope;
            minTimes = 0;
        }};
    }
}