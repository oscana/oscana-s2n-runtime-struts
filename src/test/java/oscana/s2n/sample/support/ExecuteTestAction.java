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

/**
 * {@link Execute}のテスト用アクション。
 */
@RequestScoped
public class ExecuteTestAction {

    public String actionAttr;

    @Inject
    @OscanaActionForm
    ExecuteTestForm form;


    /**
     * Executeの属性未設定（デフォールト値採用）テスト用
     */
    @Execute
    @OnError(type = ApplicationException.class, path = "forward://executeErrMsg")
    public HttpResponse defaultExecute(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
    }

    /**
     * ExecuteのremoveActionFormはtrueの場合のテスト
     */
    @Execute(removeActionForm = true, validator = false)
    public HttpResponse remove(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
    }

    /**
     * ExecuteのremoveActionFormはtrueの場合のテスト
     */
    @Execute(removeActionForm = true, validator = true)
    @OnError(type = ApplicationException.class, path = "forward://executeErrMsg")
    public HttpResponse notRemove02(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
    }

    /**
     * ExecuteのremoveActionFormはfalse(デフォールト値)の場合テスト用
     */
    @Execute(validator = false)
    public HttpResponse notRemove(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200);
    }

    /**
     * Executeのresetは未設定(デフォールト値：resetを採用)の場合テスト用
     */
    @Execute(validator = false)
    public HttpResponse defaultReset(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200).write(form.getResetVal());
    }

    /**
     * Executeのresetは存在する場合のテスト
     */
    @Execute(validator = false, reset="testForReset")
    public HttpResponse testForReset(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200).write(form.getResetVal());
    }

    /**
     * Executeのresetは空白(リセットしない)の場合テスト用
     */
    @Execute(validator = false, reset="")
    public HttpResponse noReset(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200).write(form.getResetVal());
    }

    /**
     * Executeのresetは存在しないメソッドの場合テスト用
     */
    @Execute(validator = false, reset="xxx")
    public HttpResponse resetNotExist(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200).write(form.getResetVal());
    }

    /**
     * Executeのformとactionのフィールドをリクエストスコープに設定のテスト用
     */
    @Execute(validator = false)
    public HttpResponse setAttr(HttpRequest req, ExecutionContext ctx) {
        form.setFormAttr("formAttr");
        actionAttr = "actionAttr";
        return new HttpResponse(200);
    }

    /**
     * Executeのnormalizeテスト用
     */
    @Execute
    public HttpResponse normalizeTest(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200).write("spaceVal=" + form.getSpaceVal()).write("<br>")
                .write("trimVal=" + form.getTrimVal());
    }

    /**
     * Executeのvalid時Formに値を設定することをテスト用
     */
    @Execute(validator = true, validate = "validateFormAndSetValue")
    public HttpResponse setValWithValidTest(HttpRequest req, ExecutionContext ctx) {
        return new HttpResponse(200).write(form.getUserId());
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
