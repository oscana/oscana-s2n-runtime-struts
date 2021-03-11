package oscana.s2n.struts.action;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;
import mockit.Verifications;
import nablarch.core.message.Message;
import nablarch.fw.web.MockHttpRequest;
import oscana.s2n.handler.HttpResourceHolder;
import oscana.s2n.handler.HttpResourceHolderHandler;
import oscana.s2n.struts.Globals;
import oscana.s2n.testCommon.S2NBaseTest;
import oscana.s2n.testLibrary.utill.RequestUtil;

/**
 * {@link Action}のテスト。
 *
 */
public class ActionTest extends S2NBaseTest {

    private MockHttpRequest mockHttpRequest = new MockHttpRequest();

    @Mocked
    private HttpSession mockHttpSession;

    @Mocked
    private Message msg;

    @Mocked
    private ActionMessages messages;

    @Mocked
    private ActionMessages errors;

    /**
     * ActionMappingToolのオブジェクトを生成できること
     */
    @Test
    public void testCreateStrutsMapping() {
        // テストコードを実施
        this.handle(Arrays.asList(new HttpResourceHolderHandler(),(data, context) -> {
            assertNotNull(new Action().createStrutsMapping(mockHttpRequest, context));
            return null;
        }));
    }

    /**
     * サーブレットレスポンスを返すことができること
     */
    @Test
    public void testCreateStrutsResponse() {
        // テストコードを実施
        this.handle(Arrays.asList(new HttpResourceHolderHandler(),(data, context) -> {
            assertNotNull(new Action().createStrutsResponse(context));
            return null;
        }));
    }

    /**
     * サーブレットリクエストを返すことができること
     */
    @Test
    public void testCreateStrutsRequest() {
        // テストコードを実施
        this.handle(Arrays.asList(new HttpResourceHolderHandler(),(data, context) -> {
            assertNotNull(new Action().createStrutsRequest(context));
            return null;
        }));
    }

    /**
     * キャンセルの確認テスト
     */
    @Test
    public void testIsCancelledFalse() {

        // テストコードを実施
        this.handle(Arrays.asList(new HttpResourceHolderHandler(),(data, context) -> {
            HttpServletRequest request = RequestUtil.getRequest();
            boolean b =new Action().isCancelled(request);
            assertFalse(b);
            return null;
        }));
    }

    /**
     * キャンセルの確認テスト
     */
    @Test
    public void testIsCancelledTrue() {
        new Expectations() {{
            httpServletRequest.getAttribute(Globals.CANCEL_KEY);
            result = "cancelled";
        }};

        // テストコードを実施
        this.handle(Arrays.asList(new HttpResourceHolderHandler(),(data, context) -> {
            HttpServletRequest request = RequestUtil.getRequest();
            boolean b =new Action().isCancelled(request);
            assertTrue(b);
            return null;
        }));
    }

    /**
     * メッセージをリクエストに格納されること。(メッセージある)
     *
     */
    @Test
    public void testSaveMessages1() {

        List<Message> msgs = new ArrayList<Message>();
        msgs.add(msg);
        new Expectations() {{
            messages.getMessages();
            result = msgs;

            msg.formatMessage();
            result = "";
        }};

        // テストコードを実施
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            Action action = new Action();
            HttpServletRequest request = RequestUtil.getRequest();
            action.saveMessages(request, messages);
            new Verifications() {
                {
                    httpServletRequest.setAttribute(anyString, any);
                    times = 1;
                    httpServletRequest.removeAttribute(anyString);
                    times = 0;
                }
            };

            return null;
        }));
    }

    /**
     * メッセージをリクエストに格納されること。(メッセージが空白)
     *
     */
    @Test
    public void testSaveMessages2() {
        new Expectations() {{
            messages.isEmpty();
            result = true;
        }};

        // テストコードを実施
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            Action action = new Action();
            HttpServletRequest request = RequestUtil.getRequest();
            action.saveMessages(request, messages);
            new Verifications() {
                {
                    httpServletRequest.setAttribute(anyString, any);
                    times = 0;
                    httpServletRequest.removeAttribute(anyString);
                    times = 1;
                }
            };

            return null;
        }));
    }

    /**
     * メッセージをリクエストに格納されること。(メッセージがnull)
     *
     */
    @Test
    public void testSaveMessages3() {

        // テストコードを実施
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            Action action = new Action();
            HttpServletRequest request = RequestUtil.getRequest();
            action.saveMessages(request, null);
            new Verifications() {
                {
                    httpServletRequest.setAttribute(anyString, any);
                    times = 0;
                    httpServletRequest.removeAttribute(anyString);
                    times = 1;
                }
            };

            return null;
        }));
    }

    /**
     * エラーをリクエストに格納されること。(エラー情報がある)
     *
     */
    @Test
    public void testSaveErrors1() {

        // テストコードを実施
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            Action action = new Action();
            HttpServletRequest request = RequestUtil.getRequest();

            action.saveErrors(request, errors);
            new Verifications() {
                {
                    httpServletRequest.setAttribute(anyString, any);
                    times = 1;
                    httpServletRequest.removeAttribute(anyString);
                    times = 0;
                }
            };

            return null;
        }));
    }

    /**
     * エラーをリクエストに格納されること。(エラー情報が空白)
     *
     */
    @Test
    public void testSaveErrors2() {
        new Expectations() {{
            errors.isEmpty();
            result = true;
        }};

        // テストコードを実施
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            Action action = new Action();
            HttpServletRequest request = RequestUtil.getRequest();
            action.saveErrors(request, errors);
            new Verifications() {
                {
                    httpServletRequest.setAttribute(anyString, any);
                    times = 0;
                    httpServletRequest.removeAttribute(anyString);
                    times = 1;
                }
            };

            return null;
        }));
    }

    /**
     * エラーをリクエストに格納されること。(エラー情報がnull)
     *
     */
    @Test
    public void testSaveErrors3() {

        // テストコードを実施
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            Action action = new Action();
            HttpServletRequest request = RequestUtil.getRequest();
            action.saveErrors(request, null);
            new Verifications() {
                {
                    httpServletRequest.setAttribute(anyString, any);
                    times = 0;
                    httpServletRequest.removeAttribute(anyString);
                    times = 1;
                }
            };

            return null;
        }));
    }

    /**
     * エラーをセッションに格納されること。（エラー情報がある）
     *
     */
    @Test
    public void testSaveErrorsToSession1() {

        // テストコードを実施
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            Action action = new Action();
            action.saveErrors(mockHttpSession, errors);
            new Verifications() {
                {
                    mockHttpSession.setAttribute(anyString, any);
                    times = 1;
                    mockHttpSession.removeAttribute(anyString);
                    times = 0;
                }
            };

            return null;
        }));

    }

    /**
     * エラーをセッションに格納されること。（エラー情報が空白）
     *
     */
    @Test
    public void testSaveErrorsToSession2() {
        new Expectations() {{
            errors.isEmpty();
            result = true;
        }};
        // テストコードを実施
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            Action action = new Action();
            action.saveErrors(mockHttpSession, errors);
            new Verifications() {
                {
                    mockHttpSession.setAttribute(anyString, any);
                    times = 0;
                    mockHttpSession.removeAttribute(anyString);
                    times = 1;
                }
            };

            return null;
        }));

    }

    /**
     * エラーをセッションに格納されること。（エラー情報がある）
     *
     */
    @Test
    public void testSaveErrorsToSession3() {

        // テストコードを実施
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            Action action = new Action();
            action.saveErrors(mockHttpSession, null);
            new Verifications() {
                {
                    mockHttpSession.setAttribute(anyString, any);
                    times = 0;
                    mockHttpSession.removeAttribute(anyString);
                    times = 1;
                }
            };

            return null;
        }));

    }

    @Override
    protected void setClassToRegist() {
        registClassList = Arrays.asList(HttpResourceHolder.class);
    }

}