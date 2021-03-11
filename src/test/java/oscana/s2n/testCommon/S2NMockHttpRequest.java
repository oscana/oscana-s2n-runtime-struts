package oscana.s2n.testCommon;

import nablarch.fw.web.MockHttpRequest;

public class S2NMockHttpRequest extends MockHttpRequest {

    public S2NMockHttpRequest(String string) {
        super(string);
    }

    public S2NMockHttpRequest() {
    }

    public void setPathInfo(String path) {
        super.setRequestPath(path);
    }
}
