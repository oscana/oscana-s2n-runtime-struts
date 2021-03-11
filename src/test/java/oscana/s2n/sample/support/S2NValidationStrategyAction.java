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
import oscana.s2n.sample.form.ExecuteTestForm;
import oscana.s2n.struts.action.ActionMessage;
import oscana.s2n.struts.action.ActionMessages;

/**
 * S2NValidationStrategyのテスト用アクション。
 */
@RequestScoped
public class S2NValidationStrategyAction {
    @Inject
    @OscanaActionForm
    ExecuteTestForm form;

    /**
     * ExecuteのstopOnValidationErrorはtrue（デフォールト値）の場合のバリデーションテスト用
     */
    @Execute(validator = true, validate = "validateForm,validateCommon,@")
    @OnError(type = ApplicationException.class, path = "forward://outputMessage")
    public HttpResponse doValidStopWithErr(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
    }

    /**
     * ExecuteのstopOnValidationErrorはfalseの場合のバリデーションテスト用<br>
     *
     * validate = "validateForm,validateCommon,@"の場合
     */
    @Execute(validator = true, validate = "validateForm,validateCommon,@", stopOnValidationError = false)
    @OnError(type = ApplicationException.class, path = "forward://outputMessage")
    public HttpResponse doValidNotStopWithErr01(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
    }

    /**
     * ExecuteのstopOnValidationErrorはfalseの場合のバリデーションテスト用<br>
     *
     * validate = "@,validateForm,validateCommon"の場合
     */
    @Execute(validator = true, validate = "@,validateForm,validateCommon", stopOnValidationError = false)
    @OnError(type = ApplicationException.class, path = "forward://outputMessage")
    public HttpResponse doValidNotStopWithErr02(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
    }

    /**
     * ExecuteのstopOnValidationErrorはfalseの場合のバリデーションテスト用<br>
     *
     * validate = "validateForm,@,validateCommon"の場合
     */
    @Execute(validator = true, validate = "validateForm,@,validateCommon", stopOnValidationError = false)
    @OnError(type = ApplicationException.class, path = "forward://outputMessage")
    public HttpResponse doValidNotStopWithErr03(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
    }

    /**
     * ExecuteのstopOnValidationErrorはfalseの場合のバリデーションテスト用<br>
     *
     * validate = "validateForm,validateCommon"の場合
     */
    @Execute(validator = true, validate = "validateForm,validateCommon", stopOnValidationError = false)
    @OnError(type = ApplicationException.class, path = "forward://outputMessage")
    public HttpResponse doValidNotStopWithErr04(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
    }

    /**
     * ExecuteのstopOnValidationErrorはfalseの場合のバリデーションテスト用<br>
     *
     * validator = true, validate = "validateForm,@"の場合
     */
    @Execute(validator = true, validate = "validateForm,@", stopOnValidationError = false)
    @OnError(type = ApplicationException.class, path = "forward://outputMessage")
    public HttpResponse doValidNotStopWithErr05(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
    }

    /**
     * ExecuteのstopOnValidationErrorはfalseの場合のバリデーションテスト用<br>
     *
     * validator = false, validate = "validateForm,@"の場合
     */
    @Execute(validator = false, validate = "validateForm,@", stopOnValidationError = false)
    @OnError(type = ApplicationException.class, path = "forward://outputMessage")
    public HttpResponse doValidNotStopWithErr06(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
    }

    /**
     * ExecuteのstopOnValidationErrorはfalseの場合のバリデーションテスト用<br>
     *
     * validator = false, validate = ""の場合
     */
    @Execute(validator = false, validate = "", stopOnValidationError = false)
    @OnError(type = ApplicationException.class, path = "forward://outputMessage")
    public HttpResponse doValidNotStopWithErr07(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
    }

    /**
     * バリデーションエラー時のメッセージ出力（テストケースアサート用）
     */
    public HttpResponse outputMessage(HttpRequest nabRequest, ExecutionContext nabContext) {
        ErrorMessages messages = nabContext.getRequestScopedVar(WebConfigFinder.getWebConfig().getErrorMessageRequestAttributeName());
        StringBuilder sb = new StringBuilder();
        for (String msg : messages.getPropertyMessages()) {
            sb.append(msg).append("<br>");
        }
        return new HttpResponse(200).write(sb.toString());
    }

    /**
     * Actionで定義したバリデーションメソッド（S2NValidationStrategyから呼び出し）
     */
    public ActionMessages validateCommon() {

        ActionMessages errors = new ActionMessages();
        errors.add("validateCommon", new ActionMessage("error.S2NValidAction"));
        return errors;
    }
}
