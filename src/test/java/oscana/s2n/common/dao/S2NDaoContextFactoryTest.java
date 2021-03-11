package oscana.s2n.common.dao;

import static org.junit.Assert.*;

import org.junit.Test;

import nablarch.common.dao.DaoContextFactory;
import nablarch.core.repository.SystemRepository;
import nablarch.test.core.db.DbAccessTestSupport;

/**
 * {@link S2NDaoContextFactory}のテスト
 */
public class S2NDaoContextFactoryTest extends DbAccessTestSupport {

    /**
     * DaoContextのインスタンスを作成すること
     */
    @Test
    public void testCreate() {
        S2NDaoContextFactory s2nfactory = new S2NDaoContextFactory();
        DaoContextFactory factory = SystemRepository.get("daoContextFactory");
        s2nfactory.setDaoContextFactory(factory);
        S2NDaoContext daoContext = (S2NDaoContext) s2nfactory.create();
        assertNotNull(daoContext);
    }
}
