package oscana.s2n.struts.util;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Locale;

import org.junit.Test;

import mockit.Expectations;
import mockit.Mocked;
import nablarch.core.message.MessageLevel;
import nablarch.core.message.MessageUtil;
import oscana.s2n.handler.HttpResourceHolder;
import oscana.s2n.handler.HttpResourceHolderHandler;
import oscana.s2n.testCommon.S2NBaseTest;

/**
 * {@link MessageResources}のテスト。
 *
 */
public class MessageResourcesTest extends S2NBaseTest {

    @Mocked
    private MessageUtil messageUtil;

    /**
     * メッセージIDを指定する場合、メッセージを取得できること
     */
    @Test
    public void testGetMessage_onlyMessageId() {
        new Expectations() {
            {
                MessageUtil.createMessage(MessageLevel.ERROR, "messageResources1").formatMessage();
                result = "for messageResources test 1";
            }
        };
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            MessageResources messageResources = MessageResources.getMessageResources("test");
            assertEquals("for messageResources test 1", messageResources.getMessage("messageResources1"));
            return null;
        }));
    }

    /**
     * メッセージIDとパスを指定する場合、メッセージを取得できること
     */
    @Test
    public void testGetMessage_MessageIdAndPath() {
        new Expectations() {
            {
                MessageUtil.createMessage(MessageLevel.ERROR, "messageResources2", "/test").formatMessage();
                result = "/test配下にテストデータを作成しください！";
            }
        };
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            MessageResources messageResources = MessageResources.getMessageResources("test");
            assertEquals("/test配下にテストデータを作成しください！", messageResources.getMessage("messageResources2", "/test"));
            return null;
        }));
    }

    /**
     * メッセージID、パスと名前を指定する場合、メッセージを取得できること
     */
    @Test
    public void testGetMessage() {
        new Expectations() {
            {
                MessageUtil.createMessage(MessageLevel.ERROR, "messageResources3", "/test", "junit").formatMessage();
                result = "/test配下のjunitフォルダを作成しください！";
            }
        };
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            MessageResources messageResources = MessageResources.getMessageResources("test");
            assertEquals("/test配下のjunitフォルダを作成しください！", messageResources.getMessage("messageResources3", "/test", "junit"));
            return null;
        }));
    }

    /**
     * messageIdと引数を指定する場合、メッセージを取得できること
     */
    @Test
    public void testGetMessageWithObject01() throws Exception {
        Object args[] = { "テスト" };
        new Expectations() {
            {
                MessageUtil.createMessage(MessageLevel.ERROR, "messageResources2", args).formatMessage();
                result = "テスト配下にテストデータを作成しください！";
            }
        };
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            MessageResources messageResources = MessageResources.getMessageResources("test");
            assertEquals("テスト配下にテストデータを作成しください！", messageResources.getMessage("messageResources2", args));
            return null;
        }));
    }

    /**
     * messageId、ローケール、引数を設定している場合、メッセージを取得できること
     */
    @Test
    public void testGetMessageWithObject02() throws Exception {
        Locale locale = new Locale("en");
        Object args[] = { "テスト" };
        new Expectations() {
            {
                MessageUtil.createMessage(MessageLevel.ERROR, "messageResources2", args).formatMessage(locale);
                result = "テスト配下にテストデータを作成しください！";
            }
        };
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            MessageResources messageResources = MessageResources.getMessageResources("test");
            assertEquals("テスト配下にテストデータを作成しください！", messageResources.getMessage(locale, "messageResources2", args));
            return null;
        }));

    }

    /**
     * messageId、ローケールを設定している場合、メッセージを取得できること
     */
    @Test
    public void testGetMessageWithObject03() throws Exception {
        Locale locale = new Locale("en");
        new Expectations() {
            {
                MessageUtil.createMessage(MessageLevel.ERROR, "messageResources2").formatMessage(locale);
                result = "for messageResources test 1";
            }
        };
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            MessageResources messageResources = MessageResources.getMessageResources("test");
            assertEquals("for messageResources test 1", messageResources.getMessage(locale, "messageResources2"));
            return null;
        }));
    }

    /**
     * 同じのインスタンスを返すこと
     */
    @Test
    public void testGetMessageCompare() throws Exception {
        MessageResources messageResources1 = MessageResources.getMessageResources(null);
        MessageResources messageResources2 = MessageResources.getMessageResources(null);
        assertEquals(messageResources1, messageResources2);
    }

    @Override
    protected void setClassToRegist() {
        registClassList = Arrays.asList(HttpResourceHolder.class);
    }
}