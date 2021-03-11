package oscana.s2n.sample.support;

import nablarch.fw.ExecutionContext;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpResponse;
import oscana.s2n.common.web.interceptor.Execute;

/**
 * {@link Execute}のテスト用アクション。
 */
@RequestScoped
public class ExecuteNoFormTestAction {

    /**
     * Executeのresetは存在しないのテスト
     */
    @Execute(validator = true)
    public HttpResponse hasNoForm(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200).write("noFormIsOK");
    }

}
