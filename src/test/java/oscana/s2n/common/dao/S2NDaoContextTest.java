package oscana.s2n.common.dao;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import nablarch.common.dao.DaoContext;
import nablarch.common.dao.DaoContextFactory;
import nablarch.core.repository.SystemRepository;
import nablarch.test.core.db.DbAccessTestSupport;
import oscana.s2n.common.ParamFilter;
import oscana.s2n.testLibrary.entity.Project;
import oscana.s2n.testLibrary.utill.BeanMap;

/**
 * {@link S2NDaoContext}のテスト
 */
public class S2NDaoContextTest extends DbAccessTestSupport {

    S2NDaoContext s2NDaoContext = null;

    @Before
    public void createS2NDaoContext() {
        S2NDaoContextFactory s2nfactory = new S2NDaoContextFactory();
        DaoContextFactory factory = SystemRepository.get("daoContextFactory");
        s2nfactory.setDaoContextFactory(factory);
        s2NDaoContext = (S2NDaoContext) s2nfactory.create();
    }

    /**
     * IDを設定して、データを取得すること
     */
    @Test
    public void testFindById() {
       Project result = s2NDaoContext.findById(Project.class, "0000001017");
       assertEquals("0000001017", result.getProjectId());
    }

    /**
     * 違うIDを指定した場合、nullを返すこと
     */
    @Test
    public void testFindById_Null() {
       Project result = s2NDaoContext.findById(Project.class, "000000");
       assertNull(result);
    }

    /**
     * 検索条件なしの場合、全件データを取得すること
     */
    @Test
    public void testFindAll() {
       List<Project> result = s2NDaoContext.findAll(Project.class);
       assertEquals(2, result.size());
    }

    /**
     * 引数がある場合、sqlファイルを使ってデータを取得すること
     */
    @Test
    public void testFindAllBySqlFile01() {
       BeanMap map = new BeanMap();
       map.put("projectId", "0000001017");
       List<Project> result = s2NDaoContext.findAllBySqlFile(Project.class,ParamFilter.sqlFileNameToKey("oscana.s2n.common.dao.S2NDaoContextTest#testFindAllBySqlFile01"),map);
       assertEquals(1,result.size());
       assertEquals("0000001017",result.get(0).getProjectId());
    }

    /**
     * 引数がない場合、sqlファイルを使ってデータを取得すること
     */
    @Test
    public void testFindAllBySqlFile02() {
       List<Project> result = s2NDaoContext.findAllBySqlFile(Project.class,ParamFilter.sqlFileNameToKey("oscana.s2n.common.dao.S2NDaoContextTest#testFindAllBySqlFile02"));
       assertEquals(2, result.size());
    }

    /**
     * 引数がある場合、sqlファイルを使って一件データを取得すること
     */
    @Test
    public void testFindBySqlFile() {
       BeanMap map = new BeanMap();
       map.put("projectId", "0000001017");
       Project result = s2NDaoContext.findBySqlFile(Project.class,ParamFilter.sqlFileNameToKey("oscana.s2n.common.dao.S2NDaoContextTest#testFindBySqlFile"),map);
       assertEquals("0000001017",result.getProjectId());
    }


    /**
     * 引数が違う場合、sqlファイルを使ってデータを取得しないこと
     */
    @Test
    public void testFindBySqlFile_Null() {
       BeanMap map = new BeanMap();
       map.put("projectId", "000000");
       Project result = s2NDaoContext.findBySqlFile(Project.class,ParamFilter.sqlFileNameToKey("oscana.s2n.common.dao.S2NDaoContextTest#testFindBySqlFile"),map);
       assertNull(result);
    }

    /**
     * 引数がある場合、sqlファイルを使ってデータの件数を取得すること
     */
    @Test
    public void testCountBySqlFile() {
       BeanMap map = new BeanMap();
       map.put("projectId", "0000001017");
       long result = s2NDaoContext.countBySqlFile(Project.class,ParamFilter.sqlFileNameToKey("oscana.s2n.common.dao.S2NDaoContextTest#testCountBySqlFile"),map);
       assertEquals(1,result);
    }

    /**
     * データを更新すること
     */
    @Test
    public void testUpdate() {
       Project expect = s2NDaoContext.findById(Project.class, "0000001017");
       expect.setProjectNm("test");
       s2NDaoContext.update(expect);
       Project result = s2NDaoContext.findById(Project.class, "0000001017");
       assertEquals("test",result.getProjectNm());
    }

    /**
     * バッチでデータを更新すること
     */
    @Test
    public void testBatchUpdate() {
       Project expect01 = s2NDaoContext.findById(Project.class, "0000001017");
       Project expect02 = s2NDaoContext.findById(Project.class, "0000000001");
       List<Project> projectList = new ArrayList<>();
       expect01.setProjectNm("sample");
       expect02.setProjectNm("sample");
       projectList.add(expect01);
       projectList.add(expect02);
       s2NDaoContext.batchUpdate(projectList);
       Project result01 = s2NDaoContext.findById(Project.class, "0000001017");
       Project result02 = s2NDaoContext.findById(Project.class, "0000000001");
       assertEquals("sample",result01.getProjectNm() );
       assertEquals("sample",result02.getProjectNm());
    }

    /**
     * データを追加すること
     */
    @Test
    public void testInsert() {
       Project expect = s2NDaoContext.findById(Project.class, "0000001017");
       expect.setProjectId("0000");
       s2NDaoContext.insert(expect);
       Project result = s2NDaoContext.findById(Project.class, "0000");
       assertEquals("0000",result.getProjectId());
       // テストデータを削除する
       s2NDaoContext.delete(result);
    }

    /**
     * バッチでデータを追加すること
     */
    @Test
    public void testBatchInsert() {
       Project expect01 = s2NDaoContext.findById(Project.class, "0000001017");
       Project expect02 = s2NDaoContext.findById(Project.class, "0000000001");
       expect01.setProjectId("0000");
       expect02.setProjectId("0001");
       List<Project> projectList = new ArrayList<>();
       projectList.add(expect01);
       projectList.add(expect02);
       s2NDaoContext.batchInsert(projectList);
       Project result01 = s2NDaoContext.findById(Project.class, "0000");
       Project result02 = s2NDaoContext.findById(Project.class, "0001");
       assertEquals("0000",result01.getProjectId());
       assertEquals("0001",result02.getProjectId());
       // テストデータを削除する
       s2NDaoContext.batchDelete(projectList);
    }

    /**
     * データを削除すること
     */
    @Test
    public void testDelete() {
       Project expect = s2NDaoContext.findById(Project.class, "0000001017");
       expect.setProjectId("0000");
       s2NDaoContext.insert(expect);
       Project result = s2NDaoContext.findById(Project.class, "0000");
       assertEquals("0000",result.getProjectId());
       // テストデータを削除する
       s2NDaoContext.delete(result);
       Project resultDelete = s2NDaoContext.findById(Project.class, "0000");
       assertNull(resultDelete);
    }

    /**
     * バッチでデータを削除すること
     */
    @Test
    public void testBatchDelete() {
       Project expect01 = s2NDaoContext.findById(Project.class, "0000001017");
       Project expect02 = s2NDaoContext.findById(Project.class, "0000000001");
       expect01.setProjectId("0000");
       expect02.setProjectId("0001");
       List<Project> projectList = new ArrayList<>();
       projectList.add(expect01);
       projectList.add(expect02);
       s2NDaoContext.batchInsert(projectList);
       Project result01 = s2NDaoContext.findById(Project.class, "0000");
       Project result02 = s2NDaoContext.findById(Project.class, "0001");
       assertEquals("0000",result01.getProjectId());
       assertEquals("0001",result02.getProjectId());
       // テストデータを削除する
       s2NDaoContext.batchDelete(projectList);
       Project resultDelete01 = s2NDaoContext.findById(Project.class, "0000");
       Project resultDelete02 = s2NDaoContext.findById(Project.class, "0001");
       assertNull(resultDelete01);
       assertNull(resultDelete02);
    }

    /**
     * ページングの何ページ目を検索するかを指定すること
     */
    @Test
    public void testPage() {
      DaoContext result = s2NDaoContext.page(1);
      assertNotNull(result);
    }

    /**
     * ページ内表示件数を設定すること
     */
    @Test
    public void testPer() {
      DaoContext result = s2NDaoContext.per(1);
      assertNotNull(result);
    }

    /**
     * 検索結果の取得を遅延させること
     */
    @Test
    public void testDefer() {
      DaoContext result = s2NDaoContext.defer();
      assertNotNull(result);
    }
}
