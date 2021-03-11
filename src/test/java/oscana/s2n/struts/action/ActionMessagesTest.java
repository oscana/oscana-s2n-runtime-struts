package oscana.s2n.struts.action;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * {@link ActionMessages}のテスト。
 *
 */
public class ActionMessagesTest {
    protected ActionMessages aMsgs = null;
    protected ActionMessages aMsgsForAdd = null;
    protected ActionMessages anMsgs = null;
    protected ActionMessage msg = null;

    @Before
    public void setUp() {
        aMsgs = new ActionMessages();
        anMsgs = new ActionMessages();
        aMsgsForAdd = new ActionMessages(aMsgs);

        Object[] objs1 = new Object[] { "a", "b", "c", "d", "e" };
        msg = new ActionMessage("aMessage", objs1);
    }

    @Test
    public void testEmpty() {
        assertTrue(aMsgs.isEmpty());
        assertTrue(aMsgsForAdd.isEmpty());
    }

    @Test
    public void testNotEmpty() {
        aMsgs.add("myProp", msg);
        assertFalse(aMsgs.isEmpty());
        assertFalse(aMsgsForAdd.isEmpty());
    }

    @Test
    public void testClear() {
        aMsgs.add("int", "aMessage");
        assertEquals(1, aMsgs.size());
        aMsgs.clear();
        assertEquals(0, aMsgs.size());
    }

    @Test
    public void testAdd_messageId() {
        aMsgs.add("messageId", "aMessage");
        assertEquals(1, aMsgs.size());
        assertEquals("messageId=[aMessage] propertyName=[messageId]", aMsgs.getMessages().get(0).toString());
    }

    @Test
    public void testAdd_actionMessages() {
        assertEquals(0, aMsgs.size());
        anMsgs.add("actionMessagesError1", "aMessage");
        anMsgs.add("actionMessagesError2", "aMessage");
        aMsgs.add(anMsgs);
        assertEquals(2, aMsgs.size());
        assertEquals("messageId=[aMessage] propertyName=[actionMessagesError1]", aMsgs.getMessages().get(0).toString());
        assertEquals("messageId=[aMessage] propertyName=[actionMessagesError2]", aMsgs.getMessages().get(1).toString());
    }

    @Test
    public void testAdd_actionMessage() {
        assertEquals(0, aMsgs.size());
        aMsgs.add("actionMessage", msg);
        assertEquals(1, aMsgs.size());
        assertEquals("messageId=[aMessage] propertyName=[actionMessage]", aMsgs.getMessages().get(0).toString());
    }

}
