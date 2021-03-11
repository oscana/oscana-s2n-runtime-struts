package oscana.s2n.struts.action;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

/**
 * {@link ActionMapping}のテスト。
 *
 */
public class ActionMappingTest {

    /**
     * actionMappingのオブジェクトを生成できること。
     */
    @Test
    public void testActionForward() {
        Map<String, ActionForward> forwardMap = new HashMap<String, ActionForward>();
        forwardMap.put("name", new ActionForward());
        ActionMapping mapping = new ActionMapping(forwardMap);
        assertNull(mapping.getActionId());
        assertNull(mapping.getAttribute());
        assertEquals("session", mapping.getScope());
        assertTrue(mapping.isValidate());
        assertNull(mapping.getPath());
        assertNull(mapping.getParameter());
        assertNull(mapping.getType());
        assertNull(mapping.getName());

        mapping.setActionId("12345");
        mapping.setAttribute("attribute");
        mapping.setScope("sessionScope");
        mapping.setValidate(false);
        mapping.setPath("/test");
        mapping.setParameter("parameter");
        mapping.setType("type");
        mapping.setName("name");

        assertNotNull(mapping.findForward("name"));
        assertEquals("12345", mapping.getActionId());
        assertEquals("attribute", mapping.getAttribute());
        assertEquals("sessionScope", mapping.getScope());
        assertFalse(mapping.isValidate());
        assertEquals("/test", mapping.getPath());
        assertEquals("parameter", mapping.getParameter());
        assertEquals("type", mapping.getType());
        assertEquals("name", mapping.getName());
    }

    /**
     * Name属性を設定してない場合、nullを返却すること。
     */
    @Test
    public void testActionForward_null() {
        Map<String, ActionForward> forwardMap = new HashMap<String, ActionForward>();
        ActionMapping mapping = new ActionMapping(forwardMap);

        assertNull(mapping.findForward("name"));
    }

}