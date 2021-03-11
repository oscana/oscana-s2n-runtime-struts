package oscana.s2n.struts.action;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * {@link ActionErrors}のテスト。
 *
 */
public class ActionErrorsTest {

    /**
     * ActionErrorsの空オブジェクトを生成できることを確認する</br>
     * ActionErrorsのメッセージ情報を含まれるオブジェクトを生成できることを確認する
     */
    @Test
    public void testActionErrors() {
        ActionErrors actionErrors = new ActionErrors();
        ActionErrors actionErrorsHasMessage = new ActionErrors(actionErrors);
        assertNotNull(actionErrors);
        assertNotNull(actionErrorsHasMessage);
    }
}