package oscana.s2n.struts.action;

import static org.junit.Assert.*;

import org.junit.Test;

import nablarch.fw.ExecutionContext;
import nablarch.fw.web.HttpRequest;
import oscana.s2n.testCommon.S2NMockHttpRequest;

/**
 * {@link ActionForward}のテスト。
 *
 */
public class ActionForwardTest {

    private S2NMockHttpRequest request = new S2NMockHttpRequest();

    /**
     * 引数なしのactionForwardのテスト
     */
    @Test
    public void testActionForward() {
        ActionForward actionForward = new ActionForward();
        assertNull(actionForward.getName());
        assertNull(actionForward.getPath());

        actionForward.setName("test");
        actionForward.setPath("/test");
        assertEquals("test", actionForward.getName());
        assertEquals("/test", actionForward.getPath());
    }

    /**
     * 引数pathを付けるactionForwardのテスト
     */
    @Test
    public void testActionForward_onlyPath() {
        ActionForward actionForward = new ActionForward("/test");
        assertEquals("/test", actionForward.getPath());
    }

    /**
     * 引数pathを付けるactionForwardのテスト
     */
    @Test
    public void testActionForward_httpRequestAndExecutionContext() {
        HttpRequest nabHttpRequest = request;
        ExecutionContext executionContext = new ExecutionContext();
        ActionForward actionForward = new ActionForward(nabHttpRequest, executionContext);
        actionForward.setPath("/test");
        assertNotNull(actionForward.toResponse());
        assertEquals("forward:///test", actionForward.toResponse().getContentPath().toString());
    }

    /**
     * 引数pathを付けるactionForwardのテスト
     */
    @Test
    public void testActionForward_httpRequestAndExecutionContextForJsp() {
        HttpRequest nabHttpRequest = request;
        ExecutionContext executionContext = new ExecutionContext();
        ActionForward actionForward = new ActionForward(nabHttpRequest, executionContext);
        actionForward.setPath("/test/test.jsp");
        assertNotNull(actionForward.toResponse());
        assertEquals("servlet:///test/test.jsp", actionForward.toResponse().getContentPath().toString());
    }
}