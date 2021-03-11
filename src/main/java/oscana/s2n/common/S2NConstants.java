package oscana.s2n.common;

/**
 * 互換ライブラリ用定数クラス。
 *
 * @author Rai Shuu
 *
 */
public class S2NConstants {

    /** フォームをリクエストスコープに設定する際に使用する変数名（"form"に固定）*/
    public static final String FORM = "form";

    /** フォームクラスをリクエストスコープに設定する際に使用する変数名（"formClass"に固定）*/
    public static final String FORM_CLASS = "formClass";

    /** フォームに設定するパラメータのプレフィックス（""に固定）*/
    public static final String FORM_PARAM_PREFIX = "";

    /** アクションメソッドをリクエストスコープに格納する際に使用する名前 */
    public static final String REQUEST_SCOPED_KEY_CALL_METHOD = "oscana.s2n.common.S2NConstants.Key.CallMethod";

    /** アクションメソッドの名前をスレッドコンテキストに格納する際に使用する名前 */
    public static final String THREAD_CONTEXT_KEY_CALL_METHOD_NAME = "oscana.s2n.common.S2NConstants.Key.CallMethodName";
}
