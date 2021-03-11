package oscana.s2n.struts.upload;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Arrays;

import org.junit.Test;

/**
 * {@link FormFile}のテスト。
 *
 */
public class FormFileTest {
    private String sp = System.getProperty("file.separator");

    /**
     * アップロードされるファイルのタイプ、ファイル名、ファイル内容を正常に取得できる
     * @throws IOException
     * @throws FileNotFoundException
     */
    @Test
    public void testFormFile() throws FileNotFoundException, IOException {
        byte[] fileByte = new byte[] { 49, 44, 50, 44, 51 };

        FormFile formFile = new FormFile();

        File file = new File("src" + sp + "test" + sp + "resources" + sp + "oscana" + sp + "s2n" + sp + "struts" + sp +
                "upload"  + sp + "uploadUtilTestFile1.csv");
        formFile.setContentType("UTF-8");
        formFile.setFileName("uploadTestFile");
        formFile.setSaveFile(file);

        assertEquals("UTF-8", formFile.getContentType());
        assertEquals("uploadTestFile", formFile.getFileName());
        assertEquals("src" + sp + "test" + sp + "resources" + sp + "oscana" + sp + "s2n" + sp + "struts" + sp +
                "upload" + sp + "uploadUtilTestFile1.csv", formFile.getSaveFile().toString());
        assertEquals(5, formFile.getFileSize());
        assertNotNull(formFile.getInputStream());
        assertEquals(Arrays.toString(fileByte), Arrays.toString(formFile.getFileData()));
        formFile.destroy();
    }

    /**
     * ファイルが存在してない場合、０を戻すこと
     * @throws IOException
     */
    @Test
    public void testgetFileSiz() throws Exception {
        FormFile formFile = new FormFile();
        assertEquals(0, formFile.getFileSize());
    }
}