package oscana.s2n.testLibrary.utill;

import javax.servlet.http.HttpServletRequest;

import nablarch.fw.dicontainer.nablarch.Containers;
import oscana.s2n.handler.HttpResourceHolder;

/**
 * リクエストに関するユーティリティです。<br>
 *
 */
public final class RequestUtil {


    /**
     * リクエストを返却する。
     *
     * @return リクエスト
     */
    public static HttpServletRequest getRequest() {

        HttpResourceHolder resource = Containers.get().getComponent(HttpResourceHolder.class);
        return resource.getHttpServletRequest();
    }

}