package oscana.s2n.struts.action;

import static org.junit.Assert.*;

import org.junit.Test;

import nablarch.fw.ExecutionContext;
import nablarch.fw.web.HttpRequest;
import oscana.s2n.testCommon.S2NMockHttpRequest;

/**
 * {@link ActionMappingTool}のテスト。
 *
 */
public class ActionMappingToolTest {

    private S2NMockHttpRequest request = new S2NMockHttpRequest();

    /**
     * ActionMappingToolのオブジェクトを生成できること。
     */
    @Test
    public void testActionMappingTool() {
        HttpRequest nabHttpRequest = request;
        ExecutionContext executionContext = new ExecutionContext();
        ActionMappingTool actionMappingTool = new ActionMappingTool(nabHttpRequest, executionContext);

        assertNotNull(actionMappingTool.add("name", "/test"));
        assertNotNull(actionMappingTool.createActionMapping());
    }
}