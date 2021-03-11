package oscana.s2n.common.web.download;

import java.io.IOException;
import java.io.InputStream;

import nablarch.core.util.FileUtil;
import nablarch.fw.web.HttpResponse;

/**
 * ストリームからHTTPレスポンスメッセージを生成するクラス。
 * <p/>
 * 本クラスは入力ストリームトデータのダウンロードに使用する。
 * 入力ストリームのクローズは本クラスで行う。
 *
 * @author Rai Shuu
 */
public class S2NStreamResponse extends HttpResponse {

    /**
     * {@code S2NStreamResponse}オブジェクトを生成する。
     * <p/>
     * 入力ストリームのクローズはこのメソッドで行う。
     *
     * @param inputStream 入力ストリーム
     */
    public S2NStreamResponse(InputStream inputStream) {
        initialize(inputStream);
    }

    private void initialize(InputStream inputStream) {
        int length;
        byte[] src = new byte[512];
        try {
            while ((length = inputStream.read(src)) != -1) {
                byte[] dest = new byte[length];
                System.arraycopy(src, 0, dest, 0, length);
                write(dest);
            }
        } catch (IOException e) {
            throw new RuntimeException("an error occurred while writing a response.", e);
        } finally {
            FileUtil.closeQuietly(inputStream);
        }
    }
}
