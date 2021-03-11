package oscana.s2n.handler;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.junit.Test;

import mockit.Mocked;
import nablarch.fw.ExecutionContext;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.MockHttpRequest;
import oscana.s2n.common.S2NConstants;
import oscana.s2n.testCommon.S2NBaseTest;

/**
 * {@link S2NRoutesMethodBinder}のテスト。
 */
public class S2NRoutesMethodBinderTest extends S2NBaseTest {

    @Mocked
    private HttpRequest request;

    /**
     * 呼び出しメソッドのシグネチャが正しい場合、
     * メソッドが呼ばれ、メソッドの戻り値がそのまま返されること。
     */
    @Test
    public synchronized void bindForCorrectMethod() {

        final S2NRoutesMethodBinder sut = new S2NRoutesMethodBinder("handle");

        String response = (String) this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            return sut.bind(new Action()).handle(request, executionContext);
        }));

        assertThat(response, is("invoking!!!"));
        assertEquals("1", Action.beforeExecuteFlag);
        assertEquals("1", Action.actionFlag);
        assertEquals("1", Action.afterExecuteFlag);
    }

    /**
     * ExecutionContextから本MethodBinderが呼び出し先として決定したメソッドを取得する
     *
     */
    @Test
    public void testGetMethodFromExecuteContext() {
        final S2NRoutesMethodBinder sut = new S2NRoutesMethodBinder("handle");
        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            return sut.bind(new Action()).handle(request, executionContext);
        }));

        assertNotNull(executionContext.getRequestScopedVar(S2NConstants.REQUEST_SCOPED_KEY_CALL_METHOD));
    }

    /**
     * 呼び出しメソッドのシグネチャが正しくない場合、
     * メソッドが呼ばれず、{@link nablarch.fw.Result.NotFound}がスローされること。
     */
    @Test(expected = RuntimeException.class)
    public void bindForMethodNotFound() {

        final S2NRoutesMethodBinder sut = new S2NRoutesMethodBinder("incorrectMethod");

        sut.bind(new Action()).handle(new MockHttpRequest(), executionContext);
    }

    public static final class Action {

        public static String beforeExecuteFlag = "0";
        public static String actionFlag = "0";
        public static String afterExecuteFlag = "0";

        public String handle(HttpRequest request, ExecutionContext context) {
            actionFlag = "1";
            return "invoking!!!";
        }

        public String incorrectMethod(Object request, ExecutionContext context) {
            throw new RuntimeException("unreachable");
        }

        public String beforeExecute(Method method, Object form) {
            beforeExecuteFlag = "1";
            return null;
        }

        public String afterExecute(Method method, Object form) {
            afterExecuteFlag = "1";
            return null;
        }
    }

    /**
     * beforeExecute戻り値null以外場合、
     * メソッドが呼ばれないこと。
     */
    @Test
    public synchronized void bindBeforeError() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            final S2NRoutesMethodBinder sut = new S2NRoutesMethodBinder("handle");

            HttpResponse rtn = (HttpResponse) sut.bind(new ActionBeforeError()).handle(request, executionContext);

            assertNotNull(rtn);
            assertThat(rtn.getStatusCode(), is(302));
            assertEquals("1", ActionBeforeError.beforeExecuteFlag);
            assertEquals("0", ActionBeforeError.actionFlag);
            assertEquals("1", ActionBeforeError.afterExecuteFlag);
            return null;
        }));

    }

    public static class ActionBeforeError {

        static String beforeExecuteFlag = "0";
        static String actionFlag = "0";
        static String afterExecuteFlag = "0";

        public String handle(HttpRequest request, ExecutionContext context) {
            actionFlag = "1";
            return "invoking!!!";
        }

        public String beforeExecute(Method method, Object form) {
            beforeExecuteFlag = "1";
            return "http://beforeExecute Error";
        }

        public String afterExecute(Method method, Object form) {
            afterExecuteFlag = "1";
            return null;
        }
    }

    /**
     * afterExecute戻り値null以外場合、
     * afterExecute戻り値を戻すこと。
     */
    @Test
    public synchronized void bindAfterError() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            final S2NRoutesMethodBinder sut = new S2NRoutesMethodBinder("handle");

            HttpResponse rtn = (HttpResponse) sut.bind(new ActionAfterError()).handle(request, executionContext);
            assertNotNull(rtn);
            assertThat(rtn.getStatusCode(), is(302));
            assertEquals("1", ActionAfterError.beforeExecuteFlag);
            assertEquals("1", ActionAfterError.actionFlag);
            assertEquals("1", ActionAfterError.afterExecuteFlag);
            return null;
        }));

    }

    public static final class ActionAfterError {

        public static String beforeExecuteFlag = "0";
        public static String actionFlag = "0";
        public static String afterExecuteFlag = "0";

        public String handle(HttpRequest request, ExecutionContext context) {
            actionFlag = "1";
            return "invoking!!!";
        }

        public String beforeExecute(Method method, Object form) {
            beforeExecuteFlag = "1";
            return null;
        }

        public String afterExecute(Method method, Object form) {
            afterExecuteFlag = "1";
            return "http://afterExecute Error";
        }
    }

    /**
     * beforeExecute戻り値null以外場合でも、
     * afterExecuteを呼ばれること。
     */
    @Test
    public synchronized void bindBeforeErrorAfterExecute() {

        this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
            final S2NRoutesMethodBinder sut = new S2NRoutesMethodBinder("handle");

            HttpResponse rtn = (HttpResponse) sut.bind(new ActionBeforeErrorAfterExecute()).handle(request,
                    executionContext);

            assertNotNull(rtn);
            assertThat(rtn.getStatusCode(), is(302));
            assertEquals("1", ActionBeforeErrorAfterExecute.beforeExecuteFlag);
            assertEquals("0", ActionBeforeErrorAfterExecute.actionFlag);
            assertEquals("1", ActionBeforeErrorAfterExecute.afterExecuteFlag);

            return null;
        }));

    }

    public static final class ActionBeforeErrorAfterExecute {

        public static String beforeExecuteFlag = "0";
        public static String actionFlag = "0";
        public static String afterExecuteFlag = "0";

        public String handle(HttpRequest request, ExecutionContext context) {
            actionFlag = "1";
            return "invoking!!!";
        }

        public String beforeExecute(Method method, Object form) {
            beforeExecuteFlag = "1";
            return "http://beforeExecute Error";
        }

        public String afterExecute(Method method, Object form) {
            afterExecuteFlag = "1";
            return "http://calling afterExecute";
        }
    }

    /**
     * beforeExecuteException場合、
     * afterExecuteを呼ばれないこと。
     */
    @Test
    public void bindActionBeforeException() {
        HttpResponse response = null;
        try {
            response = (HttpResponse) this.handle(Arrays.asList(new HttpResourceHolderHandler(), (data, context) -> {
                final S2NRoutesMethodBinder sut = new S2NRoutesMethodBinder("handle");

                return (HttpResponse) sut.bind(new ActionBeforeException()).handle(request,
                        executionContext);
            }));
            fail();

        } catch (Exception e) {
            assertThat(e.getMessage(), is("beforeException"));
        }
        assertNull(response);
    }

    public static final class ActionBeforeException {

        public String handle(HttpRequest request, ExecutionContext context) {
            return "invoking!!!";
        }

        public String beforeExecute(Method method, Object form) {
            throw new RuntimeException("beforeException");
        }

        public String afterExecute(Method method, Object form) {
            return "http://calling afterExecute";
        }
    }

    @Override
    protected void setClassToRegist() {
        registClassList = Arrays.asList(HttpResourceHolder.class);
    }

}
