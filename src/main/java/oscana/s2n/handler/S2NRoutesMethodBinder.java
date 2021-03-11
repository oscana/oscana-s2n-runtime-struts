/*
 * 取り込み元
 *    ライブラリ名：     nablarch-router-adaptor
 *    クラス名：         nablarch.integration.router.RoutesMethodBinder
 *    ソースリポジトリ： https://github.com/nablarch/nablarch-router-adaptor/blob/master/src/main/java/nablarch/integration/router/RoutesMethodBinder.java
 *
 * 上記ファイルを取り込み、修正を加えた。
 *
 * Copyright 2020 TIS Inc.
 *
 */
package oscana.s2n.handler;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import nablarch.core.ThreadContext;
import nablarch.fw.ExecutionContext;
import nablarch.fw.Handler;
import nablarch.fw.HandlerWrapper;
import nablarch.fw.Interceptor;
import nablarch.fw.MethodBinder;
import nablarch.fw.dicontainer.nablarch.Containers;
import nablarch.fw.handler.MethodBinding;
import nablarch.fw.web.HttpRequest;
import oscana.s2n.common.S2NConstants;
import oscana.s2n.struts.OscanaHttpResourceConverUtil;

/**
 * アクションのbeforeExecute/afterExecuteに対応したメソッドバインディングを作成する{@link MethodBinder}。
 *
 * @author Naoki Yamamoto
 * @see nablarch.integration.router.RoutesMethodBinder
 */
public class S2NRoutesMethodBinder implements MethodBinder<HttpRequest, Object> {

    /** ディスパッチするメソッド名 */
    private final String methodName;

    /** 前処理メソッド*/
    private final static String BEFORE_EXECUTE = "beforeExecute";

    /** 後処理メソッド*/
    private final static String AFTER_EXECUTE = "afterExecute";

    /**
     * コンストラクタ。
     *
     * @param methodName メソッド名
     */
    public S2NRoutesMethodBinder(final String methodName) {
        this.methodName = methodName;
    }

    @Override
    public HandlerWrapper<HttpRequest, Object> bind(final Object delegate) {
        return new MethodBinding<HttpRequest, Object>(delegate) {

            /**
             * アクションを呼び出す。
             * <p/>
             * 下記の順に処理を行う。
             * <ul>
             * <li>アクションにbeforeExecuteが存在する場合はbeforeExecuteを呼び出し、beforeExecuteの結果が存在する場合はレスポンスを作り保持する。</li>
             * <li>レスポンスがない場合はアクションメソッドを呼び出し、アクションメソッドのレスポンスをレスポンスとして保持する。</li>
             * <li>レスポンス有無に関わらずafterExecuteが存在する場合はafterExecuteを呼び出し、afterExecuteの結果が存在する場合はレスポンスを作り保持する。</li>
             * <li>最後に保持しているレスポンスを返す。</li>
             * </ul>
             *
             * @param request リクエスト
             * @param context コンテキスト
             * @return レスポンス
             */
            @Override
            public Object handle(HttpRequest request, ExecutionContext context) {

                final Method boundMethod = getMethodBoundTo(request, context);

                Handler<HttpRequest, Object> handler = new Handler<HttpRequest, Object>() {

                    @Override
                    public Object handle(HttpRequest data, ExecutionContext context) {
                        Serializable form = context.getRequestScopedVar(S2NConstants.FORM);
                        String actionName = delegate.getClass().getName();

                        Object response = null;

                        HttpResourceHolder httpResourceHolder = Containers.get().getComponent(HttpResourceHolder.class);
                        httpResourceHolder.setCurrentRequestId(request.getRequestPath());

                        Method beforeExecute = getExecuteMethod(BEFORE_EXECUTE);
                        if (beforeExecute != null) {
                            Object result = invokeExecuteMethod(beforeExecute, boundMethod, form);
                            if (result != null) {
                                response = OscanaHttpResourceConverUtil.createHttpResponse((String) result, delegate, request, context,
                                        actionName);
                            }
                        }

                        if (response == null) {
                            response = invokeExecuteMethod(boundMethod, request, context);
                        }

                        Method afterExecute = getExecuteMethod(AFTER_EXECUTE);
                        if (afterExecute != null) {
                            Object result = invokeExecuteMethod(afterExecute, boundMethod, form);
                            if (result != null) {
                                response = OscanaHttpResourceConverUtil.createHttpResponse((String) result, delegate, request, context,
                                        actionName);
                            }
                        }
                        return response;
                    }

                };
                return Interceptor.Factory.wrap(handler, boundMethod.getAnnotations())
                        .handle(request, context);
            }

            /**
             * アクションメソッドを返す。
             * <p/>
             * 後続処理で参照できるようにリクエストスコープにアクションメソッド、
             * スレッドコンテキストにアクションメソッドの名前を格納する。
             *
             * @param request リクエスト
             * @param context コンテキスト
             * @return アクションメソッド
             */
            @Override
            protected Method getMethodBoundTo(HttpRequest request, ExecutionContext context) {
                try {
                    Method method = delegate.getClass().getMethod(methodName, HttpRequest.class,
                            ExecutionContext.class);
                    context.setRequestScopedVar(S2NConstants.REQUEST_SCOPED_KEY_CALL_METHOD, method);
                    ThreadContext.setObject(S2NConstants.THREAD_CONTEXT_KEY_CALL_METHOD_NAME, methodName);
                    return method;
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }

            private Method getExecuteMethod(String methodName) {
                try {
                    return delegate.getClass().getMethod(methodName, Method.class, Object.class);
                } catch (NoSuchMethodException e) {
                    return null;
                }
            }

            private Object invokeExecuteMethod(Method executeMethod, Object... params) {
                try {
                    return executeMethod.invoke(delegate, params);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    Throwable cause = e.getCause();
                    if (RuntimeException.class.isAssignableFrom(cause.getClass())) {
                        throw (RuntimeException) cause;
                    }
                    if (Error.class.isAssignableFrom(cause.getClass())) {
                        throw (Error) cause;
                    }
                    throw new RuntimeException(cause);
                }
            }
        };
    }
}
