package oscana.s2n.common.web.interceptor;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Map;

import org.junit.Test;

import nablarch.common.web.session.SessionEntry;
import nablarch.core.repository.SystemRepository;
import nablarch.fw.ExecutionContext;
import nablarch.fw.Handler;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.MockHttpRequest;
import nablarch.fw.web.handler.HttpResponseHandler;
import oscana.s2n.testCommon.S2NNablarchTestCase;

/**
 * {@link Execute}のテスト。
 */
public class ExecuteTest extends S2NNablarchTestCase {

    /**
     * Executeのデフォールト設定のテスト（BeanValidationのみを実施する）
     *   validator:true,validate:"",removeActionForm:false,reset:"reset",stopOnValidationError:true
     */
    @Test
    public void testDefaultExecute() {
        HttpResponse res1 = sendReq("/defaultExecute?requiredVal=1");
        assertEquals(200, res1.getStatusCode());
    }

    /**
     * Executeのデフォールト設定のテスト（BeanValidationのみを実施する）
     *   validator:true,validate:"",removeActionForm:false,reset:"reset",stopOnValidationError:true
     */
    @Test
    public void testDefaultExecuteErr() {
        HttpResponse res1 = sendReq("/defaultExecute?requiredVal=");
        assertEquals(400, res1.getStatusCode());
        assertEquals("ログインIDは必須です。<br>", res1.getBodyString());
    }

    /**
     * removeActionFormはfalseのテスト
     */
    @Test
    public void testNotRemoveActionForm() {
        HttpResponse res1 = sendReq("/notRemoveSession?name=123");
        assertEquals(200, res1.getStatusCode());
        assertEquals("123", res1.getBodyString());
    }

    /**
     * removeActionFormはtrueのテスト
     */
    @Test
    public void testRemoveActionForm() {
        HttpResponse res2 = sendReq("/removeSession?name=123");
        assertEquals(200, res2.getStatusCode());
        assertEquals("", res2.getBodyString());
    }

    /**
     * removeActionFormはtrueと、精査エラーが起こる際のテスト
     */
    @Test
    public void testNotRemoveActionForm02() {
        HttpResponse res2 = sendReq("/notRemoveSession02?userId=test&name=1234");
        assertEquals(400, res2.getStatusCode());
        assertTrue(res2.getBodyString().toString().endsWith("1234"));
    }

    /**
     * validateのテスト:stopOnValidationError=true
     */
    @Test
    public void testValidStopWithErr() {
        HttpResponse res = sendReq("/s2nValidateStopWithErr");
        assertEquals(400, res.getStatusCode());
        assertEquals("フォームチェックエラー<br>", res.getBodyString());
    }

    /**
     * validateのテスト:stopOnValidationError=falseかつvalidate=aaa,bbb,@の場合、エラーメッセージを順序に出力すること
     */
    @Test
    public void testValidNotStopWithErr01() {
        HttpResponse res = sendReq("/s2nValidateNotStopWithErr01?userId=test&requiredVal2=");

        assertEquals(400, res.getStatusCode());
        String[] errors = res.getBodyString().split("<br>");
        assertTrue(errors.length == 3);
        assertEquals("フォームチェックエラー",errors[0]);
        assertEquals("アクションチェックエラー",errors[1]);
        assertEquals("{fieldName}はInt型として不正です。",errors[2]);
    }

    /**
     * validateのテスト:stopOnValidationError=falseかつvalidate=@,aaa,bbbの場合、エラーメッセージを順序に出力すること
     */
    @Test
    public void testValidNotStopWithErr02() {
        HttpResponse res = sendReq("/s2nValidateNotStopWithErr02?userId=test&requiredVal2=");

        assertEquals(400, res.getStatusCode());
        String[] errors = res.getBodyString().split("<br>");
        assertTrue(errors.length == 3);
        assertEquals("{fieldName}はInt型として不正です。",errors[0]);
        assertEquals("フォームチェックエラー",errors[1]);
        assertEquals("アクションチェックエラー",errors[2]);
    }

    /**
     * validateのテスト:stopOnValidationError=falseかつvalidate=aaa,@,bbbの場合、エラーメッセージを順序に出力すること
     */
    @Test
    public void testValidNotStopWithErr03() {
        HttpResponse res = sendReq("/s2nValidateNotStopWithErr03?userId=test&requiredVal2=");

        assertEquals(400, res.getStatusCode());
        String[] errors = res.getBodyString().split("<br>");
        assertTrue(errors.length == 3);
        assertEquals("フォームチェックエラー",errors[0]);
        assertEquals("{fieldName}はInt型として不正です。",errors[1]);
        assertEquals("アクションチェックエラー",errors[2]);
    }

    /**
     * validateのテスト:stopOnValidationError=falseかつvalidate=aaa,bbbの場合、単項目精査を一番前に追加されて、エラーメッセージを順序に出力すること。
     */
    @Test
    public void testValidNotStopWithErr04() {
        HttpResponse res = sendReq("/s2nValidateNotStopWithErr04?userId=test&requiredVal2=");

        assertEquals(400, res.getStatusCode());
        String[] errors = res.getBodyString().split("<br>");
        assertTrue(errors.length == 3);
        assertEquals("{fieldName}はInt型として不正です。",errors[0]);
        assertEquals("フォームチェックエラー",errors[1]);
        assertEquals("アクションチェックエラー",errors[2]);
    }

    /**
     * validateのテスト:validator = true,stopOnValidationError=falseかつvalidate=aaa,@の場合、エラーメッセージを順序に出力すること
     */
    @Test
    public void testValidNotStopWithErr05() {
        HttpResponse res = sendReq("/s2nValidateNotStopWithErr05?userId=test&requiredVal2=");

        assertEquals(400, res.getStatusCode());
        String[] errors = res.getBodyString().split("<br>");
        assertTrue(errors.length == 2);
        assertEquals("フォームチェックエラー",errors[0]);
        assertEquals("{fieldName}はInt型として不正です。",errors[1]);
    }

    /**
     * validateのテスト:validator = false,stopOnValidationError=falseかつvalidate=aaa,@の場合、精査を実施しないこと
     */
    @Test
    public void testValidNotStopWithErr06() {
        HttpResponse res = sendReq("/s2nValidateNotStopWithErr06?alphaNumVal=123&requiredVal2=");
        assertEquals(200, res.getStatusCode());
    }

    /**
     * validateのテスト:validator = false,stopOnValidationError=falseかつvalidate=""の場合、精査を実施しないこと
     */
    @Test
    public void testValidNotStopWithErr07() {
        HttpResponse res = sendReq("/s2nValidateNotStopWithErr07?alphaNumVal=123&requiredVal2=");
        assertEquals(200, res.getStatusCode());
    }

    /**
     * defaultResetメソッドの場合のテスト
     */
     @Test
     public void testDefaultReset() {

    	 //リセットメソッドによる更新
         HttpResponse res = sendReq("/defaultReset");
         assertEquals(200, res.getStatusCode());
         assertEquals("0", res.getBodyString());


         //リセットメソッドによる更新（フォーム値あり）
    	 res = sendReq("/defaultReset?resetVal=a");
         assertEquals(200, res.getStatusCode());
         assertEquals("a", res.getBodyString());
     }

     /**
      * 指定したResetメソッドが存在する場合のテスト
      */
      @Test
      public void testReset() {

          //リセットメソッドによる更新
          HttpResponse res = sendReq("/testReset");
          assertEquals(200, res.getStatusCode());
          assertEquals("test", res.getBodyString());


          //リセットメソッドによる更新（フォーム値あり）
          res = sendReq("/testReset?resetVal=test2");
          assertEquals(200, res.getStatusCode());
          assertEquals("test2", res.getBodyString());
      }

   /**
    * resetメソッド未設定場合のテスト
    */
    @Test
    public void testNoReset() {

    	//リセットメソッドがない場合
    	HttpResponse res = sendReq("/noReset");
        assertEquals(200, res.getStatusCode());
        assertEquals("1", res.getBodyString());

        //リセットメソッドがない場合（フォーム値あり）
        res = sendReq("/noReset?resetVal=a");
        assertEquals(200, res.getStatusCode());
        assertEquals("a", res.getBodyString());
    }

    /**
     * resetメソッド存在しない(指定したメソッド存在しない)場合のテスト
     */
     @Test
     public void testNoExistReset() {
         HttpResponse res = sendReq("/resetNotExist?resetVal=a");
         assertEquals(500, res.getStatusCode());
     }

     /**
      * resetメソッド存在しない(デフォルトresetメソッド存在しない)場合のテスト
      */
      @Test
      public void testHasNoReset() {
          HttpResponse res = sendReq("/hasNoReset?resetVal=a");
          assertEquals(200, res.getStatusCode());
      }

      /**
       * アクションにフィールドは存在しない場合のテスト
       */
       @Test
       public void testHasNoField() {
           HttpResponse res = sendReq("/hasNoField?resetVal=a");
           assertEquals(500, res.getStatusCode());
       }

       /**
        * アクションにFormは存在しない場合のテスト
        */
        @Test
        public void testHasNoForm() {
            HttpResponse res = sendReq("/hasNoForm");
            assertEquals(200, res.getStatusCode());
            assertThat("noFormIsOK", is(res.getBodyString()));
        }
    /**
     * Actionのフィールドをリクエストスコープに設定のテスト
     */
    @SuppressWarnings({ "unchecked"})
    @Test
    public void testActionAttrSet() {
        server.clearHandlers();
        server.setHandlerQueue(Arrays.asList(
                SystemRepository.get("httpCharacterEncodingHandler"),
                SystemRepository.get("httpResponseHandler"),
                SystemRepository.get("nablarchWebContextHandler"),
                SystemRepository.get("httpResourceHolderHandler"),
                SystemRepository.get("forwardingHandler"),
                SystemRepository.get("httpResourceHolderUpdateHandler"),
                new Handler<Object, Object>() {
                    public Object handle(Object data, ExecutionContext context) {

                        HttpResponse res = context.handleNext(data);

                        // 復路でActionの属性をリクエストに設定を確認
                        if (context.getRequestScopedVar("actionAttr") != null) {
                            res.write((String) context.getRequestScopedVar("actionAttr"));
                        }

                        return res;
                    }
                },
                SystemRepository.get("packageMapping"),
                new HttpResponseHandler()));
        HttpResponse res = sendReq("/actionFormAttr");
        assertEquals(200, res.getStatusCode());
        assertEquals("actionAttr", res.getBodyString());
    }

    /**
     * Formのフィールドをリクエストスコープに設定のテスト
     */
    @SuppressWarnings("unchecked")
    @Test
    public void testFormAttrSet() {
        server.clearHandlers();
        server.setHandlerQueue(Arrays.asList(
                SystemRepository.get("httpCharacterEncodingHandler"),
                SystemRepository.get("httpResponseHandler"),
                SystemRepository.get("nablarchWebContextHandler"),
                SystemRepository.get("httpResourceHolderHandler"),
                SystemRepository.get("forwardingHandler"),
                SystemRepository.get("httpResourceHolderUpdateHandler"),
                new Handler<Object, Object>() {
                    public Object handle(Object data, ExecutionContext context) {

                        HttpResponse res = context.handleNext(data);

                        // 復路でFormの属性をリクエストに設定を確認
                        if (context.getRequestScopedVar("formAttr") != null) {
                            res.write((String) context.getRequestScopedVar("formAttr"));
                        }

                        return res;
                    }
                },
                SystemRepository.get("packageMapping"),
                new HttpResponseHandler()));
        HttpResponse res = sendReq("/actionFormAttr");
        assertEquals(200, res.getStatusCode());
        assertEquals("formAttr", res.getBodyString());
    }

    /**
     * ノーマライズ処理をテストする。
     * @throws UnsupportedEncodingException
     *
     * @throws Exception
     */
    @Test
    public void testNormalizer() throws UnsupportedEncodingException {
        HttpResponse res1 = sendReq("/normalize?spaceVal=&trimVal=%20123%20");
        assertEquals(200, res1.getStatusCode());
        String[] vals = res1.getBodyString().split("<br>");
        assertTrue(vals.length == 2);
        assertEquals("spaceVal=",vals[0]);
        assertEquals("trimVal= 123 ",vals[1]);
    }

    /**
     * ノーマライズ処理をテストする。
     * @throws UnsupportedEncodingException
     *
     * @throws Exception
     */
    @Test
    public void testSetValueWithValid() throws UnsupportedEncodingException {
        HttpResponse res1 = sendReq("/setValWithValid");
        assertEquals(200, res1.getStatusCode());
        String val = res1.getBodyString();
        assertEquals("setByValid",val);
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
                SystemRepository.get("forwardingHandler"),
                SystemRepository.get("errorHandler"),
                SystemRepository.get("httpResourceHolderUpdateHandler"),
                new Handler<Object, Object>() {
                    public Object handle(Object data, ExecutionContext context) {
                        // 往路でセッションのタイムアウト時間を設定
                        context.setSessionScopedVar(ExecutionContext.FW_PREFIX + "sessionStore_expiration_date",
                                9223372036854775807L);
                        HttpResponse res = context.handleNext(data);

                        // 復路でsessionStore確認
                        for (Object obj : context.getSessionStoreMap().values()) {
                            if (obj instanceof SessionEntry) {
                                SessionEntry sessionEntry = (SessionEntry) obj;
                                for (Map.Entry<String, Object> item : sessionEntry.entrySet()) {
                                    if ("name".equals(item.getKey())) {
                                        Object sessionStored = item.getValue();
                                        if (sessionStored != null) {
                                            res.write(sessionStored.toString());
                                        }
                                        break;
                                    }
                                }
                            }
                        }

                        // 復路でActionの属性をリクエストに設定を確認
                        if (context.getRequestScopedVar("actionAttr") != null) {
                            res.write("actionAttr" + context.getRequestScopedVar("actionAttr"));
                        }
                        // 復路でFormの属性をリクエストに設定を確認
                        if (context.getRequestScopedVar("formAttr") != null) {
                            res.write("formAttr" + context.getRequestScopedVar("formAttr"));
                        }

                        return res;
                    }
                },
                SystemRepository.get("sessionStoreHandler"),
                SystemRepository.get("packageMapping"),
                new HttpResponseHandler());

    }

    /**
     * Action取得異常のテスト
     */
    @Test(expected = RuntimeException.class)
    public void testGetActionErr() {
        Execute.Impl impl = new Execute.Impl();
        impl.handle(new MockHttpRequest(), new ExecutionContext());

    }

}
