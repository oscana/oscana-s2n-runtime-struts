package oscana.s2n.sample.support;

import static org.junit.Assert.*;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import nablarch.fw.ExecutionContext;
import nablarch.fw.dicontainer.nablarch.Containers;
import nablarch.fw.dicontainer.web.RequestScoped;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpResponse;
import oscana.s2n.common.OscanaActionForm;
import oscana.s2n.common.web.interceptor.Execute;
import oscana.s2n.handler.HttpResourceHolder;
import oscana.s2n.sample.form.SampleForm;
import oscana.s2n.struts.Globals;
import oscana.s2n.struts.action.Action;
import oscana.s2n.struts.action.ActionMessage;
import oscana.s2n.struts.action.ActionMessages;
import oscana.s2n.testLibrary.utill.RequestUtil;

/**
 * strutsのactionのテスト用
 *
 */
@RequestScoped
public class StrutsAction extends Action {
    /** アクションフォーム */
    @Inject
    @OscanaActionForm
    public SampleForm sampleForm;

    /**
     * ActionMappingToolのオブジェクトを生成できること
     */
    @Execute(validator = false)
    public HttpResponse actionCreateStrutsMapping(HttpRequest nabRequest, ExecutionContext nabContext) {
        assertNotNull(createStrutsMapping(nabRequest, nabContext));
        return new HttpResponse(200);
    }

    /**
     * サーブレットレスポンスを返すことができること
     */
    @Execute(validator = false)
    public HttpResponse actionCreateStrutsResponse(HttpRequest nabRequest, ExecutionContext nabContext) {
        assertNotNull(createStrutsResponse(nabContext));
        return new HttpResponse(200);
    }

    /**
     * サーブレットリクエストを返すことができること
     */
    @Execute(validator = false)
    public HttpResponse actionCreateStrutsRequest(HttpRequest nabRequest, ExecutionContext nabContext) {
        assertNotNull(createStrutsRequest(nabContext));
        return new HttpResponse(200);
    }

    /**
     * キャンセルの確認テスト
     */
    @Execute(validator = false)
    public HttpResponse actionIsCancelled(HttpRequest nabRequest, ExecutionContext nabContext) {
        HttpServletRequest request = RequestUtil.getRequest();
        boolean b =isCancelled(request);
        return new HttpResponse(200).write(String.valueOf(b));
    }

    /**
     * メッセージをリクエストに格納されること
     *
     */
    @Execute(validator = false)
    public HttpResponse actionSaveMessages(HttpRequest nabRequest, ExecutionContext nabContext) {
        HttpServletRequest request = RequestUtil.getRequest();
        ActionMessages msgs1 = new ActionMessages();
        msgs1.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("messageResources1", null));
        saveMessages(request, msgs1);
        assertNotNull(request.getAttribute(Globals.MESSAGE_KEY));

        msgs1.clear();
        saveMessages(request, msgs1);
        assertNull(request.getAttribute(Globals.MESSAGE_KEY));

        ActionMessages msgs2 = null;
        saveMessages(request, msgs2);
        assertNull(request.getAttribute(Globals.MESSAGE_KEY));
        return new HttpResponse(200);
    }

    /**
     * エラーをリクエストに格納されること
     *
     */
    @Execute(validator = false)
    public HttpResponse actionSaveErrors(HttpRequest nabRequest, ExecutionContext nabContext) {
        HttpServletRequest request = RequestUtil.getRequest();
        ActionMessages msgs1 = new ActionMessages();
        msgs1.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("messageResources1", null));
        saveErrors(request, msgs1);
        assertNotNull(request.getAttribute(Globals.ERROR_KEY));

        msgs1.clear();
        saveErrors(request, msgs1);
        assertNull(request.getAttribute(Globals.ERROR_KEY));

        ActionMessages msgs2 = null;
        saveErrors(request, msgs2);
        assertNull(request.getAttribute(Globals.ERROR_KEY));
        return new HttpResponse(200);
    }

    /**
     * エラーをセッションに格納されること
     *
     */
    @Execute(validator = false)
    public HttpResponse actionSaveErrorsToSession(HttpRequest nabRequest, ExecutionContext nabContext) {
        HttpResourceHolder resource = Containers.get().getComponent(HttpResourceHolder.class);
        HttpSession session = resource.getHttpSession();
        ActionMessages msgs1 = new ActionMessages();
        msgs1.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("messageResources1", null));
        saveErrors(session, msgs1);
        assertNotNull(session.getAttribute(Globals.ERROR_KEY));

        msgs1.clear();
        saveErrors(session, msgs1);
        assertNull(session.getAttribute(Globals.ERROR_KEY));

        ActionMessages msgs2 = null;
        saveErrors(session, msgs2);
        assertNull(session.getAttribute(Globals.ERROR_KEY));
        return new HttpResponse(200);
    }
}
