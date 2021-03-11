package oscana.s2n.sample.support;

import javax.inject.Inject;

import nablarch.common.web.WebConfigFinder;
import nablarch.fw.ExecutionContext;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.message.ErrorMessages;
import oscana.s2n.common.OscanaActionForm;
import oscana.s2n.common.web.interceptor.Execute;
import oscana.s2n.sample.form.ExecuteErrTestForm;

/**
 * {@link Execute}のテスト用アクション。
 */
@RequestScoped
public class ExecuteErrTestAction {
    @Inject
    @OscanaActionForm
    ExecuteErrTestForm form;

    /**
     * Executeのresetは存在しないのテスト
     */
    @Execute(validator = false)
    public HttpResponse hasNoReset(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200).write(form.getResetVal());
    }


    /**
     * エラー時のメッセージ出力（テストケースアサート用）
     */
    public HttpResponse executeErrMsg(HttpRequest nabRequest, ExecutionContext nabContext) {
        ErrorMessages messages = nabContext.getRequestScopedVar(WebConfigFinder.getWebConfig().getErrorMessageRequestAttributeName());
        StringBuilder sb = new StringBuilder();
        for (String msg : messages.getPropertyMessages()) {
          sb.append(msg).append("<br>");
        }

        return new HttpResponse(200).write(sb.toString());
    }
}
