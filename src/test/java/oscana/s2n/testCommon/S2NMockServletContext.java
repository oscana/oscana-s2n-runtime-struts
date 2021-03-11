package oscana.s2n.testCommon;

import java.util.HashMap;
import java.util.Map;

public class S2NMockServletContext {

    private final Map<String, String> initParams = new HashMap<String, String>();

    public void setInitParameter(String key, String value) {
        initParams.put(key, value);
    }

    public Map<String, String> getInitParams() {
        return initParams;
    }
}
