package oscana.s2n.struts.action;

import java.util.HashMap;
import java.util.Map;

import nablarch.fw.ExecutionContext;
import nablarch.fw.web.HttpRequest;

/**
 * StrutsのActionMappingをNablarch上で再現するためのオブジェクト生成するクラス。<br>
 * <br>
 *   ・StrutsのActionMappingはStruts-config.xmlの中身が反映されるが、NablarchにはStruts-config.xmlが無い。<br>
 *   ・OscanaではStruts-config.xmlの内容をActionのソースコード上に展開することでActionMappingの動作を再現する。<br>
 *   ・ActionMappingToolはActionMappingを生成するためのツールである。<br>
 *
 * @author Fumihiko Yamamoto
 *
 */
public class ActionMappingTool {

    /** プライベート変数nabRequest */
    private HttpRequest nabHttpRequest;
    /** プライベート変数context */
    private ExecutionContext context;

    /** プライベート変数forwardMap */
    private Map<String, ActionForward> forwardMap;

    /**
     * コンストラクタ。
     * @param nabHttpRequest リクエスト
     * @param context コンテキスト
     */
    public ActionMappingTool(HttpRequest nabHttpRequest, ExecutionContext context) {
        super();
        this.forwardMap = new HashMap<>();
        this.nabHttpRequest = nabHttpRequest;
        this.context = context;
    }

    /**
     * ActionForwardを追加する。
     * @param name 名前
     * @param path パス
     * @return ActionMappingTool
     */
    public ActionMappingTool add(String name, String path) {
        ActionForward forward = new ActionForward(this.nabHttpRequest, context);
        forward.setName(name);
        forward.setPath(path);
        this.forwardMap.put(forward.name, forward);
        return this;
    }

    /**
     * これまで設定した内容に応じたActionMappingを生成する。
     * @return ActionMapping
     */
    public ActionMapping createActionMapping() {

        return new ActionMapping(forwardMap);
    }

}