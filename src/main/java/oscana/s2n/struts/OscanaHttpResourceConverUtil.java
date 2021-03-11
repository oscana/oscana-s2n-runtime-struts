package oscana.s2n.struts;

import javax.servlet.http.HttpSession;

import nablarch.core.log.Logger;
import nablarch.core.log.LoggerManager;
import nablarch.fw.ExecutionContext;
import nablarch.fw.dicontainer.nablarch.Containers;
import nablarch.fw.web.HttpRequest;
import nablarch.fw.web.HttpResponse;
import nablarch.fw.web.servlet.ServletExecutionContext;
import net.unit8.http.router.Routes;
import net.unit8.http.router.RoutingException;
import oscana.s2n.handler.HttpResourceHolder;

/**
 * SAStrutsとNablarchのデータ型の違いを吸収させるためのデータ変換のユーティリティクラス。<br>
 * <br>
 * 本APIは自動生成されたソースコード中に埋め込まれ、データ変換をサポートする。<br>
 * <br>
 * @author Fumihiko Yamamoto
 *
 */
public class OscanaHttpResourceConverUtil {

    /**
     * SAStrutsの戻り値(ファイル名)をNablarchの戻り値(HttpResponse型)に変換する。
     * @param obj SAStrutsの戻り値として返されるパス名
     * @param action Actionクラス(thisを設定)
     * @param nabRequest NablarchのHttpRequest
     * @param context NablarchのExecutionContext
     * @param actionName アクションの名前
     * @return 変換されたHttpResponse
     */
    public static HttpResponse createHttpResponse(Object obj, Object action, HttpRequest nabRequest,
            ExecutionContext context, String actionName) {

        String text = null;

        HttpResourceHolder resource = Containers.get().getComponent(HttpResourceHolder.class);
        if(resource.getForcedNextResponse()!=null) {
            return resource.getForcedNextResponse();
        }

        if (obj == null) {
            // 空のレスポンスを返す
            return new HttpResponse(200);
        } else if (obj instanceof HttpResponse) {
            return (HttpResponse) obj;
        } else if (obj instanceof String) {
            text = (String) obj;
        } else {
            //通常は起こらない。想定外事象発生時は止める
            throw new UnsupportedOperationException(obj.getClass().getName());
        }

        String url;
        if (text.toLowerCase().startsWith("http://") || text.toLowerCase().startsWith("https://")) {
            url = text;
            if (url.contains("redirect=true")) {
               url = "redirect:" + url;
            }
        } else {
            if (text.endsWith("/indexBack")) {
                //BackableUtilityのindexBackの場合（ ※前提：JSPのパスはActionのパスと一致する）
                url = "forward://" +text;
            } else if (text.startsWith("/") && text.endsWith("/")) {
                // 最後の「/」を取り除く
                text = text.substring(0, text.length() - 1);

                try {//パスがroutes.xmlに存在する場合、パスをそのまま返す
                    Routes.recognizePath(text, "GET");
                } catch (RoutingException e) {
                    //パスがroutes.xmlに存在しない場合、「/index」を付与する
                    text = text + "/index";
                }
                //action呼び出しの場合→forward呼び出し
                url = "forward://" + text;

            } else if (text.startsWith("/") && text.endsWith(".jsp")) {
                //jspファイル指定の場合
                url = "/WEB-INF/view" + text;
            } else {
                //一般的なPathの場合
                String subPath = "";
                Class<?> actionClazz = action.getClass();
                String fullActionName = actionClazz.getName();
                // 後ろのAction名を消す
                fullActionName = fullActionName.substring(0, fullActionName.length() - 6);
                String[] actionPath = fullActionName.split("\\.");
                boolean actionFlag = false;
                for (String str : actionPath) {

                    if (actionFlag) {
                        // 先頭の文字を小文字にする
                        subPath = subPath + "/" + new StringBuilder().append(Character.toLowerCase(str.charAt(0)))
                                .append(str.substring(1)).toString();
                    }

                    if ("action".equals(str)) {
                        actionFlag = true;
                    }

                }

                if (!text.startsWith("/")) {
                    subPath = subPath + "/";
                }
                url = "/WEB-INF/view" + subPath + text;
            }
        }
        if (url.endsWith("/")) {
            //JSPファイル名省略
            url = url + "index.jsp";
        }
        Logger logger = LoggerManager.get(OscanaHttpResourceConverUtil.class);
        logger.logDebug("jsp=" + url, (Object)null);

        return new HttpResponse(url);
    }

    /**
     * NablarchのExecutionContextからHttpSessionを取得する。
     * @param context NablarchのExecutionContext
     * @return contextから取り出したHttpSession
     */
    public static HttpSession getHttpSession(ExecutionContext context) {
        ServletExecutionContext cxt = (ServletExecutionContext) context;
        return cxt.getNativeHttpSession(true);

    }


}