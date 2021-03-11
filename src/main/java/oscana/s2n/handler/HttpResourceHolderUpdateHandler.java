package oscana.s2n.handler;

import nablarch.fw.ExecutionContext;
import nablarch.fw.Handler;
import nablarch.fw.dicontainer.nablarch.Containers;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.servlet.ServletExecutionContext;

/**
 * サーブレットリソースのリフレッシュを行うクラス。<br>
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
public class HttpResourceHolderUpdateHandler implements Handler<HttpRequest, Object> {

    public HttpResourceHolderUpdateHandler() {
        super();
    }

    /**
     * ハンドラーで処理する。<br>
     * <br>
     * ・HttpResourceHolderにサーブレットリソースへの参照を更新し、次のhandleの処理を行う。<br>
     * ・handleから戻ってきてもサーブレットリソースへの参照は削除しない。<br>
     *
     * @param request リクエスト
     * @param context 実行コンテキスト
     * @return 結果データ
     */
    @Override
    public Object handle(final HttpRequest request, final ExecutionContext context) {

        HttpResourceHolder resource = null;
        //Holderの取得
        resource = Containers.get().getComponent(HttpResourceHolder.class);

        //Http系リソースへの最新の参照をHttpResourceHolderに登録する
        ServletExecutionContext cxt = (ServletExecutionContext) context;
        resource.setResources(cxt.getServletContext(), cxt.getServletRequest(), cxt.getServletResponse(),
                cxt.getNativeHttpSession(true));

        return context.handleNext(request);
    }

}
