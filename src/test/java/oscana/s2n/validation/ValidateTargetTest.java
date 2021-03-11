package oscana.s2n.validation;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import nablarch.core.repository.SystemRepository;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.handler.HttpResponseHandler;
import oscana.s2n.testCommon.S2NNablarchTestCase;

/**
 * {@link ValidateTarget}のテスト。
 */
public class ValidateTargetTest extends S2NNablarchTestCase {

    /**
     * バリデーションの対象メッソドの場合チェックすることをテスト
     * チェックターゲットは@Required,@Length,@Pattern
     */
    @Test
    public void testIsTarget1() {
        HttpResponse res = sendReq("/targetTest1?lengthField=123456&byteLengthField=12345&patternField=2");
        assertEquals(400, res.getStatusCode());

        String [] checkResults = res.getBodyString().split("<br>");
        assertTrue(Arrays.asList(checkResults).contains("パスワードは5文字以下の値を入力してください。"));
        assertTrue(Arrays.asList(checkResults).contains("入力値はは入力形式が不正です。入力形式=0|1。"));
        assertTrue(Arrays.asList(checkResults).contains("ログインIDは必須です。"));
    }

    /**
     * バリデーションの対象メッソドの場合チェックすることをテスト
     * チェックターゲットは@Required,@ByteLength
     */
    @Test
    public void testIsTarget2() {
        HttpResponse res = sendReq("/targetTest2?lengthField=123456&byteLengthField=12345&patternField=2");
        assertEquals(400, res.getStatusCode());

        String [] checkResults = res.getBodyString().split("<br>");
        assertTrue(Arrays.asList(checkResults).contains("バイトは6バイト以上7バイト以下の値を入力してください。"));
        assertTrue(Arrays.asList(checkResults).contains("ログインIDは必須です。"));
    }

    /**
     * バリデーションの対象メッソドの場合チェックすることをテスト
     */
    @Test
    public void testIsNotTarget() {
        HttpResponse res = sendReq("/notTargetTest?lengthField=123456&systemCharField=a&fixLengthField=123&lengthRangeField=b&byteLengthField=12345&dateFormatField=202010&patternField=2");
        assertEquals(200, res.getStatusCode());
        assertEquals("",res.getBodyString());
    }

    /**
     * methodNameがnullの場合、falseを戻すこと
     */
    @Test
    public void testIsTarget() {
        ValidateTargetForTest validateTargetForTest = new ValidateTargetForTest();
        validateTargetForTest.setTargets("targetMethod2");
        assertFalse(validateTargetForTest.isTarget());

    }

    /**
     * targetsStringがnullと空の場合、trueを戻すこと
     */
    @Test
    public void testSetTargets() {
        ValidateTargetForTest validateTargetForTest = new ValidateTargetForTest();
        validateTargetForTest.setTargets(null);
        assertTrue(validateTargetForTest.isTarget());
        validateTargetForTest.setTargets("");
        assertTrue(validateTargetForTest.isTarget());

    }

    public class ValidateTargetForTest extends ValidateTarget {

    }

    /**
     * テスト用のハンドラリストを設定
     */
    @Override
    protected void setHandlerList() {
        handlerList = Arrays.asList(
                SystemRepository.get("httpCharacterEncodingHandler"),
                SystemRepository.get("httpResponseHandler"),
                SystemRepository.get("nablarchWebContextHandler"),
                SystemRepository.get("multipartHandler"),
                SystemRepository.get("httpResourceHolderHandler"),
                SystemRepository.get("threadContextHandler"),
                SystemRepository.get("threadContextClearHandler"),
                SystemRepository.get("forwardingHandler"),
                SystemRepository.get("errorHandler"),
                SystemRepository.get("sessionStoreHandler"),
                SystemRepository.get("packageMapping"),
                new HttpResponseHandler());
    }

}
