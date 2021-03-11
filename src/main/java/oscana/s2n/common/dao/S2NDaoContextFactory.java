package oscana.s2n.common.dao;

import nablarch.common.dao.DaoContext;
import nablarch.common.dao.DaoContextFactory;

/**
 * {@link S2NDaoContext}を生成する{@link DaoContextFactory}。
 *
 * {@link DaoContext}をカスタマイズするためにこのクラスを作成した。
 */
public class S2NDaoContextFactory extends DaoContextFactory {

    private DaoContextFactory daoContextFactory;

    /**
     * daoContextFactoryを設定する。
     *
     * @param daoContextFactory DaoContextのファクトリ
     */
    public void setDaoContextFactory(DaoContextFactory daoContextFactory) {
        this.daoContextFactory = daoContextFactory;
    }

    /**
     * DaoContextを生成する。
     *
     * @return DaoContext
     */
    @Override
    public DaoContext create() {
        return new S2NDaoContext(daoContextFactory.create());
    }
}
