package oscana.s2n.common.dao;

import java.util.List;

import javax.persistence.OptimisticLockException;

import nablarch.common.dao.DaoContext;
import nablarch.common.dao.EntityList;
import nablarch.common.dao.NoDataException;

/**
 * {@link DaoContext}をカスタマイズするクラス。
 *
 * 対象データが存在しない場合、{@link nablarch.common.dao.NoDataException}をスローせずにnullを返す。
 */
public class S2NDaoContext implements DaoContext {

    private DaoContext delegatee;

    public S2NDaoContext(DaoContext delegatee) {
        this.delegatee = delegatee;
    }

    /**
     * プライマリーキーによる検索を行う。
     *
     * @param <T> エンティティクラスの型
     * @param entityClass エンティティクラス
     * @param id プライマリーキー (複合キーの場合は定義順)
     * @return エンティティオブジェクト
     */
    @Override
    public <T> T findById(Class<T> entityClass, Object... id) {
        try {
            return delegatee.findById(entityClass, id);
        } catch (NoDataException e) {
            return null;
        }
    }

    /**
     * 全件の検索を行う。
     *
     * @param <T> エンティティクラスの型
     * @param entityClass エンティティクラス
     * @return 検索結果リスト。0件の場合は空リスト。
     */
    @Override
    public <T> EntityList<T> findAll(Class<T> entityClass) {
        return delegatee.findAll(entityClass);
    }

    /**
     * SQL_IDをもとにバインド変数を展開して検索処理を行う。
     *
     * @param <T> 検索結果をマッピングするBeanクラスの型
     * @param entityClass 検索結果をマッピングするBeanクラス
     * @param sqlId SQL_ID
     * @param params バインド変数
     * @return 検索結果リスト。0件の場合は空リスト。
     */
    @Override
    public <T> EntityList<T> findAllBySqlFile(Class<T> entityClass, String sqlId, Object params) {
        return delegatee.findAllBySqlFile(entityClass, sqlId, params);
    }

    /**
     * SQL_IDをもとに検索を行う。
     *
     * @param <T> 検索結果をマッピングするBeanクラスの型
     * @param entityClass 検索結果をマッピングするBeanクラス
     * @param sqlId SQL_ID
     * @return 検索結果リスト。0件の場合は空リスト。
     */
    @Override
    public <T> EntityList<T> findAllBySqlFile(Class<T> entityClass, String sqlId) {
        return delegatee.findAllBySqlFile(entityClass, sqlId);
    }

    /**
     * SQL_IDをもとに1件検索を行う。
     *
     * @param <T> 検索結果をマッピングするBeanクラスの型
     * @param entityClass 検索結果をマッピングするBeanクラス
     * @param sqlId SQL_ID
     * @param params バインド変数
     * @return エンティティオブジェクト
     */
    @Override
    public <T> T findBySqlFile(Class<T> entityClass, String sqlId, Object params) {
        try {
            return delegatee.findBySqlFile(entityClass, sqlId, params);
        } catch (NoDataException e) {
            return null;
        }
    }

    /**
     * SQL_IDをもとに結果件数を取得する。
     *
     * @param <T> エンティティクラスの型
     * @param entityClass エンティティクラス
     * @param sqlId SQL_ID
     * @param params バインド変数
     * @return 件数
     */
    @Override
    public <T> long countBySqlFile(Class<T> entityClass, String sqlId, Object params) {
        return delegatee.countBySqlFile(entityClass, sqlId, params);
    }

    /**
     * エンティティオブジェクトを元に更新処理を行う。
     * <p/>
     * エンティティの主キーが更新条件となる。
     *
     * @param <T> エンティティクラスの型
     * @param entity エンティティオブジェクト
     * @return 更新件数
     * @throws OptimisticLockException バージョン不一致で更新対象が存在しない場合
     */
    @Override
    public <T> int update(T entity) throws OptimisticLockException {
        return delegatee.update(entity);
    }

    /**
     * エンティティオブジェクトの情報を元に一括更新を行う。
     * <p/>
     * {@link #update(Object)}とは異なり、一括更新処理ではバージョン不一致チェックは行わない。
     * 例えば、バージョン番号が変更になっていた場合はそのレコードのみ更新されずに処理は正常に終了する。
     * バージョン番号のチェックを必要とする場合には、{@link #update(Object)}を使用すること。
     *
     * @param entities 更新対象のエンティティリスト
     * @param <T> エンティティクラスの型
     */
    @Override
    public <T> void batchUpdate(List<T> entities) {
        delegatee.batchUpdate(entities);
    }

    /**
     * エンティティオブジェクトを元に登録処理を行う。
     *
     * @param <T> エンティティクラスの型
     * @param entity エンティティオブジェクト
     */
    @Override
    public <T> void insert(T entity) {
        delegatee.insert(entity);
    }

    /**
     * エンティティオブジェクトの情報を一括で登録する。
     * @param entities エンティティリスト
     * @param <T> エンティティクラスの型
     */
    @Override
    public <T> void batchInsert(List<T> entities) {
        delegatee.batchInsert(entities);
    }

    /**
     * エンティティオブジェクトを元に削除処理を行う。
     * <p/>
     * エンティティの主キーが削除条件となる。
     *
     * @param <T> エンティティクラスの型
     * @param entity エンティティオブジェクト
     * @return 削除件数
     */
    @Override
    public <T> int delete(T entity) {
        return delegatee.delete(entity);
    }

    /**
     * エンティティオブジェクトを元に一括削除処理を行う。
     * <p/>
     * エンティティの主キーが削除条件となる。
     *
     * @param entities エンティティリスト
     * @param <T> エンティティクラスの型
     */
    @Override
    public <T> void batchDelete(List<T> entities) {
        delegatee.batchDelete(entities);
    }

    /**
     * ページングの何ページ目を検索するかを指定する。
     *
     * @param page ページ番号(1-origin)
     * @return DaoContextがそのまま返る。
     */
    @Override
    public DaoContext page(long page) {
        delegatee.page(page);
        return this;
    }

    /**
     * ページングの1ページにつき何件表示するかを指定する。
     *
     * @param per ページ内表示件数
     * @return DaoContextがそのまま返る。
     */
    @Override
    public DaoContext per(long per) {
        delegatee.per(per);
        return this;
    }

    /**
     * 検索結果の取得を遅延させる。
     *
     * @return DaoContextがそのまま返る。
     */
    @Override
    public DaoContext defer() {
        delegatee.defer();
        return this;
    }
}
