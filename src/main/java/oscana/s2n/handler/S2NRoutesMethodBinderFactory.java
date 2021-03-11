/*
 * 取り込み元
 *    ライブラリ名：     nablarch-router-adaptor
 *    クラス名：         nablarch.integration.router.RoutesMethodBinderFactory
 *    ソースリポジトリ： https://github.com/nablarch/nablarch-router-adaptor/blob/master/src/main/java/nablarch/integration/router/RoutesMethodBinderFactory.java
 *
 * 上記ファイルを取り込み、修正を加えた。
 *
 * Copyright 2020 TIS Inc.
 *
 */
package oscana.s2n.handler;

import nablarch.fw.MethodBinder;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.handler.MethodBinderFactory;

/**
 * {@link S2NRoutesMethodBinder}を生成するファクトリクラス。
 *
 * @author Hisaaki Shioiri
 * @see nablarch.integration.router.RoutesMethodBinderFactory
 */
public class S2NRoutesMethodBinderFactory implements MethodBinderFactory<Object> {

    /**
     * {@link MethodBinder}を生成する。
     *
     * @param methodName ディスパッチするメソッド名
     * @return {@link MethodBinder}インスタンス
     */
    @Override
    public MethodBinder<HttpRequest, Object> create(final String methodName) {
        return new S2NRoutesMethodBinder(methodName);
    }

}
