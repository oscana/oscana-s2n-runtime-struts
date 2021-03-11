package oscana.s2n.struts;

/**
 * ジェネリクスの型引数に対するインスタンスを獲得するためのユーティリティクラス。<br>
 * <br>
 * 型引数Tに対する以下を実現する。<br>
 * <pre>
 * new T()
 * </pre>
 * 本クラスは自動生成されたソースコード中に埋め込まれ、UniversalDaoのパラメータとして引き渡すclassを型引数から取得する用途で用いる。
 * @author Fumihiko Yamamoto
 *
 * @param <E> 型引数E
 */
public class GenericsUtil<E> {

    /** タイプ */
    private Class<E> type;

    /**
     * 型引数の内容を動的に判別しクラスを取得する。
     * @param e E
     */
    @SuppressWarnings("unchecked")
    public GenericsUtil(E... e) {
        Class<E> type = (Class<E>) e.getClass().getComponentType();
        this.type = type;
    }

    /**
     * コンストラクタで取得したクラスを返却する。
     * @return クラス
     */
    public Class<E> getType() {
        return type;
    }

}
