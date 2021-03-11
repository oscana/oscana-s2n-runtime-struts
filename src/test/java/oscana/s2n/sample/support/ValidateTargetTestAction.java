package oscana.s2n.sample.support;

import javax.inject.Inject;

import nablarch.common.web.WebConfigFinder;
import nablarch.core.message.ApplicationException;
import nablarch.fw.ExecutionContext;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.interceptor.OnError;
import nablarch.fw.web.message.ErrorMessages;
import oscana.s2n.common.OscanaActionForm;
import oscana.s2n.common.web.interceptor.Execute;
import oscana.s2n.sample.form.ValidateTargetTestForm;
import oscana.s2n.validation.ValidateTarget;

/**
 * {@link ValidateTarget}のテスト用アクション。
 */
@RequestScoped
public class ValidateTargetTestAction {
    @Inject
    @OscanaActionForm
    ValidateTargetTestForm form;

    @SuppressWarnings("unused")
    private String actionAttr;

    /**
     * validateTargetテストのメソッド（ターゲットである）
     */
    @Execute
    @OnError(type = ApplicationException.class, path = "forward://executeErrMsg")
    public HttpResponse targetMethod1(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
    }

    /**
     * validateTargetテストのメソッド（ターゲットである）
     */
    @Execute
    @OnError(type = ApplicationException.class, path = "forward://executeErrMsg")
    public HttpResponse targetMethod2(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
    }

    /**
     * validateTargetテストのメソッド（ターゲットではない）
     */
    @Execute
    @OnError(type = ApplicationException.class, path = "forward://executeErrMsg")
    public HttpResponse notTargetMethod(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
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
